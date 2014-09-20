package org.communinet.billing.domain;

import java.util.Date;

public class CustomerAccount {
	private int billPlan;
	
	private Date expiryDate;
	private NetworkSubnet subnet;

	private int accountId;
	
	public int getBillPlan() {
		return billPlan;
	}
	public void setBillPlan(int billPlan) {
		this.billPlan = billPlan;
	}
	
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public Date getExpiryDate(){
		return expiryDate;
	}
	public NetworkSubnet getNetworkSubnet() {
		return subnet;
	}
	
	public void setNetworkSubnet(NetworkSubnet subnet){
		this.subnet = subnet;
	}
	public void setAccounId(int accountId) {
		this.accountId = accountId;
		
	}
	
	public int getAccountId(){
		return accountId;
	}
	
}
