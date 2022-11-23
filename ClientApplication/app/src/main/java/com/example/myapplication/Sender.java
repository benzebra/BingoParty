package com.example.myapplication;

import java.io.PrintWriter;

public class Sender {
    private PrintWriter toServer;

    public Sender(PrintWriter dest){
        this.toServer = dest;
    }

    public void sendMessage(String type, String content){
        toServer.println(content);

        if(!content.equals("reset")){
            toServer.flush();
        }
    }
}
