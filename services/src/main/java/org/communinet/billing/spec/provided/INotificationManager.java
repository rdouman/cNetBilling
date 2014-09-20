package org.communinet.billing.spec.provided;

public interface INotificationManager {
	public void notifyCustomer(String subnet, int cidr, int messageId);
}
