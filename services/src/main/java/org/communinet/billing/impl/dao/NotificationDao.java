package org.communinet.billing.impl.dao;

import java.util.Date;

import javax.persistence.EntityManager;

import org.communinet.billing.impl.dao.jpa.CustomerNotification;
import org.communinet.billing.impl.dao.jpa.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationDao extends AbstractDao implements INotificationDao {

	static final Logger logger = LoggerFactory.getLogger(NotificationDao.class);
	private EntityManager entityManager;

	public NotificationDao() {
		this.entityManager = getEntityManager("my-app");
	}
	
	public Message getMessage (int messageId){
	
		
		logger.debug("Searching for message with id {}", messageId);

		Message message = entityManager.find(Message.class, new Integer(messageId));

		return message;

	}
	
	public void addMessage(String message){
		Message msgToAdd = new Message();
		msgToAdd.setMessage(message);
		
		persistData(msgToAdd, entityManager);
		
	}

	public void updateIssuedNotifications(int customerId, int messageId) {
		CustomerNotification notifcation = new CustomerNotification();
		notifcation.setCustomerId(customerId);
		notifcation.setMessageId(messageId);
		notifcation.setTimeSent(new Date());
		persistData(notifcation, entityManager);
	}

}
