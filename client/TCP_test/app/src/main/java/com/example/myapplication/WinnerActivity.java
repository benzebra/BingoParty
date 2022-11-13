package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WinnerActivity extends AppCompatActivity {

    private TextView winnerName;
    private Button resetButton;

    private Sender mySender;

    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);

        getSupportActionBar().hide();

        ctx = this;

        Intent intent = getIntent();
        String WINNER = intent.getStringExtra(GameLoopActivity.WINNER);
        winnerName = findViewById(R.id.winnerNameTV);

        if(WINNER.equals("timeout")){
            winnerName.setText("!!! TIMEOUT !!!");
        }else{
            winnerName.setText(WINNER);
        }

        mySender = new Sender(LobbyActivity.getServer().getToServer());

        resetButton = findViewById(R.id.resetApp_btn);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(ctx, MainActivity.class);
                ctx.startActivity(mainIntent);
            }
        });
    }
}