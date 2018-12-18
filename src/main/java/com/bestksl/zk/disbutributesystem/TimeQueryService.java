package com.bestksl.zk.disbutributesystem;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TimeQueryService extends Thread {
    private int port = 0;

    public TimeQueryService(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("server ready!");
        try {
            ServerSocket ss = new ServerSocket(port);
            while (true) {
                Socket sc = ss.accept();
                OutputStream o = sc.getOutputStream();
                o.write(new Date().toString().getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
