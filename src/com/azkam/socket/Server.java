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
            new ClientHandler(serverSocket.accept(), this).start();
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
        Object o = null;
        if(!nomMethode.equals("getPersonne")){
            o = gson.fromJson(
                    gson.toJson(
                            c.getObject()
                    ), Personne.class
            );
        }else{
            o = c.getPersonneID();

            System.out.println(o + " " + o.getClass());
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

    public static class ClientHandler extends Thread{
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private Server server;

        public ClientHandler(Socket socket, Server s) {
            this.clientSocket = socket;
            this.server = s;
        }

        public void run() {
            try {
                System.out.println("==== NEW CONNECTION FROM " + clientSocket.getInetAddress() + " ===");
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    out.println(server.processCommand(inputLine));
                    out.flush();
                }

                in.close();
                out.close();
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}