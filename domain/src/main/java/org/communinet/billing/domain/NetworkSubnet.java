package org.communinet.billing.domain;

public class NetworkSubnet {

	private int cidr;
	private String ipSubnet;
	
	public NetworkSubnet() {
	}
	
	public NetworkSubnet (String ipaddress, int cidr){
		setCidr(cidr);
		setIpSubnet(ipaddress);
	}
	
	public int getCidr() {
		return cidr;
	}
	
	public void setCidr(int cidr) {
		this.cidr = cidr;
	}
	
	public String getIpSubnet() {
		return ipSubnet;
	}
	
	public void setIpSubnet(String ipSubnet) {
		this.ipSubnet = ipSubnet;
	}
	
	@Override
	public String toString() {
		return "NetworkSubnet [cidr=" + cidr + ", ipSubnet=" + ipSubnet + "]";
	}

	
}
