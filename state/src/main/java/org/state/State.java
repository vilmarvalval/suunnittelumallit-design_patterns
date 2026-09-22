package org.state;

public abstract class State{
    private final Character character;

    public State(Character character){
        this.character=character;
    }

    public Character getCharacter(){
        return this.character;
    }

    abstract void action();
}