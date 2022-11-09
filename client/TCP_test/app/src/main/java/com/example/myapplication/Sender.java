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

        toServer.println(content);

        if(!content.equals("reset")){
            toServer.flush();
        }
    }
}
