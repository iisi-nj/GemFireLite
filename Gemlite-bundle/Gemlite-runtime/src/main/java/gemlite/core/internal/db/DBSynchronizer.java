/*                                                                         
 * Copyright 2010-2013 the original author or authors.                     
 *                                                                         
 * Licensed under the Apache License, Version 2.0 (the "License");         
 * you may not use this file except in compliance with the License.        
 * You may obtain a copy of the License at                                 
 *                                                                         
 *      http://www.apache.org/licenses/LICENSE-2.0                         
 *                                                                         
 * Unless required by applicable law or agreed to in writing, software     
 * distributed under the License is distributed on an "AS IS" BASIS,       
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and     
 * limitations under the License.                                          
 */                                                                        
package gemlite.core.internal.db;

import gemlite.core.internal.domain.DomainRegistry;
import gemlite.core.internal.domain.IMapperTool;
import gemlite.core.util.LogUtil;

import java.io.FileInputStream;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLNonTransientException;
import java.sql.SQLTransientException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.Logger;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.cache.Operation;
import com.gemstone.gemfire.cache.RegionDestroyedException;
import com.gemstone.gemfire.cache.asyncqueue.AsyncEvent;
import com.gemstone.gemfire.cache.asyncqueue.AsyncEventListener;

/**
 * 存储region的数据变化到相关配置好的数据库 确保配置的数据库数据表已经创建好 不支持自增长字段
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DBSynchronizer implements AsyncEventListener
{

    protected String dbUrl;

    protected String userName;

    protected String passwd;

    protected String errorFile;

    /**
     * 将错误数据存放到文件之前,尝试次数
     */
    protected int numErrorTries = 0;

    /**
     * 默认尝试次数
     */
    protected static final int DEFAULT_ERROR_TRIES = 3;

    /**
     * 数据库驱动名
     */
    protected String driverClass;

    protected Driver driver;

    /**
     * the current JDBC connection being used by this instance
     */
    protected Connection conn;

    /** true if this instance has been closed or not initialized */
    protected volatile boolean shutDown = true;

    protected final AsyncEventHelper helper = AsyncEventHelper.newInstance();

    protected final Logger logger = LogUtil.getCoreLog();

    protected final HashMap<String, PreparedStatement> insertStmntMap = new HashMap<String, PreparedStatement>();

    protected final HashMap<String, PreparedStatement> updtStmntMap = new HashMap<String, PreparedStatement>();

    protected final HashMap<String, PreparedStatement> deleteStmntMap = new HashMap<String, PreparedStatement>();

    // keys used in external property file
    protected static final String DBDRIVER = "driver";

    protected static final String DBURL = "url";

    protected static final String USER = "user";

    protected static final String PASSWORD = "password";

    protected static final String SECRET = "secret";

    protected static final String TRANSFORMATION = "transformation";

    protected static final String KEYSIZE = "keysize";

    protected static final String ERRORFILE = "errorfile";

    protected static final String ERRORTRIES = "errortries";

    // Log strings
    protected static final String DB_SYNCHRONIZER__1 = "DBSynchronizer::" + "processEvents: Exception while fetching prepared statement "
            + "for event '%s': %s";

    protected static final String DB_SYNCHRONIZER__2 = "DBSynchronizer::" + "processEvents: Unexpected Exception occured while processing "
            + "Events. The list of unprocessed events is: %s. " + "Attempt will be made to rollback the changes.";

    protected static final String DB_SYNCHRONIZER__3 = "DBSynchronizer::" + "processEvents: Operation failed for event '%s' " + "due to exception: %s";

    protected static final String DB_SYNCHRONIZER__4 = "DBSynchronizer::" + "closeStatements: Exception in closing prepared statement " + "with DML string: %s";

    protected static final String DB_SYNCHRONIZER__5 = "DBSynchronizer::" + "close: Exception in closing SQL Connection: %s";

    protected static final String DB_SYNCHRONIZER__6 = "DBSynchronizer::" + "init: Exception while initializing connection for driver class '%s' "
            + "and db url = %s";

    protected static final String DB_SYNCHRONIZER__7 = "DBSynchronizer::" + "processEvents: Exception occured while committing '%s' " + "to external DB: %s";

    protected static final String DB_SYNCHRONIZER__8 = "DBSynchronizer::init" + ": Illegal format of init string '%s', expected <driver>,<URL>,...";

    protected static final String DB_SYNCHRONIZER__9 = "DBSynchronizer::" + "init: Exception in loading properties file '%s' for initialization";

    protected static final String DB_SYNCHRONIZER__10 = "DBSynchronizer::" + "init: missing Driver or URL properties in file '%s'";

    protected static final String DB_SYNCHRONIZER__11 = "DBSynchronizer::" + "init: unknown property '%s' in file '%s'";

    protected static final String DB_SYNCHRONIZER__12 = "DBSynchronizer::" + "init: both password and secret properties specified in file '%s'";

    protected static final String DB_SYNCHRONIZER__13 = "DBSynchronizer::" + "init: initialized with URL '%s' using driver class '%s'";

    /**
     * Holds event that failed to be applied to underlying database and the time
     * of failure
     */
    private static class ErrorEvent implements Comparable
    {
        AsyncEvent ev;

        long errortime;

        @Override
        public int compareTo(Object o)
        {
            ErrorEvent ee = (ErrorEvent) o;
            // If events are equal, nevermind the time, else allow sorting by
            // failure time, earlier first.
            if (ee.ev.equals(this.ev))
            {
                return 0;
            }
            else if (ee.errortime > this.errortime)
            {
                return -1;
            }
            else
            {
                return 1;
            }
        }
    }

    /** keeps track of the retries that have been done for an event */
    protected final ConcurrentSkipListMap<ErrorEvent, Object[]> errorTriesMap = new ConcurrentSkipListMap<ErrorEvent, Object[]>();

    /**
     * Enumeration that defines the action to be performed in case an exception
     * is received during processing by
     * {@link DBSynchronizer#processEvents(List)}
     */
    protected static enum SqlExceptionHandler
    {
        /**
         * ignore the exception and continue to process other events in the
         * current batch
         */
        IGNORE
        {
            @Override
            public void execute(DBSynchronizer synchronizer)
            {
                // No -op
                synchronizer.logger.info("DBSynchronizer::Ignoring error");
            }

            @Override
            public boolean breakTheLoop()
            {
                return false;
            }
        },

        /**
         * ignore the exception and break the current batch of events being
         * processed
         */
        IGNORE_BREAK_LOOP
        {
            @Override
            public boolean breakTheLoop()
            {
                return true;
            }

            @Override
            public void execute(DBSynchronizer synchronizer)
            {
                // No -op
            }
        },

        /**
         * create a new database connection since the current one is no longer
         * usable
         */
        REFRESH
        {
            @Override
            public void execute(DBSynchronizer synchronizer)
            {
                synchronized (synchronizer)
                {
                    try
                    {
                        if (!synchronizer.conn.isClosed())
                        {
                            if (synchronizer.logger.isInfoEnabled())
                            {
                                synchronizer.logger.info("DBSynchronizer::" + "SqlExceptionHandler: before rollback");

                            }
                            // For safe side just roll back the transaction so
                            // far
                            synchronizer.conn.rollback();
                            if (synchronizer.logger.isInfoEnabled())
                            {
                                synchronizer.logger.info("DBSynchronizer::" + "SqlExceptionHandler: after rollback");
                            }
                        }
                    }
                    catch(SQLException sqle)
                    {
                        synchronizer.helper.log(synchronizer.logger, Level.WARNING, sqle, "DBSynchronizer::SqlExceptionHandler: "
                                + "could not successfully rollback");
                    }
                    synchronizer.basicClose();

                    if (!synchronizer.shutDown)
                    {
                        // Do not recreate the connection in case of shutdown
                        synchronizer.logger.info("DBSynchronizer::Attempting to reconnect to database");
                        synchronizer.instantiateConnection();
                    }
                }
            }
        },

        /** close the current connection */
        CLEANUP
        {
            @Override
            public void execute(DBSynchronizer synchronizer)
            {
                synchronized (synchronizer)
                {

                    try
                    {
                        if (synchronizer.conn != null && !synchronizer.conn.isClosed())
                        {
                            if (synchronizer.logger.isInfoEnabled())
                            {
                                synchronizer.logger.info("DBSynchronizer::" + "SqlExceptionHandler: before rollback");
                            }
                            // For safeside just roll back the transactions so
                            // far
                            synchronizer.conn.rollback();
                            if (synchronizer.logger.isInfoEnabled())
                            {
                                synchronizer.logger.info("DBSynchronizer::" + "SqlExceptionHandler: after rollback");
                            }
                        }
                    }
                    catch(SQLException sqle)
                    {
                        synchronizer.helper.log(synchronizer.logger, Level.WARNING, sqle, "DBSynchronizer::SqlExceptionHandler: "
                                + "could not successfully rollback");
                    }
                    synchronizer.basicClose();
                }
            }
        };

        /**
         * execute an action specified by different enumeration values after an
         * unexpected exception is received by
         * {@link DBSynchronizer#processEvents(List)}
         */
        public abstract void execute(DBSynchronizer synchronizer);

        /**
         * Returns true if processing for the current batch of events has to be
         * terminated for the current exception. Default handling is to return
         * true, unless specified otherwise by an enumerated action.
         */
        public boolean breakTheLoop()
        {
            return true;
        }
    }

    /**
     * Close this {@link DBSynchronizer} instance.
     * 
     * To prevent a possible concurrency issue between closing thread & the
     * processor thread, access to this method is synchronized on 'this'
     */
    public synchronized void close()
    {
        // Flush any pending error events to XML log
        this.flushErrorEventsToLog();
        this.shutDown = true;
        this.basicClose();
        this.helper.close();
    }

    /**
     * Basic actions to be performed to close the {@link DBSynchronizer}
     * instance though the instance will itself not be marked as having shut
     * down.
     * 
     * To prevent a possible concurrency issue between closing thread & the
     * processor thread, access to this method is synchronized on 'this'
     */
    public final synchronized void basicClose()
    {
        closeStatements(this.insertStmntMap);
        closeStatements(this.updtStmntMap);
        closeStatements(this.deleteStmntMap);
        try
        {
            if (this.conn != null && !this.conn.isClosed())
            {
                this.conn.close();
            }
        }
        catch(SQLException sqle)
        {
            if (logger.isInfoEnabled())
            {
                helper.logFormat(logger, Level.INFO, sqle, DB_SYNCHRONIZER__5, this.conn);
            }
        }
    }

    /*
     * to prevent a possible concurrency issue between closing thread & the
     * processor thread, access to this method is synchronized on 'this'
     */
    protected void closeStatements(Map<String, PreparedStatement> psMap)
    {
        Iterator<Map.Entry<String, PreparedStatement>> itr = psMap.entrySet().iterator();
        while (itr.hasNext())
        {
            Map.Entry<String, PreparedStatement> entry = itr.next();
            try
            {
                entry.getValue().close();
            }
            catch(SQLException sqle)
            {
                if (logger.isInfoEnabled())
                {
                    helper.logFormat(logger, Level.INFO, sqle, DB_SYNCHRONIZER__4, entry.getKey());
                }
            } finally
            {
                itr.remove();
            }
        }
    }
    
    public void init(String driverClass,String dbUrl,String userName,String passwd)
    {
        this.driverClass = driverClass;
        this.dbUrl = dbUrl;
        this.userName = userName;
        this.passwd = passwd;
        this.initConnection();
    }

    /**
     * Initialize this {@link DBSynchronizer} instance, creating a new JDBC
     * connection to the backend database as per the provided parameter.<BR>
     * 
     * The recommended format of the parameter string is: <BR>
     * 
     * file=&lt;path&gt; <BR>
     * 
     * The file is a properties file specifying the driver, JDBC URL, user and
     * password.<BR>
     * 
     * Driver=&lt;driver-class&gt;<BR>
     * URL=&lt;JDBC URL&gt;<BR>
     * User=&lt;user name&gt;<BR>
     * <BR>
     * Secret=&lt;encrypted password&gt;<BR>
     * Transformation=&lt;transformation for the encryption cipher&gt;<BR>
     * KeySize=&lt;size of the private key to use for encryption&gt;<BR>
     * -- OR --<BR>
     * Password=&lt;password&gt;<BR>
     * 
     * The password provided in the "Secret" property should be an encrypted one
     * generated using the "gfxd encrypt-password external" command, else the
     * "Password" property can be used to specify the password in plain-text.
     * The "Transformation" and "KeySize" properties optionally specify the
     * transformation and key size used for encryption else the defaults are
     * used ("AES" and 128 respectively). User and password are optional and
     * when not provided then JDBC URL will be used as is for connection.
     * 
     * The above properties may also be provided inline like below:<BR>
     * <BR>
     * &lt;driver-class&gt;,&lt;JDBC
     * URL&gt;[,&lt;user&gt;[,&lt;password&gt;|secret
     * =&lt;secret&gt;][,transformation=&lt;transformation&gt;][,keysize=&lt;key
     * size&gt;]<BR>
     * <BR>
     * The user and password parts are optional and can be possibly embedded in
     * the JDBC URL itself. The password can be encrypted one generated using
     * the "gfxd encrypt-password external" command in which case it should be
     * prefixed with "secret=". It can also specify the transformation and
     * keysize using the optional "transformation=..." and "keysize=..."
     * properties.
     */
    public void init(String initParamStr)
    {
        this.driver = null;
        this.driverClass = null;
        this.dbUrl = null;
        this.userName = null;
        this.passwd = null;
        this.numErrorTries = 0;
        // check the new "file=<properties file>" option first
        if (initParamStr.startsWith("file="))
        {
            String propsFile = initParamStr.substring("file=".length());
            FileInputStream fis = null;
            final Properties props = new Properties();
            try
            {
                fis = new FileInputStream(propsFile);
                props.load(fis);
            }
            catch(Exception e)
            {
                throw helper.newRuntimeException(String.format(DB_SYNCHRONIZER__9, propsFile), e);
            } finally
            {
                try
                {
                    if (fis != null)
                    {
                        fis.close();
                    }
                }
                catch(Exception e)
                {
                    // ignored
                }
            }
            try
            {
                for (Map.Entry<Object, Object> entry : props.entrySet())
                {
                    String key = ((String) entry.getKey()).trim();
                    String value = ((String) entry.getValue()).trim();
                    if (DBDRIVER.equalsIgnoreCase(key))
                    {
                        this.driverClass = value;
                    }
                    else if (DBURL.equalsIgnoreCase(key))
                    {
                        this.dbUrl = value;
                    }
                    else if (USER.equalsIgnoreCase(key))
                    {
                        this.userName = value;
                    }
                    else if (PASSWORD.equalsIgnoreCase(key))
                    {
                        this.passwd = value;
                    }
                    else if (ERRORFILE.equalsIgnoreCase(key))
                    {
                        this.errorFile = value;
                    }
                    else if (ERRORTRIES.equalsIgnoreCase(key))
                    {
                        this.numErrorTries = Integer.parseInt(value);
                    }
                    else
                    {
                        throw new IllegalArgumentException(String.format(DB_SYNCHRONIZER__11, key, propsFile));
                    }
                }
            }
            catch(IllegalArgumentException e)
            {
                throw e;
            }
            catch(Exception e)
            {
                throw helper.newRuntimeException(String.format(DB_SYNCHRONIZER__9, propsFile), e);
            }
            if (this.driverClass == null || this.driverClass.length() == 0 || this.dbUrl == null || this.dbUrl.length() == 0)
            {
                throw new IllegalArgumentException(String.format(DB_SYNCHRONIZER__10, propsFile));
            }
        }
        else
        {
            inlineInit(initParamStr);
        }
        // helper.createEventErrorLogger(errorFile);

        this.initConnection();
    }

    protected void inlineInit(String initParamStr)
    {
        logger.info("DBSynchronizer::Inline init parameters:" + initParamStr);
        String[] params = initParamStr.split(",");
        if (params.length < 2)
        {
            throw new IllegalArgumentException(String.format(DB_SYNCHRONIZER__8, initParamStr));
        }
        int paramNo = 1;
        for (String param : params)
        {
            param = param.trim();
            StringBuilder value = new StringBuilder();
            if (isArgPresent(param, DBDRIVER, value))
            {
                this.driverClass = value.toString().trim();
            }
            else if (isArgPresent(param, DBURL, value))
            {
                this.dbUrl = value.toString().trim();
            }
            else if (isArgPresent(param, USER + '=', value))
            {
                this.userName = value.toString().trim();
            }
            else if (isArgPresent(param, PASSWORD + '=', value))
            {
                this.passwd = value.toString().trim();
            }
            else if (isArgPresent(param, ERRORFILE + '=', value))
            {
                this.errorFile = value.toString().trim();
            }
            else if (isArgPresent(param, ERRORTRIES + '=', value))
            {
                this.numErrorTries = Integer.parseInt(value.toString());
            }
            else if (paramNo == 1)
            {
                // Assume this is the driver name
                this.driverClass = param.trim();
            }
            else if (paramNo == 2)
            {
                // Assume this is the db url
                this.dbUrl = param.trim();
            }
            else if (paramNo == 3)
            {
                // The third param is expected to be username if not explicitly
                // provided.
                this.userName = param.trim();
            }
            else if (paramNo == 4)
            {
                this.passwd = param.trim();
            }
            ++paramNo;
        }
    }

    protected boolean isArgPresent(String s, String prefix, StringBuilder extracted)
    {
        if ((s.length() > prefix.length() && prefix.equalsIgnoreCase(s.substring(0, prefix.length()))))
        {
            extracted.append(s.substring(prefix.length()));
            return true;
        }
        return false;
    }

    protected String trimIgnoreCase(String s, String prefix)
    {
        if (s.length() > prefix.length() && prefix.equalsIgnoreCase(s.substring(0, prefix.length())))
        {
            return s.substring(prefix.length());
        }
        else
        {
            return null;
        }
    }

    protected synchronized void initConnection()
    {
        String maskedPasswordDbUrl = null;
        if (this.dbUrl != null)
        {
            maskedPasswordDbUrl = maskPassword(this.dbUrl);
        }
        try
        {
            Class.forName(this.driverClass).newInstance();
            this.driver = DriverManager.getDriver(this.dbUrl);
        }
        catch(Exception e)
        {
            throw helper.newRuntimeException(String.format(DB_SYNCHRONIZER__6, this.driverClass, maskedPasswordDbUrl), e);
        }
        this.instantiateConnection();
        if (this.logger.isInfoEnabled())
        {
            this.helper.logFormat(this.logger, Level.INFO, null, DB_SYNCHRONIZER__13, maskedPasswordDbUrl, this.driverClass);
        }
        this.shutDown = false;
    }

    protected synchronized void instantiateConnection()
    {
        if (this.driver == null)
        {
            initConnection();
            return;
        }
        String maskedPasswordDbUrl = null;
        try
        {
            // use Driver directly for connect instead of looping through all
            // drivers as DriverManager.getConnection() would do, to avoid
            // hitting any broken drivers in the process (vertica driver is
            // known to
            // fail in acceptsURL with this set of properties)
            final Properties props = new Properties();
            // the user/password property names are standard ones also used by
            // DriverManager.getConnection(String, String, String) itself, so
            // will work for all drivers
            if (this.userName != null)
            {
                props.put("user", this.userName);
            }
            if (this.passwd != null)
            {
                props.put("password", this.passwd);
            }

            this.conn = this.driver.connect(this.dbUrl, props);
            // null to GC password as soon as possible
            props.clear();
            try
            {
                // try to set the default isolation to at least READ_COMMITTED
                // need it for proper HA handling
                if (this.conn.getTransactionIsolation() < Connection.TRANSACTION_READ_COMMITTED
                        && this.conn.getMetaData().supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_COMMITTED))
                {
                    this.conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                    if (this.dbUrl != null)
                    {
                        maskedPasswordDbUrl = maskPassword(this.dbUrl);
                    }
                    logger.info("explicitly set the transaction isolation level to " + "READ_COMMITTED for URL: " + maskedPasswordDbUrl);
                }
            }
            catch(SQLException sqle)
            {
                // ignore any exception here
            }
            this.conn.setAutoCommit(false);
            this.shutDown = false;
        }
        catch(Exception e)
        {
            if (this.dbUrl != null)
            {
                maskedPasswordDbUrl = maskPassword(this.dbUrl);
            }
            // throttle retries for connection failures
            try
            {
                Thread.sleep(200);
            }
            catch(InterruptedException ie)
            {
                Thread.currentThread().interrupt();
            }
            throw helper.newRuntimeException(String.format(DB_SYNCHRONIZER__6, this.driverClass, maskedPasswordDbUrl), e);
        }
    }

    /** mask the known password patterns from URL for exception/log messages */
    protected static final String maskPassword(final String dbUrl)
    {
        String maskedPasswordDbUrl = Pattern.compile("(password|passwd|pwd|secret)=[^;]*", Pattern.CASE_INSENSITIVE).matcher(dbUrl).replaceAll("$1=***");
        return maskedPasswordDbUrl;
    }

    public boolean processEvents(List<AsyncEvent> events)
    {
        if (this.shutDown)
        {
            return false;
        }
        boolean completedSucessfully = false;
        String listOfEventsString = null;
        // The retval will be considered true only if the list was iterated
        // completely. If the List iteration was incomplete we will return
        // false so that the events are not removed during failure.
        // As for individual events, they can get exceptions due to constraint
        // violations etc but will not cause return value to be false.
        Statement stmt = null;
        PreparedStatement ps = null;
        // keep track of the previous prepared statement in case we can optimize
        // by create a batch when the previous and current statements match
        PreparedStatement prevPS = null;
        AsyncEvent prevEvent = null;
        boolean prevPSHasBatch = false;
        Iterator<AsyncEvent> itr = events.iterator();
        AsyncEvent event = null;
        String eventString = null;
        String prevEventStr = null;
        try
        {
            while (!(completedSucessfully = !itr.hasNext()))
            {
                event = itr.next();
                Operation operation = event.getOperation();
                if (logger.isDebugEnabled())
                {
                    eventString = event.toString();
                    if (prevEvent != null)
                    {
                        prevEventStr = prevEvent.toString();
                    }
                    logger.info("DBSynchronizer::processEvents :processing PK based " + "event=" + eventString + " AsyncEvent Operation=" + operation);
                }
                else
                {
                    eventString = null;
                    prevEventStr = null;
                }
                try
                {
                    if (operation.isPutAll() || operation.isCreate())
                        ps = getExecutableInsertPrepStmntPKBased(event, prevPS);
                    else if (operation.isUpdate())
                        ps = getExecutableUpdatePrepStmntPKBased(event, prevPS);
                    else if (operation.isDestroy())
                        ps = getExecutableDeletePrepStmntPKBased(event, prevPS);
                    else
                    {
                        logger.error("DBSynchronizer::processEvents: unexpected " + "eventType " + operation + " for " + event);
                        continue;
                    }
                }
                catch(SQLException sqle)
                {
                    SqlExceptionHandler handler = handleSQLException(sqle, DB_SYNCHRONIZER__1, null, event, eventString, logger, true);
                    if (handler.breakTheLoop())
                    {
                        break;
                    }
                }
                catch(RegionDestroyedException rde)
                {
                    if (logger.isInfoEnabled())
                    {
                        logger.info("DBSynchronizer::processEvents: WBCLEvent " + event + " will  be discarded as the underlying region "
                                + "for the table has been destroyed");
                    }
                    continue;
                }
                if (logger.isDebugEnabled())
                {
                    if (eventString == null)
                    {
                        eventString = event.toString();
                    }
                    logger.debug("DBSynchronizer::processEvents: Statement=" + (ps != null ? ps : stmt) + " for event=" + eventString);
                }
                try
                {
                    int num;
                    if (prevPS != null && prevPS != ps)
                    {
                        try
                        {
                            if (prevPSHasBatch)
                            {
                                prevPS.addBatch();
                                if (logger.isDebugEnabled())
                                {
                                    logger.info("DBSynchronizer::processEvents executing " + "batch statement for prepared statement=" + prevPS + " for event="
                                            + prevEventStr);
                                }
                                final int[] res = prevPS.executeBatch();
                                num = res.length;
                                prevPSHasBatch = false;
                            }
                            else
                            {
                                num = prevPS.executeUpdate();
                            }
                            if (logger.isDebugEnabled())
                            {
                                logger.info("DBSynchronizer::processEvents total num rows " + "modified=" + num + " for prepared statement=" + prevPS
                                        + " for event=" + prevEventStr);
                            }
                            // clear event from failure map if present
                            helper.removeEventFromFailureMap(prevEvent);
                        }
                        catch(SQLException sqle)
                        {
                            if (prevPSHasBatch)
                            {
                                try
                                {
                                    prevPS.clearBatch();
                                }
                                catch(SQLException e)
                                {
                                    // ignored
                                }
                                prevPSHasBatch = false;
                            }
                            SqlExceptionHandler handler = handleSQLException(sqle, DB_SYNCHRONIZER__3, prevPS, prevEvent, prevEventStr, logger, false);
                            if (handler.breakTheLoop())
                            {
                                break;
                            }
                            prevPS = null;
                            prevEvent = null;
                            prevPSHasBatch = false;
                        }
                    }
                    // in case previous prepared statement matches the current
                    // one,
                    // it will already be added as a batch when setting the
                    // arguments
                    // by AsyncEventHelper#setColumnInPrepStatement()
                    else if (prevPS != null && ps != null)
                    {
                        prevPSHasBatch = true;
                        if (logger.isDebugEnabled())
                        {
                            logger.info("DBSynchronizer::processEvents added new row " + "as a batch for prepared statement=" + ps + " for event="
                                    + eventString);
                        }
                    }

                    prevPS = ps;
                    prevEvent = event;
                }
                catch(SQLException sqle)
                {
                    if (prevPS != null && prevPSHasBatch)
                    {
                        try
                        {
                            prevPS.clearBatch();
                        }
                        catch(SQLException e)
                        {
                            // ignored
                        }
                    }
                    SqlExceptionHandler handler = handleSQLException(sqle, DB_SYNCHRONIZER__3, ps != null ? ps : stmt, event, eventString, logger, false);
                    if (handler.breakTheLoop())
                    {
                        break;
                    }
                }
            } // end of while (event list processing loop)

            // now handle the last statement in the above loop since it is still
            // pending due to anticipated batching
            if (completedSucessfully)
            {
                try
                {
                    if (logger.isInfoEnabled())
                    {
                        if (listOfEventsString == null)
                        {
                            listOfEventsString = events.toString();
                        }
                        logger.info("DBSynchronizer::processEvents: " + "before commit of events=" + listOfEventsString);
                    }
                    int num;
                    // first the case when the previous statement was a batched
                    // one
                    // so add current one as batch and execute
                    if (prevPSHasBatch)
                    {
                        ps.addBatch();
                        if (logger.isDebugEnabled())
                        {
                            logger.info("DBSynchronizer::processEvents executing batch " + "statement for prepared statement=" + ps + " for event="
                                    + eventString);
                        }
                        final int[] res = ps.executeBatch();
                        num = res.length;
                    }
                    // next the case of a non BULK_INSERT operation;
                    // BULK_INSERT operations are always executed as a single
                    // batch
                    // by itself, so will never reach here
                    else if (ps != null)
                    {
                        num = ps.executeUpdate();
                        if (event != null)
                        {
                            // clear event from failure map if present
                            helper.removeEventFromFailureMap(event);
                        }
                    }
                    else
                    {
                        num = 0;
                    }
                    // clear event from failure map if present
                    helper.removeEventFromFailureMap(event);
                    if (logger.isDebugEnabled())
                    {
                        if (ps != null)
                        {
                            logger.info("DBSynchronizer::processEvents num rows modified=" + num + " for prepared statement=" + ps + " for event="
                                    + eventString);
                        }
                    }
                    this.conn.commit();
                    if (logger.isInfoEnabled())
                    {
                        if (listOfEventsString == null)
                        {
                            listOfEventsString = events.toString();
                        }
                        logger.info("DBSynchronizer::processEvents: " + "committed successfully for events=" + listOfEventsString);
                    }
                }
                catch(SQLException sqle)
                {

                    if (ps != null && prevPSHasBatch)
                    {
                        try
                        {
                            ps.clearBatch();
                        }
                        catch(SQLException e)
                        {
                            // ignored
                        }
                    }

                    SqlExceptionHandler handler = handleSQLException(sqle, DB_SYNCHRONIZER__7, ps != null ? ps : stmt, event, eventString, logger, true);
                    if (handler != SqlExceptionHandler.IGNORE)
                    {
                        completedSucessfully = false;
                    }
                }
            }
        }
        catch(Exception e)
        {

            if (logger != null && logger.isErrorEnabled() && !(event != null && helper.skipFailureLogging(event)))
            {
                StringBuilder sb = new StringBuilder();
                if (event != null)
                {
                    if (eventString == null)
                    {
                        eventString = event.toString();
                    }
                    sb.append("[FAILED: ").append(eventString).append(" ]");
                }
                while (itr.hasNext())
                {
                    sb.append("[ ").append(itr.next().toString()).append(" ]");
                }
                helper.logFormat(logger, Level.SEVERE, e, DB_SYNCHRONIZER__2, sb.toString());
            }
            SqlExceptionHandler.CLEANUP.execute(this);
            completedSucessfully = false;
        }

        if (completedSucessfully)
        {
            // on successful completion, log any pending errors to XML file;
            // when
            // unsuccessful then we know that batch will be retried so don't log
            // in
            // that case else it can get logged multiple times
            // clear event from failure map if present
            flushErrorEventsToLog();
        }

        if (logger.isDebugEnabled())
        {
            logger.info("DBSynchronizer::processEvents: processed " + events.size() + " events, success=" + completedSucessfully);
        }

        return completedSucessfully;
    }

    private void flushErrorEventsToLog()
    {
        Iterator<ErrorEvent> it = errorTriesMap.keySet().iterator();
        while (it.hasNext())
        {
            ErrorEvent ee = it.next();
            Object[] tries = errorTriesMap.get(ee);
            if (tries != null && tries[1] != null)
            {
                try
                {
                    helper.log(logger, Level.SEVERE, (SQLException) tries[1], ((SQLException) tries[1]).getMessage());
                }
                catch(Exception e)
                {
                    // failed to even log the exception
                    if (logger.isWarnEnabled())
                    {
                        helper.log(logger, Level.WARNING, e, e.getMessage());
                    }
                }
            }
        }
        errorTriesMap.clear();
    }

    /**
     * Get or create a {@link PreparedStatement} for an insert operation.
     */
    protected PreparedStatement getExecutableInsertPrepStmntPKBased(AsyncEvent pkEvent, PreparedStatement prevPS) throws SQLException
    {
        final String regionName = pkEvent.getRegion().getName();
        PreparedStatement ps = this.insertStmntMap.get(regionName);

        IMapperTool tool = DomainRegistry.getMapperTool(regionName);
        String tableName = DomainRegistry.regionToTable(regionName);
        List<String> valueFields = tool.getValueFieldNames();

        if (ps == null)
        {
            final String dmlString = AsyncEventHelper.getInsertString(tableName, valueFields);
            if (logger.isDebugEnabled())
            {
                logger.info("DBSynchronizer::getExecutableInsertPrepStmntPKBased: " + "preparing '" + dmlString + "' for event: " + pkEvent);
            }
            ps = conn.prepareStatement(dmlString);
            this.insertStmntMap.put(tableName, ps);
        }
        else if (prevPS == ps)
        {
            // add a new batch of values
            ps.addBatch();
        }
        int paramIndex = 1;
        Class valueClass = tool.getValueClass();
        for (int colIdx = 0; colIdx < valueFields.size(); colIdx++)
        {
            String field = valueFields.get(colIdx);
            try
            {
                Map map = PropertyUtils.describe(pkEvent.getDeserializedValue());
                Object val = map.get(field);
                String type = valueClass.getDeclaredField(field).getType().getName();
                helper.setColumnInPrepStatement(type, val, ps, this, paramIndex);
            }
            catch(Exception e)
            {
                throw new SQLException(e);
            }
            paramIndex++;
        }
        return ps;
    }

    /**
     * Get or create a {@link PreparedStatement} for a primary key based delete
     * operation.
     */

    protected PreparedStatement getExecutableDeletePrepStmntPKBased(AsyncEvent pkEvent, PreparedStatement prevPS) throws SQLException
    {

        final String regionName = pkEvent.getRegion().getName();
        PreparedStatement ps = this.deleteStmntMap.get(regionName);
        IMapperTool tool = DomainRegistry.getMapperTool(regionName);
        String tableName = DomainRegistry.regionToTable(regionName);
        List<String> keyFields = tool.getKeyFieldNames();
        if (ps == null)
        {
            final String dmlString = AsyncEventHelper.getDeleteString(tableName, keyFields);
            if (logger.isDebugEnabled())
            {
                logger.info("DBSynchronizer::getExecutableInsertPrepStmntPKBased: preparing '" + dmlString + "' for event: " + pkEvent);
            }
            ps = conn.prepareStatement(dmlString);
            this.deleteStmntMap.put(regionName, ps);
        }
        else if (prevPS == ps)
        {
            // add a new batch of values
            ps.addBatch();
        }
        
        //判断key是否为一个字段
        if(pkEvent.getKey() instanceof DataSerializable)
        setKeysInPrepStatement(pkEvent.getKey(), keyFields, tool.getValueClass(), ps, 1);
        else
        setSingleKeysInPrepStatement(pkEvent.getKey(), tool.getKeyClass(), ps, 1);
        return ps;
    }

    /**
     * Get or create a {@link PreparedStatement} for a primary key based update
     * operation.
     */
    protected PreparedStatement getExecutableUpdatePrepStmntPKBased(AsyncEvent pkEvent, PreparedStatement prevPS) throws SQLException
    {
        final String regionName = pkEvent.getRegion().getName();
        IMapperTool tool = DomainRegistry.getMapperTool(regionName);
        String tableName = DomainRegistry.regionToTable(regionName);
        List<String> valueFields = tool.getValueFieldNames();

        final int numUpdatedCols = valueFields.size();
        StringBuilder searchKeyBuff = new StringBuilder(tableName);
        int paramIndex;
        for (paramIndex = 0; paramIndex < numUpdatedCols; paramIndex++)
        {
            searchKeyBuff.append('_');
            searchKeyBuff.append(valueFields.get(paramIndex));
        }
        String searchKey = searchKeyBuff.toString();
        final Object pkValues = pkEvent.getDeserializedValue();
        final List<String> keyFields = tool.getKeyFieldNames();
        PreparedStatement ps = this.updtStmntMap.get(searchKey);
        if (ps == null)
        {
            final String dmlString = AsyncEventHelper.getUpdateString(tableName, keyFields, valueFields);
            if (logger.isDebugEnabled())
            {
                logger.info("DBSynchronizer::getExecutableInsertPrepStmntPKBased: " + "preparing '" + dmlString + "' for event: " + pkEvent);
            }
            ps = conn.prepareStatement(dmlString);
            this.updtStmntMap.put(searchKey, ps);
        }
        else if (prevPS == ps)
        {
            // add a new batch of values
            ps.addBatch();
        }
        // Set updated col values
        Class valueClass = tool.getValueClass();
        Class keyClass = tool.getKeyClass();
        for (paramIndex = 1; paramIndex <= numUpdatedCols; paramIndex++)
        {
            String field = valueFields.get(paramIndex-1);
            try
            {
                Map map = PropertyUtils.describe(pkEvent.getDeserializedValue());
                Object val = map.get(field);
                String type = valueClass.getDeclaredField(field).getType().getName();
                helper.setColumnInPrepStatement(type, val, ps, this, paramIndex);
            }
            catch(Exception e)
            {
                throw new SQLException(e);
            }
        }
        // Now set the Pk values
        setKeysInPrepStatement(pkValues, keyFields, valueClass, ps, paramIndex);
        return ps;
    }
    
    
    

    /**
     * Set the key column values in {@link PreparedStatement} for a primary key
     * based update or delete operation.
     */
    protected void setKeysInPrepStatement(final Object keyValues, final List<String> keyFields, Class valueClass, final PreparedStatement ps, int startIndex)
            throws SQLException
    {
        final int numKeyCols = keyFields.size();
        if (logger.isDebugEnabled())
        {
            StringBuilder sb = new StringBuilder().append("DBSynchronizer::setKeysInPrepStatement: setting key {");
            for (int col = 0; col < numKeyCols; col++)
            {
                if (col > 1)
                {
                    sb.append(',');
                }
                // 使用反射获取数据
                // 字段名字
                String field = keyFields.get(col);
                try
                {
                    Map map = PropertyUtils.describe(keyValues);
                    Object val = map.get(field);
                    sb.append(val);
                }
                catch(Exception e)
                {
                    throw new SQLException(e);
                }
            }
            sb.append('}');
            logger.info(sb.toString());
        }

        for (int colIndex = 0; colIndex < numKeyCols; colIndex++, startIndex++)
        {
            String field = keyFields.get(colIndex);
            try
            {
                Map map = PropertyUtils.describe(keyValues);
                Object val = map.get(field);
                String type = valueClass.getDeclaredField(field).getType().getName();
                helper.setColumnInPrepStatement(type, val, ps, this, startIndex);
            }
            catch(Exception e)
            {
                throw new SQLException(e);
            }
        }
    }
    
    /**
     * 设置单一key字段
     * @param keyValue
     * @param keyClass
     * @param ps
     * @param startIndex
     * @throws SQLException
     */
    protected void setSingleKeysInPrepStatement(final Object keyValue,Class keyClass, final PreparedStatement ps, int startIndex)
            throws SQLException
    {
        if (logger.isDebugEnabled())
        {
            StringBuilder sb = new StringBuilder().append("DBSynchronizer::setKeysInPrepStatement: setting key {");
            sb.append(keyValue);
            sb.append('}');
            logger.info(sb.toString());
        }

            try
            {
                Object val = (Object)keyValue;
                String type = keyClass.getName();
                helper.setColumnInPrepStatement(type, val, ps, this, startIndex);
            }
            catch(Exception e)
            {
                throw new SQLException(e);
            }
    }


    /**
     * Returns an {@link SqlExceptionHandler} for the given {@link SQLException}
     * .
     */
    protected SqlExceptionHandler handleSQLException(SQLException sqle)
    {
        String sqlState = sqle.getSQLState();
        // What to do if SQLState is null? Checking through the exception
        // message for common strings for now but DB specific errorCode and
        // other
        // such checks will be better.
        // Below was due to a bug in wrapper OracleDriver being used and
        // normally
        // this can never be null.
        if (sqlState == null)
        {
            // no SQLState so fallback to string matching in the message
            // for BatchUpdateException it will look at the nextException
            if (sqle instanceof BatchUpdateException && sqle.getNextException() != null)
            {
                // "42Y96" represents an unknown exception but batch exception
                // will
                // look at the nextException in any case
                sqlState = "42Y96";
            }
            else
            {
                // if connection has been closed then refresh it
                try
                {
                    synchronized (this)
                    {
                        if (this.conn == null || this.conn.isClosed())
                        {
                            return SqlExceptionHandler.REFRESH;
                        }
                    }
                }
                catch(Exception e)
                {
                    return SqlExceptionHandler.REFRESH;
                }
                // treat like a connection failure by default
                return checkExceptionString(sqle.toString().toLowerCase(), SqlExceptionHandler.REFRESH);
            }
        }
        // check for exception type first
        SqlExceptionHandler handler = checkExceptionType(sqle);
        if (handler != null)
        {
            return handler;
        }
        // next check SQLStates
        //about SQLStates see http://blog.csdn.net/cangyingaoyou/article/details/7402243
        if (sqlState.startsWith("25") || sqlState.startsWith("42"))
        {
            // constraint violations can happen in retries, so default action is
            // to
            // IGNORE them; when errorFile is provided then it will be logged to
            // that in XML format in any case
            return SqlExceptionHandler.IGNORE;
        }
        else if (sqlState.startsWith("22") || sqlState.startsWith("23"))
        {
            // if numErrorTries is defined, then retry some number of times else
            // ignore after having logged warning since retry is not likely to
            // help
            return this.numErrorTries > 0 ? SqlExceptionHandler.IGNORE_BREAK_LOOP : SqlExceptionHandler.IGNORE;
        }
        else if (sqlState.startsWith("08"))
        {
            return SqlExceptionHandler.REFRESH;
        }
        else if (sqlState.startsWith("40"))
        {
            // these are transient transaction/lock exceptions so retry whole
            // batch
            return SqlExceptionHandler.IGNORE_BREAK_LOOP;
        }
        else
        {
            if (sqle instanceof BatchUpdateException && sqle.getNextException() != null)
            {
                return handleSQLException(sqle.getNextException());
            }
            // if connection has been closed then refresh it
            try
            {
                synchronized (this)
                {
                    if (this.conn == null || this.conn.isClosed())
                    {
                        //return SqlExceptionHandler.REFRESH;
                        //这里返回忽略,防止数据无法通过,导致一直循环??TODO 需要仔细研读代码
                        return SqlExceptionHandler.IGNORE;
                    }
                }
            }
            catch(Exception e)
            {
                return SqlExceptionHandler.REFRESH;
            }
            return checkExceptionString(sqle.toString().toLowerCase(), SqlExceptionHandler.REFRESH);
        }
    }

    protected SqlExceptionHandler checkExceptionType(SQLException sqle)
    {
        if (sqle != null)
        {
            if (sqle instanceof SQLNonTransientConnectionException)
            {
                // will need to connect again
                return SqlExceptionHandler.REFRESH;
            }
            if (sqle instanceof SQLIntegrityConstraintViolationException)
            {
                // constraint violations can happen in retries, so default
                // action is to
                // IGNORE them; when errorFile is provided then it will be
                // logged to
                // that in XML format in any case
                return SqlExceptionHandler.IGNORE;
            }
            if (sqle instanceof SQLNonTransientException)
            {
                // if numErrorTries is defined, then retry some number of times
                // else
                // ignore after having logged warning since retry is not likely
                // to help
                return this.numErrorTries > 0 ? SqlExceptionHandler.IGNORE_BREAK_LOOP : SqlExceptionHandler.IGNORE;
            }
            if (sqle instanceof SQLTransientException)
            {
                // skip the remaining batch and retry whole batch again
                return SqlExceptionHandler.IGNORE_BREAK_LOOP;
            }
            if (sqle instanceof BatchUpdateException)
            {
                return checkExceptionType(sqle.getNextException());
            }
        }
        return null;
    }

    protected SqlExceptionHandler checkExceptionString(String message, SqlExceptionHandler defaultHandler)
    {
        if (message.contains("constraint"))
        {
            // likely a constraint violation
            // constraint violations can happen in retries, so default action is
            // to
            // IGNORE them; when errorFile is provided then it will be logged to
            // that in XML format in any case
            return SqlExceptionHandler.IGNORE;
        }
        else if (message.contains("syntax"))
        {
            // if numErrorTries is defined, then retry some number of times else
            // ignore after having logged warning since retry is not likely to
            // help
            return this.numErrorTries > 0 ? SqlExceptionHandler.IGNORE_BREAK_LOOP : SqlExceptionHandler.IGNORE;
        }
        else if (message.contains("connect"))
        {
            // likely a connection error
            return SqlExceptionHandler.REFRESH;
        }
        else
        {
            return defaultHandler;
        }
    }

    /**
     * Log exception including stack traces for info logging with
     * {@link #traceDBSynchronizer}, and returns an {@link SqlExceptionHandler}
     * for the given {@link SQLException}.
     */
    protected SqlExceptionHandler handleSQLException(SQLException sqle, String format, Statement stmt, AsyncEvent event, String eventString, Logger logger,
            boolean logWarning) throws SQLException
    {
        SqlExceptionHandler handler = handleSQLException(sqle);

        if (event != null && this.numErrorTries > 0)
        {

            ErrorEvent ee = new ErrorEvent();
            ee.ev = event;
            ee.errortime = System.currentTimeMillis();
            Object[] tries = this.errorTriesMap.get(ee);

            if (tries != null)
            {
                Integer numTries = (Integer) tries[0];
                if (numTries >= this.numErrorTries)
                {
                    // at this point ignore this exception and move to others in
                    // the batch
                    handler = SqlExceptionHandler.IGNORE;
                    logWarning = false;
                }
                tries[0] = Integer.valueOf(numTries.intValue() + 1);
                tries[1] = sqle;
            }
            else
            {
                this.errorTriesMap.put(ee, new Object[] { 1, sqle });
            }
        }

        boolean skipLogging = false;
        if (event != null && (logWarning || logger.isDebugEnabled()))
        {
            if (eventString == null)
            {
                skipLogging = helper.skipFailureLogging(event);
                eventString = event.toString();
            }
        }
        if (!skipLogging)
        {
            if (logWarning)
            {
                if (logger.isWarnEnabled())
                {
                    helper.logFormat(logger, Level.WARNING, sqle, format, eventString, sqle);
                    SQLException next = sqle.getNextException();
                    if (next != null)
                    {
                        helper.logFormat(logger, Level.WARNING, next, format, eventString, sqle.getNextException());
                    }
                }
            }
            if (logger.isDebugEnabled())
            {
                if (logger.isWarnEnabled())
                {
                    String stmtStr = (stmt != null ? ("executing statement=" + stmt) : "preparing statement");
                    helper.log(logger, Level.WARNING, sqle, "DBSynchronizer::" + "processEvents: Exception while " + stmtStr + " for event=" + eventString);
                    if (sqle.getNextException() != null)
                    {
                        helper.log(logger, Level.WARNING, sqle.getNextException(), "DBSynchronizer::processEvents: next exception");
                    }
                }
            }
        }

        handler.execute(this);
        return handler;
    }

    // @Override
    // public synchronized void start() {
    // if (this.shutDown) {
    // this.instantiateConnection();
    // }
    // }

}
