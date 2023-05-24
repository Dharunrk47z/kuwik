package com.example.kuwik;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyClient {
    public  Socket socket = null;


    int flag = 0;
    private Context appContext;

    public MyClient(Context context) {
        this.appContext = context;
    }

    public void connectToServer(String ip, int serverPort) {
        String temp_ip = ip.trim();

       new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   socket = new Socket(temp_ip,serverPort);

               } catch (IOException e) {
                   System.out.println("Cannot connect : "+e.getMessage());
               }
           }
       }).start();

    }

    public void disconnect() {

        try {
            if (socket != null) socket.close();

            socket = null;
            System.out.println("Socket closed");
        } catch (IOException e) {
            throw new RuntimeException(e);

        }

        socket = null;
        System.out.println("Socket set to null");
    }

    public boolean isConnected() {
        if (socket == null) {
            return false;
        } else {
            try {
                if (this.socket.isConnected()) {
                    return true;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);

            }
        }
        return false;
    }

}
