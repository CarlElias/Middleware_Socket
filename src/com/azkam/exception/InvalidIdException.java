package com.azkam.exception;

/**
 * Exception qui est levée quand on demande à récupérer une personne à
 * partir d'un identifiant incorrect (c'est-à-dire quand aucune
 * personne ne possède cet identifiant).
 */
public class InvalidIdException extends Exception
{
    /**
     * Crèe une nouvelle exception
     * @param message le message décrivant l'erreur
     */
    public InvalidIdException(String message)
    {
        super(message);
    }
}