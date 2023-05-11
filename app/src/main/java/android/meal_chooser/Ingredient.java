package android.meal_chooser;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private long id;
    private String name;
    private int amount;

    Ingredient(String name, int amount) {
        this.name = name;
        this.amount = amount;
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

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
