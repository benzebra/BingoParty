package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class GameLoopActivity extends AppCompatActivity {

    /**
     * ArrayList<int[]>       ->   ArrayList<Matrix[]>
     * Matrix[]               ->   Matrix = Lista di Matrix
     * Matrix                 ->   Lista di MatrixNumbers
     * MatrixNumbers          ->   (int numero, boolean flagExited, String numero.toString())
     */
    private ArrayList<Matrix> matrixArrayList;

    private TextView extract;
    private static Context myContext;

    public static String WINNER;
    private Button bingo_btn;
    private static boolean win = false;
    private static boolean proceed = false;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MatrixAdapter matrixAdapter;

    private Sender mySender;
    private Receiver myReceiver;
    private int[] streamInt;
    private static String[] parsedArray;
    private static String[] stream;

    //soncronizzazione
    public static final Object objMatrix = new Object();
    public static final Object objStream = new Object();
    public static boolean flagMatrix = false;
    public static boolean flagStream = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loop);

        getSupportActionBar().hide();

        extract = findViewById(R.id.estrazione);

        myContext = this;

        recyclerView = findViewById(R.id.recycler);
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        matrixArrayList = new ArrayList<Matrix>();

        matrixAdapter = new MatrixAdapter(matrixArrayList);
        recyclerView.setAdapter(matrixAdapter);

        mySender = new Sender(LobbyActivity.getServer().getToServer());
        myReceiver = LobbyActivity.getReceiver();

        bingo_btn = findViewById(R.id.BingoButton);
        bingo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(win){
                    Thread bingoMessage = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mySender.sendMessage("BINGO", "BINGO");
                        }
                    });
                    bingoMessage.start();
                }
            }
        });

        //RICEVO LE CARTELLE DAL SERVER
        Thread receiverCom = new Thread(() -> {
            synchronized (objMatrix) {
                while (!flagMatrix) {
                    try {
                        objMatrix.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                parseToList();
            }

            synchronized (objStream){
                while(!flagStream){
                    try{
                        objStream.wait();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                setStreamInt();
                renderNumber.start();
                Thread.currentThread().interrupt();
            }


        });
        receiverCom.start();

    }

    /**
     * parseToList
     *
     * Trasforma la stream in object Matrix
     */
    public void parseToList(){
        String[] array = getParsedArray();
        for(int i=0; i<array.length; i++){
            String[] tmpArray = array[i].split(" ");
            Matrix.MatrixNumbers[] numbersArray = new Matrix.MatrixNumbers[tmpArray.length];

            for(int j=0; j<tmpArray.length; j++){
                numbersArray[j] = new Matrix.MatrixNumbers(Integer.parseInt(tmpArray[j]), false);
            }

            Matrix tmp = new Matrix(numbersArray);

            matrixArrayList.add(tmp);
            matrixAdapter.notifyDataSetChanged();
        }

        mySender.sendMessage("finishrender", "finishrender");
    }

    public void setStreamInt(){
        String[] array = getStream();
        int[] newArr = new int[array.length];
        for(int i=0; i<array.length; i++){
            newArr[i] = Integer.parseInt(array[i]);
        }

        streamInt = newArr;
    }

    //imposta il numero nella grafica
    public void setMainNumber(int num){
        extract.setText(Integer.toString(num));
        findNumber(num);
    }

    /**
     * findNumber
     *
     * Cerca il numero estratto all'interno delle Matrix
     */
    public void findNumber(int num){

        Matrix.MatrixNumbers[] tmpMatrix;

        for(int i=0; i<matrixArrayList.size(); i++){
            tmpMatrix = matrixArrayList.get(i).getMyMatrix();
            for(int j=0; j<tmpMatrix.length; j++){
                if(tmpMatrix[j].getNumero() == num){
                    Matrix.MatrixNumbers[] finalTmpMatrix = tmpMatrix;
                    int finalJ = j;
                    runOnUiThread(()->{
                        finalTmpMatrix[finalJ].setFlag(true);

                        matrixAdapter.notifyDataSetChanged();
                    });
                }
            }
        }
    }

    Thread renderNumber = new Thread(() -> {
        int[] numbersStream = getStreamInt();
        int index = 0;

        while(!win){
            if(proceed){
                proceed = false;

                setMainNumber(numbersStream[index]);
                index++;
            }
        }
    });

    public static void notifyBingoButton(){
        win = true;
    }

    public static void winerAct(String name){
        Intent winnerIntent = new Intent(myContext, WinnerActivity.class);
        winnerIntent.putExtra(WINNER, name);
        myContext.startActivity(winnerIntent);
    }


    //GETTER e SETTER
    public static void setProceed(){
        proceed = true;
    }

    public static void setParsedArray(String[] arr){
        parsedArray = arr;
    }
    public String[] getParsedArray(){
        return parsedArray;
    }

    public static void setStream(String[] arr){
        stream = arr;
    }
    public String[] getStream(){
        return stream;
    }

    public int[] getStreamInt(){
        return streamInt;
    }
}