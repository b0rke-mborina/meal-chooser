package android.meal_chooser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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

    public DishesFragment() {
        // Required empty public constructor
    }

    /**
     *
     *
     * @param ingredients Existing ingredient objects.
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