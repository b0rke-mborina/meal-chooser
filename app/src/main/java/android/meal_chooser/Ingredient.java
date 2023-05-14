package android.meal_chooser;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private long id;
    private String name;
    private int amount;
    private Boolean isAvailable;
    private boolean belongsToDish;

    Ingredient(long id, String name, int amount, Boolean isAvailable, boolean belongsToDish) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.isAvailable = isAvailable;
        this.belongsToDish = belongsToDish;
    }

    public Ingredient() {

    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public void setBelongsToDish(boolean belongsToDish) {
        this.belongsToDish = belongsToDish;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public Boolean isAvailable() {
        return isAvailable;
    }

    public boolean belongsToDish() {
        return belongsToDish;
    }
}
