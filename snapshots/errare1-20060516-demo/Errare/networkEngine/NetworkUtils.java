/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Antoine PIERRONNET

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package networkEngine;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class NetworkUtils {
	
	public static InetAddress getLocalInetAddress(){
		NetworkInterface iface = null;
		if(System.getProperty("os.name").compareTo("Linux")!=0)
			try {
				return InetAddress.getLocalHost();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
		  try {
			for(Enumeration ifaces =      NetworkInterface.getNetworkInterfaces();ifaces.hasMoreElements();){
			   iface = (NetworkInterface)ifaces.nextElement();
			   System.out.println("Interface:"+ iface.getDisplayName());
			   InetAddress ia = null;
			   InetAddress temp=null;
			    for(Enumeration ips =    iface.getInetAddresses();ips.hasMoreElements();){
			    	temp=(InetAddress)ips.nextElement();
			    if(!temp.isLoopbackAddress()) ia =temp; 
			    }
			    return ia;
			   }
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws SocketException, UnknownHostException{
		System.err.println("This machine has the non-localhost address: "+getLocalInetAddress());
		
	}
}