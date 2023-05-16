package android.meal_chooser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.Date;

public class MealChooserDataSource {
    MealChooserDbHelper databaseHelper;
    SQLiteDatabase database;

    private final String[] dishColumns = {
            databaseHelper.DISH_ID,
            databaseHelper.DISH_NAME,
            databaseHelper.DISH_TIME_TO_MAKE_IN_MINUTES
    };
    private final String[] ingredientColumns = {
            databaseHelper.INGREDIENT_ID,
            databaseHelper.INGREDIENT_NAME,
            databaseHelper.INGREDIENT_AMOUNT
    };
    private final String[] dishIngredientColumns = {
            databaseHelper.DISH_INGREDIENT_ID,
            databaseHelper.DISH_INGREDIENT_DISH_ID,
            databaseHelper.DISH_INGREDIENT_INGREDIENT_ID
    };
    private final String[] recommendationHistoryColumns = {
            databaseHelper.RECOMMENDATION_HISTORY_ID,
            databaseHelper.RECOMMENDATION_HISTORY_DISH_ID,
            databaseHelper.RECOMMENDATION_HISTORY_TIMESTAMP
    };

    public MealChooserDataSource(Context context) {
        databaseHelper = new MealChooserDbHelper(context);
    }

    public void open() {
        System.out.println("Database opened");
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        System.out.println("Database closed");
        databaseHelper.close();
    }

