package android.meal_chooser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.Objects;

public class DishActivity extends AppCompatActivity {
    private LinearLayout mIngredientListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mIngredientListView = findViewById(R.id.ingredient_list_view);
        // LinearLayout item1 = new LinearLayout(getApplicationContext()); // R.layout.dish_ingredient_item
        // item1.setLayout();
        // LinearLayout item1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dish_ingredient_item);
        // mIngredientListView.addView(item1);
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