package com.example.serverapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class LobbyActivity extends AppCompatActivity {

    //graphics
    public static ServerSocket mySocket;
    private Button startButton;
    private Button closeConnectionButton;
    private TextView playNum;
    private TextView title;
    private TextView timer;

    //RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PlayerAdapter playersAdapter;

    public static ArrayList<Player> players;
    private int playersNumber;
    public int PORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        PORT = Integer.parseInt(message);

        players = new ArrayList<>();

        startButton = findViewById(R.id.startButton);
        closeConnectionButton = findViewById(R.id.close_conn_btn);
        playNum = findViewById(R.id.playersnumber);
        timer = findViewById(R.id.timer);

        recyclerView = findViewById(R.id.recyclerViewPlayers);
        layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        playersAdapter = new PlayerAdapter(players);
        recyclerView.setAdapter(playersAdapter);

        title = findViewById(R.id.textView);
        String titleTV = "Working on port: " + message;
        title.setText(titleTV);

        //OnClickListeners

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connection.interrupt();
                Intent intent = new Intent(view.getContext(), PreGameActivity.class);
                startActivity(intent);
            }
        });

        closeConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playersNumber = 4;
            }
        });

        //Thread starting
        connection.start();
        timerThread.start();
    }

    /**
     * TimerThread
     *
     * Timer che si aggiorna ogni secondo con un conto alla rovescia da 10 (timeout della socket)
     */
    Thread timerThread = new Thread(){
        @SuppressLint("SetTextI18n")
        @Override
        public void run(){
        TextView number = timer;
        AtomicInteger innerNumber = new AtomicInteger(10);
            try{
                while(innerNumber.get() != -1){
                    runOnUiThread(() -> {
                        number.setText(Integer.toString(innerNumber.getAndDecrement()));
                    });
                    Thread.sleep(1000);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    };

    /**
     * Connection Thread
     *
     * Apre una connessione localhost alla porta 8080
     * Imposta i vari oggetti (BufferedReader, PrintWriter, player)
     *
     * 3 motivi attraverso i quali si può stoppare la ricerca:
     *      1. Timeout (10 secondi)
     *      2. Numero di giocatori >= 4 (numero massimo raggiunto)
     *      3. Premo il pulsante "CLOSE CONNECTION"
     */
    Thread connection = new Thread(){
        @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
        @Override
        public void run() {
            try {
                //ServerSocket creation
                mySocket = new ServerSocket();
                mySocket.bind(new InetSocketAddress("localhost",8080));
                mySocket.setSoTimeout(10000);

                playersNumber = 0;

                runOnUiThread(() -> {
                    closeConnectionButton.setEnabled(true);
                });

                while(playersNumber <= 4) {

                    Socket clientSocket = mySocket.accept();
                    System.out.println("client socket : " + clientSocket);

                    InputStream socketIn = clientSocket.getInputStream();
                    OutputStream socketOut = clientSocket.getOutputStream();

                    InputStreamReader socketReader = new InputStreamReader(socketIn);
                    OutputStreamWriter socketWriter = new OutputStreamWriter(socketOut);

                    BufferedReader fromClient = new BufferedReader(socketReader);
                    PrintWriter toClient = new PrintWriter(socketWriter);

                    Receiver receiverFrom = new Receiver(fromClient);
                    receiverFrom.start();

                    Player tmpPlayer = new Player();

                    tmpPlayer.setSender(toClient);
                    tmpPlayer.setReceiver(fromClient);
                    tmpPlayer.setThreadReceiver(receiverFrom);
                    tmpPlayer.setAddress(clientSocket.getInetAddress().toString());
                    tmpPlayer.setClientSocket(clientSocket);

                    players.add(tmpPlayer);

                    playersNumber++;

                    runOnUiThread(() -> {
                        playNum.setText(players.size() + " / 4");
                        playersAdapter.notifyDataSetChanged();
                    });
                }
            } catch (SocketTimeoutException e) {

                runOnUiThread(() -> {
                    closeConnectionButton.setEnabled(false);
                });


                playersNumber = players.size();

                //ricezione nomi giocatori
                boolean completed = false;
                boolean[] collected = new boolean[playersNumber];
                int count = 0;

                //colleziono il nome dei giocatori
                while(!completed){
                    for(int i=0; i<playersNumber; i++){
                        if(!players.get(i).getThreadReceiver().getPlayerNAME().equals("") && !collected[i]){
                            players.get(i).setName(players.get(i).getThreadReceiver().getPlayerNAME());
                            collected[i] = true;
                            count++;

                            runOnUiThread(() -> {
                                playersAdapter.notifyDataSetChanged();
                            });
                        }
                    }

                    if(count == playersNumber){
                        completed = true;
                    }
                }

                runOnUiThread(() -> {
                    playersAdapter.notifyDataSetChanged();

                    //TODO: change condition -> playersNumber > 1
                    if(playersNumber >= 1){
                        setStartButton();
                    }else{
                        closeConnection();
                    }
                });

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    public static ArrayList<Player> getPlayers(){
        return players;
    }

    public void setStartButton(){
        startButton.setEnabled(true);
    }

    public void closeConnection(){
        try{
            if(!mySocket.isClosed()){
                mySocket.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}