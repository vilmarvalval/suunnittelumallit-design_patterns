package org.template_method;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class YahtzeeGame extends GameTemplate{
    private final Scanner scan = new Scanner(System.in);
    String[] dieFaces = {"⚀", "⚁", "⚂", "⚃", "⚄", "⚅"};
    ArrayList<Die> playerDice = new ArrayList<>(5);
    int[] counter = {0, 0, 0, 0, 0, 0};

    int[] chanceL = {1, 1, 1, 1, 1, 0};
    int[] chanceR = {0, 1, 1, 1, 1, 1};

    Map<String, Integer> pickScore = new HashMap<>();

    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Player> scoreBoard = new ArrayList<>();

    int turn=0;
    int dieThrow;
    int throwCount=0;
    String ansW;
    int score;
    int bonus;
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
        bonus = 0;

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
        bonus= players.get(player).getSpc_score();

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

        System.out.println("\nChecking combinations...");
        countScore();

        //TODO make player pick which score he wants?
        System.out.println(pickScore);
        score=0;
        pickScore.forEach((k,v)->{
            score+=v;
        });
        pickScore.clear();

        System.out.println( "Player "+players.get(player).getName()+"'s score: +"+(score+bonus)+
                            "\n(Enter anything to continue)");
        waitForInput();

        Player p = players.get(player);
        p.setScore(p.getScore() + score);
        p.setSpc_score(p.getSpc_score() + bonus);

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
                    System.out.println((i + 1) + ". die: " + dieFaces[dieValue - 1] + " (" + dieValue + ")");
                } else {
                    System.out.println((i + 1) + ". die: " + dieFaces[dieValue - 1] + " (" + dieValue + ") *");
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
        score=0;
        //count amount of each die value
        Arrays.fill(counter, 0);

        for (Die playerThrow : playerDice) {
            counter[playerThrow.getValue()-1]++;
            //score += playerThrow.getValue();
        }

        //System.out.println(Arrays.toString(counter));
        //check for chance
        if (Arrays.equals(counter, chanceL) || Arrays.equals(counter, chanceR)) {
            System.out.println("Chance!");
            //Same as in 3 or 4 of a kind; adds up all dice values.
            score = 0;
            for (Die die : playerDice) {
                score += die.getValue();
            }
            pickScore.put("Chance", score);
        }
        score=0;

        //check for large straight
        for (int l = 0; l <= 1; l++) {
            if (counter[l] > 0 &&
                    counter[l + 1] > 0 &&
                    counter[l + 2] > 0 &&
                    counter[l + 3] > 0) {

                //always gives 40p
                System.out.println("Large straight!!!");
                System.out.println((l+1)+" "+ (l+2) +" "+ (l+3) +" "+(l+4));
                pickScore.put("Large straight: "+(l+1)+" "+ (l+2) +" "+ (l+3) +" "+(l+4), 40);
            }
        }

        //check for small straight
        for (int s = 0; s <= 2; s++) {
            if (counter[s] > 0 &&
                    counter[s + 1] > 0 &&
                    counter[s + 2] > 0) {

                //always gives 30p
                System.out.println("Small straight!!");
                System.out.println((s+1)+" "+ (s+2) +" "+ (s+3));
                pickScore.put("Small straight: "+ (s+1)+" "+ (s+2) +" "+ (s+3), 30);
            }
        }
        for (int i = 0; i < counter.length; i++) {
            if (counter[i]>=2){
                System.out.println((i+1)+"'s!");
                //Ones, Twos, Threes, Fours, Fives and Sixes. Adds 2 * die's value to points.
                pickScore.put((i+1)+"'s",(i+1)*counter[i]);
            }
            //check for others
            switch (counter[i]){
                case 0, 1, 2:
                    break;
                case 3: //3 dice of the same number.
                    //First check for full house
                    for (Integer integer : counter) {
                        if (integer == 2) {
                            System.out.println("Full House!");
                            //full house always gives 25
                            pickScore.put("Full House!", 25);
                        }
                    }
                    System.out.println("Three of a kind!");
                    //Adds up all the dice.
                    score=0;
                    for (Die die : playerDice) {
                        score += die.getValue();
                    }
                    pickScore.put("Three of a kind "+i,score);
                    break;
                case 4: //4 dice with the same number.
                    System.out.println("Four of a kind!");
                    //Adds up all the dice.
                    score=0;
                    for (Die die : playerDice) {
                        score += die.getValue();
                    }
                    pickScore.put("Four of a kind "+i,score);
                    break;
                case 5: //all 5 dice have the same number.
                    System.out.println("YAHTZEE!!!!");
                    //Five of a kind grants 50 points no matter what the values of the dice are,
                    // 100 if 50 has already been given previously.
                    pickScore.put("YAHTZEE!!!!", 0);
                    if (bonus>=50){
                        System.out.println("Yahtzee bonus!");
                        bonus=100;
                    }
                    break;
            }
        }

        Arrays.fill(counter, 0);
        //System.out.println(pickScore);
    }

    public void printDice(){
        for (int i = 0; i < playerDice.size(); i++) {
            dieValue= playerDice.get(i).getValue();
            System.out.println((i+1)+". die: "+ dieFaces[dieValue-1]+" ("+dieValue+")");
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