package org.communinet.billing.ip;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.*;
public class IpRangeFilterTest{

	String[] validIps = {"10.10.10.1", "10.10.11.2", "10.10.20.45"};
	String[] invalidIps = {"10.11.30.1", "99.8.179.27", "1.164.110.105"};
	String ipRange = "10.10.0.0/16";
	
	@Before
	public void setup(){
		
	}

	@Test
	public void validIPsTest() throws UnknownHostException{
		IpRangeFilter rangeFiler = new IpRangeFilter(ipRange);
		
		for (String ip : validIps){
			Assert.assertEquals(true, rangeFiler.isInRange(InetAddress.getByName(ip)));
		}
	}
	
	@Test
	public void invalidIPsTest() throws UnknownHostException{
		IpRangeFilter rangeFiler = new IpRangeFilter(ipRange);
		
		for (String ip : invalidIps){
			Assert.assertEquals(false, rangeFiler.isInRange(InetAddress.getByName(ip)));
		}
	}
	
	@After
	public void tearDown(){
		
	}
	
 }
