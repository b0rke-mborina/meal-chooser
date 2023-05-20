package android.meal_chooser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

/**
 * Database helper class. It's a subclass of SQLiteOpenHelper. It is used to change the database.
 */
public class MealChooserDbHelper extends SQLiteOpenHelper {
    /**
     * Database file name.
     */
    private static final String DATABASE_NAME = "meal_chooser_database.db";

    /**
     * Current database version.
     */
    private static final int DATABASE_VERSION = 1;

    // table and column names

    public static final String TABLE_DISH = "dish";
    public static final String DISH_ID = "id";
    public static final String DISH_NAME = "name";
    public static final String DISH_TIME_TO_MAKE_IN_MINUTES = "time_to_make_in_minutes";
    public static final String DISH_IS_CONSIDERED = "is_considered";

    public static final String TABLE_INGREDIENT = "ingredient";
    public static final String INGREDIENT_ID = "id";
    public static final String INGREDIENT_NAME = "name";
    public static final String INGREDIENT_AMOUNT = "amount";
    public static final String INGREDIENT_IS_AVAILABLE = "is_available";
    public static final String INGREDIENT_BELONGS_TO_DISH = "belongs_to_dish";

    public static final String TABLE_DISH_INGREDIENT = "dish_ingredient";
    public static final String DISH_INGREDIENT_ID = "id";
    public static final String DISH_INGREDIENT_DISH_ID = "dish_id";
    public static final String DISH_INGREDIENT_INGREDIENT_ID = "ingredient_id";

    public static final String TABLE_RECOMMENDATION_HISTORY = "recommendation_history";
    public static final String RECOMMENDATION_HISTORY_ID = "id";
    public static final String RECOMMENDATION_HISTORY_DISH_ID = "dish_id";
    public static final String RECOMMENDATION_HISTORY_DISH_NAME = "dish_name";
    public static final String RECOMMENDATION_HISTORY_TIMESTAMP = "timestamp";

    /**
     * Parameterized constructor.
     *
     * @param context Current data.
     */
    public MealChooserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Parameterized constructor.
     *
     * @param context Current data.
     * @param name Name of the file new database is stored into.
     * @param factory Cursor behaviour of the new database.
     * @param version Version of the new database.
     */
    public MealChooserDbHelper(@Nullable Context context, @Nullable String name,
                               @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Creates the tables of the database.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create the "Dish" table
        String createDishTableQuery = "CREATE TABLE " + TABLE_DISH + "("
                + DISH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DISH_NAME + " TEXT,"
                + DISH_TIME_TO_MAKE_IN_MINUTES + " DOUBLE,"
                + DISH_IS_CONSIDERED + " INTEGER"
                + ")";
        db.execSQL(createDishTableQuery);

        // create the "Ingredient" table
        String createIngredientTableQuery = "CREATE TABLE " + TABLE_INGREDIENT + "("
                + INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + INGREDIENT_NAME + " TEXT,"
                + INGREDIENT_AMOUNT + " TEXT,"
                + INGREDIENT_IS_AVAILABLE + " INTEGER,"
                + INGREDIENT_BELONGS_TO_DISH + " INTEGER"
                + ")";
        db.execSQL(createIngredientTableQuery);

        // create the "IngredientDish" table
        String createIngredientDishTableQuery = "CREATE TABLE " + TABLE_DISH_INGREDIENT + "("
                + DISH_INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DISH_INGREDIENT_DISH_ID + " INTEGER,"
                + DISH_INGREDIENT_INGREDIENT_ID + " INTEGER,"
                + "FOREIGN KEY(" + DISH_INGREDIENT_DISH_ID + ") REFERENCES "
                    + TABLE_DISH + "(" + DISH_ID + "),"
                + "FOREIGN KEY(" + DISH_INGREDIENT_INGREDIENT_ID + ") REFERENCES "
                    + TABLE_INGREDIENT + "(" + INGREDIENT_ID + ")"
                + ")";
        db.execSQL(createIngredientDishTableQuery);

        // create the "RecommendationHistory" table
        String createRecommendationHistoryTableQuery = "CREATE TABLE "
                    + TABLE_RECOMMENDATION_HISTORY + "("
                + RECOMMENDATION_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RECOMMENDATION_HISTORY_DISH_ID + " INTEGER,"
                + RECOMMENDATION_HISTORY_DISH_NAME + " TEXT,"
                + RECOMMENDATION_HISTORY_TIMESTAMP + " INTEGER,"
                + "FOREIGN KEY(" + RECOMMENDATION_HISTORY_DISH_ID + ") REFERENCES "
                    + TABLE_DISH + "(" + DISH_ID + ")"
                + ")";
        db.execSQL(createRecommendationHistoryTableQuery);
    }

    /**
     * Is used to upgrade the database. Is run when the database version is changed to a larger
     * value.
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
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

    /**
     * Is used to downgrade the database. Is run when the database version is changed to a larger
     * value.
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop the old tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISH_INGREDIENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECOMMENDATION_HISTORY);
        db.setVersion(oldVersion);

        // create a new one
        onCreate(db);
    }
}
