package brillianceanimationstudio.brandonward.baccalculator.domain;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by BrandonWard on 7/21/2014.
 *
 * Serializable class that will be stored, written, read, and passed between fragments to pull relevent info
 */

/*    private TimePickerDialog.OnTimeSetListener mTimeSetListener = // TODO: Using this as a placeholder to show myself the code for the OnTimeSetListener
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    tHour = hourOfDay;
                    tMinute = minute;
                }
            };*/
public class userInfo implements Serializable{
    private double weight;
    private boolean weightType;//true for kilograms, false for pounds
    private boolean genderType;//true for male, false for female
    private int tHour;
    private int tMinute;
    private double drinks;

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isWeightType() {
        return weightType;
    }

    public void setWeightType(boolean weightType) {
        this.weightType = weightType;
    }

    public boolean isGenderType() {
        return genderType;
    }

    public void setGenderType(boolean genderType) {
        this.genderType = genderType;
    }

    public int gettHour() {
        return tHour;
    }

    public void settHour(int tHour) {
        this.tHour = tHour;
    }

    public int gettMinute() {
        return tMinute;
    }

    public void settMinute(int tMinute) {
        this.tMinute = tMinute;
    }

    public double getDrinks() {
        return drinks;
    }

    public void setDrinks(double drinks) {
        this.drinks = drinks;
    }
}
