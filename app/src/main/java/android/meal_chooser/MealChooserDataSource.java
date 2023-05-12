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
    private final String[] ingredientDishColumns = {
            databaseHelper.INGREDIENT_DISH_ID,
            databaseHelper.INGREDIENT_DISH_DISH_ID,
            databaseHelper.INGREDIENT_DISH_INGREDIENT_ID
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
                dish.setId(cursor.getInt(indexId));

                int indexName = cursor.getColumnIndex(databaseHelper.DISH_NAME);
                dish.setName(cursor.getString(indexName));

                int indexTimeToMake = cursor.getColumnIndex(databaseHelper.DISH_TIME_TO_MAKE_IN_MINUTES);
                dish.setTimeToMakeInMinutes(cursor.getDouble(indexTimeToMake));

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
                ingredient.setId(cursor.getInt(indexId));

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

    public Dish createDish(Dish dish) {
        ContentValues values = new ContentValues();
        values.put(databaseHelper.DISH_NAME, dish.getName());
        values.put(databaseHelper.DISH_TIME_TO_MAKE_IN_MINUTES, dish.getTimeToMakeInMinutes());
        values.put(databaseHelper.DISH_IS_CONSIDERED, dish.isConsidered());
        long insertedId = database.insert(databaseHelper.TABLE_DISH, null, values);
        dish.setId(insertedId);
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

    public void deleteDish(int dishId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(databaseHelper.TABLE_DISH, databaseHelper.DISH_ID + " = ?", new String[]{String.valueOf(dishId)});
    }

    public void deleteIngredient(int ingredientId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(databaseHelper.TABLE_INGREDIENT, databaseHelper.INGREDIENT_ID + " = ?", new String[]{String.valueOf(ingredientId)});
    }

    public void deleteRecommendationItem(int recommendationItemId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(databaseHelper.TABLE_RECOMMENDATION_HISTORY, databaseHelper.RECOMMENDATION_HISTORY_ID + " = ?", new String[]{String.valueOf(recommendationItemId)});
    }
}
