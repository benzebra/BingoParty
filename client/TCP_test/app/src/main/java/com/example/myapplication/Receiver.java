package com.example.myapplication;

import java.io.BufferedReader;
import java.util.ArrayList;

public class Receiver extends Thread{
    private final BufferedReader fromServer;

    private String[] parsedMatrix;
    private String exitedNumbers;

    public Receiver(BufferedReader from){
        this.fromServer = from;
    }

    @Override
    public void run(){
        while( !Thread.currentThread().isInterrupted() ){
            String msg;

            try{
                msg = fromServer.readLine();
                System.out.println("message from server: " + msg);

                if(msg != null){

                    switch (msg) {
                        case "start":

                            sendStartSignalCards();

                            break;
                        case "matrixstart":

                            int max = CardSelectionActivity.getCardsNumber();

                            parsedMatrix = new String[max];

                            for (int i = 0; i < max; i++) {
                                setMatrixes(fromServer.readLine(), i);
                            }

                            break;
                        case "matrixfinish":

                            setParsedMatrix();

                            GameLoopActivity.setParsedArray(parsedMatrix);

                            synchronized (GameLoopActivity.objMatrix) {
                                GameLoopActivity.flagMatrix = true;
                                GameLoopActivity.objMatrix.notify();
                            }

                            break;
                        case "startstream":

                            exitedNumbers = fromServer.readLine();

                            break;
                        case "finishstream":

                            GameLoopActivity.setStream(exitedNumbers.split(" "));

                            synchronized (GameLoopActivity.objStream) {
                                GameLoopActivity.flagStream = true;
                                GameLoopActivity.objStream.notify();
                            }

                            break;
                        case "startloop":

                            sendStartSignalLoop();

                            break;
                        case "completed":

                            sendBingoSignal();

                            break;
                        case "winnerIs":

                            startWinnerAct(fromServer.readLine());

                            break;
                        case "num":

                            notifyGameLoopToProceed();
                            break;
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void notifyGameLoopToProceed(){
        GameLoopActivity.setProceed();
    }

    public void sendStartSignalCards(){ LobbyActivity.setStartCards(); }

    public void sendStartSignalLoop(){ PreGameLobbyActivity.setStartLoop(); }

    public static void sendBingoSignal(){
        GameLoopActivity.notifyBingoButton();
    }

    public static void startWinnerAct(String name){
        GameLoopActivity.winerAct(name);
    }

    public void setMatrixes(String parsedArray, int index){
        parsedMatrix[index] = parsedArray;
    }

    public void setParsedMatrix(){
        ArrayList<Matrix> returnList = new ArrayList<>();

        for (String matrix : parsedMatrix) {

            String[] tmpArr = matrix.split(" ");
            Matrix.MatrixNumbers[] numbersArray = new Matrix.MatrixNumbers[tmpArr.length];

            for (int n = 0; n < 9; n++) {
                numbersArray[n] = new Matrix.MatrixNumbers(Integer.parseInt(tmpArr[n]), false);
            }

            Matrix tmp = new Matrix(numbersArray);

            returnList.add(tmp);
        }
    }
}