package org.communinet.billing.impl;

import java.util.List;

import org.communinet.billing.domain.CustomerAccount;
import org.communinet.billing.domain.FileUtilities;
import org.communinet.billing.impl.dao.FirewallAdapter;
import org.communinet.billing.spec.provided.IFirewallManager;
import org.springframework.stereotype.Component;

@Component
public class FirewallManager implements IFirewallManager {

	FirewallAdapter adapter;

	public FirewallManager() {
		adapter = new FirewallAdapter(FileUtilities.FIREWALL_FILENAME);
	}

	public void addFirewallExceptions(List<CustomerAccount> activeAccounts) {

		adapter.clearRules();

		for (CustomerAccount account : activeAccounts) {
			String subnet = account.getNetworkSubnet().getIpSubnet() +"/"+ account.getNetworkSubnet().getCidr();
			adapter.addRule(subnet);
		}
		
		adapter.writeRules();
	}


}
