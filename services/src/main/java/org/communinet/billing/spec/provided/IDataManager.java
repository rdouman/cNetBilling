package org.communinet.billing.spec.provided;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.communinet.billing.domain.Customer;
import org.communinet.billing.domain.IPTraffic;
import org.communinet.billing.domain.Plan;

public interface IDataManager {
	
	public List<Customer>  getAllCustomers() throws SQLException;
	
	public Customer getCustomerBySubnet(String subnet, String cidr) throws SQLException;
	
	public List<Customer> getAllActiveCustomers() throws SQLException;
	
	public Plan getBillinPlan(int planId);
	
	public void addCustomer(Customer customer);
	
	public void addDataUsage(IPTraffic traffic, Double bytesUsed);
	
	public Double getDataUsageAtDate(String subnet, int cidr, Date date);
	
	public Double getUsageForCurrentMonth(String subnet, int cidr);
}
