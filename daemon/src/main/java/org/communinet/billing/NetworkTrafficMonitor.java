package org.communinet.billing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.communinet.billing.domain.IPTraffic;
import org.communinet.billing.impl.IpRangeFilter;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NetworkTrafficMonitor extends Thread {  

	private static final String NETWORK_INTERFACE = "interface";
	private static final String SUBNET = "subnet";
	private static final String NETWORK = "network";

	static final Logger logger = LoggerFactory.getLogger(NetworkTrafficMonitor.class);


	private Map<IPTraffic, Long> trafficMap = new Hashtable <IPTraffic, Long>();

	private IpRangeFilter ipRangeFilter;



	public void run()
	{

		logger.info("Starting IpMonitor");

		String filename = "config/config.properties";

		File file = new File(filename);
		Properties properties;
		try {
			properties = loadProperties(file);


			String network_interface = properties.getProperty(NETWORK_INTERFACE);
			String subnet = properties.getProperty(SUBNET);
			String networkAddress = properties.getProperty(NETWORK);

			logger.info(
					"Initialising with parameters:\nNetwork Inteface {}\nSubnet {}\nNetwork Address {}", 
					new String[]{network_interface, subnet, networkAddress});

			if(network_interface == null || network_interface.length() == 0)
			{
				logger.info("No network interface defined");
			}

			if(subnet == null || subnet.length() == 0)
			{
				logger.info("No subnet defined");
			}


			ipRangeFilter = new IpRangeFilter(networkAddress);

			List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs  
			StringBuilder errbuf = new StringBuilder(); // For any error msgs  

			/*************************************************************************** 
			 * First get a list of devices on this system 
			 **************************************************************************/  

			int r = Pcap.findAllDevs(alldevs, errbuf);  
			if (r == Pcap.ERROR || r == Pcap.WARNING || alldevs.isEmpty()) {  
				logger.error("Can't read list of devices, error is %s", errbuf  
						.toString());  
				return;  
			}  

			logger.info("Network devices found:");  

			PcapIf device = null;

			// Ensure that we connect to the specified interface
			for (PcapIf network_device : alldevs) 
			{  
				String name = network_device.getName();

				if(name.equals(network_interface))
				{
					device = network_device;
					logger.info("using device: "+network_interface);
					break;
				}
			}  

			if(device == null)
			{
				logger.info("Could not find interface \""+network_interface+"\"");
				return;
			}


			/*************************************************************************** 
			 * Second we open up the selected device 
			 **************************************************************************/  
			int snaplen = 64 * 1024;           // Capture all packets, no trucation  
			int flags = Pcap.MODE_PROMISCUOUS; // capture all packets  
			int timeout = 10 * 1000;           // 10 seconds in millis  

			Pcap pcap =  
				Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);  

			PcapBpfProgram filter = new PcapBpfProgram();
			String expression = "src net "+networkAddress +" or dst net "+networkAddress;

			int optimize = 0; // 1 means true, 0 means false
			int netmask = 0;

			if (pcap == null) {  
				logger.error("Error while opening device for capture: "  
						+ errbuf.toString());  
				return;  
			} 

			addFilter(pcap, filter, expression, optimize, netmask);

			/*************************************************************************** 
			 * Third we create a packet handler which will receive packets from the 
			 * libpcap loop. 
			 **************************************************************************/  
			final Ip4 ip = new Ip4();  
			PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {  


				public void nextPacket(PcapPacket packet, String user) {  

					try
					{
						int packetSize = packet.size();

						if(packet.hasHeader(ip))
						{
							String destinationIp = FormatUtils.ip(packet.getHeader(ip).destination()); 
							String sourceIp = FormatUtils.ip(packet.getHeader(ip).source());

							String protocol = "Unkown";

							int sourcePort = 0;
							int destPort = 0;

							if (packet.hasHeader(Tcp.ID)) {  

								final Tcp tcp = new Tcp(); 

								protocol ="tcp";

								/* 
								 * Now get our tcp header definition (accessor) peered with actual 
								 * memory that holds the tcp header within the packet. 
								 */  
								packet.getHeader(tcp);  

								destPort = tcp.destination();  
								sourcePort = tcp.source();  

							} 

							if (packet.hasHeader(Udp.ID)) {  

								final Udp udp = new Udp();

								protocol = "udp";

								/* 
								 * Now get our udp header definition (accessor) peered with actual 
								 * memory that holds the udp header within the packet. 
								 */  
								packet.getHeader(udp);  
								destPort = udp.destination();  
								sourcePort = udp.source();

							} 

							try
							{
								addTraffic(sourceIp, sourcePort, protocol, packetSize, true);
								addTraffic(destinationIp, destPort, protocol, packetSize, false);
							}
							catch (Exception e) 
							{
								logger.error("Failed to log traffic:\n {}", e);
							}
						}  
					}
					catch (Exception e) 
					{
						logger.error("Failed to intercept traffic data :\n {}",e);
					}
				}
			};  

			/*************************************************************************** 
			 * Fourth we enter the loop and tell it to capture 10 packets. The loop 
			 * method does a mapping of pcap.datalink() DLT value to JProtocol ID, which 
			 * is needed by JScanner. The scanner scans the packet buffer and decodes 
			 * the headers. The mapping is done automatically, although a variation on 
			 * the loop method exists that allows the programmer to sepecify exactly 
			 * which protocol ID to use as the data link type for this pcap interface. 
			 **************************************************************************/  
			pcap.loop(Pcap.LOOP_INFINITE, jpacketHandler, "jNetPcap rocks!");  

			/*************************************************************************** 
			 * Last thing to do is close the pcap handle 
			 **************************************************************************/  
			pcap.close();
		}
		catch(Exception e)
		{
			logger.error("IP monitoring failed:\n {}",e);
		}
	}

	public Map<IPTraffic, Long> getTrafficMap() 
	{

		return trafficMap;
	}

	public void setTrafficMap(Map<IPTraffic, Long> trafficMap) {
		this.trafficMap = trafficMap;
	}

	public void resetTotals ()
	{
		trafficMap = new Hashtable <IPTraffic, Long>();
	}

	// we need to check if this is an upload or download
	// be able to feed a list of local ips
	protected void addTraffic(String ip, int port,
			String protocol, int packetSize, boolean isSource) 
	{
		IPTraffic trafficEndpoint = new IPTraffic();
		trafficEndpoint.setIp(ip);
		trafficEndpoint.setPort(Integer.toString(port));
		trafficEndpoint.setProtocol(protocol);
		trafficEndpoint.setSource(isSource);

		if(!trafficMap.containsKey(trafficEndpoint) )
		{
			try 
			{
				if(ipRangeFilter.isInRange(InetAddress.getByName(ip)))
				{
					logger.debug("Logging traffic for {}",ip);
					trafficMap.put(trafficEndpoint, new Long(packetSize));
				} 
			}
			catch (UnknownHostException e) 
			{
				logger.warn("Unable to determine host:\n {}",e);
			}
		}
		else
		{
			try {
				if(ipRangeFilter.isInRange(InetAddress.getByName(ip)))
				{
					logger.debug("Logging traffic for {}",ip);
					Long totalTraffic = trafficMap.get(trafficEndpoint);
					totalTraffic = totalTraffic + (long)packetSize;
					trafficMap.put(trafficEndpoint,totalTraffic);

				}
			} 
			catch (UnknownHostException e) 
			{
				logger.warn("Unable to determine host:\n {}",e);
			}
		}
	}


	private Properties loadProperties(File f)
	throws FileNotFoundException, IOException 
	{
		Properties pro = new Properties();
		if(f.exists())
		{

			FileInputStream in = new FileInputStream(f);
			pro.load(in);
		}
		return pro;
	}

	private  void addFilter(Pcap pcap, PcapBpfProgram filter,
			String expression, int optimize, int netmask) {
		int r;
		r = pcap.compile(filter, expression, optimize, netmask);

		if (r != Pcap.OK) {
			logger.error("Filter error: " + pcap.getErr());
		}
		pcap.setFilter(filter);
	}  


}  