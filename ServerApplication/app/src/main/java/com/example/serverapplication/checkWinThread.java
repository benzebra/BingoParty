package com.example.serverapplication;

import java.util.ArrayList;

/**
 * checkWinThread
 *
 * Thread di controllo per verificare se nelle cartelle di un certo player (dato in input) è presente
 * oppure no il numero estratto (anch'esso dato in input).
 */
public class checkWinThread extends Thread{
    private final int numToCheck;
    private final Player thisPlayer;

    public checkWinThread(int num, Player player){
        this.thisPlayer = player;
        this.numToCheck = num;
    }

    @Override
    public void run(){
        ArrayList<int[]> listToCheck;
        int[] arrayToCheck;

        listToCheck = thisPlayer.getMatrixArray();

        //ciclo sulle liste di ogni player
        for(int j=0; j<listToCheck.size(); j++){

            arrayToCheck = listToCheck.get(j);

            //ciclo sugli array della lista
            for(int n=0; n<arrayToCheck.length; n++){

                int tmplen;
                int newArrayDim;

                //se trovo il numero...
                if( numToCheck == arrayToCheck[n] ){
                    tmplen = arrayToCheck.length;
                    newArrayDim = tmplen-1;

                    //se la dimensione del nuovo array è 0 ho fatto bingo
                    if(newArrayDim == 0){
                        setWin(thisPlayer);
                    }else{
                        //altrimenti creo il nuovo array e lo sostituisco a quello di prima
                        int[] tmpArr = new int[newArrayDim];
                        for(int m=0; m<tmpArr.length; m++){
                            if(m<n){
                                tmpArr[m] = arrayToCheck[m];
                            }else{
                                tmpArr[m] = arrayToCheck[m+1];
                            }
                        }
                        listToCheck.set(j,tmpArr);
                        setList(listToCheck);
                    }

                    break;
                }
            }
        }

        setChecked();
    }

    public void setChecked(){
        //call a metodo in gameloop per dire che questo player è checked
        GameLoopActivity.incrementNumberOfCheckedClient();
    }

    public void setWin(Player winner){
        //call a metodo in gameloop per dire che questo player ha vinto (input -> nome player)
        GameLoopActivity.setWin(winner);
    }

    //trovo il numero e reimposto la nuova lista di player
    public void setList(ArrayList<int[]> list){
        thisPlayer.setMatrixArray(list);
    }
}
