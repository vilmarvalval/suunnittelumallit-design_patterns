package org.template_method;

public class Main{
    static void main(String[] args){
        System.out.println("Yahtzee");
        GameTemplate yahtzee = new YahtzeeGame();
        yahtzee.startGame(2);
    }
}