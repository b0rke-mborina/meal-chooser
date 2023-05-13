package android.meal_chooser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChooseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseFragment extends Fragment {

    /**
     * Key for sharing list of navigation items.
     */
    private static final String DEFAULT_TIME = "defaultTime";

    /**
     * Key for sharing list of recommendation history items.
     */
    private static final String RECOMMENDATION_HISTORY_ITEMS = "recommendationHistoryItems";

    private int defaultTime;
    private RecommendationItem[] recommendationItems;

    private EditText mInputTime;
    private Button mButtonChangeIngredients;
    private Button mButtonChangeDishes;
    private Button mButtonChooseDish;
    private ImageButton mIconButtonHistory;

    public ChooseFragment() {
        // Required empty public constructor
    }

    /**
     *
     *
     * @param defaultTime Last selected time limit.
     * @return Instance of the fragment.
     */
    public static ChooseFragment newInstance(int defaultTime, RecommendationItem[] recommendationItems) {
        ChooseFragment fragment = new ChooseFragment();
        Bundle args = new Bundle();
        args.putInt(DEFAULT_TIME, defaultTime);
        args.putSerializable(RECOMMENDATION_HISTORY_ITEMS, recommendationItems);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            defaultTime = getArguments().getInt(DEFAULT_TIME);
            recommendationItems = (RecommendationItem[]) getArguments().getSerializable(RECOMMENDATION_HISTORY_ITEMS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose, container, false);

        mInputTime = view.findViewById(R.id.input_time);
        System.out.println(defaultTime);
        mInputTime.setText(String.valueOf(defaultTime));

        mButtonChangeIngredients = view.findViewById(R.id.button_change_ingredients);
        mButtonChangeIngredients.setOnClickListener(v -> {
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ((MainActivity) Objects.requireNonNull(getActivity())).changeContentFragment(ingredientsFragment, "ingredients_fragment");
        });

        mButtonChangeDishes = view.findViewById(R.id.button_change_dishes);
        mButtonChangeDishes.setOnClickListener(v -> {
            DishesFragment dishesFragment = new DishesFragment();
            ((MainActivity) Objects.requireNonNull(getActivity())).changeContentFragment(dishesFragment, "dishes_fragment");
        });

        mButtonChooseDish = view.findViewById(R.id.button_choose_dish);
        mButtonChooseDish.setOnClickListener(v -> {
            System.out.println("Choose selected.");
            MainActivity thisActivity = (MainActivity) Objects.requireNonNull(getActivity());
            // Intent intent = new Intent(thisActivity, IngredientActivity.class);
            // intent.putExtra(RESULT, result);
            // startActivity(intent);
        });

        mIconButtonHistory = view.findViewById(R.id.button_history);
        mIconButtonHistory.setOnClickListener(v -> {
            System.out.println("History checked.");
            // start new activity which shows result
            MainActivity thisActivity = (MainActivity) Objects.requireNonNull(getActivity());
            Intent intent = new Intent(thisActivity, RecommendationHistoryActivity.class);
            intent.putExtra(RECOMMENDATION_HISTORY_ITEMS, recommendationItems);
            startActivity(intent);
        });


        return view;
    }
}