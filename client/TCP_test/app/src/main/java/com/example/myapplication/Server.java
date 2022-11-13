package com.example.myapplication;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Server {
    private InetAddress inetAddress;
    private Socket socket;
    private BufferedReader fromServer;
    private PrintWriter toServer;

    public Server() {

    }

    //GETTER
    public InetAddress getInetAddress() { return inetAddress; }

    public Socket getSocket() { return socket; }

    public BufferedReader getFromServer() { return fromServer; }

    public PrintWriter getToServer() { return toServer; }


    //SETTER
    public void setInetAddress(InetAddress inetAddress) { this.inetAddress = inetAddress; }

    public void setSocket(Socket socket) { this.socket = socket; }

    public void setFromServer(BufferedReader fromServer) { this.fromServer = fromServer; }

    public void setToServer(PrintWriter toServer) { this.toServer = toServer; }
}
