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

import gemlite.core.common.StringPrintWriter;
import gemlite.core.internal.common.JavaTypes;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;

import com.gemstone.gemfire.cache.asyncqueue.AsyncEvent;
import com.gemstone.gemfire.cache.asyncqueue.AsyncEventListener;

/**
 * Some utility methods for {@link AsyncEventListener} and WAN. Note that since
 * this is also used by the GemFireXD WAN layer, users should not change this
 * class, rather should extend it and then modify for custom changes to
 * {@link DBSynchronizer}.
 */
@SuppressWarnings("rawtypes")
public class AsyncEventHelper {

  /** the maximum size of BLOBs that will be kept in memory */
  static final int MAX_MEM_BLOB_SIZE = 128 * 1024 * 1024;
  /** the maximum size of CLOBs that will be kept in memory */
  static final int MAX_MEM_CLOB_SIZE = (MAX_MEM_BLOB_SIZE >>> 1);

  /**
   * When an event fails, then this keeps the last time when a failure was
   * logged. We don't want to swamp the logs in retries due to a syntax error,
   * for example, but we don't want to remove it from the retry map either lest
   * all subsequent dependent DMLs may start failing.
   */
  private final ConcurrentHashMap<AsyncEvent, long[]> failureLogInterval = new ConcurrentHashMap<AsyncEvent, long[]>(16, 0.75f, 2);

  /**
   * The maximum size of {@link #failureLogInterval} beyond which it will start
   * logging all failure instances. Hopefully this should never happen in
   * practise.
   */
  protected static final int FAILURE_MAP_MAXSIZE = Integer.getInteger(
      "gemfirexd.asyncevent.FAILURE_MAP_MAXSIZE", 1000000);

  /**
   * The maximum interval for logging failures of the same event in millis.
   */
  protected static final int FAILURE_LOG_MAX_INTERVAL = Integer.getInteger(
      "gemfirexd.asyncevent.FAILURE_LOG_MAX_INTERVAL", 300000);
  
  public static final boolean POSTGRESQL_SYNTAX = Boolean.getBoolean(
      "gemfirexd.asyncevent.POSTGRESQL_SYNTAX");
  
  public static final String EVENT_ERROR_LOG_FILE = "dbsync_failed_dmls.xml";
  public static final String EVENT_ERROR_LOG_ENTRIES_FILE = "dbsync_failed_dmls_entries.xml";
  
//  private EventErrorLogger evErrorLogger;

  public static AsyncEventHelper newInstance() {
    return new AsyncEventHelper();
  }

  /**
   * Append the backtrace of given exception to provided {@link StringBuilder}.
   * 
   * @param t
   *          the exception whose backtrace is required
   * @param sb
   *          the {@link StringBuilder} to which the stack trace is to be
   *          appended
   */
  public final void getStackTrace(Throwable t, StringBuilder sb) {
          t.printStackTrace(new StringPrintWriter(sb));
  }
  
  /**
   * Log the given message and exception to the provided logger.
   * 
   * @param logger
   *          the {@link Logger} to log the message to
   * @param level
   *          the {@link Level} to use for logging the message
   * @param t
   *          the exception whose backtrace is to be logged; can be null in
   *          which case it is ignored
   * @param message
   *          the message to be logged
   */
  public final void log(Logger logger, Level level, Throwable t,final String message) 
  {
      StringBuilder sb = new StringBuilder();
      sb.append(message).append(": ");
    if (t != null) {
      getStackTrace(t, sb);
    }
    if(Level.SEVERE.equals(level.toString()))
       logger.error(sb.toString());
    else
       logger.info(sb.toString());
  }

  /**
   * Log the given formatted message and exception to the provided logger. The
   * format expected is the same as supported by
   * {@link String#format(String, Object...)}.
   * 
   * @param logger
   *          the {@link Logger} to log the message to
   * @param level
   *          the {@link Level} to use for logging the message
   * @param t
   *          the exception whose backtrace is to be logged; can be null in
   *          which case it is ignored
   * @param format
   *          the message format
   * @param params
   *          the parameters to the message format
   * 
   * @see String#format(String, Object...)
   * @see Formatter#format(String, Object...)
   */
  public final void logFormat(Logger logger, Level level, Throwable t,
      String format, Object... params) {
    StringBuilder sb = new StringBuilder();
    Formatter fmt = new Formatter(sb);
    fmt.format(format, params);
    if (t != null) {
      sb.append(": ");
      getStackTrace(t, sb);
    }
    if(Level.SEVERE.equals(level.toString()))
      logger.error(sb.toString());
    else
      logger.info(sb.toString());
    fmt.close();
  }

