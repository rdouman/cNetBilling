package org.communinet.billing.domain;

public class IPTraffic 
{
	@Override
	public String toString() {
		return "NetworkTraffic [ip=" + ip + ", port=" + port + ", protocol="
				+ protocol + ", isSource=" + isSource + "]";
	}

	String ip;
	String port;
	String protocol;
	private boolean isSource;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + (isSource ? 1231 : 1237);
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		result = prime * result
				+ ((protocol == null) ? 0 : protocol.hashCode());
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
		IPTraffic other = (IPTraffic) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (isSource != other.isSource)
			return false;
		if (port == null) {
			if (other.port != null)
				return false;
		} else if (!port.equals(other.port))
			return false;
		if (protocol == null) {
			if (other.protocol != null)
				return false;
		} else if (!protocol.equals(other.protocol))
			return false;
		return true;
	}
	public void setSource(boolean isSource) 
	{
		this.isSource = isSource;
		
	}
	
	public boolean isSource()
	{
		return isSource;
	}
	

}
