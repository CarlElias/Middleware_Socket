package com.azkam;

import com.azkam.entities.Personne;
import com.azkam.socket.Client;
import com.azkam.socket.Server;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("======================================================================");
        System.out.println("===================    BIENVENUE SUR NOTRE APP   =====================");
        Scanner sc = new Scanner(System.in);
        String choix = "";
        String choixList[] = {"1", "2"};


        while ( true ){
            System.out.println("1 - Serveur");
            System.out.println("2 - Client");
            System.out.print("Veuillez faire un choix : ");
            choix = sc.nextLine();
            if( Arrays.asList(choixList).contains(choix) ){
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }else{
                System.out.println("Choix incorrect !");
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
            }
        }


        if(choix.equals("1")){
            System.out.println("DEMARRAGE DU SERVEUR");
            Server s = new Server();
            s.start();
        }else if(choix.equals("2")) {
            System.out.println("DEMARRAGE DU CLIENT");
            Client c = new Client();
            c.startClient();
        }

    }
}
