package com.Min;

import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.concurrent.*;


public class Rfc865UdpServer{
    static DatagramSocket serverSocket;
    static BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        //Open up UDP Socket at port 17 (QOTD Port):
        try {
            serverSocket = new DatagramSocket(17);
        }
        catch(SocketException e){}
            //Listen for UDP request from client:
            CompletableFuture.supplyAsync(() -> {
                while (true) {
                    DatagramPacket request = null;
                    try {
                        request = getRequest();
                    String receivedMessage = new String(request.getData(), 0, request.getLength());
                        System.out.printf("\nMessage from client: %s\n", receivedMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            while (true){
            //Send UDP reply to client:
            System.out.print("\nEnter Message: ");
            String message = null;
            try {
                message = inputReader.readLine();
                sendReply(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static DatagramPacket getRequest() throws IOException{
        byte[] buffer = new byte[512];
        DatagramPacket request = new DatagramPacket(buffer, 512);
        serverSocket.receive(request);
        return request;
    }

    public static void sendReply(String message) throws UnknownHostException, IOException{
        byte[] byteEncodedMessage = message.getBytes(); // Encode our quote into byte array
        InetAddress targetAddress = InetAddress.getByName("LAPTOP-3LECIJKQ"); // Get the address of whoever sent a request datagram packet to our server

        DatagramPacket reply = new DatagramPacket(byteEncodedMessage, byteEncodedMessage.length, targetAddress, 18);
        serverSocket.send(reply);
    }
}
