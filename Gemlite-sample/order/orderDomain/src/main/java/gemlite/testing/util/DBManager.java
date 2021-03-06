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
package gemlite.testing.util;

import gemlite.core.util.LogUtil;
import gemlite.core.util.Util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class DBManager {
	private ClassPathXmlApplicationContext ctx = null;
	private JdbcTemplate jdbcTemplate;
	private BasicDataSource dataSource;
	//table shcema
	private String schema ;

	public String getSchema()
	{
		return schema;
	}

	public void setSchema(String schema)
	{
		this.schema = schema;
	}

	public void setMaxActive(int maxActive) {
		dataSource.setMaxActive(maxActive);
	}

	private final static DBManager inst = new DBManager();
	private AtomicBoolean useable = new AtomicBoolean(false);

	private DBManager() {
		this.connect();
	}

	public final static DBManager getInstance() {
		return inst;
	}

	public final void connect() {
		LogUtil.getAppLog().info("Start to connect to Oracle database. ");
		ctx = Util.initContext("classpath:oracle-jdbc.xml");
		jdbcTemplate = ctx.getBean(JdbcTemplate.class);
		dataSource = ctx.getBean(BasicDataSource.class);
        
		if (checkConnection(dataSource)) {
			useable.set(true);
		} else {
			ctx.close();
		}
	}

	public final void disconnect() {
		useable.set(false);
		// TODO:/& �s�ޥ
		ctx.close();
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public boolean isUseable() {
		if (!useable.get()) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.connect();
		}
		return useable.get();
	}
	
	public void reconnect()
	{
		ctx = Util.initContext("ruleinfo-jdbc.xml");
		jdbcTemplate = ctx.getBean(JdbcTemplate.class);
		dataSource = ctx.getBean(BasicDataSource.class);
		useable.set(true);
	}
	
	private boolean checkConnection(BasicDataSource dataSource) {

		try {
			Connection conn = dataSource.getConnection();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public static void main(String[] args){
		DBManager inst = DBManager.inst;
		System.out.println("isUseable:" + inst.isUseable());
		
	}
}
