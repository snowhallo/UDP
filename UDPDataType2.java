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
public class UDPDataType2 {
    public static void main(String[] args) {
        String Host = "36.50.135.242";
        int port = 2207;
        String studentCode = "B23DCCN731";
        String qCode = "geqty11J";
        
        try(DatagramSocket socket = new DatagramSocket() ) {
            InetAddress address = InetAddress.getByName(Host);
            
            String request = ";" + studentCode + ";" + qCode;
            byte[] requestData = request.getBytes();
            DatagramPacket sendRequest = new DatagramPacket(
                requestData,
                requestData.length,
                address,
                port
            );
            socket.send(sendRequest);
            
            byte[] receive = new byte[4096];
            DatagramPacket receivePacket = new DatagramPacket(receive, receive.length);
            socket.receive(receivePacket);
            
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            String[] parts = response.split(";");
            String id = parts[0];
            String[] nums = parts[1].split(",");
            
            int mx = Integer.MIN_VALUE;
            int mn = Integer.MAX_VALUE;
            
            for (int i = 0; i < nums.length; i++) {
                int num = Integer.parseInt(nums[i]);
                if (num > mx) mx = num;
                if (num < mn) mn = num;
            }
            
            String result = id + ";" + mx + "," + mn;
            byte[] resultData = result.getBytes();
            DatagramPacket sendResult = new DatagramPacket(
                    resultData,
                    resultData.length,
                    receivePacket.getAddress(),
                    receivePacket.getPort()
            );
            socket.send(sendResult);
        } catch (Exception e) {
        }
    }
}
