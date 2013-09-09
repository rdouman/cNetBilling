package org.communinet.billing.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ShutDown extends Thread {
	private Connection connection;

	public ShutDown(Connection conn) {
		this.connection = conn;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		 try {
             if (connection != null) {
            	 System.out.println("Running shutdown hook");
            	 connection.close();
            	 connection = null;
            	 try
                 {
                     // the shutdown=true attribute shuts down Derby
                     DriverManager.getConnection("jdbc:derby:;shutdown=true");

                     // To shut down a specific database only, but keep the
                     // engine running (for example for connecting to other
                     // databases), specify a database in the connection URL:
                     //DriverManager.getConnection("jdbc:derby:" + dbName + ";shutdown=true");
                 }
                 catch (SQLException se)
                 {
                     if (( (se.getErrorCode() == 50000)
                             && ("XJ015".equals(se.getSQLState()) ))) {
                         // we got the expected exception
                         System.out.println("Derby shut down normally");
                         // Note that for single database shutdown, the expected
                         // SQL state is "08006", and the error code is 45000.
                     } else {
                         // if the error code or SQLState is different, we have
                         // an unexpected exception (shutdown failed)
                         System.err.println("Derby did not shut down normally");
                     }
                 }
             }
         } catch (SQLException sqle) {
             System.out.println(sqle);
         }
	}

}
