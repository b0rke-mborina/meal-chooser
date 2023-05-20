package android.meal_chooser.activities;

import android.app.Activity;
import android.content.Intent;
import android.meal_chooser.R;
import android.meal_chooser.database.MealChooserDataSource;
import android.meal_chooser.models.Dish;
import android.meal_chooser.models.Ingredient;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import java.util.Objects;

/**
 * Used for updating dish and creating new dish.
 */
public class DishActivity extends AppCompatActivity {
    /**
     * Key for sharing dish for edit.
     */
    private static final String DISH = "dish";

    /**
     * Dish for edit. It's empty if new dish is being created.
     */
    private static Dish dish;

    /**
     * Dish name input reference.
     */
    private EditText mInputName;

    /**
     * Dish time to make in minutes input reference.
     */
    private EditText mInputTimeToMake;

    /**
     * Dish is considered value checkbox reference.
     */
    private CheckBox mInputIsConsidered;

    /**
     * Dish ingredient list view reference.
     */
    private LinearLayout mIngredientListView;

    /**
     * Reference of button for adding another ingredient to the dish ingredient list view.
     */
    private ImageButton mButtonAddIngredient;

    /**
     * Reference of button for saving changes to the dish.
     */
    private Button mButtonSave;

    /**
     * Data source object instance of this activity.
     */
    public MealChooserDataSource datasource;

