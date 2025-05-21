/*
 * This class manages any GameState objects. The intended application is for
 * these to be instantiated into an array list to create a "not quite durable"
 * log, which can be used to validate the current state and store old ones.
 */
package suburbiatycoon2023;

import java.util.ArrayList;
import java.io.Serializable;
import suburbiatycoon2023.*;

public class GameState implements Serializable {
    private int euros, carbonFootprint, carbonBudget, turnsLeft;
    private ArrayList<int[]> consequenceList;
    private ArrayList<Lot> lots;
    private boolean hasLost;
    private static final int BASE_EUROS = 400000;
    private static final int BASE_BUDGET = 1000;
    private static final int TURNS_PER_MONTH = 2;

    //First GameState of a game
    public GameState(int years) {
        this.euros = BASE_EUROS;
        this.carbonFootprint = 0;
        this.carbonBudget = BASE_BUDGET;
        this.turnsLeft = years*12*TURNS_PER_MONTH;
        this.consequenceList = new ArrayList<int[]>();
        this.lots = new ArrayList<Lot>();
        this.hasLost = false;

        lots.add(new Lot());
    }
    //if loading from a save
    public GameState(ArrayList<Lot> lots) {
        this.euros = 0;
        this.carbonFootprint = 0;
        this.carbonBudget = 500;
        this.consequenceList = new ArrayList<int[]>();
        this.lots = lots;
    }

    private int euros() {
        return this.euros;
    }

    private int carbonFootprint() {
        return this.carbonFootprint;
    }

    private int carbonBudget() {
        return this.carbonBudget;
    }

    public int turnsLeft() {
        return this.turnsLeft;
    }

    public int numLots() {
        return this.lots.size();
    }

    public boolean hasLost() {
        return this.hasLost;
    }

    public String toString() {
        String output = "Balance: €" + this.euros();
        output += "\nCarbon budget: " + (this.carbonFootprint()) +
                "/" + this.carbonBudget();
        output += "\nTurns Left: " + turnsLeft();
        return output;
    }

    public String lotsSummary() {
        String output = "";
        for (Lot x : lots) {
            output += x.lotSummary();
            output += "\n\n============================\n\n";
        }
        return output;
    }

    public void addConsequence(int[] in) {
        this.consequenceList.add(in);
    }
    //calculates end-of-turn changes based on the list of consequences
    //then resets the list of consequences
    public boolean endTurn() {
        // update current values
        System.out.println(consequenceList.size());
        if (turnsLeft % TURNS_PER_MONTH == 0) {
            for (Lot x : lots) {
                this.addConsequence(x.getRentAndFootprint());
            }
        }

        for (int i = 0; i < this.consequenceList.size(); i++) {
            int[] current = this.consequenceList.get(i);
            this.euros += current[0];
            this.carbonFootprint += current[1];
            int effectsLots = current[2];
            if (effectsLots == 0) {
                continue;
            } else if (effectsLots == -1) {
                this.lots.add(new Lot(this.lots.size(), current[4], current[5], current[6]));
            } else {
                for (Lot x : this.lots) {
                    x.updateFromConsequence(new int[] { current[3], current[4], current[5], current[6] });
                }
            }
        }
        // check for loss conditions
        if (this.euros < 0){
            System.out.println("You've ran out of money");
            this.hasLost = true;
        }
        if (this.carbonFootprint > this.carbonBudget){
            System.out.println("You've exceeded your carbon budget.");
            this.hasLost = true;
        }
        // overwrite consequence list for next turn
        this.consequenceList = new ArrayList<int[]>();
        this.turnsLeft--;
        return hasLost();
    }
    public int score(){
        return this.euros + (1000-this.carbonFootprint);
    }
}
