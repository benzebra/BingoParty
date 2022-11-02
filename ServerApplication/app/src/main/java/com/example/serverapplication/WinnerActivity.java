package com.example.serverapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class WinnerActivity extends AppCompatActivity {

    private TextView winnerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);

        winnerName = findViewById(R.id.winnerName);

        Intent intent = getIntent();
        String NAME = intent.getStringExtra(GameLoopActivity.WINNER);

        winnerName.setText(NAME);
    }
}