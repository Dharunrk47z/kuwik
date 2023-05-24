package com.example.kuwik;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Sender {
    static Socket socket = null;


    OutputStream writer;
    MyClient myClient;

    Sender(MyClient myClient) {

        this.myClient = myClient;
        socket = myClient.socket;
        try {
            writer = socket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean sendFile(FileInputStream file, String filename, long fileSize) {

        try {

            System.out.println("Retrieving Stream");

            //sending header

            writer.write("file".getBytes());

            byte[] buff = new byte[1024 * 1024];
            long fileNameSize = filename.length();
            byte[] fname = filename.getBytes(StandardCharsets.UTF_8);

            byte[] fsize = longToBytes(fileSize);

            writer.write(this.longToBytes(fileNameSize));
            writer.write(fname);

            writer.write(fsize);

            int read;

            System.out.println("writing bytes +");
            System.out.println(fname.length);


            while ((read = file.read(buff)) != -1) {

                writer.write(buff, 0, read);
                System.out.println("writing bytes : " + read);

            }

            System.out.println("Sent ");
            return true;
        } catch (Exception e) {
            System.out.println("could not send file");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean send(String message) {
        try {

            writer.write("mess".getBytes());
            writer.write(longToBytes(message.length()));
            writer.write(message.getBytes());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean sendClipBoard(String message) {

        try {
            writer.write("clip".getBytes());
            writer.write(longToBytes(message.length()));
            System.out.println("length of the message is : : :" + message.length());
            writer.write(message.getBytes());
            this.send("User Shared A Clip To You");
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

//    public long bytesToLong(byte[] bytes) {
//        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
//        buffer.put(bytes);
//        buffer.flip();//need flip
//        return buffer.getLong();
//    }


}


