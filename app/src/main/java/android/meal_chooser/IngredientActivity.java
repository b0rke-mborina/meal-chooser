package android.meal_chooser;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import java.util.Objects;

/**
 * Used for updating dish and creating new dish.
 */
public class IngredientActivity extends AppCompatActivity {
    /**
     * Key for sharing ingredient for edit.
     */
    private static final String INGREDIENT = "ingredient";

    /**
     * Ingredient for edit. It's empty if new ingredient is being created.
     */
    private static Ingredient ingredient;

    /**
     * Ingredient name input reference.
     */
    private EditText mInputName;

    /**
     * Ingredient amount input reference.
     */
    private EditText mInputAmount;

    /**
     * Ingredient is available checkbox reference.
     */
    private CheckBox mInputIsAvailable;

    /**
     * Reference of button for saving changes to the ingredient.
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
        setContentView(R.layout.activity_ingredient);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // ingredient retrieval and data source object initialization
        ingredient = (Ingredient) getIntent().getSerializableExtra(INGREDIENT);
        datasource = new MealChooserDataSource(this);

        // reference inputs
        mInputName = findViewById(R.id.input_name);
        mInputAmount = findViewById(R.id.input_amount);
        mInputIsAvailable = findViewById(R.id.input_is_available);

        // set values to references view elements if ingredient is not empty
        if (ingredient.getId() > 0) {
            mInputName.setText(ingredient.getName());
            mInputAmount.setText(String.valueOf(ingredient.getAmount()));
            mInputIsAvailable.setChecked(ingredient.isAvailable());
        }

        // button for saving changes to ingredient or creating a new ingredient
        mButtonSave = findViewById(R.id.button_save);
        mButtonSave.setOnClickListener(v -> {
            // create new ingredient and add data to it
            // (id, name, amount, is available, belongs to dish)
            Ingredient changedIngredient = new Ingredient();
            changedIngredient.setId(ingredient.getId());
            changedIngredient.setName(String.valueOf(mInputName.getText()));

            try {
                changedIngredient.setAmount(
                        Integer.parseInt(String.valueOf(mInputAmount.getText()))
                );
            } catch (Exception e) {
                e.printStackTrace();
                changedIngredient.setAmount(0);
            }

            changedIngredient.setAvailable(mInputIsAvailable.isChecked());
            changedIngredient.setBelongsToDish(false);

            // update it or create new based on whether it's id empty
            datasource.open();
            if (ingredient.getId() > 0) {
                datasource.updateIngredient(changedIngredient);
            } else {
                datasource.createIngredient(changedIngredient);
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