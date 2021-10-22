<p align="center"><img src="https://symbols.getvecta.com/stencil_85/10_java-icon.e6c5a2a97a.svg" width="400"></p>

## A propos de l'App

Le but de ce TP est de réaliser en Java une application distribuée de type client/serveur avec comme support de communication les sockets TCP.

Le serveur gère une liste de personne. Une personne est définie par 2 informations : son age et son nom (mêmes informations que pour le TP1). Le serveur attribue à chaque personne un identificateur unique.
Un client peut effectuer plusieurs requêtes : 

- [Une personne](http://ecariou.perso.univ-pau.fr/cours/sd-l3/Personne.java) | [Documentation](http://ecariou.perso.univ-pau.fr/cours/sd-l3/Personne.html).
- [Gestion de la liste des personnes](http://ecariou.perso.univ-pau.fr/cours/sd-l3/DataManager.java) | [Documentation](http://ecariou.perso.univ-pau.fr/cours/sd-l3/DataManager.html).
    -*[Exception associée](http://ecariou.perso.univ-pau.fr/cours/sd-l3/InvalidIdException.java) | [Documentation](http://ecariou.perso.univ-pau.fr/cours/sd-l3/InvalidIdException.html).

Plusieurs clients doivent pouvoir être connectés simultanément au serveur.

## Aides

Il y a 2 points importants à traiter :

    -Assurer la gestion de plusieurs client simultanément. Pour cela, il faudra avoir un fonctionnement multi-threadé du serveur. Attention alors aux accès concurrents aux données du serveur.
    -Le format des messages envoyés entre le serveur et le client.

## Modification pour invocation dynamique

Modifier votre programme pour que l'appel de la méthode demandée par le client se fasse coté serveur via une invocation dynamique. On utilisera pour cela un message générique pour la requête d'appel et pour la valeur de retour.

Pour vous aidez dans cette implémentation, vous pouvez utiliser les classes suivantes qui implémentent un mécanisme d'invocation dynamique :

- [Mécanisme d'invocation dynamique](http://ecariou.perso.univ-pau.fr/cours/sd-l3/Invocation.java) | [Documentation](http://ecariou.perso.univ-pau.fr/cours/sd-l3/Invocation.html).
- [Exception levée en cas de problème](http://ecariou.perso.univ-pau.fr/cours/sd-l3/UncallableMethodException.java) | [Documentation](http://ecariou.perso.univ-pau.fr/cours/sd-l3/UncallableMethodException.html).

