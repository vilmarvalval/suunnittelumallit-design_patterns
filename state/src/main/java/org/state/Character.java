package org.state;

import java.util.Arrays;
import java.util.Scanner;

public class Character {
    private static final Scanner scan = new Scanner(System.in);
    private final State state;
    private final String[] rank = {"NOVICE", "INTERMEDIATE", "EXPERT", "MASTER"};
    //how many levels it takes to advance a rank
    private final int rankDiff = 3;
    private final int LVLMulti =10;
    private int HPCap=5;

    private int HP=5;
    private int XP=0;
    private int LVL=0;

    public Character() {
        state = new ActionSelectionState(this);
    }

    public void play() {
        while (true){
            if(state==null){
                System.out.println("Error, state null.");
                return;
            }
            if(HP==0){
                System.out.println("You died! Game over!");
                return;
            }
            if(getRank().equals("MASTER")){
                System.out.println("You have retired ranking as a MASTER of the arts!"+
                                    "\nYou won! Game over!");
                return;
            }
            state.action();
        }
    }

    public int getLVL(){
        return LVL;
    }
    public int getXP(){
        return XP;
    }
    public int getHP(){
        return HP;
    }
    public int getHPCap(){
        return HPCap;
    }
    public String getRank(){
        return rank[(int) Math.floor((double) getLVL() /rankDiff)];
    }
    public int getRankInt(){
        return (int) Math.floor((double) getLVL() /rankDiff);
    }
    public int getRankIndex(String RankName){
        return Arrays.stream(rank).toList().indexOf(RankName);
    }

    public void clearStats(){
        LVL=0;
        XP=0;
        HP=5;
        HPCap=HP;
    }

    public void addXP(double amount){
        XP = (int) (XP + amount);
        if (getXP()>=(getLVL()+1)* LVLMulti){
            XP = (XP - ((getLVL() + 1) * LVLMulti));
            LVL+=1;
            HPCap = HPCap + getLVL();
            System.out.println(
                            "\n" +
                            "------------------" +
                            "     Level up!    " +
                            "------------------"
            );
        }
    }
    public void changeHP(int amount){
        HP= Math.clamp(HP + amount, 0, getHPCap());
    }

    public int readUserChoise(String[] options){
        System.out.println("\nStats:");
        System.out.println("Rank: "+getRank());
        System.out.println("HP: "+ getHP() +"/"+ getHPCap() +
                "\nLVL: "+ getLVL() +"/"+(rankDiff*(rank.length-1)-1)+
                "\nXP: "+getXP()+"/"+(getLVL()+1)* LVLMulti);
        System.out.println("\nSelect an option:");
        for (int i = 0; i < options.length; i++) {
            System.out.println((i+1)+". "+options[i]);
        }
        //read input
        return scan.nextInt();
    }
}