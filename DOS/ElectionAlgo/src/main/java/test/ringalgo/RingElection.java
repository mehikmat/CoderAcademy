package test.ringalgo;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedHashMap;

public class RingElection {
    public static final String[] nodes = new String[]{"localhost","localhost","localhost","localhost"};
    public static final int[] ports =  new int[]{4001,4002,4003,4004};
    public static final LinkedHashMap<Integer,DatagramSocket> sockets = new LinkedHashMap<Integer,DatagramSocket>(){
        {
            try {
                put(0,new DatagramSocket(RingElection.ports[0], InetAddress.getByName(RingElection.nodes[0])));
                put(1,new DatagramSocket(RingElection.ports[1], InetAddress.getByName(RingElection.nodes[1])));
                put(2,new DatagramSocket(RingElection.ports[2], InetAddress.getByName(RingElection.nodes[2])));
                put(3,new DatagramSocket(RingElection.ports[3], InetAddress.getByName(RingElection.nodes[3])));
            }catch (Exception e){
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }
    };


    RingElection() throws Exception {
        for (int i=0; i< nodes.length;i++){
            Node node = new Node(i);
            Thread thread = new Thread(node);
            thread.start();
        }
    }

    public static void main(String args[]) throws Exception {
        RingElection obj = new RingElection();
    }
}