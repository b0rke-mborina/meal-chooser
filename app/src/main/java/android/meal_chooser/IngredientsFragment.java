package android.meal_chooser;

import android.content.Intent;
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
 * A simple {@link Fragment} subclass.
 * Use the {@link IngredientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientsFragment extends Fragment {
    /**
     * Key for sharing list of ingredients.
     */
    private static final String INGREDIENTS = "ingredients";

    private Ingredient[] items;

    /**
     * Key for sharing ingredient for edit.
     */
    private static final String INGREDIENT = "ingredient";

    private ImageButton mButtonAdd;

    /**
     * Adapter for list of ingredients
     */
    private CustomAdapter itemsAdapter;

    /**
     * Container which is a ListView and contains ingredient items.
     */
    private ListView mListContainer;

    public IngredientsFragment() {
        // Required empty public constructor
    }

    /**
     *
     *
     * @param ingredients Existing ingredient objects.
     * @return Instance of the fragment.
     */
    public static IngredientsFragment newInstance(Ingredient[] ingredients) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putSerializable(INGREDIENTS, ingredients);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            items = (Ingredient[]) getArguments().getSerializable(INGREDIENTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);

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
        itemsAdapter.setCheckBoxChangeListener((position, isChecked) -> {
            // Update the database item with the new checkbox state
            items[position].setAvailable(isChecked);
            thisActivity.ingredients[position].setAvailable(isChecked);
            thisActivity.datasource.updateIngredient(thisActivity.ingredients[position]);
        });
        itemsAdapter.setOnItemLongClickListener((parent, view1, position, id) -> {
            System.out.println("Holded " + items[position].getId());
            PopupMenu popup = new PopupMenu(thisActivity, view1);
            // inflate the popup using xml file
            popup.getMenuInflater()
                    .inflate(R.menu.menu_popup, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.delete) {
                    System.out.println("CHOSEN: DELETE");
                    thisActivity.datasource.deleteIngredient(thisActivity.ingredients[position].getId());
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
                        hashMap.put("ingredientIsAvailable", String.valueOf(listItem.isAvailable()));
                        newIngredientListItems.add(hashMap);
                    }

                    itemsAdapter.updateDataList(newIngredientListItems);
                } else if (itemId == R.id.edit) {
                    System.out.println("CHOSEN: EDIT");
                    Intent intent = new Intent(thisActivity, IngredientActivity.class);
                    intent.putExtra(INGREDIENT, items[position]);
                    startActivity(intent);
                }
                return false;
            });

            popup.show();
            return true;
        });

        // referencing, setting adapter and adding listeners to items of list container
        mListContainer = view.findViewById(R.id.list_container);
        mListContainer.setAdapter(itemsAdapter);

        // button for adding add new ingredient
        mButtonAdd = view.findViewById(R.id.button_add);
        mButtonAdd.setOnClickListener(v -> {
            System.out.println("Clicked!");
            // start new activity which shows result
            Intent intent = new Intent(thisActivity, IngredientActivity.class);
            // intent.putExtra(RESULT, result);
            startActivity(intent);
        });

        return view;
    }
}