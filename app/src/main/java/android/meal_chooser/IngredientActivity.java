package android.meal_chooser;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import java.util.Objects;

public class IngredientActivity extends AppCompatActivity {
    private static Ingredient ingredient;

    /**
     * Key for sharing ingredient for edit.
     */
    private static final String INGREDIENT = "ingredient";
    private EditText mInputName;
    private EditText mInputAmount;
    private CheckBox mInputIsAvailable;
    private Button mButtonSave;

    public MealChooserDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ingredient = (Ingredient) getIntent().getSerializableExtra(INGREDIENT);
        datasource = new MealChooserDataSource(this);

        mInputName = findViewById(R.id.input_name);
        mInputAmount = findViewById(R.id.input_amount);
        mInputIsAvailable = findViewById(R.id.input_is_available);

        if (ingredient.getId() > 0) {
            mInputName.setText(ingredient.getName());
            mInputAmount.setText(String.valueOf(ingredient.getAmount()));
            mInputIsAvailable.setChecked(ingredient.isAvailable());
        }

        mButtonSave = findViewById(R.id.button_save);
        mButtonSave.setOnClickListener(v -> {
            System.out.println("Save clicked!");
            Ingredient changedIngredient = new Ingredient();
            changedIngredient.setId(ingredient.getId());
            changedIngredient.setName(String.valueOf(mInputName.getText()));

            try {
                changedIngredient.setAmount(Integer.parseInt(String.valueOf(mInputAmount.getText())));
            } catch (Exception e) {
                e.printStackTrace();
                changedIngredient.setAmount(0);
            }
            changedIngredient.setAvailable(mInputIsAvailable.isChecked());
            changedIngredient.setBelongsToDish(false);

            datasource.open();
            if (ingredient.getId() > 0) {
                datasource.updateIngredient(changedIngredient);
            } else {
                datasource.createIngredient(changedIngredient);
            }
            datasource.close();

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
     * Defines what happens when back button is clicked.
     */
    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}