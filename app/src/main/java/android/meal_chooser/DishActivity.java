package android.meal_chooser;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Objects;

public class DishActivity extends AppCompatActivity {
    private static Dish dish;

    /**
     * Key for sharing dish for edit.
     */
    private static final String DISH = "dish";
    private EditText mInputName;
    private EditText mInputTimeToMake;
    private CheckBox mInputIsConsidered;
    private LinearLayout mIngredientListView;
    private ImageButton mButtonAddIngredient;
    private Button mButtonSave;

    public MealChooserDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        dish = (Dish) getIntent().getSerializableExtra(DISH);
        datasource = new MealChooserDataSource(this);

        if (dish.getId() > 0) {
            datasource.open();
            dish = datasource.getDish(dish.getId());
            datasource.close();
        }

        mInputName = findViewById(R.id.input_name);
        mInputTimeToMake = findViewById(R.id.input_time);
        mInputIsConsidered = findViewById(R.id.input_is_considered);
        mIngredientListView = findViewById(R.id.ingredient_list_view);

        if (dish.getId() > 0) {
            mInputName.setText(dish.getName());
            mInputTimeToMake.setText(String.valueOf(dish.getTimeToMakeInMinutes()));
            mInputIsConsidered.setChecked(dish.isConsidered());

            for (Ingredient ingredient : dish.getIngredients()) {
                LayoutInflater inflater = LayoutInflater.from(DishActivity.this); // some context
                LinearLayout itemLayout = (LinearLayout) inflater.inflate(R.layout.dish_ingredient_item, null);

                EditText inputName = itemLayout.findViewById(R.id.input_name);
                inputName.setText(ingredient.getName());
                EditText inputAmount = itemLayout.findViewById(R.id.input_amount);
                inputAmount.setText(String.valueOf(ingredient.getAmount()));

                ImageButton buttonRemove = itemLayout.findViewById(R.id.button_remove_ingredient);
                buttonRemove.setOnClickListener(v -> {
                    if (mIngredientListView.getChildCount() > 1) {
                        ((LinearLayout) itemLayout.getParent()).removeView(itemLayout);
                    }
                });

                mIngredientListView.addView(itemLayout);
            }
        } else {
            LayoutInflater inflater = LayoutInflater.from(DishActivity.this); // some context
            LinearLayout emptyItem = (LinearLayout) inflater.inflate(R.layout.dish_ingredient_item, null);

            ImageButton buttonRemove = emptyItem.findViewById(R.id.button_remove_ingredient);
            buttonRemove.setOnClickListener(v -> {
                if (mIngredientListView.getChildCount() > 1) {
                    ((LinearLayout) emptyItem.getParent()).removeView(emptyItem);
                }
            });

            mIngredientListView.addView(emptyItem);
        }

        // mIsConsidered.setOnCheckedChangeListener((buttonView, isChecked) -> {
        //     System.out.println("Is considered: " + mIsConsidered.isChecked());
        // });

        mButtonAddIngredient = findViewById(R.id.button_add_ingredient);
        mButtonAddIngredient.setOnClickListener(v -> {
            v = mIngredientListView.getChildAt(mIngredientListView.getChildCount() - 1);
            EditText inputName = v.findViewById(R.id.input_name);
            EditText inputAmount = v.findViewById(R.id.input_amount);

            if (!Objects.equals(String.valueOf(inputName.getText()), "") && !Objects.equals(String.valueOf(inputAmount.getText()), "")) {
                LayoutInflater inflater = LayoutInflater.from(DishActivity.this);
                LinearLayout emptyItem = (LinearLayout) inflater.inflate(R.layout.dish_ingredient_item, null);

                ImageButton buttonRemove = emptyItem.findViewById(R.id.button_remove_ingredient);
                buttonRemove.setOnClickListener(view -> {
                    if (mIngredientListView.getChildCount() > 1) {
                        ((LinearLayout) emptyItem.getParent()).removeView(emptyItem);
                    }
                });

                mIngredientListView.addView(emptyItem);
            }
        });

        mButtonSave = findViewById(R.id.button_save);
        mButtonSave.setOnClickListener(v -> {
            System.out.println("Save clicked!");
            Dish changedDish = new Dish();
            changedDish.setId(dish.getId());
            changedDish.setName(String.valueOf(mInputName.getText()));

            try {
                changedDish.setTimeToMakeInMinutes(Double.parseDouble(String.valueOf(mInputTimeToMake.getText())));
            } catch (Exception e) {
                e.printStackTrace();
                changedDish.setTimeToMakeInMinutes(0);
            }

            changedDish.setConsidered(mInputIsConsidered.isChecked());

            int numberOfIngredients = mIngredientListView.getChildCount();
            Ingredient[] ingredientsFromList = new Ingredient[numberOfIngredients];
            for (int i = 0; i < numberOfIngredients; i++) {
                v = mIngredientListView.getChildAt(i);
                EditText inputName = v.findViewById(R.id.input_name);
                EditText inputAmount = v.findViewById(R.id.input_amount);

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

            System.out.println("OK");
            // System.out.println(dish.);

            datasource.open();
            if (dish.getId() > 0) {
                datasource.updateDish(changedDish);
            } else {
                datasource.createDish(changedDish);
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