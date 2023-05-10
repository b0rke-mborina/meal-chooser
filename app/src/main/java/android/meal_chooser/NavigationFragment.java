package android.meal_chooser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    private ImageView mIngredientsImageView;
    private TextView mIngredientsTextView;

    private ImageView mChooseImageView;
    private TextView mChooseTextView;

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

        mIngredientsImageView = view.findViewById(R.id.ingredients_image_view);
        mIngredientsTextView = view.findViewById(R.id.ingredients_text_view);
        mChooseImageView = view.findViewById(R.id.choose_image_view);
        mChooseTextView = view.findViewById(R.id.choose_text_view);
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