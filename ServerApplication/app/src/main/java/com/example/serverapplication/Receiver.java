package com.example.serverapplication;

import java.io.BufferedReader;

public class Receiver extends Thread{

    private final BufferedReader fromClient;
    private String playerNAME;
    private int playerMatrix = 0;

    public Receiver(BufferedReader fromClient){
        this.fromClient = fromClient;
    }

    /**
     * I messaggi ricevuti possono essere di diverso tipo, grazie a questo Thread quando li ricevo
     * li smisto anche, a seconda del loro utilizzo.
     */
    @Override
    public void run(){
        String message;
        boolean firstInput = true;

        while(!Thread.interrupted()){
            try{
                message = fromClient.readLine();

                if(message != null) {

                    if (firstInput) {
                        //nome
                        setPlayerName(message);
                        firstInput = false;

                    } else if (message.equalsIgnoreCase("cardsnumber")) {
                        //numero schede
                        setPlayerMatrix(Integer.parseInt(fromClient.readLine()));

                    } else if (message.equalsIgnoreCase("bingo")) {
                        //bingo!
                        notifyGameLoopBingo(getPlayerNAME());

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void notifyGameLoopBingo(String name){
        GameLoopActivity.setBingo(name);
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
