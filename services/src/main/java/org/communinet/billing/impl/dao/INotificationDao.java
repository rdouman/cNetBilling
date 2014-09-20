package org.communinet.billing.impl.dao;

import org.communinet.billing.impl.dao.jpa.Message;

public interface INotificationDao {

	public Message getMessage (int messageId);
	
	public void addMessage(String message);

	public void updateIssuedNotifications(int customerId, int messageId);
}
