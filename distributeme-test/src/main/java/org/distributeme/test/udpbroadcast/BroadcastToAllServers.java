package org.distributeme.test.udpbroadcast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 22.02.15 18:11
 */
public class BroadcastToAllServers {
	public static void main(String a[]) throws Exception{
		System.out.println("Trying to broadcast");
		DatagramPacket outgoing = new DatagramPacket(new String("register").getBytes(), "register".length());
		DatagramSocket socket = new DatagramSocket();
		socket.setBroadcast(true);
		outgoing.setAddress(InetAddress.getByAddress(new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255}));
		for (int i=9250; i<9400; i++) {
			outgoing.setPort(i);
			socket.send(outgoing);
		}
		System.out.println("Sent ...");
	}
}
