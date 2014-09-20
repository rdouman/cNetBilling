package org.communinet.billing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.communinet.billing.domain.IPTraffic;
import org.communinet.billing.spec.ICNetBillingServerSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Synchronise extends Thread
{

    private NetworkTrafficMonitor monitor = null;
    private int MINUTES = 1000 * 60;
    private int TIME_OUT = 1 * MINUTES;
    


	
	
	private ICNetBillingServerSpec icNetBilling;
	
	static final Logger logger = LoggerFactory.getLogger(Synchronise.class);

	public Synchronise(NetworkTrafficMonitor monitor, ICNetBillingServerSpec iCNetBillingSpec) throws Exception 
	{
		this.monitor = monitor;
		this.icNetBilling = iCNetBillingSpec;
	}


	@Override
	public void run() 
	{
		for (;;)
		{
			try 
			{
				Map<IPTraffic, Long> trafficMap = monitor.getTrafficMap();
				monitor.resetTotals();
				update(trafficMap);
				
				sleep(TIME_OUT);
			} 
			catch (Exception e) 
			{
				logger.error("Error synchronising with db:\n{}",e);
			}
		}
	}
	
	private void update(Map<IPTraffic, Long> map){
            Set<IPTraffic> keySet = map.keySet();
            IPTraffic[] array = keySet.toArray(new IPTraffic[keySet.size()]);
            logger.debug("Updating database");
			for(int i=0; i < array.length; i++) {
				IPTraffic ip_data = array [i];
				icNetBilling.addDataUsage(ip_data, (double)map.get(ip_data));
            }
	}
	
}
