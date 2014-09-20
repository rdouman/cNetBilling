package org.communinet.billing.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.communinet.billing.domain.Customer;
import org.communinet.billing.domain.IPTraffic;
import org.communinet.billing.domain.NetworkSubnet;
import org.communinet.billing.domain.Plan;
import org.communinet.billing.impl.dao.IDataManagerDao;
import org.communinet.billing.impl.dao.jpa.BillinPlan;
import org.communinet.billing.impl.dao.jpa.CustomerAccount;
import org.communinet.billing.impl.dao.jpa.DataUsage;
import org.communinet.billing.spec.provided.IDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataManager implements IDataManager {

	@Autowired
	IDataManagerDao dataManagerDao;

	public List<Customer> getAllCustomers() throws SQLException {
		return unwrapCustomers(dataManagerDao.getAllCustomers());
	}

	public Customer getCustomerBySubnet(String subnet, String cidr)
			throws SQLException {
		Customer customerInfo = unwrapCustomer(dataManagerDao
				.getCustomerInfoBySubnet(subnet, Integer.parseInt(cidr)));
		return customerInfo;
	}

	public List<Customer> getAllActiveCustomers() throws SQLException {
		List<Customer> allCustomers = getAllCustomers();
		List<Customer> activeCustomers = new ArrayList<Customer>();
		for (Customer customer : allCustomers) {
			List<CustomerAccount> customerAccounts = getCustomerAccounts(customer);
			customer.setAccounts(unwrapCustomerAccounts(customerAccounts));
			
			for (CustomerAccount account : customerAccounts){
			int planId = account.getPlanId();
			Plan billingPlan = unwrapBillingPlan(dataManagerDao
					.getBillingPlanById(planId));
			double dataUsage = dataManagerDao.getDataUsage(account.getIpSubnet(), account.getIpCidr(), getFirstDayOfCurrentMonth(),
					getLastDayOfCurrentMonth());

			if (dataUsage < billingPlan.getLimit()) {
				activeCustomers.add(customer);
			}
		}

		}
		return activeCustomers;

	}

	private List<CustomerAccount> getCustomerAccounts(Customer customer) {
		List<CustomerAccount> customerAccounts = dataManagerDao.getCustomerAccountsByCustomerId(customer
				.getCustomerId());
		return customerAccounts;
	}

	private List<org.communinet.billing.domain.CustomerAccount> unwrapCustomerAccounts(
			List<CustomerAccount> customerAccounts) {
		
		List<org.communinet.billing.domain.CustomerAccount> output = new ArrayList<>();
		
		for (CustomerAccount acc : customerAccounts){
			output.add(unwrappCustomerAccount(acc));
		}
		
		return output;
	}

	private org.communinet.billing.domain.CustomerAccount unwrappCustomerAccount(
			CustomerAccount acc) {
		org.communinet.billing.domain.CustomerAccount account = new org.communinet.billing.domain.CustomerAccount();
		account.setBillPlan(acc.getPlanId());
		account.setExpiryDate(acc.getExpiryDate());
		account.setAccounId(acc.getAccountId());
		account.setNetworkSubnet(new NetworkSubnet(acc.getIpSubnet(), acc.getIpCidr()));
		return account;
	}

	public void addCustomer(Customer customer) {
		dataManagerDao.addCustomer(wrapCustomer(customer));
	}

	public void addDataUsage(IPTraffic traffic, Double bytesUsed) {
		dataManagerDao.addDataUsage(wrapDataUsage(traffic, bytesUsed));
	}

	public Double getDataUsageAtDate(String subnet, int cidr, Date date) {
		return dataManagerDao.getDataUsage(subnet, cidr,
				getFirstDayOfMonth(date), date);
	}

	public Double getUsageForCurrentMonth(String subnet, int cidr) {
		return getDataUsageAtDate(subnet, cidr, new Date());
	}

	public Plan getBillinPlan(int planId) {
		BillinPlan billingPlan = dataManagerDao.getBillingPlanById(planId);
		return unwrapBillingPlan(billingPlan);
	}

	public void logDataUsage() {

	}

	private DataUsage wrapDataUsage(IPTraffic ipTraffic, Double bytesUsed) {
		String ip = ipTraffic.getIp();
		int port = Integer.parseInt(ipTraffic.getPort());
		String protocol = ipTraffic.getProtocol();

		DataUsage usage = new DataUsage();
		usage.setDate(new Date());
		usage.setIpAddress(ip);
		usage.setPort(port);
		usage.setBytes(bytesUsed);
		usage.setUpload(ipTraffic.isSource());
		usage.setProtocol(protocol);

		return usage;
	}

	private Plan unwrapBillingPlan(BillinPlan billingPlanById) {
		Plan plan = new Plan();
		plan.setLimit(billingPlanById.getPlanLimit());
		plan.setPlanDesc(billingPlanById.getPlanDesc());
		plan.setPlanId(billingPlanById.getId());
		return plan;
	}

	private List<Customer> unwrapCustomers(
			List<org.communinet.billing.impl.dao.jpa.Customer> customers) {

		List<Customer> outputCustomers = new ArrayList<Customer>();
		for (org.communinet.billing.impl.dao.jpa.Customer customer : customers) {
			outputCustomers.add(unwrapCustomer(customer));
		}

		return outputCustomers;
	}

	private org.communinet.billing.impl.dao.jpa.Customer wrapCustomer(
			Customer customer) {
		org.communinet.billing.impl.dao.jpa.Customer wrappedCustomer = new org.communinet.billing.impl.dao.jpa.Customer();

		wrappedCustomer.setContactNumber(customer.getContactNumber());
		wrappedCustomer.setEmailAddress(customer.getEmailAddress());
		wrappedCustomer.setName(customer.getFirstName());
		wrappedCustomer.setSurname(customer.getLastName());

		return wrappedCustomer;
	}

	private Customer unwrapCustomer(
			org.communinet.billing.impl.dao.jpa.Customer customer) {
		Customer unwrappedCustomer = new Customer();
		unwrappedCustomer.setContactNumber(customer.getContactNumber());
		unwrappedCustomer.setEmailAddress(customer.getEmailAddress());
		unwrappedCustomer.setFirstName(customer.getName());
		unwrappedCustomer.setLastName(customer.getSurname());

		return unwrappedCustomer;
	}

	private static Date getFirstDayOfCurrentMonth() {
		Calendar calender = Calendar.getInstance();
		calender.set(Calendar.DAY_OF_MONTH, 1);
		calender.set(Calendar.AM_PM, Calendar.AM);
		calender.set(Calendar.HOUR, 0);
		calender.set(Calendar.MINUTE,0);
		calender.set(Calendar.SECOND,0);
		return calender.getTime();
	}

	private static Date getLastDayOfCurrentMonth() {
		Calendar calender = Calendar.getInstance();
		calender.set(Calendar.DAY_OF_MONTH, Calendar.getInstance()
				.getActualMaximum(Calendar.DAY_OF_MONTH));
		calender.set(Calendar.AM_PM, Calendar.AM);
		calender.set(Calendar.HOUR, 0);
		calender.set(Calendar.MINUTE,0);
		calender.set(Calendar.SECOND,0);		
		return calender.getTime();
	}

	private static Date getLastDayOfMonth(Date date) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		
		calender.set(Calendar.DAY_OF_MONTH, Calendar.getInstance()
				.getActualMaximum(Calendar.DAY_OF_MONTH));
		calender.set(Calendar.AM_PM, Calendar.AM);
		calender.set(Calendar.HOUR, 0);
		calender.set(Calendar.MINUTE,0);
		calender.set(Calendar.SECOND,0);		
		return calender.getTime();
	}
	
	private static Date getFirstDayOfMonth(Date date) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.set(Calendar.DAY_OF_MONTH, 1);
		calender.set(Calendar.AM_PM, Calendar.AM);
		calender.set(Calendar.HOUR, 0);
		calender.set(Calendar.MINUTE,0);
		calender.set(Calendar.SECOND,0);		
		return calender.getTime();
	}

}
