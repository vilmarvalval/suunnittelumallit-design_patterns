package org.template_method;

public class Player {
    private final String name;
    private final int id;
    private int reg_score;
    private int spc_score;

    public Player(String name, int id){
        this.name = name;
        this.id =id;
        this.reg_score =0;
        this.spc_score =0;
    }

    public String getName(){
        return this.name;
    }
    public int getId(){
        return this.id;
    }
    public int getScore(){
        return this.reg_score;
    }
    public void setScore(int score){
        this.reg_score =score;
    }
    public int getSpc_score(){
        return this.spc_score;
    }
    public void setSpc_score(int score){
        this.spc_score=score;
    }
}