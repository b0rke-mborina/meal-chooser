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
import java.util.Arrays;
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

    /**
     * Key for sharing ingredient for edit.
     */
    private static final String DISH = "dish";

    /**
     * Request code for starting dish activity.
     */
    private static final int DISH_ACTIVITY_REQUEST_CODE = 3;

    private ImageButton mButtonAdd;

    /**
     * Adapter for list of dishes
     */
    private CustomAdapter itemsAdapter;

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

        MainActivity thisActivity = (MainActivity) Objects.requireNonNull(getActivity());

        // create data for adapter from fragment argument
        List<HashMap<String, String>> dishListItems = new ArrayList<>();
        for (Dish item : items) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("dishId", String.valueOf(item.getId()));
            hashMap.put("dishTime", item.getTimeToMakeInMinutes() + " min");
            hashMap.put("dishName", item.getName());
            hashMap.put("dishIsConsidered", String.valueOf(item.isConsidered()));
            dishListItems.add(hashMap);
        }

        // define adapter with custom mapping
        String[] from = {"dishTime", "dishName", "dishIsConsidered"};
        int[] to = {R.id.item_time, R.id.item_name, R.id.item_is_considered};

        itemsAdapter = new CustomAdapter(getActivity(), dishListItems,
                R.layout.dish_list_item, from, to);
        itemsAdapter.setCheckBoxChangeListener((position, isChecked) -> {
            // Update the database item with the new checkbox state
            Dish dish = thisActivity.datasource.getDish(thisActivity.dishes[position].getId());
            dish.setConsidered(isChecked);
            items[position].setConsidered(isChecked);
            thisActivity.dishes[position].setConsidered(isChecked);
            thisActivity.datasource.updateDish(dish);
        });
        itemsAdapter.setOnItemLongClickListener((parent, v, position, id) -> {
            System.out.println("Hold");
            PopupMenu popup = new PopupMenu(thisActivity, v);
            // inflate the popup using xml file
            popup.getMenuInflater()
                    .inflate(R.menu.menu_popup, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.delete) {
                    System.out.println("CHOSEN: DELETE");
                    thisActivity.datasource.deleteDish(thisActivity.dishes[position].getId());
                    Dish[] dishes = thisActivity.datasource.getAllDishes();
                    items = dishes;
                    thisActivity.setDishes(dishes);

                    // create data for adapter from fragment argument
                    List<HashMap<String, String>> newDishListItems = new ArrayList<>();
                    for (Dish listItem : items) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("dishId", String.valueOf(listItem.getId()));
                        hashMap.put("dishTime", listItem.getTimeToMakeInMinutes() + " min");
                        hashMap.put("dishName", listItem.getName());
                        hashMap.put("dishIsConsidered", String.valueOf(listItem.isConsidered()));
                        newDishListItems.add(hashMap);
                    }

                    itemsAdapter.updateDataList(newDishListItems);
                } else if (itemId == R.id.edit) {
                    System.out.println("CHOSEN: EDIT");
                    Intent intent = new Intent(thisActivity, DishActivity.class);
                    intent.putExtra(DISH, items[position]);
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
            Intent intent = new Intent(thisActivity, DishActivity.class);
            intent.putExtra(DISH, new Dish());
            thisActivity.startActivityForResult(intent, DISH_ACTIVITY_REQUEST_CODE);
        });

        return view;
    }
}