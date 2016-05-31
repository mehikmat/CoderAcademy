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
    // The hard-coded InetAddress of the DatagramPacket is "230.0.0.1" and is a group identifier (rather than the Internet
    // address of the machine on which a single client is running).
    // This particular address was arbitrarily chosen from the reserved for this purpose.

    // Created in this way, the DatagramPacket is destined for all clients listening to port number 4446 who are member of the
    // "230.0.0.1" group.

    // Notice that the server uses a DatagramSocket to broadcast packet received by the client over a MulticastSocket.
    // Alternatively, it could have used a MulticastSocket. The socket used by the server to send the DatagramPacket is not important.
    // What's important when broadcasting packets is the addressing information contained in the DatagramPacket,and the socket
    // used by the client to listen for it.

    public static void main(String[] args) {
        // Could be used DatagramSocket instead if the server only sends message and doesn't receive other peers message.
        MulticastSocket s = null;
        try {
            InetAddress group = InetAddress.getByName("230.0.0.1");

            // The hard-coded port number is 6789 (the client must have a MulticastSocket bound to this port).
            // The port number doesn't actually matter in this example because the client never send anything to the server.
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
