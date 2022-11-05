package com.example.serverapplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Receiver extends Thread{
    private BufferedReader fromClient;
    private String playerNAME;
    private int playerMatrix = 0;

    public Receiver(BufferedReader fromClient){
        this.fromClient = fromClient;
    }

    /*  Messages recived by the client are 2 different types:
     *   1. firstInput (Client name, [String])
     *   2. GAME MATRIX NUMBER (number of cards choosen by Client, 0 < # < 5, [int])
     */
    @Override
    public void run(){
        String message = "";
        boolean firstInput = true;

        while(!Thread.interrupted()){
            try{
                message = fromClient.readLine();
                System.out.println("DEBUG Receiver:28, message: " + message);

                if(message != null){
                    if(firstInput){
                        setPlayerName(message);
                        firstInput = false;
                    }else if(message.equals("cardsnumber")){
                        setPlayerMatrix(Integer.parseInt(fromClient.readLine()));
                    }else{
                        notifyGameLoop();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void notifyGameLoop(){
        GameLoopActivity.setProceed();
    }

    public void setPlayerName(String NAME){
        this.playerNAME = NAME;
    }

    public String getPlayerNAME(){
        return this.playerNAME;
    }

    public void setPlayerMatrix(int num){
        this.playerMatrix = num;
    }

    public int getPlayerMatrix(){
        return this.playerMatrix;
    }
}
