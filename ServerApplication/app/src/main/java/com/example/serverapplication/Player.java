package com.example.serverapplication;

import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Player {
    private PrintWriter sender;
    private BufferedReader receiver;
    private String address;
    private Receiver threadReceiver;
    private Socket clientSocket;
    private String name;
    private int matrixNumber;
    private ArrayList<int[]> matrixArray;

    public Player(){ }

    // GETTER
    public PrintWriter getSender() {
        return sender;
    }

    public BufferedReader getReceiver() {
        return receiver;
    }

    public String getAddress() { return address; }

    public Receiver getThreadReceiver() {
        return threadReceiver;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public String getName() { return name; }

    public int getMatrixNumber() { return matrixNumber; }

    public ArrayList<int[]> getMatrixArray() { return matrixArray; }


    // SETTER
    public void setSender(PrintWriter sender) {
        this.sender = sender;
    }

    public void setReceiver(BufferedReader receiver) {
        this.receiver = receiver;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setThreadReceiver(Receiver threadReceiver) {
        this.threadReceiver = threadReceiver;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void setName(String name) { this.name = name; }

    public void setMatrixNumber(int number) {this.matrixNumber = number; }

    public void setMatrixArray(ArrayList<int[]> list) {this.matrixArray = list; }

}