    public Dish[] getAllDishes() {
        Cursor cursor = database.query(databaseHelper.TABLE_DISH, dishColumns,
                null, null, null, null, null);
        System.out.println("Returned " + cursor.getCount() + " rows");
        Dish[] dishes = new Dish[cursor.getCount()];

        int index = 0;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Dish dish = new Dish();

                int indexId = cursor.getColumnIndex(databaseHelper.DISH_ID);
                dish.setId(cursor.getLong(indexId));

                int indexName = cursor.getColumnIndex(databaseHelper.DISH_NAME);
                dish.setName(cursor.getString(indexName));

                int indexTimeToMake = cursor.getColumnIndex(databaseHelper.DISH_TIME_TO_MAKE_IN_MINUTES);
                dish.setTimeToMakeInMinutes(cursor.getDouble(indexTimeToMake));

                int indexIsConsidered = cursor.getColumnIndex(databaseHelper.DISH_IS_CONSIDERED);
                int isConsidered = cursor.getInt(indexIsConsidered);
                dish.setConsidered(isConsidered == 1);

                dishes[index] = dish;
            }
        }
        cursor.close();

        return dishes;
    }

    public Ingredient[] getAllIngredients() {
        String selection = databaseHelper.INGREDIENT_BELONGS_TO_DISH + " = ?";
        String[] selectionArgs = {String.valueOf(0)};

        Cursor cursor = database.query(databaseHelper.TABLE_INGREDIENT, ingredientColumns,
                selection, selectionArgs, null, null, null);
        System.out.println("Returned " + cursor.getCount() + " rows");
        Ingredient[] ingredients = new Ingredient[cursor.getCount()];

        int index = 0;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Ingredient ingredient = new Ingredient();

                int indexId = cursor.getColumnIndex(databaseHelper.INGREDIENT_ID);
                ingredient.setId(cursor.getLong(indexId));

                int indexName = cursor.getColumnIndex(databaseHelper.INGREDIENT_NAME);
                ingredient.setName(cursor.getString(indexName));

                int indexAmount = cursor.getColumnIndex(databaseHelper.INGREDIENT_AMOUNT);
                ingredient.setAmount(cursor.getInt(indexAmount));

                int indexIsAvailable = cursor.getColumnIndex(databaseHelper.INGREDIENT_IS_AVAILABLE);
                int isAvailableValue = cursor.getInt(indexIsAvailable);
                if (isAvailableValue == 1) {
                    ingredient.setAvailable(true);
                } else if (isAvailableValue == 0) {
                    ingredient.setAvailable(false);
                } else {
                    ingredient.setAvailable(null);
                }

                int indexBelongsToDish = cursor.getColumnIndex(databaseHelper.INGREDIENT_BELONGS_TO_DISH);
                int belongsToDishValue = cursor.getInt(indexBelongsToDish);
                ingredient.setBelongsToDish(belongsToDishValue == 1);

                ingredients[index] = ingredient;
            }
        }
        cursor.close();

        return ingredients;
    }

    public RecommendationItem[] getAllRecommendationHistoryItems() {
        Cursor cursor = database.query(databaseHelper.TABLE_RECOMMENDATION_HISTORY, recommendationHistoryColumns,
                null, null, null, null, null);
        System.out.println("Returned " + cursor.getCount() + " rows");
        RecommendationItem[] recommendationItems = new RecommendationItem[cursor.getCount()];

        int index = 0;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                RecommendationItem recommendationItem = new RecommendationItem();

                int indexId = cursor.getColumnIndex(databaseHelper.RECOMMENDATION_HISTORY_ID);
                recommendationItem.setId(cursor.getLong(indexId));

                int indexDishId = cursor.getColumnIndex(databaseHelper.RECOMMENDATION_HISTORY_DISH_ID);
                recommendationItem.setDishId(cursor.getLong(indexDishId));

                int indexDishName = cursor.getColumnIndex(databaseHelper.RECOMMENDATION_HISTORY_DISH_NAME);
                recommendationItem.setDishName(cursor.getString(indexDishName));

                int indexIsTimestamp = cursor.getColumnIndex(databaseHelper.RECOMMENDATION_HISTORY_TIMESTAMP);
                recommendationItem.setTimestamp(cursor.getLong(indexIsTimestamp));

                recommendationItems[index] = recommendationItem;
                index++;
            }
        }
        cursor.close();

        return recommendationItems;
    }

    // TODO: create method getDish()
    public Dish getDish(long dishId) {
        String selection = databaseHelper.DISH_ID + " = ?";
        String[] selectionArgs = {String.valueOf(dishId)};

        Cursor cursor = database.query(databaseHelper.TABLE_INGREDIENT, ingredientColumns,
                selection, selectionArgs, null, null, null);
        // System.out.println("Returned " + cursor.getCount() + " rows");
        Dish dish = new Dish();

        if(cursor.getCount() == 1) {
            if (cursor.moveToFirst()) {
                int indexId = cursor.getColumnIndex(databaseHelper.DISH_ID);
                dish.setId(cursor.getLong(indexId));

                int indexName = cursor.getColumnIndex(databaseHelper.DISH_NAME);
                dish.setName(cursor.getString(indexName));

                int indexTimeToMake = cursor.getColumnIndex(databaseHelper.DISH_TIME_TO_MAKE_IN_MINUTES);
                dish.setTimeToMakeInMinutes(cursor.getDouble(indexTimeToMake));

                int indexIsConsidered = cursor.getColumnIndex(databaseHelper.DISH_IS_CONSIDERED);
                int isConsidered = cursor.getInt(indexIsConsidered);
                dish.setConsidered(isConsidered == 1);
            }
        }
        cursor.close();

        String selectionDishIngredient = databaseHelper.DISH_INGREDIENT_DISH_ID + " = ?";
        String[] selectionArgsDishIngredient = {String.valueOf(dishId)};
        Cursor cursorDishIngredient = database.query(databaseHelper.TABLE_DISH_INGREDIENT, dishIngredientColumns,
                selectionDishIngredient, selectionArgsDishIngredient, null, null, null);
        System.out.println("Returned " + cursorDishIngredient.getCount() + " rows");
        long[] ingredientIds = new long[cursorDishIngredient.getCount()];

        int index = 0;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int indexId = cursor.getColumnIndex(databaseHelper.DISH_INGREDIENT_INGREDIENT_ID);
                ingredientIds[index] = cursor.getLong(indexId);
                index++;
            }
        }
        cursorDishIngredient.close();

        Ingredient[] ingredients = new Ingredient[cursorDishIngredient.getCount()];
        int indexIngredient = 0;
        for (long ingredientId : ingredientIds) {
            ingredients[indexIngredient] = getIngredient(ingredientId);
        }
        dish.setIngredients(ingredients);

        return dish;
    }

    public Ingredient getIngredient(long ingredientId) {
        String selection = databaseHelper.INGREDIENT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(ingredientId)};
        Cursor cursor = database.query(databaseHelper.TABLE_INGREDIENT, dishIngredientColumns,
                selection, selectionArgs, null, null, null);
        // System.out.println("Returned " + cursorIngredient.getCount() + " rows");
        Ingredient ingredient = new Ingredient();

        if(cursor.getCount() == 1) {
            if (cursor.moveToFirst()) {
                int indexId = cursor.getColumnIndex(databaseHelper.INGREDIENT_ID);
                ingredient.setId(cursor.getLong(indexId));

                int indexName = cursor.getColumnIndex(databaseHelper.INGREDIENT_NAME);
                ingredient.setName(cursor.getString(indexName));

                int indexAmount = cursor.getColumnIndex(databaseHelper.INGREDIENT_AMOUNT);
                ingredient.setAmount(cursor.getInt(indexAmount));

                int indexIsAvailable = cursor.getColumnIndex(databaseHelper.INGREDIENT_IS_AVAILABLE);
                int isAvailableValue = cursor.getInt(indexIsAvailable);
                if (isAvailableValue == 1) {
                    ingredient.setAvailable(true);
                } else if (isAvailableValue == 0) {
                    ingredient.setAvailable(false);
                } else {
                    ingredient.setAvailable(null);
                }

                int indexBelongsToDish = cursor.getColumnIndex(databaseHelper.INGREDIENT_BELONGS_TO_DISH);
                int belongsToDishValue = cursor.getInt(indexBelongsToDish);
                ingredient.setBelongsToDish(belongsToDishValue == 1);
            }
        }
        cursor.close();

        return ingredient;
    }

    public Dish createDish(Dish dish) {
        ContentValues values = new ContentValues();
        values.put(databaseHelper.DISH_NAME, dish.getName());
        values.put(databaseHelper.DISH_TIME_TO_MAKE_IN_MINUTES, dish.getTimeToMakeInMinutes());
        values.put(databaseHelper.DISH_IS_CONSIDERED, dish.isConsidered());
        long insertedId = database.insert(databaseHelper.TABLE_DISH, null, values);
        dish.setId(insertedId);

        long dishId = dish.getId();
        for (Ingredient ingredient : dish.getIngredients()) {
            createIngredient(ingredient);
            createDishIngredient(dishId, ingredient.getId());
        }

        return dish;
    }

    public Ingredient createIngredient(Ingredient ingredient) {
        ContentValues values = new ContentValues();
        values.put(databaseHelper.INGREDIENT_NAME, ingredient.getName());
        values.put(databaseHelper.INGREDIENT_AMOUNT, ingredient.getAmount());
        values.put(databaseHelper.INGREDIENT_IS_AVAILABLE, ingredient.isAvailable());
        values.put(databaseHelper.INGREDIENT_BELONGS_TO_DISH, ingredient.belongsToDish());
        long insertedId = database.insert(databaseHelper.TABLE_INGREDIENT, null, values);
        ingredient.setId(insertedId);
        return ingredient;
    }

    public long createDishIngredient(long dishId, long ingredientId) {
        ContentValues values = new ContentValues();
        values.put(databaseHelper.DISH_INGREDIENT_DISH_ID, dishId);
        values.put(databaseHelper.DISH_INGREDIENT_INGREDIENT_ID, ingredientId);
        return database.insert(databaseHelper.TABLE_DISH_INGREDIENT, null, values);
    }

    public RecommendationItem createRecommendationItem(RecommendationItem recommendationItem) {
        ContentValues values = new ContentValues();
        values.put(databaseHelper.RECOMMENDATION_HISTORY_DISH_ID, recommendationItem.getId());
        values.put(databaseHelper.RECOMMENDATION_HISTORY_TIMESTAMP, new Date().getTime() / 1000L);
        long insertedId = database.insert(databaseHelper.TABLE_RECOMMENDATION_HISTORY, null, values);
        recommendationItem.setId(insertedId);
        return recommendationItem;
    }

    public Dish updateDish(Dish dish) {
        long dishId = dish.getId();

        ContentValues values = new ContentValues();
        values.put(databaseHelper.DISH_NAME, dish.getName());
        values.put(databaseHelper.DISH_TIME_TO_MAKE_IN_MINUTES, dish.getTimeToMakeInMinutes());
        values.put(databaseHelper.DISH_IS_CONSIDERED, dish.isConsidered());

        String selection = databaseHelper.DISH_ID + " = ?";
        String[] selectionArgs = { String.valueOf(dishId) };
        database.update(databaseHelper.TABLE_DISH, values, selection, selectionArgs);

        String selectionDishIngredient = databaseHelper.DISH_INGREDIENT_DISH_ID + " = ?";
        String[] selectionArgsDishIngredient = {String.valueOf(dishId)};
        Cursor cursor = database.query(databaseHelper.TABLE_DISH_INGREDIENT, dishIngredientColumns,
                selectionDishIngredient, selectionArgsDishIngredient, null, null, null);
        System.out.println("Returned " + cursor.getCount() + " rows");
        long[] ingredientIds = new long[cursor.getCount()];

        int index = 0;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int indexId = cursor.getColumnIndex(databaseHelper.DISH_INGREDIENT_INGREDIENT_ID);
                ingredientIds[index] = cursor.getLong(indexId);
                index++;
            }
        }
        cursor.close();

        database.delete(databaseHelper.TABLE_DISH_INGREDIENT, databaseHelper.DISH_INGREDIENT_DISH_ID + " = ?", new String[]{String.valueOf(dishId)});

        for (long ingredientId : ingredientIds) {
            deleteIngredient(ingredientId);
        }

        for (Ingredient ingredient : dish.getIngredients()) {
            createIngredient(ingredient);
        }

        return dish;
    }

    public Ingredient updateIngredient(Ingredient ingredient) {
        long ingredientId = ingredient.getId();

        ContentValues values = new ContentValues();
        values.put(databaseHelper.INGREDIENT_NAME, ingredient.getName());
        values.put(databaseHelper.INGREDIENT_AMOUNT, ingredient.getAmount());
        values.put(databaseHelper.INGREDIENT_IS_AVAILABLE, ingredient.isAvailable());
        values.put(databaseHelper.INGREDIENT_BELONGS_TO_DISH, ingredient.belongsToDish());

        String selection = databaseHelper.INGREDIENT_ID + " = ?";
        String[] selectionArgs = { String.valueOf(ingredientId) };

        database.update(databaseHelper.TABLE_INGREDIENT, values, selection, selectionArgs);
        return ingredient;
    }

    public void deleteDish(long dishId) {
        database.delete(databaseHelper.TABLE_DISH, databaseHelper.DISH_ID + " = ?", new String[]{String.valueOf(dishId)});

        String selectionDishIngredient = databaseHelper.DISH_INGREDIENT_DISH_ID + " = ?";
        String[] selectionArgsDishIngredient = {String.valueOf(dishId)};
        Cursor cursor = database.query(databaseHelper.TABLE_DISH_INGREDIENT, dishIngredientColumns,
                selectionDishIngredient, selectionArgsDishIngredient, null, null, null);
        System.out.println("Returned " + cursor.getCount() + " rows");
        long[] ingredientIds = new long[cursor.getCount()];

        int index = 0;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int indexId = cursor.getColumnIndex(databaseHelper.DISH_INGREDIENT_INGREDIENT_ID);
                ingredientIds[index] = cursor.getLong(indexId);
                index++;
            }
        }
        cursor.close();

        database.delete(databaseHelper.TABLE_DISH_INGREDIENT, databaseHelper.DISH_INGREDIENT_DISH_ID + " = ?", new String[]{String.valueOf(dishId)});

        for (long ingredientId : ingredientIds) {
            deleteIngredient(ingredientId);
        }
    }

    public void deleteIngredient(long ingredientId) {
        database.delete(databaseHelper.TABLE_INGREDIENT, databaseHelper.INGREDIENT_ID + " = ?", new String[]{String.valueOf(ingredientId)});
    }

    public void deleteRecommendationItem(long recommendationItemId) {
        database.delete(databaseHelper.TABLE_RECOMMENDATION_HISTORY, databaseHelper.RECOMMENDATION_HISTORY_ID + " = ?", new String[]{String.valueOf(recommendationItemId)});
    }
}
