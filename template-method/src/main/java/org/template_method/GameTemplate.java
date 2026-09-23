package org.template_method;

public abstract class GameTemplate {
    //template
    public final void startGame(int playerCount){

        initGame(playerCount);
        int playerTurn=0;
        while(!endGame()){
            playTurn(playerTurn);
            playerTurn = ++playerTurn % playerCount;
        }
        displayWinner();
    }

    public abstract void initGame(int playerCount);
    public abstract void playTurn(int player);
    public abstract boolean endGame();
    public abstract void displayWinner();
}