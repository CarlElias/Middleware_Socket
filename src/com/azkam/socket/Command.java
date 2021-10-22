package com.azkam.socket;

public class Command {
    private String commandType;
    private Object o;
    private Integer personneID;

    public String getCommandType() {
        return commandType;
    }

    public Object getObject() {
        return o;
    }

    public Integer getPersonneID() {
        return personneID;
    }

    public Command() {
        super();
    }

    public Command(Object o){
        super();
        this.o = o;
    }

    public Command(String commandType, Object o) {
        this.commandType = commandType;
        this.o = o;
    }

    public Command(String commandType, Object o, Integer personneID) {
        this.commandType = commandType;
        this.o = o;
        this.personneID = personneID;
    }
}
