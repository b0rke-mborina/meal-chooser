package android.meal_chooser.fragments;

import android.meal_chooser.MainActivity;
import android.meal_chooser.R;
import android.meal_chooser.models.NavigationItem;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Objects;

/**
 * Navigation of the app. Shows and implements main navigation of the application. Is shown at the
 * bottom of the screen. Is used to change between three main fragments of the main activity
 * (Choose, Ingredients, Dishes).
 *
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
     * Ingredients navigation item container reference.
     */
    public LinearLayout mIngredientsItem;

    /**
     * Image of the ingredients navigation item reference.
     */
    private ImageView mIngredientsImageView;

    /**
     * Title of the ingredients navigation item reference.
     */
    private TextView mIngredientsTextView;

    /**
     * Choose navigation item container reference.
     */
    public LinearLayout mChooseItem;

    /**
     * Image of the choose navigation item reference.
     */
    private ImageView mChooseImageView;

    /**
     * Title of the choose navigation item reference.
     */
    private TextView mChooseTextView;

    /**
     * Dishes navigation item container reference.
     */
    public LinearLayout mDishesItem;

    /**
     * Image of the dishes navigation item reference.
     */
    private ImageView mDishesImageView;

    /**
     * Title of the dishes navigation item reference.
     */
    private TextView mDishesTextView;

    /**
     * Required empty public constructor.
     */
    public NavigationFragment() {}

    /**
     * Creates new instance of the fragment with retrieved arguments.
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

    /**
     * Runs when fragment is created. Retrieves and saves arguments.
     *
     * @param savedInstanceState Data used before in activity.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            items = (NavigationItem[]) getArguments().getSerializable(NAV_ITEMS);
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
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);

        // the ingredients are added to the list view one by one to make it simple
        // without using RecyclerView and without rotating ListView

        // ingredients item referencing
        mIngredientsItem = view.findViewById(R.id.ingredients_nav_item);
        mIngredientsImageView = view.findViewById(R.id.ingredients_image_view);
        mIngredientsTextView = view.findViewById(R.id.ingredients_text_view);

        // choose item referencing
        mChooseItem = view.findViewById(R.id.choose_nav_item);
        mChooseImageView = view.findViewById(R.id.choose_image_view);
        mChooseTextView = view.findViewById(R.id.choose_text_view);

        // dishes item referencing
        mDishesItem = view.findViewById(R.id.dishes_nav_item);
        mDishesImageView = view.findViewById(R.id.dishes_image_view);
        mDishesTextView = view.findViewById(R.id.dishes_text_view);

        // set click listeners to items which change main content fragments
        mIngredientsItem.setOnClickListener(v -> {
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ((MainActivity) Objects.requireNonNull(getActivity()))
                    .changeContentFragment(ingredientsFragment, "ingredients_fragment");
        });
        mChooseItem.setOnClickListener(v -> {
            ChooseFragment chooseFragment = new ChooseFragment();
            ((MainActivity) Objects.requireNonNull(getActivity()))
                    .changeContentFragment(chooseFragment, "choose_fragment");
        });
        mDishesItem.setOnClickListener(v -> {
            DishesFragment dishesFragment = new DishesFragment();
            ((MainActivity) Objects.requireNonNull(getActivity()))
                    .changeContentFragment(dishesFragment, "dishes_fragment");
        });

        // set drawables and titles to items
        mIngredientsImageView.setImageDrawable(Objects.requireNonNull(getActivity())
                .getDrawable(items[0].getIcon()));
        mIngredientsTextView.setText(getString(items[0].getTitle()));
        mChooseImageView.setImageDrawable(Objects.requireNonNull(getActivity())
                .getDrawable(items[1].getIcon()));
        mChooseTextView.setText(getString(items[1].getTitle()));
        mDishesImageView.setImageDrawable(Objects.requireNonNull(getActivity())
                .getDrawable(items[2].getIcon()));
        mDishesTextView.setText(getString(items[2].getTitle()));

        return view;
    }
}