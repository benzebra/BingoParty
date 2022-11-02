package com.example.myapplication;

public class Matrix {

    public static class MatrixNumbers{
        private boolean flag;       //true -> uscito, false -> non uscito
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


    private MatrixNumbers[] myMatrix;

    //Costruttore
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
