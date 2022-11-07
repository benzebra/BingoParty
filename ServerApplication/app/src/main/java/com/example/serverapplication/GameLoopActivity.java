package com.example.serverapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

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
    private static boolean proceed = false;
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

        btn = new Button[4];
        btn[0] = findViewById(R.id.player1_btn);
        btn[1] = findViewById(R.id.player2_btn);
        btn[2] = findViewById(R.id.player3_btn);
        btn[3] = findViewById(R.id.player4_btn);

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

        //fragment
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
                            for(int i=0; i<checkWinThreadArray.length; i++){
                                checkWinThreadArray[i].interrupt();
                            }
                            signal = true;

                            runOnUiThread(() -> {
                                if(switchNumber != 4){
                                    fragment_player = new Fragment_Player(players.get(switchNumber).getMatrixArray(), players.get(switchNumber).getName());
                                    FragmentTransaction ft = fragmentManager.beginTransaction();
                                    ft.replace(R.id.fragmentCont, fragment_player);
                                    ft.commit();
                                }
                            });
                        }
                    }
                    index++;
                }
            }

            winnerIs();
        }
    });

    public void winnerIs(){
        System.out.println("winneris method");

        gameLoop.interrupt();
        for(Player user : players){
            user.getThreadReceiver().interrupt();
        }

        Intent winnerIntent = new Intent(this, WinnerActivity.class);
        System.out.println("DEBUG 173 WINNERINTENT");
        winnerIntent.putExtra(WINNER, winnermsg);
        startActivity(winnerIntent);
    }

    public static void incrementNumberOfCheckedClient(){
        numberOfCheckedClient++;
    }
    public void setNumberOfCheckedClient(){
        numberOfCheckedClient = 0;
    }

    public static void setWin(Player winner){
        //win = true;
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

    public static void setProceed(){
        proceed = true;
    }

    public static void setBingo(String name){
        System.out.println("setted bingo");
        win = true;
    }
}