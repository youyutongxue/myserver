package com.uniquedu.json;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionFactory {
	private ConnectionFactory(){
		
	} 
	private static ConnectionFactory factory;
	
	public static synchronized ConnectionFactory newInstance(){
		if(factory==null){
			factory=new ConnectionFactory();
		}
		return factory;
	}
	/**
	 * 使用的tomcat的数据库连接池的方法获得数据库的连接
	 * @return 和数据库的连接
	 */
	public Connection createConnection(){
		Connection connection=null;
		try {
			Context context=new InitialContext();
			DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/TestDB");//获取数据库连接
			connection=ds.getConnection();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
}
