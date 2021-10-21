package com.azkam.socket;

import com.azkam.entities.Personne;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Gson gson = new Gson();

    final Scanner sc = new Scanner(System.in);

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public Command sendMessage(String msg) throws IOException {
        out.println(msg);
        String res = in.readLine();
        return gson.fromJson(res, Command.class);
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public Personne createPersonFromCLI(){
        //On récupére les valeurs pour la création d'un objet personne
        System.out.print("\t Veuillez entrer le nom de la personne : ");
        String nom = sc.nextLine();
        System.out.print("\t Veuillez entrer l'age de la personne : ");
        String age = sc.nextLine();
        //Création de la nouvelle personne
        return new Personne(
                Integer.parseInt(age),
                nom
        );
    }

    public String forgeAndLaunchCommand(Object p,String commandInString) throws IOException {
        //Création de la commande
        Command c = new Command(
                commandInString,
                p
        );

        //Envoi de la commande en string au format JSON
        return this.analyzeResponse(
                this.sendMessage(
                        gson.toJson(c)
                )
        );
    }

    public void addPersonne() throws IOException {
        //CREATION D'UNE NOUVELLE PERSONNE
        System.out.println("\t CREATION D'UNE NOUVELLE PERSONNE ");

        //Création de la nouvelle personne
        Personne p = this.createPersonFromCLI();

        System.out.println(
                this.forgeAndLaunchCommand(
                        p,
                        "addPersonne"
                )
        );
    }

    public void askForIdFromPerson() throws IOException {
        //RECHERCHE DE L'ID D'UNE PERSONNE A PARTIR DE SES INFORMATIONS
        System.out.println("\t OBTENEZ L'ID D'UNE PERSONNE A PARTIR DE SES INFORMATIONS ");

        //Création de la nouvelle personne
        Personne p = this.createPersonFromCLI();

        System.out.println(
                this.forgeAndLaunchCommand(
                        p,
                        "getId"
                )
        );
    }

    public void getPersonFromID() throws IOException {
        //RECHERCHE D'UNE PERSONNE A PARTIR DE SON ID
        System.out.println("\t RECHERCHE D'UNE PERSONNE A PARTIR DE SON ID ");

        //Création de la nouvelle personne
        System.out.print("\t Veuillez entrer l'ID de la personne à rechercher : ");
        String ID = sc.nextLine();

        System.out.println(
                this.forgeAndLaunchCommand(
                        ID,
                        "getPersonne"
                )
        );
    }

    public String analyzeResponse(Command c){
        String output = "@@@@";
        Object s = c.getObject();

        System.out.println(s);
        if(s instanceof Double){
            s = (int) Math.floor( (double) s) ;
        }else{
            s = null;
        }

        switch (c.getCommandType()){
            case "getPersonne":
                output = (s == null) ? "La personne recherchée n'existe pas" : "Infos sur la personne recherchée - " + s.toString();
                break;

            case "getId":
                output =  ( s.equals( new Integer(-1) ) ) || (s == null)? "La personne demandée n'existe pas" : "Personne trouvée - ID : " + s.toString();
                break;

            case "addPersonne":
                output = "L'identifiant de la nouvelle personne ajoutée est " + s;
                break;
        }
        return output;
    }

    public void startClient() throws IOException {

        Client c = new Client();
        System.out.print("Adresse IP du serveur : ");

        while (true){
            c.startConnection((new Scanner(System.in)).nextLine(), 9926);
            System.out.println("Connexion au serveur");



            new Thread(() -> {
                String msg;
                System.out.print(
                        "=======================================================\n" +
                                "\t 1 - Créer une nouvelle personne \n" +
                                "\t 2 - Chercher une personne par son ID \n" +
                                "\t 3 - Chercher une personne par ses informations \n" +
                                "=======================================================\n\n" +
                                "Choix : "
                );
                msg = sc.nextLine();
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                try {
                    switch (msg) {
                        case "1":
                            c.addPersonne();
                            break;
                        case "2":
                            c.getPersonFromID();
                            break;
                        case "3":
                            c.askForIdFromPerson();
                            break;
                        default:
                            System.out.println("Veuillez entrer une valeur correcte !");
                            break;
                    }
                } catch (IOException e) {
                    try {
                        stopConnection();
                    } catch (IOException ioException) {
                    }
                    System.out.println("Serveur déconecté");
                }
            }).start();
        }


    }
}