  /**
   * Get a DML string that can be used to insert rows in given table. Any
   * auto-increment columns are skipped from the insert statement assuming that
   * they have to be re-generated by the backend database.
   * 
   * @param tableName
   *          name of the table
   * @param tableMetaData
   *          meta-data of the columns of the table
   * @param hasAutoIncrementColumns
   *          should be true if table has any auto-increment columns and those
   *          are to be skipped in the insert string
   * 
   * @throws SQLException
   *           in case of an error in getting column information from table
   *           meta-data
   */
  public static final String getInsertString(String tableName,List<String> valueFields) throws SQLException {
    // skip identity columns, if any
    StringBuilder sbuff = new StringBuilder().append("insert into ");
    // remove quotes from tableName
    if (POSTGRESQL_SYNTAX) {
      String unQuotedTableName = tableName.replace("\"", "");
      sbuff.append(unQuotedTableName);
    } else {
      sbuff.append(tableName);
    }
    
    final int numColumns = valueFields.size();
    // need to specify only the non-autogen column names
    sbuff.append('(');
    for (int pos = 0; pos < numColumns; pos++) {
        sbuff.append(valueFields.get(pos)).append(',');
    }
    sbuff.setCharAt(sbuff.length() - 1, ')');
    sbuff.append(" values (");
    for (int pos = 0; pos < numColumns; pos++) {
        sbuff.append("?,");
    }
    sbuff.setCharAt(sbuff.length() - 1, ')');
    return sbuff.toString();
  }

  /**
   * Get a DML string that can be used to delete rows in given table. Caller
   * needs to pass the primary-key column information as a
   * {@link ResultSetMetaData} obtained from
   * {@link AsyncEvent#getPrimaryKeysAsResultSet()}.
   * 
   * @param tableName
   *          name of the table
   * @param pkMetaData
   *          meta-data of the primary key columns of the table
   * 
   * @throws SQLException
   *           in case of an error in getting column information from primary
   *           key meta-data
   */
  public static final String getDeleteString(String tableName,List<String> keyFields) throws SQLException 
  {
    StringBuilder sbuff = new StringBuilder().append("delete from ");
    // remove quotes from tableName
    if (POSTGRESQL_SYNTAX) 
    {
      String unQuotedTableName = tableName.replace("\"", "");
      sbuff.append(unQuotedTableName);
    } else {
      sbuff.append(tableName);
    }
    sbuff.append(" where ");
    // use the primary key columns to fire the delete on backend DB;
    // tables having no primary keys are received as BULK_DML statements
    // and will not reach here
    final int numCols = keyFields.size();
    for (int col = 0; col < numCols-1; col++) {
      sbuff.append(keyFields.get(col));
      sbuff.append("=? and ");
    }
    sbuff.append(keyFields.get(numCols-1));
    sbuff.append("=?");
    return sbuff.toString();
  }

  /**
   * Get a DML string that can be used to update rows in given table. Caller
   * needs to pass the primary-key column information as a
   * {@link ResultSetMetaData} obtained from
   * {@link AsyncEvent#getPrimaryKeysAsResultSet()}, and the meta-data of updated
   * columns as a {@link ResultSetMetaData} obtained from
   * {@link AsyncEvent#getNewRowsAsResultSet()}.
   * 
   * @param tableName
   *          name of the table
   * @param pkMetaData
   *          meta-data of the primary key columns of the table
   * @param updateMetaData
   *          meta-data of the updated columns of the table
   * 
   * @throws SQLException
   *           in case of an error in getting column information from primary
   *           key meta-data, or from meta-data of updated columns
   */
  public static final String getUpdateString(String tableName,List<String> keyFields,List<String> valueFields)
      throws SQLException {
    StringBuilder sbuff = new StringBuilder().append("update ");
    // remove quotes from tableName
    if (POSTGRESQL_SYNTAX) {
      String unQuotedTableName = tableName.replace("\"", "");
      sbuff.append(unQuotedTableName);
    } else {
      sbuff.append(tableName);
    }
    sbuff.append(" set ");
    final int numPkCols = keyFields.size();
    for (int col = 0; col < valueFields.size(); col++) {
      sbuff.append(valueFields.get(col));
      sbuff.append("=?,");
    }
    sbuff.setCharAt(sbuff.length() - 1, ' ');
    sbuff.append("where ");
    for (int col = 0; col < numPkCols; col++) 
    {
      if(col>0)
      {
        sbuff.append("and ");
      }
      sbuff.append(keyFields.get(col));
      sbuff.append("=? ");
    }
    return sbuff.toString();
  }

