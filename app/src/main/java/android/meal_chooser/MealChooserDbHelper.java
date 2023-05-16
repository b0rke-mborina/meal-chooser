package android.meal_chooser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class MealChooserDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "meal_chooser_database.db";
    private static final int DATABASE_VERSION = 1;

    final String TABLE_DISH = "dish";
    final String DISH_ID = "id";
    final String DISH_NAME = "name";
    final String DISH_TIME_TO_MAKE_IN_MINUTES = "time_to_make_in_minutes";
    final String DISH_IS_CONSIDERED = "is_considered";

    final String TABLE_INGREDIENT = "ingredient";
    final String INGREDIENT_ID = "id";
    final String INGREDIENT_NAME = "name";
    final String INGREDIENT_AMOUNT = "amount";
    final String INGREDIENT_IS_AVAILABLE = "is_available";
    final String INGREDIENT_BELONGS_TO_DISH = "belongs_to_dish";

    final String TABLE_DISH_INGREDIENT = "dish_ingredient";
    final String DISH_INGREDIENT_ID = "id";
    final String DISH_INGREDIENT_DISH_ID = "dish_id";
    final String DISH_INGREDIENT_INGREDIENT_ID = "ingredient_id";

    final String TABLE_RECOMMENDATION_HISTORY = "recommendation_history";
    final String RECOMMENDATION_HISTORY_ID = "id";
    final String RECOMMENDATION_HISTORY_DISH_ID = "dish_id";
    final String RECOMMENDATION_HISTORY_DISH_NAME = "dish_name";
    final String RECOMMENDATION_HISTORY_TIMESTAMP = "timestamp";

    public MealChooserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public MealChooserDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the "Dish" table
        String createDishTableQuery = "CREATE TABLE " + TABLE_DISH + "("
                + DISH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DISH_NAME + " TEXT,"
                + DISH_TIME_TO_MAKE_IN_MINUTES + " DOUBLE,"
                + DISH_IS_CONSIDERED + " INTEGER"
                + ")";
        db.execSQL(createDishTableQuery);

        // Create the "Ingredient" table
        String createIngredientTableQuery = "CREATE TABLE " + TABLE_INGREDIENT + "("
                + INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + INGREDIENT_NAME + " TEXT,"
                + INGREDIENT_AMOUNT + " TEXT,"
                + INGREDIENT_IS_AVAILABLE + " INTEGER,"
                + INGREDIENT_BELONGS_TO_DISH + " INTEGER"
                + ")";
        db.execSQL(createIngredientTableQuery);

        // Create the "IngredientDish" table
        String createIngredientDishTableQuery = "CREATE TABLE " + TABLE_DISH_INGREDIENT + "("
                + DISH_INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DISH_INGREDIENT_DISH_ID + " INTEGER,"
                + RECOMMENDATION_HISTORY_DISH_NAME + " TEXT,"
                + DISH_INGREDIENT_INGREDIENT_ID + " INTEGER,"
                + "FOREIGN KEY(" + DISH_INGREDIENT_DISH_ID + ") REFERENCES " + TABLE_DISH + "(" + DISH_ID + "),"
                + "FOREIGN KEY(" + DISH_INGREDIENT_INGREDIENT_ID + ") REFERENCES " + TABLE_INGREDIENT + "(" + INGREDIENT_ID + ")"
                + ")";
        db.execSQL(createIngredientDishTableQuery);

        // Create the "RecommendationHistory" table
        String createRecommendationHistoryTableQuery = "CREATE TABLE " + TABLE_RECOMMENDATION_HISTORY + "("
                + RECOMMENDATION_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RECOMMENDATION_HISTORY_DISH_ID + " INTEGER,"
                + RECOMMENDATION_HISTORY_TIMESTAMP + " INTEGER,"
                + "FOREIGN KEY(" + RECOMMENDATION_HISTORY_DISH_ID + ") REFERENCES " + TABLE_DISH + "(" + DISH_ID + ")"
                + ")";
        db.execSQL(createRecommendationHistoryTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop the old tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISH_INGREDIENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECOMMENDATION_HISTORY);

        // create a new one
        onCreate(db);
    }
}
