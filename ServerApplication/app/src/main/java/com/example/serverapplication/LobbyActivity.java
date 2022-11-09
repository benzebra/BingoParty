package com.example.serverapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class LobbyActivity extends AppCompatActivity {

    public static ServerSocket mySocket;
    private Button startButton;
    private TextView playNum;
    private TextView title;
    private TextView timer;

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

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        startButton = findViewById(R.id.startButton);

        recyclerView = findViewById(R.id.recyclerViewPlayers);
        layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        players = new ArrayList<>();

        playersAdapter = new PlayerAdapter(players);
        recyclerView.setAdapter(playersAdapter);

        title = findViewById(R.id.textView);
        String titleTV = "Working on port: " + message;
        title.setText(titleTV);

        playNum = findViewById(R.id.playersnumber);
        timer = findViewById(R.id.timer);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connection.interrupt();
                Intent intent = new Intent(view.getContext(), PreGameActivity.class);
                startActivity(intent);
            }
        });

        PORT = Integer.parseInt(message);
        connection.start();
        timerThread.start();
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

    public void setStartButton(){
        startButton.setEnabled(true);
    }

    public static ArrayList<Player> getPlayers(){
        return players;
    }

    Thread timerThread = new Thread(){
        @SuppressLint("SetTextI18n")
        @Override
        public void run(){
        TextView number = timer;
        AtomicInteger innerNumber = new AtomicInteger(9);
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

    Thread connection = new Thread(){
        @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
        @Override
        public void run() {
            try {
                mySocket = new ServerSocket(PORT);
                mySocket.setSoTimeout(10000);

                playersNumber = 0;

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
                    playNum.setText(playersNumber + " / 4");
                    playersAdapter.notifyDataSetChanged();
                });

                }
            } catch (SocketTimeoutException e) {

                //ricezione nomi giocatori
                boolean completed = false;
                boolean[] collected = new boolean[playersNumber];
                int count = 0;

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

                /*
                //inoltro nomi ai giocatori
                for(int i=0; i<playersNumber; i++){
                    players.get(i).setName(players.get(i).getThreadReceiver().getPlayerNAME());
                    for(int j=0; j<playersNumber; j++){
                        if(j != i){
                            PrintWriter tmp = players.get(i).getSender();
                            tmp.println(players.get(j).getThreadReceiver().getPlayerNAME());
                            tmp.flush();
                        }
                    }
                }
                */

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
}