package com.example.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by r.tabulov on 05.09.2016.
 */
public class SocketTest {
    public static void main(String[] args) throws IOException {
        Socket s = new Socket(InetAddress.getByName("stackoverflow.com"), 80);
        PrintWriter pw = new PrintWriter(s.getOutputStream());
        pw.print("GET / HTTP/1.1\r\n");
        pw.print("Host: stackoverflow.com\r\n\r\n");
        pw.flush();
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String t;
        while ((t = br.readLine()) != null) System.out.println(t);
        br.close();
    }
}
