package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;

public class CardSelectionActivity extends AppCompatActivity {

    public static String EXTRA_MESSAGE;
    public static String EXTRA_MESSAGE_NAME;
    private Button EnterButton;
    private Button Increase;
    private Button Decrease;

    private TextView CardsNumber;

    private Server myServer;
    private static Sender mySender;
    private Receiver myReceiver;

    private static int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_selection);

        EnterButton = findViewById(R.id.enter_button);
        Increase = findViewById(R.id.increase);
        Decrease = findViewById(R.id.decrease);

        CardsNumber = findViewById(R.id.cards_number);

        myServer = LobbyActivity.getServer();
        mySender = new Sender(myServer.getToServer());
        //System.out.println("DEBUG CardSelection:41, this is mysender: " + mySender.toString());
        myReceiver = LobbyActivity.getReceiver();
    }

    //Incremento il numero di cartelle ( max: 4 )
    public void inc(View view){
        int tmpCards = Integer.parseInt(CardsNumber.getText().toString());

        if(tmpCards != 4) {
            int increasedCards = tmpCards + 1;
            CardsNumber.setText(Integer.toString(increasedCards));
        }else{
            Toast toastMax = new Toast(this);
            toastMax.setText("Numero massimo di tabelle raggiunto");
            toastMax.show();
        }
    }

    //Decremento il numero di cartelle ( min: 1 )
    public void dec(View view){
        int tmpCards = Integer.parseInt(CardsNumber.getText().toString());

        if(tmpCards != 1) {
            int decreasedCards = tmpCards - 1;
            CardsNumber.setText(Integer.toString(decreasedCards));
        }else{
            Toast toastMin = new Toast(this);
            toastMin.setText("Numero minimo di tabelle raggiunto");
            toastMin.show();
        }
    }

    public void play(View view){
        number = Integer.parseInt(CardsNumber.getText().toString());

        /*
        Intent gameIntent = new Intent(this, GameLoopActivity.class);
        startActivity(gameIntent);
         */

        Thread sendToServer = new Thread(() -> {
            mySender.sendMessage("cardsNumber", "cardsnumber");
            mySender.sendMessage("cardsNumber", Integer.toString(getCardsNumber()));
        });
        sendToServer.start();

        Intent lobbyIntent = new Intent(this, PreGameLobbyActivity.class);
        lobbyIntent.putExtra(EXTRA_MESSAGE, "cardselected");
        lobbyIntent.putExtra(EXTRA_MESSAGE_NAME, LobbyActivity.getNameF());
        startActivity(lobbyIntent);
    }

    public static int getCardsNumber(){
        return number;
    }
}