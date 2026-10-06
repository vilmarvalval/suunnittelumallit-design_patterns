package org.chainOfResponsibility;

public abstract class Handler{
    private Handler nextHandler;

    public void process(Message message){
        if (nextHandler != null){
            nextHandler.process(message);
        }
    }

    public void setNextHandler(Handler nextHandler){
        this.nextHandler = nextHandler;
    }
}