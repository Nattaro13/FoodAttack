package com.foodattack.foodattack;

/**
 * Created by Xue Hui on 16/7/2015.
 */
public class PersonInfo {
    private boolean isEating;
    private String personName;
    private String mealID;

    public PersonInfo(boolean isEating,String name, String mealType) {
        this.isEating = isEating;
        this.personName = name;
        this.mealID = mealType;
    }

    public String getName() {
        return this.personName;
    }

    public String getMealID() {
        return this.mealID;
    }

    public boolean getEating() {
        return this.isEating;
    }

}
