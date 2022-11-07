package com.example.serverapplication;

import java.io.BufferedReader;

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
                System.out.println("DEBUG Receiver, message: " + message);

                if(message != null){
                    if(firstInput){
                        setPlayerName(message);
                        firstInput = false;
                    }else if(message.equals("cardsnumber")){
                        setPlayerMatrix(Integer.parseInt(fromClient.readLine()));
                    }else if(message.equalsIgnoreCase("bingo")){
                        System.out.println("bingo message received");
                        notifyGameLoopBingo(getPlayerNAME());
                    }else{
                        notifyGameLoopProceed();
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

    public static void notifyGameLoopProceed(){
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
