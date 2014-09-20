package org.communinet.billing.domain;

import java.util.List;


public class Customer {
	private String firstName;
	private String lastName;
	
	private String contactNumber;
	private String emailAddress;
	
	private int customerId;
	private List<CustomerAccount> accounts;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public int getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public List<CustomerAccount> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<CustomerAccount> accounts) {
		this.accounts = accounts;
	}
}
