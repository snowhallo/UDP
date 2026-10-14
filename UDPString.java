/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class UDPString {
    public static void main(String[] args) {
        String Host = "36.50.135.242";
        int port = 2208;
        String studentCode = "B23DCCN731";
        String qCode = "68pjukXs";
        
        try(DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName(Host);
            
            String request = ";" +studentCode + ";" + qCode;
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
            String s = parts[1];
            
            int mx = 0;
            char ch =' '; 
            
            Map<Character, Integer> mp = new HashMap<>();
            for (char c : s.toCharArray()){
                mp.put(c, mp.getOrDefault(c, 0) + 1);
                if (mp.get(c) >= mx) {
                    mx = mp.get(c);
                    ch = c;
                }
            }
            
            String pos = "";
            
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == ch) {
                    pos += (i + 1) + ",";
                }
            }
            
            String result = id + ";" + ch + ":" + pos;
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