  /**
   * Set the parameters to the prepared statement for a
   * {@link AsyncEvent.Type#BULK_DML} or {@link AsyncEvent.Type#BULK_INSERT} operation.
   * The implementation creates a batch for {@link AsyncEvent.Type#BULK_INSERT} and
   * also tries to add as a batch for {@link AsyncEvent.Type#BULK_DML} in case the
   * previous prepared statement is same as this one.
   * 
   * @param event
   *          the {@link AsyncEvent} object
   * @param evType
   *          the {@link AsyncEvent.Type} of the event
   * @param ps
   *          the prepared statement to be used for prepare
   * @param prevPS
   *          the prepared statement used for the previous event; in case it is
   *          same as the current one the new update for a
   *          {@link AsyncEvent.Type#BULK_DML} operation will be added as a batch
   * @param sync
   *          the {@link DBSynchronizer} object, if any; it is used to store
   *          whether the current driver is JDBC4 compliant to enable performing
   *          BLOB/CLOB operations {@link PreparedStatement#setBinaryStream},
   *          {@link PreparedStatement#setCharacterStream}
   * 
   * @return true if the event was {@link AsyncEvent.Type#BULK_INSERT} and false
   *         otherwise
   * 
   * @throws SQLException
   *           in case of an exception in getting meta-data or setting
   *           parameters
   */
//  public final boolean setParamsInBulkPreparedStatement(AsyncEvent event,
//      AsyncEvent.Type evType, PreparedStatement ps, PreparedStatement prevPS,
//      DBSynchronizer sync) throws SQLException {
//    if (evType.isBulkInsert()) {
//      // need to deserialize individual rows and execute as batch prepared
//      // statement
//      final ResultSetMetaData rsmd = event.getResultSetMetaData();
//      final int numColumns = rsmd.getColumnCount();
//      final ResultSet rows = event.getNewRowsAsResultSet();
//      // prepared statement will already be set correctly to skip auto-increment
//      // columns assuming getInsertString was used to get the SQL string
//      final boolean skipAutoGenCols = (event.tableHasAutogeneratedColumns()
//          && (sync == null || sync.skipIdentityColumns()));
//      while (rows.next()) {
//        int paramIndex = 1;
//        for (int rowPos = 1; rowPos <= numColumns; rowPos++) {
//          if (!skipAutoGenCols || !rsmd.isAutoIncrement(rowPos)) {
//            setColumnInPrepStatement(rsmd.getColumnType(rowPos), ps, rows,
//                rowPos, paramIndex, sync);
//            paramIndex++;
//          }
//        }
//        ps.addBatch();
//      }
//      return true;
//    }
//    else {
//      if (prevPS == ps) {
//        // add a new batch of values
//        ps.addBatch();
//      }
//      final ResultSet params = event.getNewRowsAsResultSet();
//      final ResultSetMetaData rsmd;
//      final int columnCount;
//      if (params != null
//          && (columnCount = (rsmd = params.getMetaData()).getColumnCount()) > 0) {
//        for (int paramIndex = 1; paramIndex <= columnCount; paramIndex++) {
//          setColumnInPrepStatement(rsmd.getColumnType(paramIndex), ps, params,
//              paramIndex, paramIndex, sync);
//        }
//      }
//      return false;
//    }
//  }

