package com.azkam.exception;

/**
 * Exception levée quand, pour une raison quelconque, on a pas pu
 * appeler une méthode ou que son appel s'est mal passé
 */
public class UncallableMethodException extends Exception {

    public UncallableMethodException(String msg) {
        super(msg);
    }

}