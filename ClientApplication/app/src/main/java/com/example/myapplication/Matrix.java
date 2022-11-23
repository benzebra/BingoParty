package com.example.myapplication;

/**
 * Matrix
 *
 * Oggetto creato ad hoc per poter gestire l eliste di numeri (o cartelle), ricevute dal server e
 * con diverse possibiltà di renderizzazione (se il numero è estratto oppure no)
 */
public class Matrix {

    public static class MatrixNumbers{
        private boolean flag;       //true -> exited, false -> not exited
        private int numero;
        private String numToString;

        public MatrixNumbers(int number, boolean flag){
            this.numero = number;
            this.flag = flag;
            this.numToString = Integer.toString(number);
        }

        //GETTER
        public boolean getFlag() {
            return flag;
        }

        public int getNumero() {
            return numero;
        }

        public String getNumToString() {
            return numToString;
        }

        //SETTER
        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public void setNumero(int numero) {
            this.numero = numero;
        }

        public void setNumToString(String numToString) {
            this.numToString = numToString;
        }
    }


    private final MatrixNumbers[] myMatrix;

    public Matrix(MatrixNumbers[] myMatrix) {
        this.myMatrix = myMatrix;
    }

    //GETTER
    public MatrixNumbers[] getMyMatrix() {
        return myMatrix;
    }

    @Override
    public String toString(){
        String utilityString = "";

        for(int i=0; i<9; i++){
            utilityString = utilityString + myMatrix[i].getNumToString() + " ";
        }

        return utilityString;
    }
}