  /**
   * Set column value at given index in a prepared statement. The implementation
   * tries using the matching underlying type to minimize any data type
   * conversions, and avoid creating wrapper Java objects (e.g. {@link Integer}
   * for primitive int).
   * 
   * @param type
   *          the SQL type of the column as specified by JDBC {@link Types}
   *          class
   * @param ps
   *          the prepared statement where the column value has to be set
   * @param row
   *          the source row as a {@link ResultSet} from where the value has to
   *          be extracted
   * @param rowPosition
   *          the 1-based position of the column in the provided
   *          <code>row</code>
   * @param paramIndex
   *          the 1-based position of the column in the target prepared
   *          statement (provided <code>ps</code> argument)
   * @param sync
   *          the {@link DBSynchronizer} object, if any; it is used to store
   *          whether the current driver is JDBC4 compliant to enable performing
   *          BLOB/CLOB operations {@link PreparedStatement#setBinaryStream},
   *          {@link PreparedStatement#setCharacterStream}
   * 
   * @throws SQLException
   *           in case of an exception in setting parameters
   */
  public final void setColumnInPrepStatement(String type,Object val,PreparedStatement ps, final DBSynchronizer sync,int paramIndex) throws SQLException {
    switch (type) {
      case JavaTypes.STRING:
          if(val == null || StringUtils.isEmpty(val.toString()))
              ps.setNull(paramIndex, Types.VARCHAR);
          else
          {
              final String realVal = (String)val;
              ps.setString(paramIndex, realVal);
          }
        break;
      case JavaTypes.INT1:
      case JavaTypes.INT2:
      case JavaTypes.INT3:
          if(val == null || StringUtils.isEmpty(val.toString()))
              ps.setNull(paramIndex, Types.INTEGER);
          else
          {
              final int realVal = (int)val;
              ps.setInt(paramIndex, realVal);
          }
          break;
      case JavaTypes.DOUBLE1:
      case JavaTypes.DOUBLE2:
          if(val == null || StringUtils.isEmpty(val.toString()))
              ps.setNull(paramIndex, Types.DOUBLE);
          else
          {
              final double realVal = (double)val;
              ps.setDouble(paramIndex, realVal);
          }
        break;
      case JavaTypes.FLOAT1:
      case JavaTypes.FLOAT2:
          if(val == null || StringUtils.isEmpty(val.toString()))
              ps.setNull(paramIndex, Types.FLOAT);
          else
          {
              final float realVal = (float)val;
              ps.setDouble(paramIndex, realVal);
          }
        break;
      case JavaTypes.BOOLEAN1:
      case JavaTypes.BOOLEAN2:
          if(val == null || StringUtils.isEmpty(val.toString()))
              ps.setNull(paramIndex, Types.BOOLEAN);
          else
          {
              final boolean realVal = (boolean)val;
              ps.setBoolean(paramIndex, realVal);
          }
        break;
      case JavaTypes.DATE_SQL:
          if(val == null || StringUtils.isEmpty(val.toString()))
              ps.setNull(paramIndex, Types.DATE);
          else
          {
              final Date realVal = (Date)val;
              ps.setDate(paramIndex, realVal);
          }
        break;
      case JavaTypes.DATE_UTIL:
          if(val == null || StringUtils.isEmpty(val.toString()))
              ps.setNull(paramIndex, Types.DATE);
          else
          {
              final java.util.Date realVal = (java.util.Date)val;
              ps.setDate(paramIndex, new Date(realVal.getTime()));
          }
        break;
      case JavaTypes.BIGDECIMAL:
          if(val == null || StringUtils.isEmpty(val.toString()))
              ps.setNull(paramIndex, Types.DECIMAL);
          else
          {
              final BigDecimal realVal = (BigDecimal)val;
              ps.setBigDecimal(paramIndex, realVal);
          }
        break;
      case JavaTypes.TIME:
          if(val == null || StringUtils.isEmpty(val.toString()))
              ps.setNull(paramIndex, Types.TIME);
          else
          {
              final Time realVal = (Time)val;
              ps.setTime(paramIndex, realVal);
          }
        break;
      case JavaTypes.TIMESTAMP:
          if(val == null || StringUtils.isEmpty(val.toString()))
              ps.setNull(paramIndex, Types.TIMESTAMP);
          else
          {
              final Timestamp realVal = (Timestamp)val;
              ps.setTimestamp(paramIndex, realVal);
          }
        break;
      case JavaTypes.OBJECT:
          if(val == null || StringUtils.isEmpty(val.toString()))
              ps.setNull(paramIndex, Types.JAVA_OBJECT);
          else
          {
              final Object realVal = (Object)val;
              ps.setObject(paramIndex, realVal);
          }
        break;
      default:
        throw new UnsupportedOperationException("java.sql.Type = "
            + type + " not supported");
    }
  }

