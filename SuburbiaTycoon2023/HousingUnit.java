package suburbiatycoon2023;

import java.io.Serializable;

public class HousingUnit implements Serializable{
    private int rent, realEstateValue, carbonFootprint; //determined by the property's material and housing level

    private final int BASE_RENT = 750;
    private final int BASE_ESTATE_VALUE = 150000;

    //fallback defualt constructor
    public HousingUnit(){
        this.rent = BASE_RENT;
        this.carbonFootprint = 3;
        this.realEstateValue = BASE_ESTATE_VALUE;
    }
    public HousingUnit(int material, int qualityLevel){
        int qualityModifier = qualityLevel/10;
        this.rent = BASE_RENT + (BASE_RENT * (qualityModifier));
        this.carbonFootprint = (material + qualityLevel);
        this.realEstateValue = BASE_ESTATE_VALUE + ((BASE_ESTATE_VALUE + qualityModifier));
    }

    public int getRent(){
        return this.rent; 
    }
    public int getRealEstateValue(){
        return this.realEstateValue;
    }
    public int getFootprint(){
        return this.carbonFootprint;
    }

    public int setRent(int in){
        this.rent = in;
        return getRent();
    }
    public int setValue(int in){
        this.realEstateValue = in; 
        return getRealEstateValue();
    }
    public int setFootprint(int in){
        this.carbonFootprint = in;
        return getFootprint();
    }
}