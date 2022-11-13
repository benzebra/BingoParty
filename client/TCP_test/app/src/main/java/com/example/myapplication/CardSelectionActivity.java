package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
    public Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_selection);

        getSupportActionBar().hide();

        ctx = this;

        //Buttons
        EnterButton = findViewById(R.id.enter_button);
        Increase = findViewById(R.id.increase);
        Decrease = findViewById(R.id.decrease);

        CardsNumber = findViewById(R.id.cards_number);

        myServer = LobbyActivity.getServer();
        mySender = new Sender(myServer.getToServer());
        myReceiver = LobbyActivity.getReceiver();


        EnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = Integer.parseInt(CardsNumber.getText().toString());

                Thread sendToServer = new Thread(() -> {
                    mySender.sendMessage("cardsNumber", "cardsnumber");
                    mySender.sendMessage("cardsNumber", Integer.toString(getCardsNumber()));
                });
                sendToServer.start();

                Intent lobbyIntent = new Intent(ctx, PreGameLobbyActivity.class);
                lobbyIntent.putExtra(EXTRA_MESSAGE, "cardselected");
                lobbyIntent.putExtra(EXTRA_MESSAGE_NAME, LobbyActivity.getNameF());
                startActivity(lobbyIntent);
            }
        });


        //Incremento il numero di cartelle ( max: 4 )
        Increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tmpCards = Integer.parseInt(CardsNumber.getText().toString());

                if(tmpCards != 4) {
                    int increasedCards = tmpCards + 1;
                    CardsNumber.setText(Integer.toString(increasedCards));
                }else{
                    Toast toastMax = new Toast(ctx);
                    toastMax.setText("Numero massimo di tabelle raggiunto");
                    toastMax.show();
                }
            }
        });


        //Decremento il numero di cartelle ( min: 1 )
        Decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tmpCards = Integer.parseInt(CardsNumber.getText().toString());

                if(tmpCards != 1) {
                    int decreasedCards = tmpCards - 1;
                    CardsNumber.setText(Integer.toString(decreasedCards));
                }else{
                    Toast toastMin = new Toast(ctx);
                    toastMin.setText("Numero minimo di tabelle raggiunto");
                    toastMin.show();
                }
            }
        });
    }

    public static int getCardsNumber(){
        return number;
    }
}