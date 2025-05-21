/*
 * Author: Nicholas Bolthouse
 * 
 * Whoever is reading this, I want to make note of the kindness,
 * flexibility, and understanding Ive been shown by the CS department, 
 * particularly Aidan Mooney and Stephen Brown. This program might 
 * not be where I'd like it to be, but without their support, this 
 * never would have been started, much less finished. 
 * Thank you all for your support in a difficult time.
 */

package suburbiatycoon2023;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class Driver {
    private static InputParser inParser;
    private static Game game;

    private static final int CONSEQUENCE_SIZE = 7;

    public static void main(String[] args)
            throws FileNotFoundException, ClassNotFoundException, IOException {
        inParser = new InputParser(new Scanner(System.in));
        String response = inParser.promptUser(
                "Welcome to Suburbia Simulator 2023.\n\n" +
                        "New game\t\t\tLoad Game\t\t\tExit\n(type response)",
                new String[] { "new game", "load game", "exit" });

        if (response.equals("exit")) {
            return;
        } else if (response.equals("new game")) {
            newGame();
        } else if (response.equals("load game")) {
            loadGame();
        }
        exitProgram(game.playGame());

        return;
    }

    //creates a new game based on user input of how many years they want to play for
    private static void newGame()
            throws FileNotFoundException {
        String firstPrompt = "In this game, you will take on the role of an independent" +
        " land developer, trying to make money while upgrading your properties in an" +
        " eco friendly way. Twice a month, you will be presented with choices to make," +
        " but be wary - the cost of doing business is not always clear upfront, and" + 
        " there is a cost to every decision you make. Remember to keep your pockets deep" +
        " and your carbon footprint low, or you will lose.\n\n" + 
        "How many years do you want to play for (4+ is recommended, 89478485 is the maximum)";
        int years = inParser.promptInt(firstPrompt, true);
        game = new Game(inParser, readEventsFile(), years);

    }
    //loads a saved game from save.bin
    private static void loadGame()
            throws FileNotFoundException, ClassNotFoundException, IOException {
        File f = new File("save.bin");
        if (f.exists()) {
            game = new Game(inParser, readSaveFile(), readEventsFile());
        } else {
            System.out.println("Unable to find save file, starting from new");
            newGame();
        }
    }
    //gives the player an opportunity to save a game still in progress
    private static void exitProgram(boolean hasLost)
            throws IOException {
        if (hasLost) {
            System.out.println("Game over");
            return;
        }
        else if (game.getState().turnsLeft() < 1 && !hasLost){
            System.out.println("Congratulations! You win!\nScore: " + 
            game.getState().score());
            return;
        }
        String response = inParser.promptUser(
                "Would you like to save your game? " +
                        "It will overwrite any previous save file.",
                new String[] { "yes", "no" });
        if (response.equals("yes")) {
            saveGame();
        } else {
            return;
        }
    }
    //serializes the game state and writes it to  save.bin
    private static void saveGame()
            throws IOException {
        System.out.println("Game saved successfully");
        FileOutputStream fos = new FileOutputStream("save.bin");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(game.getState());
        oos.flush();
        oos.close();
    }
    //reads the game state from save.bin
    private static GameState readSaveFile()
            throws ClassNotFoundException, IOException {
        FileInputStream fis = new FileInputStream("save.bin");
        ObjectInputStream ois = new ObjectInputStream(fis);
        GameState state = (GameState) ois.readObject();
        ois.close();
        return state;
    }
    //reads in the events from the event file.
    //yes, I know its messy, sorry.
    private static ArrayList<Event> readEventsFile() throws FileNotFoundException {
        File src = new File("Events.txt");
        Scanner srcIn = new Scanner(src);
        ArrayList<Event> events = new ArrayList<Event>();
        int counter =0;
        while (srcIn.hasNextLine()) {
            String prompt = srcIn.nextLine();
            int numResponses = srcIn.nextInt();
            srcIn.nextLine();
            String[] responses = new String[numResponses];
            for (int i = 0; i < numResponses; i++) {
                responses[i] = srcIn.nextLine();
            }
            int[][] consequences = new int[numResponses][CONSEQUENCE_SIZE];
            for (int i = 0; i < numResponses; i++) {
                int[] temp = new int[CONSEQUENCE_SIZE];
                for (int j = 0; j < CONSEQUENCE_SIZE; j++) {
                    temp[j] = srcIn.nextInt();
                }
                consequences[i] = temp;

                if (srcIn.hasNextLine())srcIn.nextLine();
            }
            counter++;
            //nSystem.out.println(counter);
            events.add(new Event(prompt, responses, consequences));
        }
        srcIn.close();
        return events;
    }
}