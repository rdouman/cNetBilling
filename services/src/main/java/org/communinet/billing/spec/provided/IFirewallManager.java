package org.communinet.billing.spec.provided;

import java.util.List;

import org.communinet.billing.domain.CustomerAccount;

public interface IFirewallManager {
	public void addFirewallExceptions(List<CustomerAccount> activeAccounts);
}
