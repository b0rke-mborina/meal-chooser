package android.meal_chooser;

import java.io.Serializable;

public class Dish implements Serializable {
    private long id;
    private String name;
    private double timeToMakeInMinutes;
    private boolean isConsidered;
    private Ingredient[] ingredients;

    Dish(String name, double timeToMakeInMinutes, boolean isConsidered, Ingredient[] ingredients) {
        this.name = name;
        this.timeToMakeInMinutes = timeToMakeInMinutes;
        this.isConsidered = isConsidered;
        this.ingredients = ingredients;
    }

    public Dish() {

    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeToMakeInMinutes(double timeToMakeInMinutes) {
        this.timeToMakeInMinutes = timeToMakeInMinutes;
    }

    public void setConsidered(boolean considered) {
        isConsidered = considered;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getTimeToMakeInMinutes() {
        return timeToMakeInMinutes;
    }

    public boolean isConsidered() {
        return isConsidered;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }
}
