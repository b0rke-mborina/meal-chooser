package android.meal_chooser;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private String name;
    private int amount;

    Ingredient(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
