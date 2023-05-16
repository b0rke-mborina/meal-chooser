package android.meal_chooser;

import java.io.Serializable;

public class RecommendationItem implements Serializable {
    private long id;
    private long dishId;
    private String dishName;
    private long timestamp;

    public RecommendationItem() {}

    public RecommendationItem(long id, long dishId, String dishName, long timestamp) {
        this.id = id;
        this.dishId = dishId;
        this.dishName = dishName;
        this.timestamp = timestamp;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDishId(long dishId) {
        this.dishId = dishId;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public long getDishId() {
        return dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
