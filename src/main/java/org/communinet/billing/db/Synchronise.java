package org.communinet.billing.db;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.communinet.billing.ip.IpMonitor;
import org.communinet.billing.ip.NetworkTraffic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Synchronise extends Thread
{
	 /* the default framework is embedded*/
    private String driver = "org.apache.derby.jdbc.ClientDriver";
    private String protocol = "jdbc:derby://localhost:1527/";
    private IpMonitor monitor = null;
    private int MINUTES = 1000 * 60;
    private int TIME_OUT = 10 * MINUTES;
	private Connection conn;
	
	static final Logger logger = LoggerFactory.getLogger(Synchronise.class);

	public Synchronise(IpMonitor monitor) 
	{
		this.monitor = monitor;
		try
		{
			setup();
		} 
		catch (SQLException e)
		{
			logger.error("Caught Exception in Synchronise:\n {}",e);
		}
	}

	private void setup() throws SQLException 
	{
		loadDriver();
		
		Properties props = new Properties(); // connection properties

        String dbName = "cNetBilling"; // the name of the database

        /*
         * This connection specifies create=true in the connection URL to
         * cause the database to be created when connecting for the first
         * time. To remove the database, remove the directory derbyDB (the
         * same as the database name) and its contents.
         *
         * The directory derbyDB will be created under the directory that
         * the system property derby.system.home points to, or the current
         * directory (user.dir) if derby.system.home is not set.
         */
         conn = DriverManager.getConnection(protocol + dbName, props);

        System.out.println("Connected to and created database " + dbName);
		
		
	}
	
	@Override
	public void run() 
	{
		for (;;)
		{
			try 
			{
	            // We want to control transactions manually. Autocommit is on by
	            // default in JDBC.
	            conn.setAutoCommit(false);
				Map<NetworkTraffic, Long> trafficMap = monitor.getTrafficMap();
				monitor.resetTotals();
				update(trafficMap,conn);
				
				sleep(TIME_OUT);
			} 
			catch (Exception e) 
			{
				logger.error("Error synchronising with db:\n{}",e);
			}
		}
	}
	private void update(Map<NetworkTraffic, Long> map, Connection conn)
	{

		/* We will be using Statement and PreparedStatement objects for
		 * executing SQL. These objects, as well as Connections and ResultSets,
		 * are resources that should be released explicitly after use, hence
		 * the try-catch-finally pattern used below.
		 * We are storing the Statement and Prepared statement object references
		 * in an array list for convenience.
		 */
		/* This ArrayList usage may cause a warning when compiling this class
		 * with a compiler for J2SE 5.0 or newer. We are not using generics
		 * because we want the source to support J2SE 1.4.2 environments. */
		
		ArrayList<Statement> statements = new ArrayList<Statement>(); // list of Statements, PreparedStatements
		PreparedStatement psInsert = null;
		Statement s = null;
		ResultSet rs = null;
		try
		{
		

            /* Creating a statement object that we can use for running various
             * SQL statements commands against the database.*/
            s = conn.createStatement();
            statements.add(s);

            psInsert = conn.prepareStatement(
                        "insert into IP_COUNTER values (?, ?, ?, ?, ?, ?)");
            
            statements.add(psInsert);

            Set<NetworkTraffic> keySet = map.keySet();
            NetworkTraffic[] array = keySet.toArray(new NetworkTraffic[keySet.size()]);
			for(int i=0; i < array.length; i++)
            {
				NetworkTraffic ip_data = array [i];
				
            	int param =1;
            	psInsert.setString(param++, ip_data.getIp());
            	System.out.println("writing "+map.get(ip_data)+ "bytes");
            	System.out.println("Capturing "+(double)map.get(ip_data)/1024/1024+ " megs");
            	psInsert.setDouble(param++, (double)map.get(ip_data)/1024/1024);
            	psInsert.setTimestamp(param++, new Timestamp(new Date().getTime()));
            	psInsert.setTimestamp(param++, new Timestamp(new Date().getTime()));
            	psInsert.setString(param++,ip_data.getPort());
            	psInsert.setBoolean(param++,ip_data.isSource());
            	psInsert.execute();
            	System.out.println("insert successful");
            	
            }
            
            rs = report(rs,conn,s);
            
            conn.commit();
            System.out.println("Committed the transaction");

        }
        catch (SQLException sqle)
        {
            printSQLException(sqle);
        } finally {
            // release all open resources to avoid unnecessary memory usage

            // ResultSet
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
            } catch (SQLException sqle) {
                printSQLException(sqle);
            }

            // Statements and PreparedStatements
            int i = 0;
            while (!statements.isEmpty()) {
                // PreparedStatement extend Statement
                Statement st = (Statement)statements.remove(i);
                try {
                    if (st != null) {
                        st.close();
                        st = null;
                    }
                } catch (SQLException sqle) {
                    printSQLException(sqle);
                }
            }

        }
	}
	
	// We actually want to move this code to a class/thread of its own.
	private ResultSet report(ResultSet rs, Connection conn2, Statement s)
	{
		/*
          We select the rows and verify the results.
		 */
		BufferedWriter outputStream = null;
		try
		{
			rs = s.executeQuery("select IP_ADDRESS, sum(total_data), upload from ip_counter group by ip_address, upload");
			
				outputStream = new BufferedWriter(new FileWriter("outputdata.txt"));
				
			
			while (rs.next())
			{
				System.out.println("Reading result set");
				int param = 1;
				String address = rs.getString(param++);
				double total = rs.getDouble(param++);
				boolean is_upload = rs.getBoolean(param++);
				
				outputStream.write(address+" "+Math.round(total)+" "+((is_upload == true)?"up":"down")+"\n");
				outputStream.flush();

			}
			
			System.out.println("Done processing result set");
		} 
		catch (SQLException e) 
		{
			System.out.println("Fail to generate usage report");
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			System.out.println("Fail to generate usage report");
			e.printStackTrace();
		}
		finally
		{
			try {
				if(outputStream != null)
				{
					outputStream.close();
				}
			} catch (IOException e) 
			{
				System.out.println("Error closing file stream");
				e.printStackTrace();
			}
		}

		return rs;

	}

	/**
     * Loads the appropriate JDBC driver for this environment/framework. For
     * example, if we are in an embedded environment, we load Derby's
     * embedded Driver, <code>org.apache.derby.jdbc.EmbeddedDriver</code>.
     */
    private void loadDriver() {
        /*
         *  The JDBC driver is loaded by loading its class.
         *  If you are using JDBC 4.0 (Java SE 6) or newer, JDBC drivers may
         *  be automatically loaded, making this code optional.
         *
         *  In an embedded environment, this will also start up the Derby
         *  engine (though not any databases), since it is not already
         *  running. In a client environment, the Derby engine is being run
         *  by the network server framework.
         *
         *  In an embedded environment, any static Derby system properties
         *  must be set before loading the driver to take effect.
         */
        try {
            Class.forName(driver).newInstance();
            System.out.println("Loaded the appropriate driver");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("\nUnable to load the JDBC driver " + driver);
            System.err.println("Please check your CLASSPATH.");
            cnfe.printStackTrace(System.err);
        } catch (InstantiationException ie) {
            System.err.println(
                        "\nUnable to instantiate the JDBC driver " + driver);
            ie.printStackTrace(System.err);
        } catch (IllegalAccessException iae) {
            System.err.println(
                        "\nNot allowed to access the JDBC driver " + driver);
            iae.printStackTrace(System.err);
        }
    }
    
    /**
     * Prints details of an SQLException chain to <code>System.err</code>.
     * Details included are SQL State, Error code, Exception message.
     *
     * @param e the SQLException from which to print details.
     */
    public static void printSQLException(SQLException e)
    {
        // Unwraps the entire exception chain to unveil the real cause of the
        // Exception.
        while (e != null)
        {
            System.err.println("\n----- SQLException -----");
            System.err.println("  SQL State:  " + e.getSQLState());
            System.err.println("  Error Code: " + e.getErrorCode());
            System.err.println("  Message:    " + e.getMessage());
            // for stack traces, refer to derby.log or uncomment this:
            //e.printStackTrace(System.err);
            e = e.getNextException();
        }
    }

}
