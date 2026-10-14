/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author Admin
 */
public class UDPDataType {
    public static void main(String[] args) {
        String Host = "36.50.135.242";
        int port = 2207;
        String studentCode = "B23DCCN731";
        String qCode = "1CL3JfXQ";
        try(DatagramSocket socket = new DatagramSocket()) {
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
            
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
            
            String[] parts = response.split(";");
            String id = parts[0];
            int n = Integer.parseInt(parts[1]);
            String[] nums = parts[2].split(",");
            
            Set<Integer> st = new HashSet<>();
            for (int i = 0; i < nums.length; i++){
                st.add(Integer.parseInt(nums[i]));
            }
            
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (int i = 1; i <= n; i++) {
                if (!st.contains(i)){
                    if (!first){
                        sb.append(",");
                    }
                    sb.append(i);
                    first = false;
                }
            }
            
            String result = id + ";" + sb.toString();
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
