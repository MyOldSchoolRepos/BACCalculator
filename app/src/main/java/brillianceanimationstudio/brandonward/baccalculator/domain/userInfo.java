package brillianceanimationstudio.brandonward.baccalculator.domain;

import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by BrandonWard on 7/21/2014.
 * <p/>
 * Serializable class that will be stored, written, read, and passed between fragments to pull relevent info
 */

/*    private TimePickerDialog.OnTimeSetListener mTimeSetListener = // TODO: Using this as a placeholder to show myself the code for the OnTimeSetListener
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    tHour = hourOfDay;
                    tMinute = minute;
                }
            };*/
public class userInfo implements Serializable {
    private double weight;
    private boolean weightType;//true for kilograms, false for pounds
    private boolean genderType;//true for male, false for female
    private int tHour;
    private int tMinute;
    private double drinks;
    private int lastDay;
    private int lastMonth;
    private int lastYear;
    private static final String USER_STATE = "this.user.information";

    public static String getPrefsKey() {
        return USER_STATE;
    }

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

    public String toPrefsString(userInfo userInfo) {

        return weight + "/" + weightType + "/" + genderType + "/" + tHour + "/" + tMinute + "/" + drinks
                + "/" + lastDay + "/" + lastMonth + "/" + lastYear;
    }

    public userInfo readPrefsString(String prefsString) {
//This number needs to match however many components I am saving.
            if (!(prefsString.equals("") || prefsString == null)) {
                String[] parts = prefsString.split("/");
                weight = Double.parseDouble(parts[0]);
                weightType = Boolean.parseBoolean(parts[1]);
                genderType = Boolean.parseBoolean(parts[2]);
                tHour = Integer.parseInt(parts[3]);
                tMinute = Integer.parseInt(parts[4]);
                drinks = Double.parseDouble(parts[5]);
                lastDay = Integer.parseInt(parts[6]);
                lastMonth = Integer.parseInt(parts[7]);
                lastYear = Integer.parseInt(parts[8]);
            }
        return this;
    }

    public int getLastDay() {
        return lastDay;
    }

    public void setLastDay(int lastDay) {
        this.lastDay = lastDay;
    }

    public int getLastMonth() {
        return lastMonth;
    }

    public void setLastMonth(int lastMonth) {
        this.lastMonth = lastMonth;
    }

    public int getLastYear() {
        return lastYear;
    }

    public void setLastYear(int lastYear) {
        this.lastYear = lastYear;
    }

}

