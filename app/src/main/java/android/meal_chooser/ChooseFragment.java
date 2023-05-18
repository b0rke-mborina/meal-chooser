package android.meal_chooser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

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

            // recommend dish
            Dish dish = new Dish(101, "Pasta carbonara", 20, true,
                new Ingredient[]{
                        new Ingredient(21, "Pasta", 1, null, true),
                        new Ingredient(22, "Bacon", 1, null, true)
            });

            // create the object of dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);

            // set dialog title
            builder.setTitle(dish.getName());

            // set dialog message
            StringJoiner ingredientJoiner = new StringJoiner(", ");
            for (Ingredient ingredient : dish.getIngredients()) {
                ingredientJoiner.add(ingredient.getName());
            }
            String ingredientList = ingredientJoiner.toString();
            builder.setMessage("You should eat this.\n\nIt takes " + dish.getTimeToMakeInMinutes()
                    + "min to make.\n\nRequired ingredients:\n" + ingredientList);

            // set cancelable true for when the user clicks on the outside the dialog then it will close
            builder.setCancelable(true);

            // when the user clicks the positive button new item will be added to the database and data will be updated
            builder.setPositiveButton("Yes, I'll eat this", (dialog, which) -> {
                RecommendationItem recommendationItem = new RecommendationItem();
                recommendationItem.setDishId(dish.getId());
                recommendationItem.setDishName(dish.getName());
                thisActivity.datasource.createRecommendationItem(recommendationItem);
                thisActivity.setRecommendationItems(thisActivity.datasource.getAllRecommendationHistoryItems());
            });

            // If user click on the negative button then dialog box will close
            builder.setNegativeButton("Not this, thank you", (dialog, which) -> dialog.cancel());

            // create and show the dialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        mIconButtonHistory = view.findViewById(R.id.button_history);
        mIconButtonHistory.setOnClickListener(v -> {
            System.out.println("History checked.");
            // start new activity which shows result
            MainActivity thisActivity = (MainActivity) Objects.requireNonNull(getActivity());
            Intent intent = new Intent(thisActivity, RecommendationHistoryActivity.class);
            intent.putExtra(RECOMMENDATION_HISTORY_ITEMS, thisActivity.getRecommendationItems());
            startActivityForResult(intent, 1);
        });


        return view;
    }
}