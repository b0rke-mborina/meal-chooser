package android.meal_chooser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * ORM (Object-relational mapping) class. Is used to manipulate the data in the database.
 */
public class MealChooserDataSource {
    /**
     * Database helper instance.
     */
    MealChooserDbHelper databaseHelper;
    /**
     * Database instance.
     */
    SQLiteDatabase database;

    /**
     * Array of columns of the dish table. It's used for selection.
     */
    private static final String[] dishColumns = {
            MealChooserDbHelper.DISH_ID,
            MealChooserDbHelper.DISH_NAME,
            MealChooserDbHelper.DISH_TIME_TO_MAKE_IN_MINUTES,
            MealChooserDbHelper.DISH_IS_CONSIDERED
    };

    /**
     * Array of columns of the ingredient table. It's used for selection.
     */
    private final String[] ingredientColumns = {
            MealChooserDbHelper.INGREDIENT_ID,
            MealChooserDbHelper.INGREDIENT_NAME,
            MealChooserDbHelper.INGREDIENT_AMOUNT,
            MealChooserDbHelper.INGREDIENT_IS_AVAILABLE,
            MealChooserDbHelper.INGREDIENT_BELONGS_TO_DISH
    };

    /**
     * Array of columns of the dishIngredient table. It's used for selection.
     */
    private final String[] dishIngredientColumns = {
            MealChooserDbHelper.DISH_INGREDIENT_ID,
            MealChooserDbHelper.DISH_INGREDIENT_DISH_ID,
            MealChooserDbHelper.DISH_INGREDIENT_INGREDIENT_ID
    };

    /**
     * Array of columns of the recommendation history table. It's used for selection.
     */
    private final String[] recommendationHistoryColumns = {
            MealChooserDbHelper.RECOMMENDATION_HISTORY_ID,
            MealChooserDbHelper.RECOMMENDATION_HISTORY_DISH_ID,
            MealChooserDbHelper.RECOMMENDATION_HISTORY_DISH_NAME,
            MealChooserDbHelper.RECOMMENDATION_HISTORY_TIMESTAMP
    };

    /**
     * Parameterized constructor. Initializes database helper instance with context.
     *
     * @param context Current data.
     */
    public MealChooserDataSource(Context context) {
        databaseHelper = new MealChooserDbHelper(context);
    }

    /**
     * Opens the database.
     */
    public void open() {
        System.out.println("Database opened");
        database = databaseHelper.getWritableDatabase();
    }

    /**
     * Closes the database.
     */
    public void close() {
        System.out.println("Database closed");
        databaseHelper.close();
    }

