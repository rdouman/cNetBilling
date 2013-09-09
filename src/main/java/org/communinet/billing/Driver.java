package org.communinet.billing;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.communinet.billing.db.Synchronise;
import org.communinet.billing.ip.IpMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Driver {
	
	static final Logger logger = LoggerFactory.getLogger(Driver.class);
	// 10 minutes
	public static final int LOOP_DELAY = 10 * 1000 * 60 ;
	/** 
	 * Main startup method 
	 *  
	 * @param args 
	 *          ignored 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws InterruptedException 
	 */  
	
	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException 
	{ 

		logger.info("Initializing billing application");
		
		IpMonitor monitor = null; 

		Synchronise database = null;

		// This while loop address an issue in the jnetpcap library
		// where the library crashes sporadically.
		do {
			
			if(monitor == null || !monitor.isAlive())
			{
				logger.info("Starting ip monitor");
				monitor = new IpMonitor();
				monitor.start();
			}

			if(database == null || !database.isAlive())
			{
				logger.info("Starting database synchronization");
				database = new Synchronise(monitor);
				database.start();
			}

			Thread.sleep(LOOP_DELAY);
			
		}while (true);
		
		
	}
}
