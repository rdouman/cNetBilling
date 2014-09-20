package org.communinet.billing.impl;

import org.communinet.billing.domain.IPTraffic;

public interface IDataAdapter {
	public void addDataUsage(IPTraffic ip_data, double usage)throws Exception;
}
