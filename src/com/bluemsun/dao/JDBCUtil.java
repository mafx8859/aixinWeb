package com.bluemsun.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBCUtil {
	 private static DataSource dataSource=null;  
	    static{  
	        dataSource=new ComboPooledDataSource("aixinPool");
	    }
	    public static Connection getConnection(){  
	        Connection conn=null;  
	        try {  
	             conn=dataSource.getConnection();  
	        } catch (SQLException e) {  
	            e.printStackTrace();  
	        }  
	        return conn;  
	    }  
	  
	    public static DataSource getDataSource() {
	        return dataSource;
	    }
	    /** 
	     * 
	     * @param conn 
	     */  
	    public static void closeConn(Connection conn){  
	        try {  
	            if(conn!=null && conn.isClosed()){  
	                conn.close();  
	            }  
	        } catch (SQLException e) {  
	            e.printStackTrace();  
	        }  
	    }  
}