    /**
     * Runs when activity is created. Displays view of the activity and implements activity
     * functionalities.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.
     *                           <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // dish retrieval and data source object initialization
        dish = (Dish) getIntent().getSerializableExtra(DISH);
        datasource = new MealChooserDataSource(this);

        // updates dish from database if dish is not empty for getting ingredients list
        if (dish.getId() > 0) {
            datasource.open();
            dish = datasource.getDish(dish.getId());
            datasource.close();
        }

        // reference inputs
        mInputName = findViewById(R.id.input_name);
        mInputTimeToMake = findViewById(R.id.input_time);
        mInputIsConsidered = findViewById(R.id.input_is_considered);
        mIngredientListView = findViewById(R.id.ingredient_list_view);

        // add content to view elements based on whether the dish is empty or not
        if (dish.getId() > 0) {
            // if dish is not empty

            // set values to inputs
            mInputName.setText(dish.getName());
            mInputTimeToMake.setText(String.valueOf(dish.getTimeToMakeInMinutes()));
            mInputIsConsidered.setChecked(dish.isConsidered());

            // add item views to list of ingredients, one item for each ingredient
            for (Ingredient ingredient : dish.getIngredients()) {
                // create ingredient item view with context
                LayoutInflater inflater = LayoutInflater.from(DishActivity.this);
                LinearLayout itemLayout = (LinearLayout) inflater
                        .inflate(R.layout.dish_ingredient_item, null);

                // add value to inputs
                EditText inputName = itemLayout.findViewById(R.id.input_name);
                inputName.setText(ingredient.getName());
                EditText inputAmount = itemLayout.findViewById(R.id.input_amount);
                inputAmount.setText(String.valueOf(ingredient.getAmount()));

                // button for removing ingredient item from the list
                ImageButton buttonRemove = itemLayout.findViewById(R.id.button_remove_ingredient);
                buttonRemove.setOnClickListener(v -> {
                    // use parent to remove it from the list if there is at least one other item
                    // remaining
                    if (mIngredientListView.getChildCount() > 1) {
                        ((LinearLayout) itemLayout.getParent()).removeView(itemLayout);
                    }
                });

                mIngredientListView.addView(itemLayout);
            }
        } else {
            // if dish is empty

            // create empty ingredient item view with context
            LayoutInflater inflater = LayoutInflater.from(DishActivity.this);
            LinearLayout emptyItem = (LinearLayout) inflater
                    .inflate(R.layout.dish_ingredient_item, null);

            // button for removing ingredient item from the list
            ImageButton buttonRemove = emptyItem.findViewById(R.id.button_remove_ingredient);
            buttonRemove.setOnClickListener(v -> {
                // use parent to remove it from the list if there is at least one other item
                // remaining
                if (mIngredientListView.getChildCount() > 1) {
                    ((LinearLayout) emptyItem.getParent()).removeView(emptyItem);
                }
            });

            mIngredientListView.addView(emptyItem);
        }

        // button for adding another ingredient item to the list of ingredient items
        mButtonAddIngredient = findViewById(R.id.button_add_ingredient);
        mButtonAddIngredient.setOnClickListener(v -> {
            // reference view elements
            v = mIngredientListView.getChildAt(mIngredientListView.getChildCount() - 1);
            EditText inputName = v.findViewById(R.id.input_name);
            EditText inputAmount = v.findViewById(R.id.input_amount);

            // add another ingredient item to the list of ingredient items only if last ingredient
            // is full
            if (!Objects.equals(String.valueOf(inputName.getText()), "")
                    && !Objects.equals(String.valueOf(inputAmount.getText()), "")) {
                // create empty ingredient item view with context
                LayoutInflater inflater = LayoutInflater.from(DishActivity.this);
                LinearLayout emptyItem = (LinearLayout) inflater
                        .inflate(R.layout.dish_ingredient_item, null);

                // button for removing ingredient item from the list
                ImageButton buttonRemove = emptyItem.findViewById(R.id.button_remove_ingredient);
                buttonRemove.setOnClickListener(view -> {
                    // use parent to remove it from the list if there is at least one other item
                    // remaining
                    if (mIngredientListView.getChildCount() > 1) {
                        ((LinearLayout) emptyItem.getParent()).removeView(emptyItem);
                    }
                });

                mIngredientListView.addView(emptyItem);
            }
        });

        // button for saving changes to dish or creating a new dish
        mButtonSave = findViewById(R.id.button_save);
        mButtonSave.setOnClickListener(v -> {
            // create new dish and add data to it (id, name, time to make, is considered)
            Dish changedDish = new Dish();
            changedDish.setId(dish.getId());
            changedDish.setName(String.valueOf(mInputName.getText()));

            try {
                changedDish.setTimeToMakeInMinutes(
                        Double.parseDouble(String.valueOf(mInputTimeToMake.getText()))
                );
            } catch (Exception e) {
                e.printStackTrace();
                changedDish.setTimeToMakeInMinutes(0);
            }

            changedDish.setConsidered(mInputIsConsidered.isChecked());

            // create array of ingredient objects based on data from item list and set it to dish
            int numberOfIngredients = mIngredientListView.getChildCount();
            Ingredient[] ingredientsFromList = new Ingredient[numberOfIngredients];
            for (int i = 0; i < numberOfIngredients; i++) {
                // reference view elements
                v = mIngredientListView.getChildAt(i);
                EditText inputName = v.findViewById(R.id.input_name);
                EditText inputAmount = v.findViewById(R.id.input_amount);

                // create new ingredient and add data to it
                // (id, name, amount, is available, belongs to dish)
                Ingredient ingredient = new Ingredient();
                ingredient.setId(0);
                ingredient.setName(String.valueOf(inputName.getText()));

                try {
                    ingredient.setAmount(Integer.parseInt(String.valueOf(inputAmount.getText())));
                } catch (Exception e) {
                    e.printStackTrace();
                    ingredient.setAmount(0);
                }

                ingredient.setAvailable(null);
                ingredient.setBelongsToDish(true);
                ingredientsFromList[i] = ingredient;
            }
            changedDish.setIngredients(ingredientsFromList);

            // update it or create new based on whether it's id empty
            datasource.open();
            if (dish.getId() > 0) {
                datasource.updateDish(changedDish);
            } else {
                datasource.createDish(changedDish);
            }
            datasource.close();

            // finish the activity
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        });
    }

    /**
     * Defines listener on back button in action bar.
     *
     * @return boolean True value.
     */
    @Override
    public boolean onSupportNavigateUp() {
        // handle back button (top right corner of the screen) press
        onBackPressed();
        return true;
    }

    /**
     * Defines what happens when back button is clicked. The activity finishes.
     */
    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}