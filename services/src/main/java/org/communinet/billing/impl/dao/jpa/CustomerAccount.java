package org.communinet.billing.impl.dao.jpa;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.eclipse.persistence.annotations.Index;

@Entity
@Table(uniqueConstraints= {@UniqueConstraint(columnNames={"ipSubnet", "ipCidr"})})
public class CustomerAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int accountId;


	private int customerId;
	
	@Index
	private String ipSubnet;
	
	@Index
	private int ipCidr;

	@Temporal(TemporalType.TIMESTAMP)
	private Date expiryDate;
	
	private int planId;
	
	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}
	
	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	
	public String getIpSubnet() {
		return ipSubnet;
	}

	public void setIpSubnet(String ipSubnet) {
		this.ipSubnet = ipSubnet;
	}

	public int getIpCidr() {
		return ipCidr;
	}

	public void setIpCidr(int ipCidr) {
		this.ipCidr = ipCidr;
	}

	
	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accountId;
		result = prime * result + customerId;
		result = prime * result
				+ ((expiryDate == null) ? 0 : expiryDate.hashCode());
		result = prime * result + ipCidr;
		result = prime * result
				+ ((ipSubnet == null) ? 0 : ipSubnet.hashCode());
		result = prime * result + planId;
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
		CustomerAccount other = (CustomerAccount) obj;
		if (accountId != other.accountId)
			return false;
		if (customerId != other.customerId)
			return false;
		if (expiryDate == null) {
			if (other.expiryDate != null)
				return false;
		} else if (!expiryDate.equals(other.expiryDate))
			return false;
		if (ipCidr != other.ipCidr)
			return false;
		if (ipSubnet == null) {
			if (other.ipSubnet != null)
				return false;
		} else if (!ipSubnet.equals(other.ipSubnet))
			return false;
		if (planId != other.planId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CustomerAccount [accountId=" + accountId + ", customerId="
				+ customerId + ", ipSubnet=" + ipSubnet + ", ipCidr=" + ipCidr
				+ ", expiryDate=" + expiryDate + ", planId=" + planId + "]";
	}

	
}
