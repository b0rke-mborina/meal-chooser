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
        int[] to = {R.id.item_amount, R.id.item_text, R.id.item_is_available};


        itemsAdapter = new CustomAdapter(getActivity(), ingredientListItems, from, to);
        itemsAdapter.setOnItemLongClickListener((parent, view1, position, id) -> {
            System.out.println("Holded " + items[position].getId());
            MainActivity thisActivity = (MainActivity) Objects.requireNonNull(getActivity());
            PopupMenu popup = new PopupMenu(thisActivity, view1);
            // inflate the popup using xml file
            popup.getMenuInflater()
                    .inflate(R.menu.menu_popup, popup.getMenu());
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
            MainActivity thisActivity = (MainActivity) Objects.requireNonNull(getActivity());
            Intent intent = new Intent(thisActivity, IngredientActivity.class);
            // intent.putExtra(RESULT, result);
            startActivity(intent);
        });

        return view;
    }
}