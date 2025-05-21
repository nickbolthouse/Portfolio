/*
 * Notes to self: finish happiness functions, add calculations for player assistance in summary,
 * write upgradeLot() function, write addresses, and prompt integration
 */
package suburbiatycoon2023;
import suburbiatycoon2023.*;
import java.io.Serializable;

public class Lot implements Serializable{
    private int installationLevel, material, happiness;
    private String address;
    private HousingUnit[] units;

    //addresses
    private final String[] ADDRESSES = {
        "31 Spooner Street", "21 Main Street", "154 Dry Gulch Road", 
        "45 Oakwood Lane","88 Riverside Drive", "307 Willowbrook Avenue",
        "162 Maple Street", "213 Birchwood Court", "74 Elm Terrace",
        "501 Sunset Boulevard", "129 Greenwood Circle", "402 Brookside Road",
        "55 Hawthorne Place"};

    //Syntactic Sugar/human readable values
    private final int EMPTY_LOT = 0;
    private final int DECREPIT = 1;
    private final int LOW_INCOME = 2;
    private final int MIDDLE_CLASS = 3;
    private final int PREMIUM = 4;
    private final int SUPER_PREMIUM = 5;

    //for material choices (to calculate carbon footprint)
    private final int CONCRETE = 3;
    private final int STEEL = 2;
    private final int BRICK = 2;
    private final int TIMBER = 1;
    private final int PACKED_EARTH = -3;

    //for tenant happiness
    private static final int ANGRYMOB = -5;
    private static final int ENRAGED = -4;
    private static final int DISGRUNTLED = -3;
    private static final int UNHAPPY = -2;
    private static final int ANNOYED = -1;
    private static final int INDIFFERENT = 0;
    private static final int MEH = 1;
    private static final int ACCEPTING = 2;
    private static final int CONTENT = 3;
    private static final int HAPPY = 4;
    private static final int ECSTATIC = 5;

    //default constructor. if this gets called, weird things happened
    public Lot(){
        this.units = new HousingUnit[10];
        this.installationLevel = LOW_INCOME;
        this.material = CONCRETE;
        this.happiness = INDIFFERENT;
        this.address = this.ADDRESSES[0];
        populateUnits();
        
    }
    public Lot(int addressIndex, int numUnits, int material, int installationLevel){
        this.units = new HousingUnit[numUnits];
        this.installationLevel = installationLevel;
        this.material = material;
        this.happiness = INDIFFERENT;
        this.address = this.ADDRESSES[addressIndex];
        populateUnits();
    }
    
    private void populateUnits(){
        for(int i=0; i < this.units.length; i++){
            this.units[i] = new HousingUnit(this.material, this.installationLevel);
        }

    }
    private int calculateExpectedRent(){
        int total = 0;
        for (HousingUnit x : this.units){
            if ((this.happiness) < DISGRUNTLED && this.installationLevel < DECREPIT){
                total += x.getRent();
            }
        }
        if (this.happiness < UNHAPPY){
            System.out.println("Your tenants are unhappy. If they grow more discontent, "+ 
            "they may stop paying rent");
        }
        return total;
    }
    private int calculateLotValue(){
        int total = 0;
        for (HousingUnit x : this.units){
            total += x.getRealEstateValue();
        }
        return total;
    }
    private int calculateCarbonFootprint(){
        int total = 0;
        for (HousingUnit x : this.units){
            total += x.getFootprint();
        }
        return total;
    }
    private String describeHappiness() {
        switch (this.happiness){
         case ANGRYMOB: 
            return ("hiring lawyers.");
        case ENRAGED:
            return ("filing formal complaints.");
        case DISGRUNTLED: 
            return ("not paying rent.");
        case UNHAPPY: 
            return ("insulting your mother.");
        case ANNOYED: 
            return("drinking more than usual.");
        case INDIFFERENT: 
            return("watching too much Netflix.");
        case MEH: 
            return("going about their lives.");
        case ACCEPTING: 
            return("relaxing with friends.");
        case CONTENT: 
            return("having champagne with dinner.");
        case HAPPY: 
            return("toasting your health.");
        case ECSTATIC: 
            return("enjoying every moment.");
        default:
            return ("pining for the Fjords??????");
        }
    }
    public String lotSummary(){
        String summary = this.address + ": State of Lot: " + this.installationLevel;
        summary += "\nThis Month's expected rent: " + calculateExpectedRent();
        summary += "\nCarbon footprint: " + calculateCarbonFootprint();
        summary += "\nTotal lot value: " + calculateLotValue();
        summary += "\nThe tenants are " + describeHappiness();

        return summary;
    }

    public int[] getRentAndFootprint(){
        int[] consequence = new int[7];
        consequence[0] = calculateExpectedRent();
        consequence[1] = calculateCarbonFootprint();
        return consequence;
    }
    private void alterRent(int in){
        double percentAdded = (double)in/100;
        for (HousingUnit x : this.units){
            x.setRent((int)(x.getRent() + x.getRent()*percentAdded));
        }
    }

    private void alterRealEstateValue(int in){
        double percentAdded = (double)in/100;
        for (HousingUnit x : this.units){
            x.setRent((int)(x.getRealEstateValue() + x.getRealEstateValue()*percentAdded));
            //percent added is negative if value is to be lowered.
        }
    }

    private void renovateUnits(int materialChoice, int installationLevel){
        if (this.installationLevel == EMPTY_LOT){
            this.installationLevel = installationLevel;
            this.material = materialChoice;

            for (HousingUnit x : this.units){
                x.setFootprint(this.material + this.installationLevel);
            }
        }
    }
    /*
    * will come in as [int change in happiness, int change in real estate value,
    * int change in housing material, int change in installation level
    */
    public void updateFromConsequence(int[] in){
        if (in[0] !=0){
            alterRent(in[0]);
        }
        if (in[1] != 0){
            alterRealEstateValue(in[1]);
        }
        if (in[2] != 0 || in[3] !=0){
            if (in[3] == 0){
                renovateUnits(in[2], this.installationLevel);
            }
            else if (in[2] == 0){
                renovateUnits(this.material, in[3]);
            }
            else{
                renovateUnits(in[2], in[3]);
            }
        }
    }
}
