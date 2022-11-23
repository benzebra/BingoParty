package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class LobbyActivity extends AppCompatActivity {

    private TextView tw;
    private TextView waiting;

    private static Server myServer;
    private static Sender mySender;
    private static Receiver myReceiver;
    private static String nameF;

    private final String SERVER_IP = "localhost";
    private static boolean startCards = false;

    private Thread threePoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        getSupportActionBar().hide();

        tw = findViewById(R.id.introTV);
        waiting = findViewById(R.id.threePoints);

        String message;
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

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

        tw.setText("Hello " + message + ", wait for other opponents");
        startSignalCards();
        serverConnection(message);
    }

    /**
     * serverConnection
     *
     * Metodo che permette di collegarsi al server aperto nel LocalHost.
     * Utilizza la classe
     *      java.net.Socket
     * Inoltre, inizializza tutti gli oggetti necessari alla connesione (oggetto server)
     * @param name
     */
    public void serverConnection(String name){
        myServer = new Server();

        new Thread(new Runnable() {
            @Override
            public void run() {

                //connection
                try{
                    InetAddress address = InetAddress.getByName(SERVER_IP);
                    myServer.setInetAddress(address);

                    Socket mySocket = new Socket(address, 8080);
                    myServer.setSocket(mySocket);

                    InputStream socketIn = myServer.getSocket().getInputStream();
                    OutputStream socketOut = myServer.getSocket().getOutputStream();

                    InputStreamReader socketReader = new InputStreamReader(socketIn);
                    OutputStreamWriter socketWriter = new OutputStreamWriter(socketOut);

                    myServer.setFromServer(new BufferedReader(socketReader));
                    myServer.setToServer(new PrintWriter(socketWriter));

                    myReceiver = new Receiver(myServer.getFromServer());
                    mySender = new Sender(myServer.getToServer());

                    mySender.sendMessage("name",name);
                    setNameF(name);
                    myReceiver.start();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * startSignalCards
     *
     * Quando arriva la comunicazione dal server attiva la CardSelectionActivity
     */
    public void startSignalCards(){
        Intent CardSelectionIntent = new Intent(this, CardSelectionActivity.class);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(startCards){
                        startActivity(CardSelectionIntent);
                        break;
                    }
                }
            }
        }).start();
    }

    //GETTER e SETTER
    public static void setStartCards(){
        startCards = true;
    }

    public static Server getServer(){
        return myServer;
    }

    public static Receiver getReceiver(){
        return myReceiver;
    }

    public static String getNameF(){
        return nameF;
    }

    public static void setNameF(String str){
        nameF = str;
    }
}