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

    private Thread threePoints;
    //public static boolean flag = false;

    private static boolean startCards = false;
    //private static boolean startLoop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        tw = findViewById(R.id.introTV);
        waiting = findViewById(R.id.threePoints);

        String message;
        Intent intent = getIntent();

        //System.out.println(flag);

        //if(flag){
        //if(intent.hasExtra(CardSelectionActivity.EXTRA_MESSAGE)){
        //    message = intent.getStringExtra(CardSelectionActivity.EXTRA_MESSAGE);
        //}else{
            message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        //}

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

        /*
        if(!message.equals("cardselected")){
            tw.setText("Hello " + message + ", wait for other opponents");
            flag = true;
            startSignalCards();
            serverConnection(message);
        }else{
            String name = intent.getStringExtra(CardSelectionActivity.EXTRA_MESSAGE_NAME);
            tw.setText("Hello " + name + ", wait for other opponents");
            startSignalGame();
        }
         */
        tw.setText("Hello " + message + ", wait for other opponents");
        startSignalCards();
        serverConnection(message);
    }

    public void serverConnection(String name){
        myServer = new Server();
        //System.out.println("out of thread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //System.out.println("connection");
                //create localhost InetAddress
                byte[] localhost = new byte[4];
                for(int n=0; n<4; n++){
                    localhost[n] = 0;
                }

                //connection
                try{
                    InetAddress address = InetAddress.getByAddress(localhost);
                    myServer.setInetAddress(address);

                    myServer.setSocket(new Socket(address, 1050));
                    //System.out.println(myServer.getSocket());

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

    /*
    public void startSignalGame(){
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

     */

    public static void setStartCards(){ startCards = true; }

    //public static void setStartLoop(){ startLoop = true; }

    public static Server getServer(){ return myServer; }

    public static Sender getSender(){ return mySender; }

    public static Receiver getReceiver(){ return myReceiver; }

    public static String getNameF(){ return nameF; }

    public static void setNameF(String str){ nameF = str; }
}