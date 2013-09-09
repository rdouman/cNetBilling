package org.communinet.billing.ip;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * I am a filter used to determine if a given IP Address is covered by the IP range specified in 
 * the constructor.  I accept IP ranges in the form of full single IP addresses, e.g. 10.1.0.23
 * or network/netmask pairs in CIDR format e.g. 10.1.0.0/16
 */
public class IpRangeFilter{

    private final long network;
    private final long netmask;

    private final String ipRange;

    private static final Pattern PATTERN = Pattern.compile("((?:\\d|\\.)+)(?:/(\\d{1,2}))?");

    public IpRangeFilter(String ipRange) throws UnknownHostException {
        Matcher matcher = PATTERN.matcher(ipRange);
        if (matcher.matches()) {
                String networkPart = matcher.group(1);
                String cidrPart = matcher.group(2);

                long netmask = 0;
                int cidr = cidrPart == null ? 32 : Integer.parseInt(cidrPart);
                for (int pos = 0; pos < 32; ++pos) {
                        if (pos >= 32-cidr) {
                                netmask |= (1L << pos);
                        }
                }

                this.network = netmask & toMask(InetAddress.getByName(networkPart));
                this.netmask = netmask;
                this.ipRange = ipRange;

        } else {
                throw new IllegalArgumentException("Not a valid IP range: " + ipRange);
        }
    }

    public String getIpRange() {
        return ipRange;
    }

    public boolean evaluate(InetAddress address) {
        return isInRange(address);
    }

    public boolean isInRange(InetAddress address) {
        return network == (toMask(address) & netmask);
    }

    /**
     * Convert the bytes in the InetAddress into a bit mask stored as a long.
     * We could use int's here, but java represents those in as signed numbers, which can be a pain 
     * when debugging.
     * @see http://www.captain.at/howto-java-convert-binary-data.php
     */
    static long toMask(InetAddress address) {
        byte[] data = address.getAddress();
        long accum = 0;
        int idx = 3;
        for ( int shiftBy = 0; shiftBy < 32; shiftBy += 8 ) {
                accum |= ( (long)( data[idx] & 0xff ) ) << shiftBy;
                idx--;
        }
        return accum;
    }
}
