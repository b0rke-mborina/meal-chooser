package android.meal_chooser;

import java.io.Serializable;

public class Dish implements Serializable {
    private String name;
    private double timeToMakeInMinutes;
    private Ingredient[] ingredients;

    Dish(String name, double timeToMakeInMinutes, Ingredient[] ingredients) {
        this.name = name;
        this.timeToMakeInMinutes = timeToMakeInMinutes;
        this.ingredients = ingredients;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeToMakeInMinutes(double timeToMakeInMinutes) {
        this.timeToMakeInMinutes = timeToMakeInMinutes;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public double getTimeToMakeInMinutes() {
        return timeToMakeInMinutes;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }
}
