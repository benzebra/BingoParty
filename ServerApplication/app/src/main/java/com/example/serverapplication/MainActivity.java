package com.example.serverapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static String EXTRA_MESSAGE;

    public static final String PORT = "8080";

    private TextView portTV;
    private Button connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        connect = findViewById(R.id.openServerBtn);
        portTV = findViewById(R.id.portTV);
        portTV.setText("Working on port: " + PORT);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LobbyActivity.class);
                intent.putExtra(EXTRA_MESSAGE, PORT);
                startActivity(intent);
            }
        });
    }
}