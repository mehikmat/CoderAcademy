package test.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

/**
 * Created by hdhamee on 5/27/16.
 */
public class MulticastPeer {
    public static void main(String[] args) {
        // args give message contents & destination multicast group
        // eg. java MulticastPeer "message" all-hosts.mcast.net
        MulticastSocket s = null;
        try {
            InetAddress group = InetAddress.getByName("224.0.0.4");

            // The hard-coded port number is 6789 (the client must have a
            // MulticastSocket bound to this port).
            s = new MulticastSocket(6789);

            // the input InetAddress is the multicast group
            s.joinGroup(group);

            byte[] m = "ABC".getBytes();

            DatagramPacket dp = new DatagramPacket(m, m.length, group, 6789);
            s.send(dp);

            // buffer used to receive
            byte[] buffer = new byte[1000];

            while(true) {
                DatagramPacket indp = new DatagramPacket(buffer, buffer.length);
                s.receive(indp);
                String msg = new String(indp.getData(),0,indp.getLength());
                System.out.println("Received: " + msg );
                if ("Stop".equalsIgnoreCase(msg)){
                    break;
                }
            }
            s.leaveGroup(group);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if(s != null)
                s.close();
        }
    }
}
