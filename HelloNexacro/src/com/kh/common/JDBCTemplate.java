package com.kh.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class JDBCTemplate {
	
	public static Connection getConnection(){
		Connection conn = null;
		try {
			//DBCP를 이용한 Connection객체이용
			//javax.naming.InitialContext.InitialContext() throws NamingException
			Context ctx = new InitialContext();
			/*
			 * jndi구조
			 * /			=> 루트디렉토리:comp말고 다른 하위디렉토리는 아직 없음. 향후, user, org등 추가예정)
			 * 	└comp
			 * 		└env	=> dd에 기록된 component환경변수가 등록됨.
			 * 		└UserTransaction
			 */
			//initCtx의 lookup메서드를 이용해서 "java:/comp/env" 에 해당하는 객체를 리턴.
//			Context envContext  = (Context)ctx.lookup("java:/comp/env");
			
			//envCtx의 lookup메서드를 이용해서 "jdbc/orcl"에 해당하는 객체를 리턴
			//type은 javax.sql.DataSource
//			DataSource pool = (DataSource)envContext.lookup("jdbc/myoracle");
			
			//축약버젼
			DataSource pool = (DataSource)ctx.lookup("java:comp/env/jdbc/myoracle");//자바환경설정+server.xml의 추가코드 name
			conn = pool.getConnection();
			conn.setAutoCommit(false);// 기본값은 true, 원칙은 application에서 모든걸 제어하는 것임.
			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return conn;
	}
	
	
	
	/*public static Connection getConnection(){
		Connection conn = null;
		Properties prop = new Properties();
		
		try {
			//클래스객체 위치찾기 : 절대경로를 반환한다. 
			// "/" : 루트디렉토리를 절대경로로 URL객체로 반환한다.
			// "./" : 현재디렉토리를 절대경로로 URL객체로 반환한다.
			// "./driver.properties : 현재경로의 driver.properties파일의 경로를 URL객체로 반환함.
			//URL java.lang.Class.getResource(String name)
			String fileName = JDBCTemplate.class.getResource("/driver.properties").getPath();
			//System.out.println(fileName);
			prop.load(new FileReader(fileName));
			
			String driver = prop.getProperty("driver");
			String url = prop.getProperty("url");
			String user = prop.getProperty("user");
			String password = prop.getProperty("password");
			
			//클래스객체등록
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);// 기본값은 true, 원칙은 application에서 모든걸 제어하는 것임.
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}*/
	
	public static void close(Connection conn){
		try {
			if(conn!=null && !conn.isClosed())
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Statement stmt){
		try {
			if(stmt!=null & !stmt.isClosed())
				stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet rset){
		try {
			if(rset!=null && !rset.isClosed())
				rset.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void commit(Connection conn){
		try {
			if(conn!=null && !conn.isClosed())
				conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void rollback(Connection conn){
		try {
			if(conn!=null && !conn.isClosed())
				conn.rollback();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
