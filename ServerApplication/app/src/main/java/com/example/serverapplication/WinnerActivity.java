package com.example.serverapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class WinnerActivity extends AppCompatActivity {

    private TextView winnerName;
    private ArrayList<Player> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);

        winnerName = findViewById(R.id.winnerName);

        Intent intent = getIntent();
        String NAME = intent.getStringExtra(GameLoopActivity.WINNER);

        System.out.println("winneract");
        players = LobbyActivity.getPlayers();
        Thread sendWinner = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("sendwin");
                for(Player user : players){
                    user.getSender().println("winnerIs");
                    user.getSender().flush();
                    user.getSender().println(NAME);
                    user.getSender().flush();
                }
            }
        });
        sendWinner.start();


        winnerName.setText(NAME);
    }
}