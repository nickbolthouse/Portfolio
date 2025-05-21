package suburbiatycoon2023;

import java.io.InputStream;
import java.util.ArrayList;
import suburbiatycoon2023.GameState;
import suburbiatycoon2023.InputParser;

public class Game {
    private InputParser inStream;
    private GameState state;
    private ArrayList<Event> events;
    // a bit of a lazy solution, but this works since there is only one save file.
    private static final String[] commonCommands = {
            "exit", "status", "help"
    };
    private boolean newTurn;
    //for a new game
    public Game(InputParser inStream, ArrayList<Event> events, int turnsLeft) {
        this.inStream = inStream;
        this.state = new GameState(turnsLeft);
        this.events = events;
        this.newTurn = true;
    }
    //for loading from a save
    public Game(InputParser inStream, GameState state,
            ArrayList<Event> events) {
        this.inStream = inStream;
        this.state = state;
        this.events = events;
        this.newTurn = true;
    }

    public GameState getState() {
        return this.state;
    }
    //runs the main gameplay loop. returns boolean hasLost (the game)
    public boolean playGame() {
        int index = 0;
        this.newTurn = true;
        while (this.state.turnsLeft() > 0 && !this.state.hasLost()) {
            if (this.newTurn){
                index = (int) (Math.random() * (this.events.size()) - 1);
                this.newTurn = false;
            }
            if (!promptEvent(index))
                break;
        }
        return this.state.hasLost();
    }
    //prompts events. returns false if the player wants to exit
    private boolean promptEvent(int index) {
        String response = inStream.promptUser(
                this.events.get(index).prompt(),
                catResponses(this.events.get(index).acceptedResponses()));
        if (isCommonCommand(response)) {
            return runCommonCommand(response);
        } else {
            this.state.addConsequence(this.events.get(index).chooseOutcome(response));
            this.state.endTurn();
            this.newTurn = true;
            return true;
        }
    }
    // shoves all the acceptable responses into one array.
    private String[] catResponses(String[] in) {
        String[] acceptedResponses = new String[in.length + commonCommands.length];
        for (int i = 0; i < commonCommands.length; i++) {
            acceptedResponses[i] = commonCommands[i];
        }
        for (int i = 0; i < in.length; i++) {
            acceptedResponses[i + commonCommands.length] = in[i];
        }

        return acceptedResponses;
    }

    private boolean isCommonCommand(String in) {
        for (int i = 0; i < commonCommands.length; i++) {
            if (in.equals(commonCommands[i])) {
                return true;
            }
        }
        return false;
    }
    //determines the action of common commands
    private boolean runCommonCommand(String in) {
        if (in.equals(commonCommands[0])) {
            return false;
        } else if (in.equals(commonCommands[1])) {
            System.out.println(this.state.toString());
            System.out.println(this.state.lotsSummary());
            return true;
        } else if (in.equals((commonCommands[2]))) {
            System.out.println("exit: choose to save exit the game\n" +
                    "status: see everything, including all your lot information\n" +
                    "help: show this again");
            return true;
        } else {
            System.out.println("If you made it here, how?");
            return true;
        }
    }

}
