package com.azkam.socket;

public class Command {
    private String commandType;
    private Object o;

    public String getCommandType() {
        return commandType;
    }

    public Object getObject() {
        return o;
    }

    public Command(Object o){
        super();
        this.o = o;
    }

    public Command(String commandType, Object o) {
        this.commandType = commandType;
        this.o = o;
    }
}
