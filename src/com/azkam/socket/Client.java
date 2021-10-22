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
    private Gson gson = new Gson();

    final Scanner sc = new Scanner(System.in);

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

    public Command forgeCommand(Object p,String commandInString) throws IOException {
        //Création de la commande
        if(commandInString.equals("getPersonne")){
            return new Command(
                commandInString,
                null,
                (Integer) p
            );
        }

        return new Command(
                commandInString,
                p
        );
    }

    public Command addPersonne() throws IOException {
        //CREATION D'UNE NOUVELLE PERSONNE
        System.out.println("\t CREATION D'UNE NOUVELLE PERSONNE ");

        //Création de la nouvelle personne
        Personne p = this.createPersonFromCLI();


        return this.forgeCommand(
                p,
                "addPersonne"
        );
    }

    public Command askForIdFromPerson() throws IOException {
        //RECHERCHE DE L'ID D'UNE PERSONNE A PARTIR DE SES INFORMATIONS
        System.out.println("\t OBTENEZ L'ID D'UNE PERSONNE A PARTIR DE SES INFORMATIONS ");

        //Création de la nouvelle personne
        Personne p = this.createPersonFromCLI();

        return this.forgeCommand(
                p,
                "getId"
        );
    }

    public Command getPersonFromID() throws IOException {
        //RECHERCHE D'UNE PERSONNE A PARTIR DE SON ID
        System.out.println("\t RECHERCHE D'UNE PERSONNE A PARTIR DE SON ID ");

        //Création de la nouvelle personne
        System.out.print("\t Veuillez entrer l'ID de la personne à rechercher : ");
        Integer ID = sc.nextInt();


        return this.forgeCommand(
                ID,
                "getPersonne"
        );
    }

    public String analyzeResponse(Command c){
        String output = "";
        Object s = c.getObject();

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

        final Scanner sc_final = new Scanner(System.in);
        System.out.print("Veuiller entrer l'IP du serveur : ");
        final Socket clientSocket = new Socket( sc_final.nextLine(), 9926);
        Client cli = this;
        final PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        Thread envoyer = new Thread(new Runnable() {
            String msg;
            Command command;
            String reception;
            @Override
            public void run() {

                try {
                    while (true){
                        System.out.print(
                                "=======================================================\n" +
                                        "\t 1 - Créer une nouvelle personne \n" +
                                        "\t 2 - Chercher une personne par son ID \n" +
                                        "\t 3 - Chercher une personne par ses informations \n" +
                                        "=======================================================\n\n" +
                                        "Choix : "
                        );
                        msg = sc_final.nextLine();
                        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                        if(msg.equals("1")){
                                command = cli.addPersonne();
                        }else if(msg.equals("2")){
                            command = cli.getPersonFromID();
                        }else if(msg.equals("3")){
                            command = cli.askForIdFromPerson();
                        }
                        out.println(gson.toJson(command));
                        out.flush();

                        reception = in.readLine();
                        if(reception != null){
                            System.out.println( cli.analyzeResponse(
                                    gson.fromJson(
                                            reception,
                                            Command.class
                                    )
                            ) );
                        }
                        reception = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        envoyer.start();


    }

    public static void main(String[] args) throws IOException {
        Client c = new Client();
        c.startClient();
    }
}