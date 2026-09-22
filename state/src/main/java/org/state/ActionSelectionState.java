package org.state;

public class ActionSelectionState extends State{
    private final double baseXP=5;
    private final double trainMlt=2;
    private final double fightMlt=5;

    public ActionSelectionState(Character character){
        super(character);
        this.getCharacter().clearStats();
    }

    public void action() {
        String[] options={"Train","Meditate","Fight"};
        String rank;
        switch (this.getCharacter().readUserChoise(options)){
            case 1:
                System.out.println("\nTraining...");
                this.getCharacter().addXP((int)Math.ceil(Math.random()*baseXP*(trainMlt+getCharacter().getRankInt())));
                break;
            case 2:
                rank = "INTERMEDIATE";
                if (getCharacter().getRankInt()>= getCharacter().getRankIndex(rank)) {
                    System.out.println("\nMeditating...");
                    this.getCharacter().addXP((int) (Math.random()* (1+getCharacter().getRankInt())));
                    this.getCharacter().changeHP((int) Math.ceil(Math.random() * (baseXP+ getCharacter().getRankInt()-1)));
                } else {
                    System.out.println("Your rank must be at least '"+rank+"' to meditate!");
                }
                break;
            case 3:
                 rank = "EXPERT";
                if (getCharacter().getRankInt()>=getCharacter().getRankIndex(rank)) {
                    System.out.println("\nFighting...");
                    this.getCharacter().changeHP((int) -Math.ceil(Math.random() * 5));
                    this.getCharacter().addXP((int) Math.ceil(Math.random() * baseXP * (fightMlt + getCharacter().getRankInt())));
                } else {
                    System.out.println("Your rank must be at least '"+rank+"' to fight!");
                }
                break;
        }
    }
}