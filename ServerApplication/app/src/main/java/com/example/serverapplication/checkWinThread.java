package com.example.serverapplication;

import java.util.ArrayList;

public class checkWinThread extends Thread{
    private int numToCheck;
    private Player thisPlayer;
    //private boolean checked = false;
    private boolean win = false;

    public checkWinThread(int num, Player player){
        this.thisPlayer = player;
        this.numToCheck = num;
    }

    @Override
    public void run(){
        ArrayList<int[]> listToCheck;
        int[] arrayToCheck;

        //ciclo su i players
        //for(int i=0; i<players.length; i++){

            //listToCheck = players[i].getMatrixArray();
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
                        System.out.println("CW: number founded");
                        tmplen = arrayToCheck.length;
                        //System.out.println("winthread:38 - " + currentThread().getName() + " " + tmplen);
                        newArrayDim = tmplen-1;
                        //System.out.println("winthread:40 - " + currentThread().getName() + " " + newArrayDim);
                        //System.out.println("winthread:41 - " + currentThread().getName() + " " + numToCheck + " " + arrayToCheck[n]);

                        //se la dimensione del nuovo array è 0 ho fatto bingo
                        if(newArrayDim == 0){
                            setWin(thisPlayer);
                            //winnerIs(players[i].getName());
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
        //}

        setChecked();
    }

    public void setChecked(){
        //this.checked = true;
        //call a metodo in gameloop per dire che questo player è checked
        GameLoopActivity.incrementNumberOfCheckedClient();
    }

    public void setWin(Player winner){
        //System.out.println("winthread:71");
        this.win = true;
        //call a metodo in gameloop per dire che questo player ha vinto (input -> nome player)
        GameLoopActivity.setWin(winner);
    }

    //trovo il numero e reimposto la nuova lista di player
    public void setList(ArrayList<int[]> list){
        thisPlayer.setMatrixArray(list);
    }
}
