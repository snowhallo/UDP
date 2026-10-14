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
public class UDPObject {
    public static void main(String[] args) {
        String Host = "36.50.135.242";
        int port = 2209;
        String studentCode = "B23DCCN731";
        String qCode = "1R4XrJvQ";
        
        try(DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName(Host);
            
            String request = ";" + studentCode + ";" + qCode;
            byte[] requestData = request.getBytes();
            DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, address, port);
            socket.send(requestPacket);
            
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
            Student student = (Student) ois.readObject();
            
            String name = student.getName().trim().toLowerCase();
            String[] words = name.split("\\s+");
            
            StringBuilder sb = new StringBuilder();
            StringBuilder sb1 = new StringBuilder();
            
            for (int i = 0; i < words.length; i++){
                String w = words[i];
                sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1));
                if (i < words.length - 1) {
                    sb.append(" ");
                    sb1.append(w.charAt(0));
                }
            }
            
            student.setName(sb.toString());
            student.setEmail(words[words.length - 1] + sb1.toString() + "@ptit.edu.vn");
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(student);
            oos.flush();
            byte[] objByte = baos.toByteArray();
            
            byte[] response = new byte[8 + objByte.length];
            System.arraycopy(id, 0, response, 0, 8);
            System.arraycopy(objByte, 0, response, 8, objByte.length);
            DatagramPacket responsePacket = new DatagramPacket(response, response.length, receivePacket.getAddress(), receivePacket.getPort());
            socket.send(responsePacket);
            
        } catch (Exception e) {
        }
    }
}

class Student implements Serializable {
    private static final long serialVersionUID = 20171107L;
    private String id, code, name, email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Student(String code) {
        this.code = code;
    }

    public Student(String id, String code, String name, String email) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.email = email;
    }
    
}
