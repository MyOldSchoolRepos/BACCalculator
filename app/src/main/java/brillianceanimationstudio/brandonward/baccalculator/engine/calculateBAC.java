package brillianceanimationstudio.brandonward.baccalculator.engine;

import java.util.Calendar;


import brillianceanimationstudio.brandonward.baccalculator.domain.userInfo;

/**
 * Created by BrandonWard on 7/20/2014.
 */

//TODO: Engine is still not quite working to calculate.  I think that my time in hours is CORRECT, the error is elsewhere.
    //Why does it return negatives sometimes??
//TODO: Back to the drawing boards tomorrow. This is the last piece..........
public class calculateBAC {

    private userInfo user;

    public calculateBAC(userInfo userInfo) {
        this.user = userInfo;
    }

    public double calculateBloodAlcoholContent() {
        double weight;
        double waterConstant;
        double metabolismConstant;
        double drinks;
        double drinkingPeriod;
        double BLOOD_ALCOHOL_CONTENT;
        if (user.isWeightType()) {
            weight = user.getWeight();
        } else {
            weight = convertToKilos(user.getWeight());
        }
        waterConstant = getGenderWaterConstant(user.isGenderType());
        metabolismConstant = getGenderMetabolismConstant(user.isGenderType());
        drinks = user.getDrinks();
        drinkingPeriod = calculateDrinkingPeriod(user);
        double waterXweight = waterConstant*weight;
        double metabXdrinkPrd = metabolismConstant*drinkingPeriod;
        BLOOD_ALCOHOL_CONTENT = (0.806 * drinks * 1.2);
        BLOOD_ALCOHOL_CONTENT = BLOOD_ALCOHOL_CONTENT/(waterXweight);
        BLOOD_ALCOHOL_CONTENT = BLOOD_ALCOHOL_CONTENT - (metabXdrinkPrd);
        // TODO: Remove this formula if it now works
        // BLOOD_ALCOHOL_CONTENT = (((0.806 * drinks * 1.2) / (waterConstant * weight)) - (metabolismConstant * drinkingPeriod));
        return BLOOD_ALCOHOL_CONTENT;
    }

    private double convertToKilos(double pounds) {
        return (pounds * 0.453592);
    }

    private double getGenderWaterConstant(boolean genderType) {
        if (genderType) {
            return 0.58;
        } else {
            return 0.49;
        }
    }

    private double getGenderMetabolismConstant(boolean genderType) {
        if (genderType) {
            return 0.015;
        } else {
            return 0.017;
        }
    }

    private double calculateDrinkingPeriod(userInfo userInfo) {
        double drinkingPeriod;
        Calendar rightNow = Calendar.getInstance();
        if (rightNow.get(Calendar.HOUR)< userInfo.gettHour() && rightNow.get(Calendar.MINUTE) < userInfo.gettMinute()){
            drinkingPeriod = (rightNow.get(Calendar.HOUR)*60+rightNow.get(Calendar.MINUTE)+24*60)-(userInfo.gettHour()*60+userInfo.gettMinute());
            drinkingPeriod = drinkingPeriod/60;
        }
        else {
            drinkingPeriod = (rightNow.get(Calendar.HOUR) * 60 + rightNow.get(Calendar.MINUTE)) - (userInfo.gettHour() * 60 + userInfo.gettMinute());
            drinkingPeriod = drinkingPeriod / 60;
        }
        return drinkingPeriod;
    }
}