/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UDP;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author Admin
 */
public class UDPObject2 {
    public static void main(String[] args) {
        String Host = "36.50.135.242";
        int port = 2209;
        String studentCode = "B23DCCN731";
        String qCode = "cClSXpRZ";
        try(DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName(Host);
            
            String request = ";" + studentCode + ";" + qCode;
            byte[] requestData = request.getBytes();
            DatagramPacket requestSend = new DatagramPacket(requestData, requestData.length, address, port);
            socket.send(requestSend);
            
            byte[] receive = new byte[4096];
            DatagramPacket receivePacket = new DatagramPacket(receive, receive.length);
            socket.receive(receivePacket);
            
            byte[] data = receivePacket.getData();
            int datalen = receivePacket.getLength();
            
            byte[] id = new byte[8];
            System.arraycopy(data, 0, id, 0, 8);
            
            int objlen = datalen - 8;
            ByteArrayInputStream bais = new ByteArrayInputStream(data, 8, objlen);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Customer customer = (Customer) ois.readObject();
            
            String dob = customer.getDayOfBirth();
            String[] parts = dob.split("-");
            customer.setDayOfBirth(parts[1]+"/"+parts[0]+"/"+parts[2]);
            
            String name = customer.getName().trim().toLowerCase();
            String[] words = name.split("\\s+");
            String lastname = words[words.length - 1].toUpperCase();
            
            StringBuilder sb = new StringBuilder();
            StringBuilder sb1 = new StringBuilder();
            for (int i = 0; i < words.length - 1; i++) {
                String w = words[i];
                sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1));
                sb1.append(w.charAt(0));
                if (i < words.length - 2){
                    sb.append(" ");
                }
            }
            sb1.append(lastname.toLowerCase());
            customer.setName(lastname + ", " + sb.toString());
            customer.setUserName(sb1.toString());
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(customer);
            oos.flush();
            byte[] objByte = baos.toByteArray();
            
            byte[] sendData = new byte[8 + objByte.length];
            System.arraycopy(id, 0, sendData, 0, 8);
            System.arraycopy(objByte, 0, sendData, 8, objByte.length);
            DatagramPacket responsePacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
            socket.send(responsePacket);
            
        } catch (Exception e) {
        }
    }
}

class Customer implements Serializable {
    private static final long serialVersionUID = 20151107L;
    private String id, code, name, dayOfBirth, userName;

    public Customer(String id, String code, String name, String dayOfBirth, String userName) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.dayOfBirth = dayOfBirth;
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(String dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
}
