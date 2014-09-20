package org.communinet.billing;

import org.communinet.billing.spec.ICNetBillingServerSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Driver {

	static final Logger logger = LoggerFactory.getLogger(Driver.class);
	// 10 minutes
	public static final int LOOP_DELAY = 1 * 1000 * 60;

	/**
	 * Main startup method
	 * 
	 * @param args
	 *            ignored
	 * @throws Exception 
	 */

	public static void main(String[] args) throws Exception {

		    
		NetworkTrafficMonitor monitor = null;

		Synchronise database = null;

		// create and configure beans
		ApplicationContext context = new FileSystemXmlApplicationContext(
				new String[] { "config/spring.xml" });

		ICNetBillingServerSpec iCNetBillingSpec = (ICNetBillingServerSpec) context
				.getBean("CNetBillingServer");

		// retrieve configured instance
		///CustomerManager firewallRules = new CustomerManager(iCNetBillingSpec);

		// This while loop address an issue in the jnetpcap library
		// where the library crashes sporadically.
		do {
			logger.debug("The traffic monitor is set to {}", monitor);
			logger.debug("The database synchronisation is set to {}", database);
		//	logger.debug("The firewall synchronisation is is set to {}", firewallRules);
			try {
				if (monitor == null || !monitor.isAlive()) {
					logger.info("Starting ip monitor");
					monitor = new NetworkTrafficMonitor();
					monitor.start();
				}

				if (database == null || !database.isAlive()) {
					logger.info("Starting database synchronization");
					database = new Synchronise(monitor, iCNetBillingSpec);
					database.start();
				}

		/*		if (firewallRules == null || !firewallRules.isAlive()) {
					logger.info("Start firewall rules manager");
					firewallRules = new CustomerManager(iCNetBillingSpec);
					firewallRules.start();
				}*/
				

			} finally {
				Thread.sleep(LOOP_DELAY);
			}
		} while (true);

	}
}
