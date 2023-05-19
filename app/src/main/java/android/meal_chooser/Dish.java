package android.meal_chooser;

import java.io.Serializable;

/**
 * Dish class represents dish entity. Dish is recommended to the user.
 */
public class Dish implements Serializable {
    /**
     * Id of the dish object in database.
     */
    private long id;

    /**
     * Name of the dish.
     */
    private String name;

    /**
     * Time to prepare the dish in minutes.
     */
    private double timeToMakeInMinutes;

    /**
     * Defines whether the dish is considered in choosing.
     */
    private boolean isConsidered;

    /**
     * Ingredients needed to prepare the dish.
     */
    private Ingredient[] ingredients;

    /**
     * Empty non-parameterized constructor.
     */
    public Dish() {}

    /**
     * Parameterized constructor.
     *
     * @param id Id of the new dish.
     * @param name Name of the new dish.
     * @param timeToMakeInMinutes Time to prepare in minutes of the new dish.
     * @param isConsidered Considered value of the new dish.
     * @param ingredients Ingredients of the new dish.
     */
    public Dish(long id, String name, double timeToMakeInMinutes, boolean isConsidered,
                Ingredient[] ingredients) {
        this.id = id;
        this.name = name;
        this.timeToMakeInMinutes = timeToMakeInMinutes;
        this.isConsidered = isConsidered;
        this.ingredients = ingredients;
    }

    /**
     * Id setter.
     *
     * @param id New id of the dish.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Name setter.
     *
     * @param name New name of the dish.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Time to prepare in minutes setter.
     *
     * @param timeToMakeInMinutes New time to prepare in minutes of the dish.
     */
    public void setTimeToMakeInMinutes(double timeToMakeInMinutes) {
        this.timeToMakeInMinutes = timeToMakeInMinutes;
    }

    /**
     * Considered value setter.
     *
     * @param considered New considered value of the dish.
     */
    public void setConsidered(boolean considered) {
        isConsidered = considered;
    }

    /**
     * Ingredients setter.
     *
     * @param ingredients New ingredients of the dish.
     */
    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Id getter.
     *
     * @return Id of the dish.
     */
    public long getId() {
        return id;
    }

    /**
     * Name getter.
     *
     * @return Name of the dish.
     */
    public String getName() {
        return name;
    }

    /**
     * Time to prepare in minutes getter.
     *
     * @return Time to prepare in minutes of the dish.
     */
    public double getTimeToMakeInMinutes() {
        return timeToMakeInMinutes;
    }

    /**
     * Considered value getter.
     *
     * @return Considered value of the dish.
     */
    public boolean isConsidered() {
        return isConsidered;
    }

    /**
     * Ingredients getter.
     *
     * @return Ingredients of the dish.
     */
    public Ingredient[] getIngredients() {
        return ingredients;
    }
}
