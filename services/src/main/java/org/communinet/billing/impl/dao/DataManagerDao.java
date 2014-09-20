package org.communinet.billing.impl.dao;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.communinet.billing.impl.IpRangeFilter;
import org.communinet.billing.impl.dao.jpa.BillinPlan;
import org.communinet.billing.impl.dao.jpa.Customer;
import org.communinet.billing.impl.dao.jpa.CustomerAccount;
import org.communinet.billing.impl.dao.jpa.DataUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DataManagerDao extends AbstractDao implements IDataManagerDao {

	static final Logger logger = LoggerFactory.getLogger(DataManagerDao.class);
	private EntityManager entityManager;

	public DataManagerDao() {
		this.entityManager = getEntityManager("my-app");
	}

	// We actually want to move this code to a class/thread of its own.
	public Customer getCustomerInfoBySubnet(String subnet, int cidr) {

		logger.debug("Searching for customer {}, {}", subnet, cidr);

		Query query = entityManager
				.createQuery("select cust from Customer cust inner join CustomerAccount acc on cust.customerId = acc.customerId  where acc.ipSubnet = :subnet and acc.ipCidr = :cidr ");

		int paramIndex = 1;
		query.setParameter(paramIndex++, subnet);
		query.setParameter(paramIndex++, cidr);

		Customer customer = (Customer) query.getSingleResult();

		return customer;

	}

	// We actually want to move this code to a class/thread of its own.
	public CustomerAccount getCustomerAccountBySubnet(String subnet, int cidr) {

		logger.debug("Searching for customer account {}, {}", subnet, cidr);

		Query query = entityManager
				.createQuery("select acc from CustomerAccount acc where acc.ipSubnet = :subnet and acc.ipCidr = :cidr ");

		int paramIndex = 1;
		query.setParameter(paramIndex++, subnet);
		query.setParameter(paramIndex++, cidr);

		CustomerAccount customerAccount = (CustomerAccount) query
				.getSingleResult();

		return customerAccount;

	}

	public void addDataUsage(DataUsage usage) {
		logger.debug("Logging usage data {}",usage);
		
		persistData(usage,entityManager);

	}


	public double getDataUsage(String subnet, int cidr, Date startDate,
			Date endDate) {

		double totalData = 0;
		try {

			Query query = entityManager
					.createQuery("select usage.ipAddress, sum(uages.bytes), usage.upload from DataUsage usage group by usage.ipAddress, usage.upload where usage.date between :startDate and :endDate");

			int paramIndex = 1;
			query.setParameter(paramIndex++, startDate);
			query.setParameter(paramIndex++, endDate);

			@SuppressWarnings("unchecked")
			List<Object[]> result = query.getResultList();

			IpRangeFilter ipFilter = new IpRangeFilter(subnet + "/" + cidr);

			for (int i = 0; result != null && i < result.size(); i++) {
				int param = 1;
				String address = (String) result.get(i)[param++];
				Double usage = (Double) result.get(i)[param++];

				if (ipFilter.isInRange(InetAddress.getByName(address))) {
					totalData += usage.doubleValue();
				}
			}
		} catch (UnknownHostException e) {
			logger.error("Unable to data usage information {}", e.getMessage());
		}

		return totalData;

	}

	public List<Customer> getAllCustomers() {
		Query query = entityManager.createQuery(
				"select cust from Customer cust", Customer.class);

		@SuppressWarnings("unchecked")
		List<Customer> customers = query.getResultList();
	
		return customers;
	}

	public List<CustomerAccount> getCustomerAccountsByCustomerId(int customerId){

		Query query = entityManager
				.createQuery("select select acc from CustomerAccount acc where acc.customerId = :customerId");

		int paramIndex = 1;
		query.setParameter(paramIndex++, customerId);

		@SuppressWarnings("unchecked")
		List<CustomerAccount> result = query.getResultList();
		
		return result;

	}
	
	public BillinPlan getBillingPlanById(int planId) {
		BillinPlan plan = entityManager.find(BillinPlan.class, planId);
		return plan;
	}

	public void addCustomer(Customer customer) {
		persistData(customer, entityManager);
	}
	

}