  /**
   * Check if logging for the same event repeatedly has to be skipped. This
   * keeps a map of failed event against the last logged time and doubles the
   * time interval to wait before next logging on every subsequent call for the
   * same event.
   */
  public final boolean skipFailureLogging(AsyncEvent event) {
    boolean skipLogging = false;
    // if map has become large then give up on new events but we don't expect
    // it to become too large in practise
    if (this.failureLogInterval.size() < FAILURE_MAP_MAXSIZE) {
      // first long in logInterval gives the last time when the log was done,
      // and the second tracks the current log interval to be used which
      // increases exponentially
      // multiple currentTimeMillis calls below may hinder performance
      // but not much to worry about since failures are expected to
      // be an infrequent occurance (and if frequent then we have to skip
      // logging for quite a while in any case)
      long[] logInterval = this.failureLogInterval.get(event);
      if (logInterval == null) {
        logInterval = this.failureLogInterval.putIfAbsent(event,
            new long[] { System.currentTimeMillis(), 1000 });
      }
      if (logInterval != null) {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - logInterval[0]) < logInterval[1]) {
          skipLogging = true;
        }
        else {
          logInterval[0] = currentTime;
          // don't increase logInterval to beyond a limit (5 mins by default)
          if (logInterval[1] <= (FAILURE_LOG_MAX_INTERVAL / 4)) {
            logInterval[1] *= 4;
          }
          // TODO: should the retries be throttled by some sleep here?
        }
      }
    }
    return skipLogging;
  }

  /**
   * After a successful event execution remove from failure map if present (i.e.
   * if the event had failed on a previous try).
   */
  public final boolean removeEventFromFailureMap(AsyncEvent event) {
    return this.failureLogInterval.remove(event) != null;
  }  
  /**
   * Return true if failure map has entries.
   */
  public final boolean hasFailures() {
    return this.failureLogInterval.size() > 0;
  }

  /**
   * Encrypt the password of a given user for storage in file or memory.
   * 
   * @param user
   *          the name of user
   * @param password
   *          the password to be encrypted
   * @param transformation
   *          the algorithm to use for encryption e.g. AES/ECB/PKCS5Padding or
   *          Blowfish (e.g. see <a href=
   *          "http://docs.oracle.com/javase/6/docs/technotes/guides/security/SunProviders.html"
   *          >Sun Providers</a> for the available names in Oracle's Sun JDK}
   * @param keySize
   *          the size of the private key of the given "transformation" to use
   */
//  public static final String encryptPassword(String user, String password,
//      String transformation, int keySize) throws Exception {
//    return GemFireXDUtils.encrypt(password, transformation, GemFireXDUtils
//        .getUserPasswordCipherKeyBytes(user, transformation, keySize));
//  }

  /**
   * Decrypt the password of a given user encrypted using
   * {@link #encryptPassword(String, String, String, int)}.
   * 
   * @param user
   *          the name of user
   * @param encPassword
   *          the encrypted password to be decrypted
   * @param transformation
   *          the algorithm to use for encryption e.g. AES/ECB/PKCS5Padding or
   *          Blowfish (e.g. see <a href=
   *          "http://docs.oracle.com/javase/6/docs/technotes/guides/security/SunProviders.html"
   *          >Sun Providers</a> for the available names in Oracle's Sun JDK}
   * @param keySize
   *          the size of the private key of the given "transformation" to use
   */
//  public static final String decryptPassword(String user, String encPassword,
//      String transformation, int keySize) throws Exception {
//    return GemFireXDUtils.decrypt(encPassword, transformation, GemFireXDUtils
//        .getUserPasswordCipherKeyBytes(user, transformation, keySize));
//  }

  /**
   * Any cleanup required when closing.
   */
  public void close() {
    this.failureLogInterval.clear();
  }

  /**
   * Return a new GemFireXD runtime exception to wrap an underlying exception with
   * optional message detail. When an external exception is received in a
   * callback, using this method to wrap it is recommended for GemFireXD engine to
   * deal with it cleanly.
   * 
   * @param message
   *          the detail message of the exception
   * @param t
   *          the underlying exception to be wrapped
   */
  public RuntimeException newRuntimeException(String message, Throwable t) {
    return new RuntimeException(message, t);
  }
  
//  public void createEventErrorLogger(String errorFileName) {
//    // Use the default if not provided
//    if (errorFileName == null) {
//      errorFileName = EVENT_ERROR_LOG_FILE;
//    }
//    evErrorLogger = new EventErrorLogger(errorFileName);
//  }
  
//  public void logEventError(AsyncEvent ev, Exception e) throws Exception {
//    if (evErrorLogger == null) {
//      throw new Exception("AsyncEvent Error Logger not created");
//    }
//    this.log(logger, level, t, message);
//  }
}
