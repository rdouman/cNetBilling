package org.communinet.billing.impl.dao;

import java.util.Date;
import java.util.List;

import org.communinet.billing.impl.dao.jpa.BillinPlan;
import org.communinet.billing.impl.dao.jpa.Customer;
import org.communinet.billing.impl.dao.jpa.CustomerAccount;
import org.communinet.billing.impl.dao.jpa.DataUsage;

public interface IDataManagerDao {
	public Customer getCustomerInfoBySubnet(String subnet, int cidr);
	
	public void addDataUsage(DataUsage usage);
	
	public double getDataUsage(String subnet, int cidr, Date startDate,
			Date endDate);

	public List<Customer> getAllCustomers();

	public BillinPlan getBillingPlanById(int planId);

	public void addCustomer(Customer customer);

	public List<CustomerAccount> getCustomerAccountsByCustomerId(int customerId);
	
}
