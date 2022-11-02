package com.example.serverapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameLoopActivity extends AppCompatActivity {

    private ArrayList<Player> players;
    private boolean[] numbers;
    private static boolean win = false;
    private static boolean proceed = false;
    private static int numberOfCheckedClient;

    private TextView newNumber;
    private TextView numberList;

    private RecyclerView recyclerView_playerFields;
    private RecyclerView.LayoutManager layoutManager_playerFields;
    private PlayersFieldAdapter playersFieldAdapter;

    public static String WINNER;
    public static String winnermsg;

    public static String stream;

    public static void setStream(String streamGen){
        stream = streamGen;
    }
    public String getStream(){
        return stream;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loop);

        //array giocatori
        players = LobbyActivity.getPlayers();

        //graphics
        newNumber = findViewById(R.id.newNumber);
        numberList = findViewById(R.id.numberList);

        //recycler
        recyclerView_playerFields = findViewById(R.id.recycler_playersFields);
        layoutManager_playerFields = new GridLayoutManager(this, 2);
        recyclerView_playerFields.setLayoutManager(layoutManager_playerFields);
        playersFieldAdapter = new PlayersFieldAdapter(players);
        recyclerView_playerFields.setAdapter(playersFieldAdapter);

        //invio segnale startloop
        sendstart.start();

        //array numeri usciti
        numbers = new boolean[90];
        Arrays.fill(numbers, false);

        //gameLoop(parseStream(getStream()));
        gameLoop.start();
    }

    Thread gameLoop = new Thread(new Runnable() {
        @Override
        public void run() {
            int index = 0;
            int[] streamInt = parseStream(getStream());

            while(!win){
                if(proceed){
                    proceed = false;

                    setNumberOfCheckedClient();
                    int number = streamInt[index];

                    int finalIndex = index;

                    runOnUiThread(() -> {

                        int startingIndex;
                        String toDisp = "";

                        if((finalIndex-5) > 0){
                            startingIndex = finalIndex-5;
                        }else {
                            startingIndex = 0;
                        }

                        for(int i = startingIndex; i<finalIndex; i++){
                            toDisp = toDisp + streamInt[i] + " ";
                        }
                        numberList.setText(toDisp);
                        newNumber.setText(Integer.toString(number));
                    });

                    Thread[] checkWinThreadArray = new checkWinThread[players.size()];
                    for(int i=0; i<players.size(); i++){
                        checkWinThreadArray[i] = new checkWinThread(number, players.get(i));
                        checkWinThreadArray[i].start();
                    }

                    boolean signal = false;
                    while(!signal){
                        if(numberOfCheckedClient == players.size()){
                            for(Thread t : checkWinThreadArray){
                                t.interrupt();
                            }
                            signal = true;
                        }
                    }

                    runOnUiThread(()->{
                        playersFieldAdapter.notifyDataSetChanged();
                    });
                    index++;
                }
            }

            winnerIs();
        }
    });

    /*
    public void gameLoop(int[] streamInt){
        int index = 0;

        while(!win){
            //mettere un if(booleano) [settato in true quando il receiver riceve un numero che indica l'indice]
            if(proceed) {
                proceed = false;
                //while(!win && index<streamInt.length){
                setNumberOfCheckedClient();
                int number = streamInt[index];
                System.out.println("Gameloop:73 - " + number);

                newNumber.setText(Integer.toString(number));

                //avvio tutti i thread che controllano (uno per giocatore)
                //prova a farlo con threadpool
                Thread[] checkWinThreads = new checkWinThread[players.size()];
                for (int i = 0; i < players.size(); i++) {
                    checkWinThreads[i] = new checkWinThread(number, players.get(i));
                    checkWinThreads[i].start();
                }

                //una volta avviati i thread aspetto il risultato
                //entro in un loop infinito in cui controllo che il numero di utenti controllati sia uguale a il totale
                boolean signal = false;
                while (!signal) {
                    //posso utilizzar eun metodo mentre questo sta ciclendo all'infinito (due utilizzi nello stesso thread)
                    if (numberOfCheckedClient == players.size()) {
                        for (int i = 0; i < players.size(); i++) {
                            checkWinThreads[i].interrupt();
                        }
                        signal = true;
                    } else {
                        //wait
                    }
                }
                index++;
            }
        }

        winnerIs();
    }

     */

    public void winnerIs(){
        Intent winnerIntent = new Intent(this, WinnerActivity.class);
        winnerIntent.putExtra(WINNER, winnermsg);
        startActivity(winnerIntent);
    }

    public static void incrementNumberOfCheckedClient(){
        numberOfCheckedClient++;
    }
    public void setNumberOfCheckedClient(){
        numberOfCheckedClient = 0;
    }

    public static void setWin(String winnerThread){
        win = true;
        winnermsg = winnerThread;
    }

    public int[] parseStream(String mystream){
        String[] tmpArr = mystream.split(" ");
        int[] newArr = new int[tmpArr.length];
        for(int i=0; i<newArr.length; i++){
            newArr[i] = Integer.parseInt(tmpArr[i]);
        }

        return newArr;
    }

    Thread sendstart = new Thread(new Runnable() {
        @Override
        public void run() {
            for(Player value : players){
                value.getSender().println("startloop");
                value.getSender().flush();
            }
        }
    });

    public static void setProceed(){
        proceed = true;
    }
}