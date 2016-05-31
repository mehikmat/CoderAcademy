package test.ringalgo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hdhamee on 5/31/16.
 */
public class Node implements Runnable {
    private int nodeId;
    DatagramSocket nextNode = null;
    DatagramSocket previousNode = null;

    public Node(int nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public void run() {
        System.out.println("Starting node " + nodeId);
        try {
            while (true) {
                byte[] msg = new byte[1024];

                DatagramPacket receivePacket = new DatagramPacket(msg, msg.length);

                if (nodeId == RingElection.nodes.length -1){
                    nextNode = RingElection.sockets.get(0);
                }

                if (nodeId != 0) {
                    previousNode = RingElection.sockets.get(nodeId - 1);
                } else {
                    previousNode = RingElection.sockets.get(RingElection.nodes.length - 1);
                }
                previousNode.receive(receivePacket);

                String receivedMsg = new String(receivePacket.getData()).trim();
                System.out.println("Node " + nodeId + " received : " + receivedMsg + " from " + previousNode.getInetAddress());

                //DatagramPacket sendPacket = new DatagramPacket(okMsgBytes, okMsgBytes.length, receivePacket.getAddress(), receivePacket.getPort());
                List<String> nodes = Arrays.asList(receivedMsg.split(":",-1));

                if (nodes.contains(nodeId)) {
                    System.out.println("contains ");
                } else {
                    System.out.println("not contain");

                }
            }
        }catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

}
