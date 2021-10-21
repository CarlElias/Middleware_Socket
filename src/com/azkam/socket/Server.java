package com.azkam.socket;

import com.azkam.dao.DataManager;
import com.azkam.entities.Personne;
import com.azkam.invocation.Invocation;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private Invocation invocation;
    private Gson gson = new Gson();

    public Server(){
        this.invocation = new Invocation( new DataManager());
    }

    public void start() throws Exception {
        System.out.println("======= STARTING SERVER =======");
        serverSocket = new ServerSocket(9926);
        System.out.println("==== WAITING FOR CONNECTION ===");

        while (true){

            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("==== WAITING FOR COMMANDS ====");
            new Thread(() -> {
                System.out.println("===== CONNECTION ACCEPTED =====");
                try {
                    String greeting = in.readLine();
                    out.println(
                            processCommand(greeting)
                    );
                    System.out.println(invocation.getDao());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        }

    }

    public void stop() throws IOException {
        System.out.println("======= CLOSING SERVER =======");
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
        System.out.println("======= SERVER CLOSED ========");
    }

    public Object processCommand(String s) throws Exception {
        Command c = gson.fromJson(s, Command.class);
        String nomMethode = c.getCommandType();
        Object o;
        if(!nomMethode.equals("getPersonne")){
            o = gson.fromJson(
                    gson.toJson(
                            c.getObject()
                    ), Personne.class
            );
        }else{
            o = (Object) c.getObject();
            //o = Integer.parseInt((String) c.getObject());
        }

        Class types[] = { o.getClass() };
        Object param[] = { o };


        try {
            Object res = this.invocation.invoquer(
                    nomMethode,
                    types,
                    param
            );
            return gson.toJson(
                    new Command(c.getCommandType(), res)
            );
        }catch (Exception e){
            return gson.toJson(
                    new Command(
                            c.getCommandType(), null
                    )
            );
        }

    }

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.start();
    }
}