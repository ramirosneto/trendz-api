package br.com.trendzapi.utils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Resource {

    public static String getIp() {
        try {
            InetAddress ownIP = InetAddress.getLocalHost();
//System.out.println("My IP Address : "+ ownIP.getHostAddress());
            String myIp = ownIP.getHostAddress();
            System.out.println("IP :"+myIp);
//ownIP = InetAddress.getLoopbackAddress();
//System.out.println(ownIP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void getInfoByIp(String ipv6) {
        /*try {
            WebServiceClient client = new WebServiceClient.Builder(42, "license_key").build();
            String[] aux = ipv6.split("%");
            InetAddress ipAddress = InetAddress.getByName(aux[0]);

            CountryResponse response = client.country(ipAddress);

            Country country = response.getCountry();
            Log.d("RAMIRO", country.getName());
        } catch (GeoIp2Exception | IOException e) {
            e.printStackTrace();
        }*/
    }

    private boolean isValidIp4Address(final String hostName) {
        try {
            return Inet4Address.getByName(hostName) != null;
        } catch (UnknownHostException ex) {
            return false;
        }
    }

    private boolean isValidIp6Address(final String hostName) {
        try {
            return Inet6Address.getByName(hostName) != null;
        } catch (UnknownHostException ex) {
            return false;
        }
    }
}
