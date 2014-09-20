package org.communinet.billing.impl;

import org.communinet.billing.domain.IPTraffic;
import org.communinet.billing.spec.ICNetBillingServerSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CNetBillingServer implements ICNetBillingServerSpec {

	@Autowired
	IDataAdapter data;
	
	static final Logger logger = LoggerFactory.getLogger(CNetBillingServer.class);

	public void addDataUsage(IPTraffic ip_data, double usage) {
		try {
			data.addDataUsage(ip_data, usage);
		} catch (Exception e) {
			logger.error("Cannot log data for ip_data {}", ip_data);
			throw new RuntimeException(e);
		}

	}

}
