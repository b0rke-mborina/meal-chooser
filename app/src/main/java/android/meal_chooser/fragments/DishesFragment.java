package android.meal_chooser.fragments;

import android.content.Intent;
import android.meal_chooser.activities.DishActivity;
import android.meal_chooser.MainActivity;
import android.meal_chooser.R;
import android.meal_chooser.models.Dish;
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
 * Fragment of the app which shows list of dishes and implements functionalities related to dishes.
 *
 * A simple {@link Fragment} subclass.
 * Use the {@link DishesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DishesFragment extends Fragment {
    /**
     * Key for sharing list of dishes.
     */
    private static final String DISHES = "dishes";

    /**
     * Storage for list of dishes.
     */
    private Dish[] items;

    /**
     * Key for sharing dish for edit.
     */
    private static final String DISH = "dish";

    /**
     * Request code for starting dish activity.
     */
    private static final int DISH_ACTIVITY_REQUEST_CODE = 3;

    /**
     * Reference of button for adding new dish.
     */
    private ImageButton mButtonAdd;

    /**
     * Adapter for list of dishes
     */
    private CustomAdapter itemsAdapter;

    /**
     * Container which is a ListView and contains dish items.
     */
    private ListView mListContainer;

    /**
     * Required empty public constructor.
     */
    public DishesFragment() {}

    /**
     * Creates new instance of the fragment with added arguments.
     *
     * @param dishes Existing dish objects.
     * @return Instance of the dishes fragment.
     */
    public static DishesFragment newInstance(Dish[] dishes) {
        DishesFragment fragment = new DishesFragment();
        Bundle args = new Bundle();
        args.putSerializable(DISHES, dishes);
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
            items = (Dish[]) getArguments().getSerializable(DISHES);
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
        View view = inflater.inflate(R.layout.fragment_dishes, container, false);

        // parent activity reference for further use
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

        // change listener for checkboxes of items in list view
        itemsAdapter.setCheckBoxChangeListener((position, isChecked) -> {
            // update the database and data storage with the new checkbox state
            Dish dish = thisActivity.datasource.getDish(thisActivity.dishes[position].getId());
            dish.setConsidered(isChecked);
            items[position].setConsidered(isChecked);
            thisActivity.dishes[position].setConsidered(isChecked);
            thisActivity.datasource.updateDish(dish);
        });

        // long click listener for items in list view
        itemsAdapter.setOnItemLongClickListener((parent, v, position, id) -> {
            // create and inflate the popup using xml file
            PopupMenu popup = new PopupMenu(thisActivity, v);
            popup.getMenuInflater()
                    .inflate(R.menu.menu_popup, popup.getMenu());

            // click listener for items of the menu
            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.delete) {
                    // delete dish when delete popup item is clicked

                    // update the database and data storage
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

                    // update fragment list view
                    itemsAdapter.updateDataList(newDishListItems);
                } else if (itemId == R.id.edit) {
                    // start new activity for editing dish when edit popup item is clicked
                    Intent intent = new Intent(thisActivity, DishActivity.class);
                    intent.putExtra(DISH, items[position]);
                    thisActivity.startActivityForResult(intent, DISH_ACTIVITY_REQUEST_CODE);
                }
                return false;
            });

            popup.show();
            return true;
        });

        // reference list view and set adapter to list container
        mListContainer = view.findViewById(R.id.list_container);
        mListContainer.setAdapter(itemsAdapter);

        // button for adding new dish
        mButtonAdd = view.findViewById(R.id.button_add);
        mButtonAdd.setOnClickListener(v -> {
            // start new activity for adding new dish
            Intent intent = new Intent(thisActivity, DishActivity.class);
            intent.putExtra(DISH, new Dish());
            thisActivity.startActivityForResult(intent, DISH_ACTIVITY_REQUEST_CODE);
        });

        return view;
    }

    /**
     * Updates list in the fragment view. Used for updating the list view from different classes.
     *
     * @param dishes New updated data which the list view is to be updated with.
     */
    public void updateList(Dish[] dishes) {
        // create data for adapter from fragment argument
        List<HashMap<String, String>> dishListItems = new ArrayList<>();
        for (Dish listItem : dishes) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("dishId", String.valueOf(listItem.getId()));
            hashMap.put("dishTime", listItem.getTimeToMakeInMinutes() + " min");
            hashMap.put("dishName", listItem.getName());
            hashMap.put("dishIsConsidered", String.valueOf(listItem.isConsidered()));
            dishListItems.add(hashMap);
        }

        // update data storage and list view
        items = dishes;
        itemsAdapter.updateDataList(dishListItems);
    }
}