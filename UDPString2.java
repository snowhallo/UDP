/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author Admin
 */
public class UDPString2 {
    public static void main(String[] args) {
        String Host = "36.50.135.242";
        int port = 2208;
        String studentCode = "B23DCCN731";
        String qCode = "m7duHYMI";
        try(DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName(Host);
            
            String request = ";" + studentCode + ";" + qCode;
            byte[] requestData = request.getBytes();
            DatagramPacket requestSend = new DatagramPacket(requestData, requestData.length, address, port);
            socket.send(requestSend);
            
            byte[] receive = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receive, receive.length);
            socket.receive(receivePacket);
            
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            String[] parts = response.split(";");
            String id = parts[0];
            String data = parts[1].trim().toLowerCase();
            String[] words = data.split("\\s+");
            
            StringBuilder sb = new StringBuilder();
            for (String w : words) {
                sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1)).append(" ");
            }
            data = sb.toString().trim();
            String result = id + ";" + data;
            byte[] resultData = result.getBytes();
            DatagramPacket resultPacket = new DatagramPacket(resultData, resultData.length, receivePacket.getAddress(), receivePacket.getPort());
            socket.send(resultPacket);
            
        } catch (Exception e) {
        }
    }
}
