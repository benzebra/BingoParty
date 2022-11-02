package com.example.myapplication;

import java.io.PrintWriter;

public class Sender {
    private PrintWriter toServer;

    public Sender(PrintWriter dest){
        this.toServer = dest;
    }

    public void sendMessage(String type, String content){
        //message switch

        /*
        if(type.equals("name")){

            toServer.println(content);

        }else if(type.equals("cardsNumber")){

            toServer.println(content);

        }else if(type.equals("")){

        }

         */
        //System.out.println("DEBUG Sender:29, message: " + content);
        toServer.println(content);
        //System.out.println("DEBUG Sender:31, message sent, now it's time to flush");
        toServer.flush();
        //System.out.println("DEBUG Sender:33, flushed");
    }
}
