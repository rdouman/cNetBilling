package org.communinet.billing.domain;

public enum NotificationTypes {
	ACCOUNT_EXPIRED(100), USAGE_50_PERCENT(101), USAGE_80_PERCENT(102), USAGE_100_PERCENT(102);
	
	private int messageId;

	private NotificationTypes(int messageId) {
		this.messageId = messageId;
	}

	public int getMessageId() {
		return messageId;
	}
	
	public NotificationTypes getNotificationTypeFromMessageId(int messageId){
		for (NotificationTypes notificationType : values()){
			if (messageId == notificationType.getMessageId()){
				return notificationType;
			}
		}
		
		return null;
	}
}
