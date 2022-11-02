package com.example.myapplication;

import java.io.BufferedReader;
import java.util.ArrayList;

public class Receiver extends Thread{
    private BufferedReader fromServer;

    private String[] parsedMatrix;
    private ArrayList<Matrix> matrixArrayList;
    private Object lock = new Object();
    //private boolean flag = false;
    private String exitedNumbers;
    private int index = 0;
    private int exitedNumber;

    public Receiver(BufferedReader from){
        this.fromServer = from;
    }

    @Override
    public void run(){
        while( !Thread.currentThread().isInterrupted() ){
            String msg = "";

            try{
                msg = fromServer.readLine();
                System.out.println("message from server: " + msg);

                if(msg != null){

                    if(msg.equals("start")){
                        //System.out.println("received start message from server");
                        matrixArrayList = new ArrayList<>();
                        sendStartSignalCards();

                    }else if (msg.equals("matrixstart")) {

                        int max = CardSelectionActivity.getCardsNumber();

                        parsedMatrix = new String[max];

                        for (int i=0; i<max; i++) {
                            setMatrixes(fromServer.readLine(), i);
                        }

                    }else if(msg.equals("matrixfinish")){
                        setParsedMatrix();

                        GameLoopActivity.setParsedArray(parsedMatrix);

                        synchronized (GameLoopActivity.objMatrix){
                            GameLoopActivity.flagMatrix = true;
                            GameLoopActivity.objMatrix.notify();
                        }

                    }else if(msg.equals("startstream")) {
                        //setNumber(msg);
                        //----------------------------
                        //una volta ricevute le tabelle il server manda i seguenti messaggi;
                        //"startstream"
                        //90 messaggi con l'ordine di uscita di tutti i numeri
                        //li memorizzo in array in modo tale da poterli riprendere dal gameloop senza static methods
                        //----------------------------
                        exitedNumbers = fromServer.readLine();
                    }else if(msg.equals("finishstream")) {

                        GameLoopActivity.setStream(exitedNumbers.split(" "));

                        synchronized (GameLoopActivity.objStream){
                            GameLoopActivity.flagStream = true;
                            GameLoopActivity.objStream.notify();
                        }
                    }else if(msg.equals("startloop")){
                        //System.out.println("received startloop message from server");
                        //LobbyActivity.flag = true;
                        sendStartSignalLoop();
                        //setExitedNumber(Integer.parseInt(fromServer.readLine()));
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void sendStartSignalCards(){ LobbyActivity.setStartCards(); }
    public void sendStartSignalLoop(){ PreGameLobbyActivity.setStartLoop(); }

    public void setExitedNumber(int num){
        exitedNumber = num;
    }

    /*public String getNumber(){
        return exitedNumbers[index];
    }

     */

    public void setMatrixes(String parsedArray, int index){
        parsedMatrix[index] = parsedArray;
    }

    public void setParsedMatrix(){
        ArrayList<Matrix> returnList = new ArrayList<Matrix>();

        for(int j=0; j<parsedMatrix.length; j++){

            String[] tmpArr = parsedMatrix[j].split(" ");
            Matrix.MatrixNumbers[] numbersArray = new Matrix.MatrixNumbers[tmpArr.length];

            for(int n=0; n<9; n++){
                numbersArray[n] = new Matrix.MatrixNumbers(Integer.parseInt(tmpArr[n]), false);
            }

            Matrix tmp = new Matrix(numbersArray);

            returnList.add(tmp);
        }
    }

    public ArrayList<Matrix> getMatrix(){
        int iter = 0;
        while(matrixArrayList.isEmpty()){
            iter++;
        }
        //System.out.println("iter: " + iter);
        return matrixArrayList;
    }
}