    /**
     * Retrieves all dishes from the database.
     *
     * @return Array of dishes.
     */
    public Dish[] getAllDishes() {
        // make selection and create object
        Cursor cursor = database.query(MealChooserDbHelper.TABLE_DISH, dishColumns,
                null, null, null, null, null);
        System.out.println("Returned " + cursor.getCount() + " rows");
        Dish[] dishes = new Dish[cursor.getCount()];

        // add data to object while iterating through selection results
        int index = 0;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Dish dish = new Dish();

                int indexId = cursor.getColumnIndex(MealChooserDbHelper.DISH_ID);
                dish.setId(cursor.getLong(indexId));

                int indexName = cursor.getColumnIndex(MealChooserDbHelper.DISH_NAME);
                dish.setName(cursor.getString(indexName));

                int indexTimeToMake = cursor
                        .getColumnIndex(MealChooserDbHelper.DISH_TIME_TO_MAKE_IN_MINUTES);
                dish.setTimeToMakeInMinutes(cursor.getDouble(indexTimeToMake));

                int indexIsConsidered = cursor
                        .getColumnIndex(MealChooserDbHelper.DISH_IS_CONSIDERED);
                int isConsidered = cursor.getInt(indexIsConsidered);
                dish.setConsidered(isConsidered == 1);

                dishes[index] = dish;
                index++;
            }
        }
        cursor.close();

        return dishes;
    }

    /**
     * Retrieves all ingredients from the database.
     *
     * @return Array of ingredients.
     */
    public Ingredient[] getAllIngredients() {
        // make selection and create object
        String selection = MealChooserDbHelper.INGREDIENT_BELONGS_TO_DISH + " = ?";
        String[] selectionArgs = {String.valueOf(0)};

        Cursor cursor = database.query(MealChooserDbHelper.TABLE_INGREDIENT, ingredientColumns,
                selection, selectionArgs, null, null, null);
        System.out.println("Returned " + cursor.getCount() + " rows");
        Ingredient[] ingredients = new Ingredient[cursor.getCount()];

        // add data to object while iterating through selection results
        int index = 0;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Ingredient ingredient = new Ingredient();

                int indexId = cursor.getColumnIndex(MealChooserDbHelper.INGREDIENT_ID);
                ingredient.setId(cursor.getLong(indexId));

                int indexName = cursor.getColumnIndex(MealChooserDbHelper.INGREDIENT_NAME);
                ingredient.setName(cursor.getString(indexName));

                int indexAmount = cursor.getColumnIndex(MealChooserDbHelper.INGREDIENT_AMOUNT);
                ingredient.setAmount(cursor.getInt(indexAmount));

                int indexIsAvailable = cursor
                        .getColumnIndex(MealChooserDbHelper.INGREDIENT_IS_AVAILABLE);
                int isAvailableValue = cursor.getInt(indexIsAvailable);
                if (isAvailableValue == 1) {
                    ingredient.setAvailable(true);
                } else if (isAvailableValue == 0) {
                    ingredient.setAvailable(false);
                } else {
                    ingredient.setAvailable(null);
                }

                int indexBelongsToDish = cursor
                        .getColumnIndex(MealChooserDbHelper.INGREDIENT_BELONGS_TO_DISH);
                int belongsToDishValue = cursor.getInt(indexBelongsToDish);
                ingredient.setBelongsToDish(belongsToDishValue == 1);

                ingredients[index] = ingredient;
                index++;
            }
        }
        cursor.close();

        return ingredients;
    }

    /**
     * Retrieves all recommendation history items from the database.
     *
     * @return Array of recommendation history items.
     */
    public RecommendationItem[] getAllRecommendationHistoryItems() {
        // make selection and create object
        Cursor cursor = database.query(MealChooserDbHelper.TABLE_RECOMMENDATION_HISTORY,
                recommendationHistoryColumns, null, null, null, null, null);
        System.out.println("Returned " + cursor.getCount() + " rows");
        RecommendationItem[] recommendationItems = new RecommendationItem[cursor.getCount()];

        // add data to object while iterating through selection results
        int index = 0;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                RecommendationItem recommendationItem = new RecommendationItem();

                int indexId = cursor.getColumnIndex(MealChooserDbHelper.RECOMMENDATION_HISTORY_ID);
                recommendationItem.setId(cursor.getLong(indexId));

                int indexDishId = cursor
                        .getColumnIndex(MealChooserDbHelper.RECOMMENDATION_HISTORY_DISH_ID);
                recommendationItem.setDishId(cursor.getLong(indexDishId));

                int indexDishName = cursor
                        .getColumnIndex(MealChooserDbHelper.RECOMMENDATION_HISTORY_DISH_NAME);
                recommendationItem.setDishName(cursor.getString(indexDishName));

                int indexIsTimestamp = cursor
                        .getColumnIndex(MealChooserDbHelper.RECOMMENDATION_HISTORY_TIMESTAMP);
                recommendationItem.setTimestamp(cursor.getLong(indexIsTimestamp));

                recommendationItems[index] = recommendationItem;
                index++;
            }
        }
        cursor.close();

        // sort items by timestamp, sort newest first, sort it by converting to list and back
        List<RecommendationItem> recommendationItemList = Arrays.asList(recommendationItems);
        recommendationItemList.sort((obj1, obj2) ->
                Long.compare(obj2.getTimestamp(), obj1.getTimestamp()));

        return recommendationItemList.toArray(new RecommendationItem[0]);
    }

    /**
     * Retrieves a dish from the database.
     *
     * @param dishId Id of the dish to be retrieved.
     * @return Retrieved dish.
     */
    public Dish getDish(long dishId) {
        // make selection and create object
        String selection = MealChooserDbHelper.DISH_ID + " = ?";
        String[] selectionArgs = {String.valueOf(dishId)};

        Cursor cursor = database.query(MealChooserDbHelper.TABLE_DISH, dishColumns,
                selection, selectionArgs, null, null, null);
        Dish dish = new Dish();

        // add data to object from selection result
        if(cursor.getCount() == 1) {
            if (cursor.moveToFirst()) {
                int indexId = cursor.getColumnIndex(MealChooserDbHelper.DISH_ID);
                dish.setId(cursor.getLong(indexId));

                int indexName = cursor.getColumnIndex(MealChooserDbHelper.DISH_NAME);
                dish.setName(cursor.getString(indexName));

                int indexTimeToMake = cursor
                        .getColumnIndex(MealChooserDbHelper.DISH_TIME_TO_MAKE_IN_MINUTES);
                dish.setTimeToMakeInMinutes(cursor.getDouble(indexTimeToMake));

                int indexIsConsidered = cursor
                        .getColumnIndex(MealChooserDbHelper.DISH_IS_CONSIDERED);
                int isConsidered = cursor.getInt(indexIsConsidered);
                dish.setConsidered(isConsidered == 1);
            }
        }
        cursor.close();

        // make selection of dish ingredient records and create array
        String selectionDishIngredient = MealChooserDbHelper.DISH_INGREDIENT_DISH_ID + " = ?";
        String[] selectionArgsDishIngredient = {String.valueOf(dishId)};
        Cursor cursorDishIngredient = database.query(MealChooserDbHelper.TABLE_DISH_INGREDIENT,
                dishIngredientColumns, selectionDishIngredient, selectionArgsDishIngredient,
                null, null, null);
        System.out.println("Returned " + cursorDishIngredient.getCount() + " rows");
        long[] ingredientIds = new long[cursorDishIngredient.getCount()];

        // add ingredient ids to array while iterating through selection results
        int index = 0;
        if (cursorDishIngredient.getCount() > 0) {
            while (cursorDishIngredient.moveToNext()) {
                int indexIngredientId = cursorDishIngredient
                        .getColumnIndex(MealChooserDbHelper.DISH_INGREDIENT_INGREDIENT_ID);
                ingredientIds[index] = cursorDishIngredient.getLong(indexIngredientId);
                index++;
            }
        }
        cursorDishIngredient.close();

        // get array of ingredients and add it to dish
        Ingredient[] ingredients = new Ingredient[cursorDishIngredient.getCount()];
        int indexIngredient = 0;
        for (long ingredientId : ingredientIds) {
            ingredients[indexIngredient] = getIngredient(ingredientId);
            indexIngredient++;
        }
        dish.setIngredients(ingredients);

        return dish;
    }

    /**
     * Retrieves a ingredient from the database.
     *
     * @param ingredientId Id of the ingredient to be retrieved.
     * @return Retrieved ingredient.
     */
    public Ingredient getIngredient(long ingredientId) {
        // make selection and create object
        String selection = MealChooserDbHelper.INGREDIENT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(ingredientId)};
        Cursor cursor = database.query(MealChooserDbHelper.TABLE_INGREDIENT, ingredientColumns,
                selection, selectionArgs, null, null, null);
        Ingredient ingredient = new Ingredient();

        // add data to object from selection result
        if(cursor.getCount() == 1) {
            if (cursor.moveToFirst()) {
                int indexId = cursor.getColumnIndex(MealChooserDbHelper.INGREDIENT_ID);
                ingredient.setId(cursor.getLong(indexId));

                int indexName = cursor.getColumnIndex(MealChooserDbHelper.INGREDIENT_NAME);
                ingredient.setName(cursor.getString(indexName));

                int indexAmount = cursor.getColumnIndex(MealChooserDbHelper.INGREDIENT_AMOUNT);
                ingredient.setAmount(cursor.getInt(indexAmount));

                int indexIsAvailable = cursor
                        .getColumnIndex(MealChooserDbHelper.INGREDIENT_IS_AVAILABLE);
                int isAvailableValue = cursor.getInt(indexIsAvailable);
                if (isAvailableValue == 1) {
                    ingredient.setAvailable(true);
                } else if (isAvailableValue == 0) {
                    ingredient.setAvailable(false);
                } else {
                    ingredient.setAvailable(null);
                }

                int indexBelongsToDish = cursor
                        .getColumnIndex(MealChooserDbHelper.INGREDIENT_BELONGS_TO_DISH);
                int belongsToDishValue = cursor.getInt(indexBelongsToDish);
                ingredient.setBelongsToDish(belongsToDishValue == 1);
            }
        }
        cursor.close();

        return ingredient;
    }

    /**
     * Creates a dish in the database.
     *
     * @param dish Dish to be created.
     * @return Created dish.
     */
    public Dish createDish(Dish dish) {
        // create values for database
        ContentValues values = new ContentValues();
        values.put(MealChooserDbHelper.DISH_NAME, dish.getName());
        values.put(MealChooserDbHelper.DISH_TIME_TO_MAKE_IN_MINUTES, dish.getTimeToMakeInMinutes());
        values.put(MealChooserDbHelper.DISH_IS_CONSIDERED, dish.isConsidered());

        // add values to database
        long insertedId = database.insert(MealChooserDbHelper.TABLE_DISH, null, values);
        dish.setId(insertedId);

        // add ingredients and belonging dish ingredient records to database
        long dishId = dish.getId();
        for (Ingredient ingredient : dish.getIngredients()) {
            ingredient = createIngredient(ingredient);
            createDishIngredient(dishId, ingredient.getId());
        }

        return dish;
    }

    /**
     * Creates an ingredient in the database.
     *
     * @param ingredient Ingredient to be created.
     * @return Created ingredient.
     */
    public Ingredient createIngredient(Ingredient ingredient) {
        // create values for database
        ContentValues values = new ContentValues();
        values.put(MealChooserDbHelper.INGREDIENT_NAME, ingredient.getName());
        values.put(MealChooserDbHelper.INGREDIENT_AMOUNT, ingredient.getAmount());

        Boolean isAvailable = ingredient.isAvailable();
        if (isAvailable == null) {
            values.put(MealChooserDbHelper.INGREDIENT_IS_AVAILABLE, -1);
        } else if (isAvailable) {
            values.put(MealChooserDbHelper.INGREDIENT_IS_AVAILABLE, 1);
        } else {
            values.put(MealChooserDbHelper.INGREDIENT_IS_AVAILABLE, 0);
        }

        boolean belongsToDish = ingredient.belongsToDish();
        if (belongsToDish) {
            values.put(MealChooserDbHelper.INGREDIENT_BELONGS_TO_DISH, 1);
        } else {
            values.put(MealChooserDbHelper.INGREDIENT_BELONGS_TO_DISH, 0);
        }

        // add values to database
        long insertedId = database.insert(MealChooserDbHelper.TABLE_INGREDIENT, null, values);
        ingredient.setId(insertedId);
        return ingredient;
    }

    /**
     * Creates a dish ingredient record in the database.
     *
     * @param dishId Id of the dish which is to be connected to the ingredient.
     * @param ingredientId Id of the ingredient which is to be connected to the dish.
     * @return Id of the dish ingredient record.
     */
    public long createDishIngredient(long dishId, long ingredientId) {
        // create values for database
        ContentValues values = new ContentValues();
        values.put(MealChooserDbHelper.DISH_INGREDIENT_DISH_ID, dishId);
        values.put(MealChooserDbHelper.DISH_INGREDIENT_INGREDIENT_ID, ingredientId);

        // add values to database
        return database.insert(MealChooserDbHelper.TABLE_DISH_INGREDIENT, null, values);
    }

    /**
     * Creates a recommendation history item in the database.
     *
     * @param recommendationItem Recommendation history item to be created.
     * @return Created recommendation history item.
     */
    public RecommendationItem createRecommendationItem(RecommendationItem recommendationItem) {
        // create values for database
        ContentValues values = new ContentValues();
        values.put(MealChooserDbHelper.RECOMMENDATION_HISTORY_DISH_ID,
                recommendationItem.getDishId());
        values.put(MealChooserDbHelper.RECOMMENDATION_HISTORY_DISH_NAME,
                recommendationItem.getDishName());
        values.put(MealChooserDbHelper.RECOMMENDATION_HISTORY_TIMESTAMP,
                new Date().getTime() / 1000L);

        // add values to database
        long insertedId = database.insert(MealChooserDbHelper.TABLE_RECOMMENDATION_HISTORY,
                null, values);
        recommendationItem.setId(insertedId);
        return recommendationItem;
    }

    /**
     * Updates a dish in the database.
     *
     * @param dish Dish to be updated.
     * @return Updated dish.
     */
    public Dish updateDish(Dish dish) {
        long dishId = dish.getId();

        // create values for database
        ContentValues values = new ContentValues();
        values.put(MealChooserDbHelper.DISH_NAME, dish.getName());
        values.put(MealChooserDbHelper.DISH_TIME_TO_MAKE_IN_MINUTES, dish.getTimeToMakeInMinutes());
        values.put(MealChooserDbHelper.DISH_IS_CONSIDERED, dish.isConsidered());

        // update dish in database
        String selection = MealChooserDbHelper.DISH_ID + " = ?";
        String[] selectionArgs = { String.valueOf(dishId) };
        database.update(MealChooserDbHelper.TABLE_DISH, values, selection, selectionArgs);

        // make dish ingredient selection and create array
        String selectionDishIngredient = MealChooserDbHelper.DISH_INGREDIENT_DISH_ID + " = ?";
        String[] selectionArgsDishIngredient = {String.valueOf(dishId)};
        Cursor cursor = database.query(MealChooserDbHelper.TABLE_DISH_INGREDIENT,
                dishIngredientColumns, selectionDishIngredient, selectionArgsDishIngredient,
                null, null, null);
        System.out.println("Returned " + cursor.getCount() + " rows");
        long[] ingredientIds = new long[cursor.getCount()];

        // add ingredient ids to array while iterating through selection results
        int index = 0;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int indexId = cursor
                        .getColumnIndex(MealChooserDbHelper.DISH_INGREDIENT_INGREDIENT_ID);
                ingredientIds[index] = cursor.getLong(indexId);
                index++;
            }
        }
        cursor.close();

        // delete belonging dish ingredient records and ingredients in database
        database.delete(MealChooserDbHelper.TABLE_DISH_INGREDIENT,
                MealChooserDbHelper.DISH_INGREDIENT_DISH_ID + " = ?",
                new String[]{String.valueOf(dishId)});
        for (long ingredientId : ingredientIds) {
            deleteIngredient(ingredientId);
        }

        // add new ingredients and belonging dish ingredient records to database
        for (Ingredient ingredient : dish.getIngredients()) {
            ingredient = createIngredient(ingredient);
            createDishIngredient(dishId, ingredient.getId());
        }

        return dish;
    }

    /**
     * Updates an ingredient in the database.
     *
     * @param ingredient Ingredient to be updated.
     * @return Updated ingredient.
     */
    public Ingredient updateIngredient(Ingredient ingredient) {
        long ingredientId = ingredient.getId();

        // create values for database
        ContentValues values = new ContentValues();
        values.put(MealChooserDbHelper.INGREDIENT_NAME, ingredient.getName());
        values.put(MealChooserDbHelper.INGREDIENT_AMOUNT, ingredient.getAmount());
        values.put(MealChooserDbHelper.INGREDIENT_IS_AVAILABLE, ingredient.isAvailable());
        values.put(MealChooserDbHelper.INGREDIENT_BELONGS_TO_DISH, ingredient.belongsToDish());

        // update values in database
        String selection = MealChooserDbHelper.INGREDIENT_ID + " = ?";
        String[] selectionArgs = { String.valueOf(ingredientId) };
        database.update(MealChooserDbHelper.TABLE_INGREDIENT, values, selection, selectionArgs);

        return ingredient;
    }

    /**
     * Deletes a dish in the database.
     *
     * @param dishId Id of the dish to be deleted.
     */
    public void deleteDish(long dishId) {
        // delete dish in database
        database.delete(MealChooserDbHelper.TABLE_DISH,
                MealChooserDbHelper.DISH_ID + " = ?", new String[]{String.valueOf(dishId)});

        // make selection and create array
        String selectionDishIngredient = MealChooserDbHelper.DISH_INGREDIENT_DISH_ID + " = ?";
        String[] selectionArgsDishIngredient = {String.valueOf(dishId)};
        Cursor cursor = database.query(MealChooserDbHelper.TABLE_DISH_INGREDIENT,
                dishIngredientColumns, selectionDishIngredient, selectionArgsDishIngredient,
                null, null, null);
        System.out.println("Returned " + cursor.getCount() + " rows");
        long[] ingredientIds = new long[cursor.getCount()];

        // add ingredient ids to array while iterating through selection results
        int index = 0;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int indexId = cursor
                        .getColumnIndex(MealChooserDbHelper.DISH_INGREDIENT_INGREDIENT_ID);
                ingredientIds[index] = cursor.getLong(indexId);
                index++;
            }
        }
        cursor.close();

        // delete belonging dish ingredient records and ingredients in database
        database.delete(MealChooserDbHelper.TABLE_DISH_INGREDIENT,
                MealChooserDbHelper.DISH_INGREDIENT_DISH_ID + " = ?",
                new String[]{String.valueOf(dishId)});
        for (long ingredientId : ingredientIds) {
            deleteIngredient(ingredientId);
        }
    }

    /**
     * Deletes an ingredient in the database.
     *
     * @param ingredientId Id of the ingredient to be deleted.
     */
    public void deleteIngredient(long ingredientId) {
        // delete ingredient in database
        database.delete(MealChooserDbHelper.TABLE_INGREDIENT,
                MealChooserDbHelper.INGREDIENT_ID + " = ?",
                new String[]{String.valueOf(ingredientId)});
    }

    /**
     * Deletes a recommendation history item in the database.
     *
     * @param recommendationItemId Id of the recommendation history item to be deleted.
     */
    public void deleteRecommendationItem(long recommendationItemId) {
        // delete recommendation item in database
        database.delete(MealChooserDbHelper.TABLE_RECOMMENDATION_HISTORY,
                MealChooserDbHelper.RECOMMENDATION_HISTORY_ID + " = ?",
                new String[]{String.valueOf(recommendationItemId)});
    }
}
