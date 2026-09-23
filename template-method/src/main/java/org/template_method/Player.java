package org.template_method;

public class Player {
    private final String name;
    private final int id;
    private int score;

    public Player(String name, int id){
        this.name = name;
        this.id =id;
        this.score=0;
    }

    public String getName(){
        return this.name;
    }
    public int getId(){
        return this.id;
    }
    public int getScore(){
        return this.score;
    }
    public void setScore(int score){
        this.score=score;
    }
}