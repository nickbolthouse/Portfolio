/*
 * This class is primarily syntactic sugar, just so I can save my sanity from
 * having to catch bad inputs on the spot each time and declutter my code.
 */
package suburbiatycoon2023;

import java.util.Scanner;

public class InputParser {
    private Scanner inStream;

    public InputParser(Scanner stream) {
        this.inStream = stream;
    }

    //reads for a sting from System.in
    public String promptUser(String query, String[] acceptedResponseStrings) {
        String response = "";
        do {
            System.out.println(query + "\n_______________\n");
            for (int i = 0; i < acceptedResponseStrings.length; i++) {
                System.out.print(acceptedResponseStrings[i] + "\t");
            }
            System.out.println();

            response = (this.inStream.nextLine()).toLowerCase();

            for (int i = 0; i < acceptedResponseStrings.length; i++) {
                String temp = acceptedResponseStrings[i].toLowerCase();
                if (temp.equals(response)) {
                    return response;
                }
            }
            System.out.println("Command not recognized. Type your answer as it is written, or enter " +
                    "\"help\" to see other commands.");
        } while (true);
    }
    //reads for an integer from System.in
    public int promptInt(String query, boolean positiveOnly) {
        int response;
        do {
            System.out.println(query);
            try {
                response = Integer.parseInt(this.inStream.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("please enter a valid" +
                        (positiveOnly ? ", positive, " : " ") + "integer.");
                continue;
            }

            if (positiveOnly && response < 0) {
                System.out.println("Please enter a positive integer");
                continue;
            } else {
                return response;
            }
        } while (true);
    }
}
