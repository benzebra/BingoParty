package com.example.serverapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class WinnerActivity extends AppCompatActivity {

    private TextView winnerName;
    private Button restart_btn;
    private static ArrayList<Player> players;

    private static Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);

        ctx = this;

        winnerName = findViewById(R.id.winnerName);

        Intent intent = getIntent();
        String NAME = intent.getStringExtra(GameLoopActivity.WINNER);

        players = LobbyActivity.getPlayers();
        Thread sendWinner = new Thread(new Runnable() {
            @Override
            public void run() {
                for(Player user : players){
                    user.getSender().println("winnerIs");
                    user.getSender().flush();
                    user.getSender().println(NAME);
                    user.getSender().flush();
                }
            }
        });
        sendWinner.start();
        resetGame();

        winnerName.setText(NAME);

        restart_btn = findViewById(R.id.restart_btn);
        restart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }

    public void resetGame(){
        Thread stopReceiversAndConnection = new Thread(new Runnable() {
            @Override
            public void run() {
                for(Player user : players){
                    user.getThreadReceiver().interrupt();
                }

                try {
                    LobbyActivity.mySocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        stopReceiversAndConnection.start();
    }


}