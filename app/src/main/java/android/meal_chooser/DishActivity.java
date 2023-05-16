package android.meal_chooser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import java.util.Objects;

public class DishActivity extends AppCompatActivity {
    private EditText mInputName;
    private EditText mInputTimeToMake;
    private CheckBox mIsConsidered;
    private LinearLayout mIngredientListView;
    private Button mButtonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mInputName = findViewById(R.id.input_name);
        mInputTimeToMake = findViewById(R.id.input_time);

        mIsConsidered = findViewById(R.id.input_is_considered);
        mIsConsidered.setOnCheckedChangeListener((buttonView, isChecked) -> {
            System.out.println("Is considered: " + mIsConsidered.isChecked());
        });

        mIngredientListView = findViewById(R.id.ingredient_list_view);
        LayoutInflater inflater = LayoutInflater.from(DishActivity.this); // some context
        LinearLayout row = (LinearLayout) inflater.inflate(R.layout.dish_ingredient_item, null);
        mIngredientListView.addView(row);

        mButtonSave = findViewById(R.id.button_save);
        mButtonSave.setOnClickListener(v -> {
            System.out.println("Save clicked!");
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
        finish();
    }
}