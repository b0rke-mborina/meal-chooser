package android.meal_chooser;

import java.io.Serializable;

/**
 * Dish class represents ingredient entity. Ingredients are managed by the user.
 */
public class Ingredient implements Serializable {
    /**
     * Id of the ingredient object in database.
     */
    private long id;

    /**
     * Name of the ingredient.
     */
    private String name;

    /**
     * Amount of the ingredient defines number of units.
     */
    private int amount;

    /**
     * Available value of the ingredient defines if user has ingredient in possession.
     */
    private Boolean isAvailable;

    /**
     * Value of the ingredient which defines if ingredient is a part of a dish or not.
     */
    private boolean belongsToDish;

    /**
     * Empty non-parameterized constructor.
     */
    public Ingredient() {}

    /**
     * Parameterized constructor.
     *
     * @param id Id of the new ingredient.
     * @param name Name of the new ingredient.
     * @param amount Amount of the new ingredient.
     * @param isAvailable Available value of the new ingredient.
     * @param belongsToDish Belongs to dish value of the new ingredient.
     */
    public Ingredient(long id, String name, int amount, Boolean isAvailable,
                      boolean belongsToDish) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.isAvailable = isAvailable;
        this.belongsToDish = belongsToDish;
    }

    /**
     * Id setter.
     *
     * @param id New id of the ingredient.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Name setter.
     *
     * @param name New name of the ingredient.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Amount setter.
     *
     * @param amount New amount of the ingredient.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Available value setter.
     *
     * @param available New available value of the ingredient.
     */
    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    /**
     * Belongs to dish value setter.
     *
     * @param belongsToDish New belongs to dish value of the ingredient.
     */
    public void setBelongsToDish(boolean belongsToDish) {
        this.belongsToDish = belongsToDish;
    }

    /**
     * Id getter.
     *
     * @return Id of the ingredient.
     */
    public long getId() {
        return id;
    }

    /**
     * Name getter.
     *
     * @return Name of the ingredient.
     */
    public String getName() {
        return name;
    }

    /**
     * Amount getter.
     *
     * @return Amount of the ingredient.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Available value getter.
     *
     * @return Available value of the ingredient.
     */
    public Boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Belongs to dish value getter.
     *
     * @return Belongs to dish value of the ingredient.
     */
    public boolean belongsToDish() {
        return belongsToDish;
    }
}
