package org.communinet.billing.spec;

import org.communinet.billing.domain.IPTraffic;

public interface ICNetBillingServerSpec {

	void addDataUsage(IPTraffic ip_data, double d);

}
