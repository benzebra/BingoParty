package com.example.serverapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class GameLoopActivity extends AppCompatActivity {

    private ArrayList<Player> players;
    private boolean[] numbers;
    private static boolean win = false;
    private static int numberOfCheckedClient;

    private TextView newNumber;
    private TextView numberList;

    private Fragment_Player fragment_player;
    private FragmentManager fragmentManager;
    private int switchNumber = 4;

    public static String WINNER;
    public static String winnermsg;

    private Button[] btn;

    public static String stream;

    private boolean finished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loop);

        getSupportActionBar().hide();

        //array giocatori
        players = LobbyActivity.getPlayers();

        //graphics
        newNumber = findViewById(R.id.newNumber);
        numberList = findViewById(R.id.numberList);

        btn = new Button[4];
        btn[0] = findViewById(R.id.player1_btn);
        btn[1] = findViewById(R.id.player2_btn);
        btn[2] = findViewById(R.id.player3_btn);
        btn[3] = findViewById(R.id.player4_btn);

        //fragment buttons
        for(int i=0; i<players.size(); i++){
            int finalI = i;
            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switchNumber = finalI;
                }
            });
        }

        for(int i=players.size(); i<4; i++){
            btn[i].setEnabled(false);
        }

        //fragment manager
        fragmentManager = getSupportFragmentManager();

        //standard fragment
        Fragment_STD fragment_std = Fragment_STD.newInstance();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragmentCont, fragment_std);
        ft.commit();

        //array numeri usciti
        numbers = new boolean[90];
        Arrays.fill(numbers, false);

        //invio segnale startloop
        sendstart.start();

        gameLoop.start();
        timeout.start();
    }

    /**
     * gameLoop Thread
     *
     * Principalmente eseguo 3 azioni:
     *      1. Notifico tutti i giocatori che sto estraendo un nuovo numero
     *      2. Mostro a display il nuovo numero (e lo aggiungo alla lista di quelli usciti)
     *      3. Controllo che il numero sia presente nelle matrici dei giocatori (chekWinThreads)
     *          3a. Se presente lo tolgo dalla lista
     *
     * Eseguo queste azioni ripetutamente ogni 1500ms fino a che si verifica una di queste 3 condizioni:
     *      1. Un giocatore ha fatto "BINGO" (win = true)
     *      2. E' finita la lista di numeri (finished = true)
     *      3. Timeout (dopo 180000ms)
     */
    Thread gameLoop = new Thread(new Runnable() {
        @Override
        public void run() {
            int[] streamInt = parseStream(getStream());

            while(!win){                //condizione 1
                if(!finished){          //condizione 2
                    for(int i=0; i<streamInt.length; i++){
                        if(win){        //condizione 1
                            break;
                        }

                        //notifico l'utente che è uscito un nuovo numero
                        for(Player user : players){
                            user.getSender().println("num");
                            user.getSender().flush();
                        }

                        setNumberOfCheckedClient();
                        int number = streamInt[i];

                        //mostro il numero a display
                        int finalIndex = i;
                        runOnUiThread(() -> {
                            int startingIndex;
                            String toDisp = "";

                            if((finalIndex-5) > 0){
                                startingIndex = finalIndex-5;
                            }else {
                                startingIndex = 0;
                            }

                            for(int j = startingIndex; j<finalIndex; j++){
                                toDisp = toDisp + streamInt[j] + " ";
                            }
                            numberList.setText(toDisp);
                            newNumber.setText(Integer.toString(number));
                        });

                        //avvio i checkWinThreads
                        Thread[] checkWinThreadArray = new checkWinThread[players.size()];
                        for(int j=0; j<players.size(); j++){
                            checkWinThreadArray[j] = new checkWinThread(number, players.get(j));
                            checkWinThreadArray[j].start();
                        }

                        //attendo che i checkWinThreads abbiano finito la ricerca
                        boolean signal = false;
                        while(!signal){
                            if(numberOfCheckedClient == players.size()){
                                for(int j=0; j<checkWinThreadArray.length; j++){
                                    checkWinThreadArray[j].interrupt();
                                }
                                signal = true;

                                runOnUiThread(() -> {
                                    //aggiorno il fragment
                                    if(switchNumber != 4){
                                        fragment_player = new Fragment_Player(players.get(switchNumber).getMatrixArray(), players.get(switchNumber).getName());
                                        FragmentTransaction ft = fragmentManager.beginTransaction();
                                        ft.replace(R.id.fragmentCont, fragment_player);
                                        ft.commit();
                                    }
                                });
                            }
                        }

                        try {
                            Thread.sleep(1500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    finished = true;
                }else{
                    //condizione 2
                    winnerIs(true);
                }
            }
            //condizione 1
            winnerIs(false);
        }
    });

    public void winnerIs(boolean timeout){
        gameLoop.interrupt();
        Intent winnerIntent = new Intent(this, WinnerActivity.class);

        if(timeout){                //condizione 3
            WINNER = "timeout";
        }

        winnerIntent.putExtra(WINNER, winnermsg);
        startActivity(winnerIntent);
    }

    //supporto per i checkWinThreads
    public static void incrementNumberOfCheckedClient(){
        numberOfCheckedClient++;
    }

    public void setNumberOfCheckedClient(){
        numberOfCheckedClient = 0;
    }

    public static void setWin(Player winner){
        winnermsg = winner.getName();
        winner.getSender().println("completed");
        winner.getSender().flush();
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
            Thread.currentThread().interrupt();
        }
    });

    public static void setBingo(String name){
        win = true;
    }

    Thread timeout = new Thread(new Runnable() {
        @Override
        public void run() {
            try{
                Thread.sleep(180000);
            }catch(Exception e){
                e.printStackTrace();
            }
            winnerIs(true);
        }
    });


    //GETTER e SETTER di stream
    public static void setStream(String streamGen){
        stream = streamGen;
    }

    public String getStream(){
        return stream;
    }

}