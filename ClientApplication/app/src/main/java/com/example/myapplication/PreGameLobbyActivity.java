package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PreGameLobbyActivity extends AppCompatActivity {

    private TextView tw;
    private TextView waiting;

    private Thread threePoints;

    public static boolean startLoop = false;

    public static void setStartLoop(){ startLoop = true; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_game_lobby);

        getSupportActionBar().hide();

        tw = findViewById(R.id.introTVpregame);
        waiting = findViewById(R.id.threePointspregame);

        Intent intent = getIntent();
        String name = intent.getStringExtra(CardSelectionActivity.EXTRA_MESSAGE_NAME);

        threePoints = new Thread(){
            @Override
            public void run(){
                TextView space = waiting;
                try {
                    while (true) {
                        runOnUiThread(() -> { space.setText("."); });
                        Thread.sleep(800);
                        runOnUiThread(() -> { space.setText(".."); });
                        Thread.sleep(800);
                        runOnUiThread(() -> { space.setText("..."); });
                        Thread.sleep(800);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };

        threePoints.start();

        tw.setText("Hello " + name + ", wait for other opponents");
        startSignalGameLoop();
    }

    public void startSignalGameLoop(){
        Intent GameLoopIntent = new Intent(this, GameLoopActivity.class);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(startLoop){
                        startActivity(GameLoopIntent);
                        break;
                    }
                }
            }
        }).start();
    }
}