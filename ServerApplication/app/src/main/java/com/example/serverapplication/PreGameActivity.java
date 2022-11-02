package com.example.serverapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class PreGameActivity extends AppCompatActivity {

    private ArrayList<Player> playerArrayList;

    private TextView threePointsTV;
    private Button playButton;

    private String field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregame);

        threePointsTV = findViewById(R.id.threePointsTV);
        playButton = findViewById(R.id.playButton);

        playerArrayList = LobbyActivity.getPlayers();

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameIntent = new Intent(view.getContext(), GameLoopActivity.class);
                startActivity(gameIntent);
            }
        });

        setField(createField());

        sendStartAndMatrix.start();

        threePointsThread.start();
    }

    /**
     * Send the start signal ("start"), receive the matrix number (0 < x < 5) and assign to every player
     */
    Thread sendStartAndMatrix = new Thread(() -> {
        for (Player value : playerArrayList) {
            if (value != null) {
                value.getSender().println("start");
                value.getSender().flush();
            }
        }

        boolean completed = false;
        boolean[] collected = new boolean[playerArrayList.size()];
        int count = 0;

        while(!completed){
            for(int i=0; i< playerArrayList.size(); i++){
                if(!(playerArrayList.get(i).getThreadReceiver().getPlayerMatrix() == 0) && !collected[i]){

                    //prendo il numero di matrici dal Thread receiver e lo setto nell'oggeto Player
                    playerArrayList.get(i).setMatrixNumber(playerArrayList.get(i).getThreadReceiver().getPlayerMatrix());

                    //chiamo la funzione per creare le matrici di gioco di quel player e inviarle
                    createGameMatrix(playerArrayList.get(i));

                    //invio la stream di numeri casuali
                    sendStream(playerArrayList.get(i).getSender(), getField());

                    collected[i] = true;
                    count++;
                }
            }
            if(count == playerArrayList.size()){
                completed = true;
            }
        }

        runOnUiThread(() -> {
            GameLoopActivity.setStream(getField());
            playButton.setEnabled(true);
        });

        Thread.currentThread().interrupt();
    });

    public void createGameMatrix(Player myPlayer){
        ArrayList<int[]> tmpList = newMatrix(myPlayer.getMatrixNumber());
        myPlayer.setMatrixArray(tmpList);
        sendMatrix(myPlayer.getSender(), myPlayer.getMatrixArray());
    }

    public String createField(){
        boolean[] fieldCheck = new boolean[90];
        Arrays.fill(fieldCheck, false);
        String genfield = "";
        Random randomGen = new Random();

        for(int i=0; i<fieldCheck.length; i++){

            int tmp = randomGen.nextInt(90);

            if(!fieldCheck[tmp]){
                fieldCheck[tmp] = true;
                tmp = tmp + 1;
                genfield = genfield + tmp + " ";
            }else{
                i--;
            }
        }
        return genfield;
    }

    /**
     * Create the ArrayList of int[] associated to the client
     *
     * @param /int/ num (numebr of matrixes)
     * @return ArrayList of matrixes
     */
    public ArrayList<int[]> newMatrix(int num){
        Random generator = new Random();
        ArrayList<int[]> matrixArray = new ArrayList<int[]>();
        for(int i=0; i<num; i++){
            int[] tmp = new int[9];
            for(int j=0; j<9; j++){
                int tmpGen = 1 + generator.nextInt(90);
                boolean present = false;
                for(int f=0; f<j; f++){
                    if (tmp[f] == tmpGen) {
                        present = true;
                    }
                }

                if(!present){
                    tmp[j] = tmpGen;
                }else{
                    j--;
                }
            }
            matrixArray.add(tmp);
        }

        return matrixArray;
    }

    public void sendMatrix(PrintWriter sender, ArrayList<int[]> myList){
        /**
         * first message : "matrixstart"
         * send the ArrayList separatly (matrixnumber * 9 messages)
         * last message : "matrixfinish"
         */
        sender.println("matrixstart");
        sender.flush();

        //divido myList
        for(int i=0; i<myList.size(); i++){
            sender.println(stringify(myList.get(i)));
            sender.flush();
        }

        sender.println("matrixfinish");
        sender.flush();
    }

    /**
     * Integer array toString(), more easy sending to client
     *
     * @param /[int[]]/ array
     * @return [String] array
     */
    public String stringify(int[] array){
        String tmp = "";
        for(int i=0; i<array.length; i++){
            if(i != array.length-1){
                tmp = tmp + array[i] + " ";
            }else{
                tmp = tmp + array[i];
            }
        }

        return tmp;
    }

    public void sendStream(PrintWriter sender, String field){
        sender.println("startstream");
        sender.flush();
        sender.println(field);
        sender.flush();
        sender.println("finishstream");
        sender.flush();
    }

    Thread threePointsThread = new Thread(() -> {
        TextView space = threePointsTV;
        try {
            while (true) {
                runOnUiThread(() -> { space.setText("."); });
                Thread.sleep(1000);
                runOnUiThread(() -> { space.setText(".."); });
                Thread.sleep(1000);
                runOnUiThread(() -> { space.setText("..."); });
                Thread.sleep(1000);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    });

    public String getField(){
        return field;
    }

    public void setField(String field){
        this.field = field;
    }
}