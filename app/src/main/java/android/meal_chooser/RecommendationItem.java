package android.meal_chooser;

import java.io.Serializable;

public class RecommendationItem implements Serializable {
    /**
     * Id of the recommendation history item object in database.
     */
    private long id;

    /**
     * Id of the recommended dish.
     */
    private long dishId;

    /**
     * Name of the recommended dish.
     */
    private String dishName;

    /**
     * Unix timestamp of the recommendation.
     */
    private long timestamp;

    /**
     * Empty non-parameterized constructor.
     */
    public RecommendationItem() {}

    /**
     * Parameterized constructor.
     *
     * @param id Id of the new recommendation.
     * @param dishId Dish id of the new recommendation.
     * @param dishName Dish name of the new recommendation.
     * @param timestamp Unix timestamp of the new recommendation.
     */
    public RecommendationItem(long id, long dishId, String dishName, long timestamp) {
        this.id = id;
        this.dishId = dishId;
        this.dishName = dishName;
        this.timestamp = timestamp;
    }

    /**
     * Id setter.
     *
     * @param id New id of the recommendation.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Dish id setter.
     *
     * @param dishId New dish id of the recommendation.
     */
    public void setDishId(long dishId) {
        this.dishId = dishId;
    }

    /**
     * Dish name setter.
     *
     * @param dishName New dish name of the recommendation.
     */
    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    /**
     * Unix timestamp setter.
     *
     * @param timestamp New unix timestamp of the recommendation.
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Id getter.
     *
     * @return Id of the recommendation.
     */
    public long getId() {
        return id;
    }

    /**
     * Dish id getter.
     *
     * @return Dish id of the recommendation.
     */
    public long getDishId() {
        return dishId;
    }

    /**
     * Dish name getter.
     *
     * @return Dish name of the recommendation.
     */
    public String getDishName() {
        return dishName;
    }

    /**
     * Unix timestamp getter.
     *
     * @return Unix timestamp of the recommendation.
     */
    public long getTimestamp() {
        return timestamp;
    }
}
