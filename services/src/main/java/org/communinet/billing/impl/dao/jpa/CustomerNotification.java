package org.communinet.billing.impl.dao.jpa;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class CustomerNotification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int notificationId;
	
	private int messageId ;
	
	private int customerId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timeSent;

	public int getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public Date getTimeSent() {
		return timeSent;
	}

	public void setTimeSent(Date timeSent) {
		this.timeSent = timeSent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + customerId;
		result = prime * result + messageId;
		result = prime * result + notificationId;
		result = prime * result
				+ ((timeSent == null) ? 0 : timeSent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerNotification other = (CustomerNotification) obj;
		if (customerId != other.customerId)
			return false;
		if (messageId != other.messageId)
			return false;
		if (notificationId != other.notificationId)
			return false;
		if (timeSent == null) {
			if (other.timeSent != null)
				return false;
		} else if (!timeSent.equals(other.timeSent))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CustomerNotification [notificationId=" + notificationId
				+ ", messageId=" + messageId + ", customerId=" + customerId
				+ ", timeSent=" + timeSent + "]";
	}
	
	
	
	
}
