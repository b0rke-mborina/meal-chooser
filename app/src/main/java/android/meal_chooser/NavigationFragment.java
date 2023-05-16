package android.meal_chooser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavigationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationFragment extends Fragment {
    /**
     * Key for sharing list of navigation items.
     */
    private static final String NAV_ITEMS = "navigationItems";

    /**
     * List of plant items received as argument.
     */
    private NavigationItem[] items;

    /**
     * Adapter for list of navigation items.
     */
    // private SimpleAdapter plantsAdapter;

    /**
     * Container which is a ListView and contains navigation items.
     */
    // private ListView mListContainer;

    public LinearLayout mIngredientsItem;
    private ImageView mIngredientsImageView;
    private TextView mIngredientsTextView;

    public LinearLayout mChooseItem;
    private ImageView mChooseImageView;
    private TextView mChooseTextView;

    public LinearLayout mDishesItem;
    private ImageView mDishesImageView;
    private TextView mDishesTextView;
    public NavigationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param navItems Items for bottom navigation.
     * @return A new instance of fragment NavigationFragment.
     */
    public static NavigationFragment newInstance(NavigationItem[] navItems) {
        NavigationFragment fragment = new NavigationFragment();
        Bundle args = new Bundle();
        args.putSerializable(NAV_ITEMS, navItems);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            items = (NavigationItem[]) getArguments().getSerializable(NAV_ITEMS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);

        // TODO: make nav list in list view

        /*// create data for adapter from fragment argument
        List<HashMap<String, String>> navListItems = new ArrayList<>();
        for (NavigationItem item : items) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("icon", Integer.toString(item.getIcon()));
            hashMap.put("title", getString(item.getTitle()));
            navListItems.add(hashMap);
        }

        String[] from = {"icon", "title"};
        int[] to = {R.id.image_view, R.id.text_view};
        plantsAdapter = new SimpleAdapter(getActivity(), navListItems,
                R.layout.navigation_item, from, to);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mListContainer = (ListView) view.findViewById(R.id.list_container);
        mListContainer.setLayoutManager(layoutManager);*/

        mIngredientsItem = view.findViewById(R.id.ingredients_nav_item);
        mIngredientsItem.setOnClickListener(v -> {
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ((MainActivity) Objects.requireNonNull(getActivity())).changeContentFragment(ingredientsFragment, "ingredients_fragment");
        });
        mIngredientsImageView = view.findViewById(R.id.ingredients_image_view);
        mIngredientsTextView = view.findViewById(R.id.ingredients_text_view);

        mChooseItem = view.findViewById(R.id.choose_nav_item);
        mChooseItem.setOnClickListener(v -> {
            ChooseFragment chooseFragment = new ChooseFragment();
            ((MainActivity) Objects.requireNonNull(getActivity())).changeContentFragment(chooseFragment, "choose_fragment");
        });
        mChooseImageView = view.findViewById(R.id.choose_image_view);
        mChooseTextView = view.findViewById(R.id.choose_text_view);

        mDishesItem = view.findViewById(R.id.dishes_nav_item);
        mDishesItem.setOnClickListener(v -> {
            DishesFragment dishesFragment = new DishesFragment();
            ((MainActivity) Objects.requireNonNull(getActivity())).changeContentFragment(dishesFragment, "dishes_fragment");
        });
        mDishesImageView = view.findViewById(R.id.dishes_image_view);
        mDishesTextView = view.findViewById(R.id.dishes_text_view);

        mIngredientsImageView.setImageDrawable(Objects.requireNonNull(getActivity()).getDrawable(items[0].getIcon()));
        mIngredientsTextView.setText(getString(items[0].getTitle()));
        mChooseImageView.setImageDrawable(Objects.requireNonNull(getActivity()).getDrawable(items[1].getIcon()));
        mChooseTextView.setText(getString(items[1].getTitle()));
        mDishesImageView.setImageDrawable(Objects.requireNonNull(getActivity()).getDrawable(items[2].getIcon()));
        mDishesTextView.setText(getString(items[2].getTitle()));

        return view;
    }
}