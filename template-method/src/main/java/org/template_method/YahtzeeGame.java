package org.template_method;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class YahtzeeGame extends GameTemplate{
    private final Scanner scan = new Scanner(System.in);
    String[] die = {"⚀", "⚁", "⚂", "⚃", "⚄", "⚅"};
    ArrayList<Die> playerDice = new ArrayList<>(5);
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Player> scoreBoard = new ArrayList<>();

    int turn=0;
    int dieThrow;
    int throwCount=0;
    String ansW;
    int score;
    int dieValue;

    boolean badInput;
    String[] split;

    @Override
    public void initGame(int playerCount) {
        System.out.println("Starting Yahtzee -game with "+playerCount+" -players");
        turn =1;
        throwCount=0;
        players.clear();
        ansW="";
        score = 0;

        for (int i = 0; i < playerCount; i++) {
            Player p = new Player((i+1)+"", i);
            players.add(p);
        }

        System.out.println( "\n------------" +
                            "\n   TURN "+turn +
                            "\n------------"
        );
    }

    @Override
    public void playTurn(int player) {
        playerDice.clear();
        for (int i = 0; i < 5; i++) {
            playerDice.add(new Die());
        }

        System.out.println(
                "\nPlayer "+players.get(player).getName()+"'s turn." +
                "\nThrowing dice..."
        );

        //TODO create input command to skip turn(return -1)/exit at any point?
        throwDice();

        System.out.println("\nCounting score...");
        countScore();

        System.out.println( "Player "+players.get(player).getName()+"'s score: +"+score+
                            "\n(Enter anything to continue)");
        waitForInput();

        Player p = players.get(player);
        p.setScore(p.getScore() + score);

        playerDice.clear();
        throwCount=0;

        if (player+1==players.size()){
            printScoreBoard();

            turn++;
            if(turn<14) {
                System.out.println( "\n------------" +
                                    "\n   TURN " + turn +
                                    "\n------------"
                );
            }
        }
    }

    public void throwDice(){
        for (int i = 0; i < 5; i++) {
            dieThrow = (int) (1+Math.round(Math.random()*5));
            playerDice.get(i).setValue(dieThrow);
        }
        printDice();

        System.out.println("\n"+(3-throwCount)+" rethrows left. Rethrow? (Y/n)");
        ansW = scan.nextLine().toLowerCase();
        while (!ansW.equalsIgnoreCase("n")&&throwCount<3){
            do {
                System.out.println("""
                        Enter dice you want to lock separated by a space, order doesn't matter(e.g. 1 5 3)
                        Pressing enter leaves dice unlocked.
                        Afterwards, all unlocked dice will be thrown again.""");

                ansW = scan.nextLine();
                split = ansW.split("\\s+");

                if (Arrays.toString(split).equals("[]")){
                    badInput=false;
                } else {
                    for (String s : split) {
                        if (!s.isEmpty())
                            try {
                                playerDice.get(
                                        Integer.parseInt(s)-1
                                ).lock();
                                badInput=false;
                            } catch (Throwable e) {
                                System.out.println("BAD INPUT, TRY AGAIN.\n");
                                printDice();
                                badInput = true;
                            }
                    }
                }

            } while (badInput);

            System.out.println("Rethrowing...");
            for (Die value : playerDice) {
                if (!value.isLocked()) {
                    dieThrow = (int) (1 + Math.round(Math.random() * 5));
                    value.setValue(dieThrow);
                }
            }

            throwCount++;
            for (int i = 0; i < playerDice.toArray().length; i++) {
                dieValue= playerDice.get(i).getValue();
                if (playerDice.get(i).isLocked()) {
                    System.out.println((i + 1) + ". die: " + die[dieValue - 1] + " (" + dieValue + ")");
                } else {
                    System.out.println((i + 1) + ". die: " + die[dieValue - 1] + " (" + dieValue + ") *");
                }
            }

            ansW="";
            if((3-throwCount)>=1) {
                System.out.println("\n" + (3 - throwCount) + " rethrows left. Rethrow? (Y/n)");
                ansW = scan.nextLine();
            }
        }
    }

    public void waitForInput(){
        //wait for String or simply enter
        try
            {
                new BufferedReader(new InputStreamReader(System.in)).readLine();
            }
        catch (IOException e)
            {
                // TODO do exception handling
            }
    }

    public void countScore(){
        //TODO an actual Yahtzee score counter.
        score=0;

        for (Die playerThrow : playerDice) {
            score += playerThrow.getValue();
        }
    }

    public void printDice(){
        for (int i = 0; i < playerDice.size(); i++) {
            dieValue= playerDice.get(i).getValue();
            System.out.println((i+1)+". die: "+die[dieValue-1]+" ("+dieValue+")");
        }
    }

    public void printScoreBoard(){
        //TODO Scoreboard dictates turn order. To change or not to change?
        bubbleSort(players);
        System.out.println("SCOREBOARD:");
        for (Player p : players) {
            System.out.println("Player " + p.getName() + ": " + p.getScore());
        }
        System.out.println( "(Enter anything to continue)");
        waitForInput();
    }

    @Override
    public boolean endGame() {
        return turn > 13;
    }

    @Override
    public void displayWinner() {
        printScoreBoard();
    }

    public void bubbleSort(ArrayList<Player> array) {
        boolean swapped = true;

        while (swapped) {
            swapped = false;

            for (int i = 0; i < array.size() - 1; i++) {
                if (array.get(i).getScore() < array.get(i + 1).getScore()) {

                    Player tmp = array.get(i);
                    array.set(i, array.get(i + 1));
                    array.set(i + 1, tmp);

                    swapped = true;
                }
            }
        }
    }

}