package android.meal_chooser.fragments;

import android.content.Intent;
import android.meal_chooser.activities.IngredientActivity;
import android.meal_chooser.MainActivity;
import android.meal_chooser.R;
import android.meal_chooser.models.Ingredient;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Fragment of the app which shows list of ingredients and implements functionalities related to
 * ingredients.
 *
 * A simple {@link Fragment} subclass.
 * Use the {@link IngredientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientsFragment extends Fragment {
    /**
     * Key for sharing list of ingredients.
     */
    private static final String INGREDIENTS = "ingredients";

    /**
     * Storage for list of ingredients.
     */
    private Ingredient[] items;

    /**
     * Key for sharing ingredient for edit.
     */
    private static final String INGREDIENT = "ingredient";

    /**
     * Request code for starting ingredient activity.
     */
    private static final int INGREDIENT_ACTIVITY_REQUEST_CODE = 2;

    /**
     * Reference of button for adding new ingredient.
     */
    private ImageButton mButtonAdd;

    /**
     * Adapter for list of ingredients.
     */
    private CustomAdapter itemsAdapter;

    /**
     * Container which is a ListView and contains ingredient items.
     */
    private ListView mListContainer;

    /**
     * Required empty public constructor.
     */
    public IngredientsFragment() {}

    /**
     * Creates new instance of the fragment with added arguments.
     *
     * @param ingredients Existing ingredient objects.
     * @return Instance of the ingredients fragment.
     */
    public static IngredientsFragment newInstance(Ingredient[] ingredients) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putSerializable(INGREDIENTS, ingredients);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Runs when fragment is created. Retrieves and saves arguments.
     *
     * @param savedInstanceState Data used before in activity.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            items = (Ingredient[]) getArguments().getSerializable(INGREDIENTS);
        }
    }

    /**
     * Creates view of the fragment. Adds functionality to view elements.
     *
     * @param inflater Object for building layout objects.
     * @param container Parent of the view which is to be created.
     * @param savedInstanceState Data used before in activity.
     * @return Created view of the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);

        // parent activity reference for further use
        MainActivity thisActivity = (MainActivity) Objects.requireNonNull(getActivity());

        // create data for adapter from fragment argument
        List<HashMap<String, String>> ingredientListItems = new ArrayList<>();
        for (Ingredient item : items) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("ingredientId", String.valueOf(item.getId()));
            hashMap.put("ingredientAmount", String.valueOf(item.getAmount()));
            hashMap.put("ingredientName", item.getName());
            hashMap.put("ingredientIsAvailable", String.valueOf(item.isAvailable()));
            ingredientListItems.add(hashMap);
        }

        // define adapter with custom mapping
        String[] from = {"ingredientAmount", "ingredientName", "ingredientIsAvailable"};
        int[] to = {R.id.item_amount, R.id.item_name, R.id.item_is_available};
        itemsAdapter = new CustomAdapter(getActivity(), ingredientListItems,
                R.layout.ingredient_list_item, from, to);

        // change listener for checkboxes of items in list view
        itemsAdapter.setCheckBoxChangeListener((position, isChecked) -> {
            // update the database and data storage with the new checkbox state
            items[position].setAvailable(isChecked);
            thisActivity.ingredients[position].setAvailable(isChecked);
            thisActivity.datasource.updateIngredient(thisActivity.ingredients[position]);
        });

        // long click listener for items in list view
        itemsAdapter.setOnItemLongClickListener((parent, view1, position, id) -> {
            // create and inflate the popup using xml file
            PopupMenu popup = new PopupMenu(thisActivity, view1);
            popup.getMenuInflater()
                    .inflate(R.menu.menu_popup, popup.getMenu());

            // click listener for items of the menu
            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.delete) {
                    // delete dish when delete popup item is clicked

                    // update the database and data storage
                    thisActivity.datasource
                            .deleteIngredient(thisActivity.ingredients[position].getId());
                    Ingredient[] ingredients = thisActivity.datasource.getAllIngredients();
                    items = ingredients;
                    thisActivity.setIngredients(ingredients);

                    // create data for adapter from fragment argument
                    List<HashMap<String, String>> newIngredientListItems = new ArrayList<>();
                    for (Ingredient listItem : items) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("ingredientId", String.valueOf(listItem.getId()));
                        hashMap.put("ingredientAmount", String.valueOf(listItem.getAmount()));
                        hashMap.put("ingredientName", listItem.getName());
                        hashMap.put("ingredientIsAvailable",
                                String.valueOf(listItem.isAvailable()));
                        newIngredientListItems.add(hashMap);
                    }

                    // update fragment list view
                    itemsAdapter.updateDataList(newIngredientListItems);
                } else if (itemId == R.id.edit) {
                    // start new activity for editing ingredient when edit popup item is clicked
                    Intent intent = new Intent(thisActivity, IngredientActivity.class);
                    intent.putExtra(INGREDIENT, items[position]);
                    thisActivity.startActivityForResult(intent, INGREDIENT_ACTIVITY_REQUEST_CODE);
                }
                return false;
            });

            popup.show();
            return true;
        });

        // reference list view and set adapter to list container
        mListContainer = view.findViewById(R.id.list_container);
        mListContainer.setAdapter(itemsAdapter);

        // button for adding new ingredient
        mButtonAdd = view.findViewById(R.id.button_add);
        mButtonAdd.setOnClickListener(v -> {
            // start new activity for adding new ingredient
            Intent intent = new Intent(thisActivity, IngredientActivity.class);
            intent.putExtra(INGREDIENT, new Ingredient());
            thisActivity.startActivityForResult(intent, INGREDIENT_ACTIVITY_REQUEST_CODE);
        });

        return view;
    }

    /**
     * Updates list in the fragment view. Used for updating the list view from different classes.
     *
     * @param ingredients New updated data which the list view is to be updated with.
     */
    public void updateList(Ingredient[] ingredients) {
        // create data for adapter from fragment argument
        List<HashMap<String, String>> ingredientListItems = new ArrayList<>();
        for (Ingredient listItem : ingredients) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("ingredientId", String.valueOf(listItem.getId()));
            hashMap.put("ingredientAmount", String.valueOf(listItem.getAmount()));
            hashMap.put("ingredientName", listItem.getName());
            hashMap.put("ingredientIsAvailable", String.valueOf(listItem.isAvailable()));
            ingredientListItems.add(hashMap);
        }

        // update data storage and list view
        items = ingredients;
        itemsAdapter.updateDataList(ingredientListItems);
    }
}