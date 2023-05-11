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
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DishesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DishesFragment extends Fragment {
    /**
     * Key for sharing list of dishes.
     */
    private static final String DISHES = "dishes";

    private Dish[] items;

    private ImageButton mButtonAdd;

    /**
     * Adapter for list of dishes
     */
    private SimpleAdapter itemsAdapter;

    /**
     * Container which is a ListView and contains dish items.
     */
    private ListView mListContainer;

    public DishesFragment() {
        // Required empty public constructor
    }

    /**
     *
     *
     * @param dishes Existing dish objects.
     * @return Instance of the fragment.
     */
    public static DishesFragment newInstance(Dish[] dishes) {
        DishesFragment fragment = new DishesFragment();
        Bundle args = new Bundle();
        args.putSerializable(DISHES, dishes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            items = (Dish[]) getArguments().getSerializable(DISHES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dishes, container, false);

        // create data for adapter from fragment argument
        List<HashMap<String, String>> dishListItems = new ArrayList<>();
        for (Dish item : items) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("dishTime", item.getTimeToMakeInMinutes() + " min");
            hashMap.put("dishName", item.getName());
            dishListItems.add(hashMap);
        }

        // define adapter with custom mapping
        String[] from = {"dishTime", "dishName"};
        int[] to = {R.id.item_time, R.id.item_text};
        itemsAdapter = new SimpleAdapter(getActivity(), dishListItems,
                R.layout.dish_list_item, from, to);

        // referencing, setting adapter and adding listeners to items of list container
        mListContainer = view.findViewById(R.id.list_container);
        mListContainer.setAdapter(itemsAdapter);

        mListContainer.setOnItemLongClickListener((parent, v, position, id) -> {
            System.out.println("Hold");
            MainActivity thisActivity = (MainActivity) Objects.requireNonNull(getActivity());
            PopupMenu popup = new PopupMenu(thisActivity, v);
            // inflate the popup using xml file
            popup.getMenuInflater()
                    .inflate(R.menu.menu_popup, popup.getMenu());
            popup.show();
            return true;
        });

        // button for adding add new ingredient
        mButtonAdd = view.findViewById(R.id.button_add);
        mButtonAdd.setOnClickListener(v -> {
            System.out.println("Clicked!");
            // start new activity which shows result
            MainActivity thisActivity = (MainActivity) Objects.requireNonNull(getActivity());
            Intent intent = new Intent(thisActivity.getApplicationContext(), DishActivity.class);
            // intent.putExtra(RESULT, result);
            startActivity(intent);
        });

        return view;
    }
}