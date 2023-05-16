package android.meal_chooser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import java.util.Objects;

public class IngredientActivity extends AppCompatActivity {
    private EditText mInputName;
    private EditText mInputAmount;
    private CheckBox mInputIsAvailable;
    private Button mButtonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mInputName = findViewById(R.id.input_name);
        mInputAmount = findViewById(R.id.input_amount);

        mInputIsAvailable = findViewById(R.id.input_is_available);
        mInputIsAvailable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            System.out.println("Is available: " + mInputIsAvailable.isChecked());
        });

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