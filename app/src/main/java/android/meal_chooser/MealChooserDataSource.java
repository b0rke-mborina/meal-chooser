package android.meal_chooser;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MealChooserDataSource {
    MealChooserDbHelper databaseHelper;
    SQLiteDatabase database;

    private final String[] dishColumns = {
            databaseHelper.DISH_ID,
            databaseHelper.DISH_NAME,
            databaseHelper.DISH_TIME_TO_MAKE
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

                int indexTimeToMake = cursor.getColumnIndex(databaseHelper.DISH_TIME_TO_MAKE);
                dish.setTimeToMakeInMinutes(cursor.getDouble(indexTimeToMake));

                dishes[index] = dish;
            }
        }

        return dishes;
    }

    public Dish createDish(Dish dish) {
        ContentValues values = new ContentValues();
        values.put(databaseHelper.DISH_NAME, dish.getName());
        values.put(databaseHelper.DISH_TIME_TO_MAKE, dish.getTimeToMakeInMinutes());
        long insertedId = database.insert(databaseHelper.TABLE_DISH, null, values);
        dish.setId(insertedId);
        return dish;
    }

    public Dish updateDish(Dish dish) {
        long dishId = dish.getId();

        ContentValues values = new ContentValues();
        values.put(databaseHelper.DISH_NAME, dish.getName());
        values.put(databaseHelper.DISH_TIME_TO_MAKE, dish.getTimeToMakeInMinutes());

        String selection = databaseHelper.DISH_ID + " = ?";
        String[] selectionArgs = { String.valueOf(dishId) };

        database.update(databaseHelper.TABLE_DISH, values, selection, selectionArgs);
        return dish;
    }

    public void deleteDish(int dishId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(databaseHelper.TABLE_DISH, databaseHelper.DISH_ID + " = ?", new String[]{String.valueOf(dishId)});
    }
}
