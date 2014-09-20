package org.communinet.billing.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.communinet.billing.domain.Customer;
import org.communinet.billing.domain.CustomerAccount;
import org.communinet.billing.domain.IPTraffic;
import org.communinet.billing.domain.Plan;
import org.communinet.billing.spec.ICNetBillingSpec;
import org.communinet.billing.spec.provided.IDataManager;
import org.communinet.billing.spec.provided.IFirewallManager;
import org.communinet.billing.spec.provided.INotificationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CNetBillingBean implements ICNetBillingSpec {

	@Autowired
	IDataManager iDataManager;
	
	@Autowired
	IFirewallManager iFirewallManager;
	
	@Autowired
	INotificationManager iNotificationManager;
	
	public CNetBillingBean() {
	}
	
	public List<Customer> getAllCustomers() throws SQLException {
		return iDataManager.getAllCustomers();
	}

	public Customer getCustomerBySubnet(String subnet, String cidr)
			throws SQLException {
		
		return iDataManager.getCustomerBySubnet(subnet, cidr);
	}

	public List<Customer> getAllActiveCustomers() throws SQLException {
		return iDataManager.getAllActiveCustomers();
	}
	
	
	public void addFirewallExceptions(List<CustomerAccount> allActiveAccounts){
		iFirewallManager.addFirewallExceptions(allActiveAccounts);
	}

	public void addCustomer(Customer customer) {
		iDataManager.addCustomer(customer);
		
	}
	
	public void notifyCustomer(String subnet, int cidr, int messageId) {
		iNotificationManager.notifyCustomer(subnet, cidr, messageId);
		
	}
	
	public Plan getBillinPlan(int planId){
		return iDataManager.getBillinPlan(planId);
	}
	
	public Double getDataUsageAtDate(String subnet, int cidr, Date date){
		return iDataManager.getDataUsageAtDate(subnet, cidr, date);
	}
	
	public Double getUsageForCurrentMonth(String subnet, int cidr){
		return iDataManager.getUsageForCurrentMonth(subnet, cidr);
	}

	@Override
	public void addDataUsage(IPTraffic traffic, Double bytesUsed) {
		iDataManager.addDataUsage(traffic, bytesUsed);
		
	}

}
