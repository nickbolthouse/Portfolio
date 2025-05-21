package suburbiatycoon2023;

import java.io.Serializable;

public class Event implements Serializable{
    private String prompt;
    private String[] acceptedResponses;
    private int[][] consequenceList;
    private static final int[] DEFAULT_CONSEQUENCE = {0,0,0,0,0,0,0};

    public Event(String prompt, String[] acceptedResponses, int[][] consequenceList){
        this.prompt = prompt;
        this.acceptedResponses = acceptedResponses;
        this.consequenceList = consequenceList;
    }
    public String prompt(){
        return this.prompt;
    }
    public String[] acceptedResponses(){
        return this.acceptedResponses;
    }
    public int[] chooseOutcome(String in){
        for (int i=0; i < acceptedResponses.length; i++){
            if (in.equals(acceptedResponses[i].toLowerCase())){
                return consequenceList[i];
            }
        }
        //if all else fails, return a consequence that does nothing
        System.out.print("unable to parse outcome, returning default");
        return DEFAULT_CONSEQUENCE;
    }
    public String toString(){
        String output = prompt + "\n";
        for (int i=0; i < acceptedResponses().length; i++){
            output += "\t" + acceptedResponses[i];
        }
        output += "\n";
        for (int i=0; i < consequenceList.length;i++){
            for (int j=0; j < consequenceList[i].length; j++){
                output+= consequenceList[i][j];
            }
            output += "\n";
        }

        return output;
    }
}

/* Structure of event in text file
* prompt (explain the scenario)
*  number of accepted responses
*   accepted response strings (1 per line)
*   consequences (1 value per line)
*
*consequences will be represented as follows:
* int[7]
*   change in euros 
*   change in carbon footprint 
*   integer (-1 to create new lot, 0 for no changes, 1 to update all lots.)             
*   index (0-12) --or-- % rent change
*   number of housing units --or-- % estate value change
*   material --or-- material change {-3,1,2,3}
*   installation level --or-- installation level change (0-5, <1 pays rent)

Will be written in files like the following example:
Hippy hoppity, your code is ___ my property
2
Now
Not
1 1 0 0 0 0 0
0 0 0 0 0 0 0
*/

