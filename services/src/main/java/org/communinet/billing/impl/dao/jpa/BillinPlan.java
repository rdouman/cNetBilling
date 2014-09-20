package org.communinet.billing.impl.dao.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BillinPlan {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int id;
	private String planDesc;
	private int planLimit;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPlanDesc() {
		return planDesc;
	}
	public void setPlanDesc(String planDesc) {
		this.planDesc = planDesc;
	}
	public int getPlanLimit() {
		return planLimit;
	}
	public void setPlanLimit(int planLimit) {
		this.planLimit = planLimit;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result
				+ ((planDesc == null) ? 0 : planDesc.hashCode());
		result = prime * result + planLimit;
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
		BillinPlan other = (BillinPlan) obj;
		if (id != other.id)
			return false;
		if (planDesc == null) {
			if (other.planDesc != null)
				return false;
		} else if (!planDesc.equals(other.planDesc))
			return false;
		if (planLimit != other.planLimit)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "BillinPlan [id=" + id + ", planDesc=" + planDesc
				+ ", planLimit=" + planLimit + "]";
	}
	
	

}
