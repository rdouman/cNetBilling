package org.communinet.billing.impl;

import org.communinet.billing.impl.dao.IDataManagerDao;
import org.communinet.billing.impl.dao.INotificationAdapter;
import org.communinet.billing.impl.dao.INotificationDao;
import org.communinet.billing.impl.dao.jpa.Customer;
import org.communinet.billing.impl.dao.jpa.Message;
import org.communinet.billing.spec.provided.INotificationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationManager implements INotificationManager {

	@Autowired
	INotificationDao iNotificationDao;
	
	@Autowired
	INotificationAdapter iNotificationAdapter;
	
	@Autowired
	IDataManagerDao iDataManagerDao;
	
	public void notifyCustomer(String subnet, int cidr, int messageId){
		Message message = iNotificationDao.getMessage(messageId);
		Customer customer = iDataManagerDao.getCustomerInfoBySubnet(subnet, cidr);
		
		iNotificationAdapter.notifyCustomer(customer.getContactNumber(), message.getMessage());
		iNotificationDao.updateIssuedNotifications(customer.getCustomerId(), messageId);
		
		
		
	}
	
	public void isNotificationIssued (Customer customer, int messageId){
		
	}
}
