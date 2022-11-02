package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class GameLoopActivity extends AppCompatActivity {

    //private static ArrayList<int[]> matrixList;    //---------------> trasforma in elementi Matrix
    /*
    ArrayList<int[]>       ->   ArrayList<Matrix[]>
    Matrix[]               ->   Matrix = Lista di Matrix
    Matrix                 ->   Lista di MatrixNumbers
    MatrixNumbers          ->   (int numero, boolean flagExited, String numero.toString())
     */
    private ArrayList<Matrix> matrixArrayList;
    private TextView extract;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MatrixAdapter matrixAdapter;

    private Sender mySender;
    private Receiver myReceiver;

    private static String[] parsedArray;
    private static String[] stream;

    private int[] streamInt;

    public static Object objMatrix = new Object();
    public static Object objStream = new Object();
    public static Object objStreamInt = new Object();
    public static boolean flagMatrix = false;
    public static boolean flagStream = false;
    public static boolean flagStreamInt = false;


    public static void setParsedArray(String[] arr){
        parsedArray = arr;
    }
    public String[] getParsedArray(){
        return parsedArray;
    }

    public static void setStream(String[] arr){
        stream = arr;
        /*
        System.out.println("DEBUG Loop:51, stream: ");
        for(int i=0; i<stream.length; i++){
            System.out.print(stream[i] + " ");
        }

         */
    }
    public String[] getStream(){
        return stream;
    }

    /*public void setStreamInt(int[] stream){
        streamInt = stream;
    }*/
    public int[] getStreamInt(){
        return streamInt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loop);

        //System.out.println("DEBUG GameLoop");

        getSupportActionBar().hide();

        extract = findViewById(R.id.estrazione);

        recyclerView = findViewById(R.id.recycler);
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        matrixArrayList = new ArrayList<Matrix>();

        matrixAdapter = new MatrixAdapter(matrixArrayList);
        recyclerView.setAdapter(matrixAdapter);

        mySender = new Sender(LobbyActivity.getServer().getToServer());
        myReceiver = LobbyActivity.getReceiver();

        //INVIO NUMERO DI CARTELLE AL SERVER
        /*
        Thread sendToServer = new Thread(() -> {
            mySender.sendMessage("cardsNumber", Integer.toString(CardSelectionActivity.getCardsNumber()));
        });
        sendToServer.start();
         */

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

            /*
            synchronized (objStreamInt){
                while(!flagStreamInt){
                    renderNumber.start();
                    Thread.currentThread().interrupt();
                }
            }

             */

        });
        receiverCom.start();

        /*
        //RICEVO LA STREAM DI NUMERI CHE ESCONO DAL SERVER
        Thread receiverStream = new Thread(() -> {
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
        receiverStream.start();

         */


        //matrixArrayList = myReceiver.getMatrix();

        //System.out.println("debug loop:64, listsize: " + matrixArrayList.size());

        /*
        matrixArrayList = myReceiver.getMatrix();
        System.out.println(matrixArrayList.size());
        matrixAdapter.notifyDataSetChanged();

         */
    }

    /*
    public void setMatrixArrayList() throws InterruptedException {
        matrixArrayList = myReceiver.getMatrix();

        matrixAdapter.notifyDataSetChanged();

        //----------DEBUG--------------//
        System.out.println("DEBUG GameLoop:56");
        for(int i=0; i<matrixArrayList.size(); i++){
            for(int j=0; j<matrixArrayList.get(i).getMyMatrix().length; j++){
                System.out.print(matrixArrayList.get(i).getMyMatrix()[j].getNumero() + " ");
            }
        }
        System.out.println(" ");
        System.out.println("DEBUG GameLoop:63");
        //-----------------------------//
    }


     */
    /*
    public static void setMatrixList(String fromServer){
        int[] tmpArray = parseString(fromServer);

        matrixArrayList.add(new Matrix(tmpArray));

        matrixAdapter.notifyDataSetChanged();
    }


    public static int[] parseString(String array) {
        String[] newArr = array.split(" ");
        int[] tmp = new int[newArr.length];
        for(int i=0; i<newArr.length; i++){
            tmp[i] = Integer.parseInt(newArr[i]);
        }

        return tmp;
    }
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
            //System.out.println("DEBUG 155 " + tmp);
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

    public void setMainNumber(int num){
        extract.setText(Integer.toString(num));
        findNumber(num);
    }

    public void findNumber(int num){

        Matrix.MatrixNumbers[] tmpMatrix;

        for(int i=0; i<matrixArrayList.size(); i++){
            tmpMatrix = matrixArrayList.get(i).getMyMatrix();
            for(int j=0; j<tmpMatrix.length; j++){
                if(tmpMatrix[j].getNumero() == num){
                    //?????????????????????????????????????????
                    //toast??????????????????'

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

        for(int i=0; i<numbersStream.length; i++){

            //faccio uscire un nuovo numero
            setMainNumber(numbersStream[i]);

            //invio il riferimento del nuovo numero al server
            mySender.sendMessage("exitedNumber", Integer.toString(i));

            //attendo 2 secondi prima del prossimo numero
            try {
                Thread.sleep(2500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
}