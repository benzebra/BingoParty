package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
    }

    public void sendMessage(View view){
        Intent intent = new Intent(this, LobbyActivity.class);
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        String NAME = editText.getText().toString();

        if(NAME.length() > 0 && !NAME.equals("timeout")){
            intent.putExtra(EXTRA_MESSAGE, NAME);
            startActivity(intent);
        }else {
            Toast toast = new Toast(this);
            toast.setText("Invalid username");
            toast.show();
        }
    }
}