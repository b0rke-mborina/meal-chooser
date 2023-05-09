package android.meal_chooser;

public class Dish {
    private String name;
    private Ingredient[] ingredients;
    private double timeToMakeInMinutes;

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public void setTimeToMakeInMinutes(double timeToMakeInMinutes) {
        this.timeToMakeInMinutes = timeToMakeInMinutes;
    }

    public String getName() {
        return name;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public double getTimeToMakeInMinutes() {
        return timeToMakeInMinutes;
    }
}
