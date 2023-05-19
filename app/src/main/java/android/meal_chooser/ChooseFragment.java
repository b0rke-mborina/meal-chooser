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
import java.util.Random;
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

    /**
     * Request code for starting recommendation history activity.
     */
    private static final int RECOMMENDATION_HISTORY_ACTIVITY_REQUEST_CODE = 1;

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
            Dish dish = chooseDish();

            // create the object of dialog and set cancelable true (when the user clicks on the outside the dialog then it will close)
            AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
            builder.setCancelable(true);

            if (dish.getId() > 0) {
                // set dialog texts and buttons based on chosen dish
                builder.setTitle(dish.getName());
                StringJoiner ingredientJoiner = new StringJoiner(", ");
                for (Ingredient ingredient : dish.getIngredients()) {
                    ingredientJoiner.add(ingredient.getName());
                }
                String ingredientList = ingredientJoiner.toString();
                builder.setMessage("You should eat this.\n\nIt takes " + dish.getTimeToMakeInMinutes()
                        + "min to make.\n\nRequired ingredients:\n" + ingredientList);

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
            } else {
                // set dialog texts and buttons to show choosing not successful
                builder.setTitle("Dish could not be chosen");
                builder.setMessage("No dish could be chosen based on this selection.\n\n"
                        + "Change some of this and try again:\n"
                        + "- increase time limit (take some more time to make food)\n"
                        + "- considered dishes (be less picky)\n"
                        + "- available ingredients (buy ingredients)");

                // if user clicks on the negative button then dialog box will close
                builder.setNegativeButton("OK", (dialog, which) -> dialog.cancel());
            }

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
            thisActivity.startActivityForResult(intent, RECOMMENDATION_HISTORY_ACTIVITY_REQUEST_CODE);
        });

        return view;
    }

    public Dish chooseDish() {
        MainActivity thisActivity = (MainActivity) Objects.requireNonNull(getActivity());

        // get all dishes and time limit
        Dish[] dishes = thisActivity.datasource.getAllDishes();
        double timeLimit = Double.parseDouble(String.valueOf(mInputTime.getText()));

        // select only dishes which are to be considered and which can be prepared in time
        Dish[] consideredDishes = new Dish[dishes.length];
        int numberOfConsideredDishes = 0;
        for (Dish dish : dishes) {
            if (dish.isConsidered() && dish.getTimeToMakeInMinutes() <= timeLimit) {
                consideredDishes[numberOfConsideredDishes] = dish;
                numberOfConsideredDishes++;
            }
        }

        // get all available ingredients
        Ingredient[] availableIngredients = new Ingredient[thisActivity.ingredients.length];
        int numberOfAvailableIngredients = 0;
        for (Ingredient ingredient : thisActivity.ingredients) {
            if (ingredient.isAvailable() && ingredient.getAmount() > 0) {
                availableIngredients[numberOfAvailableIngredients] = ingredient;
                numberOfAvailableIngredients++;
            }
        }

        // select dishes which can be prepared (ingredients for them are available)
        Dish[] possibleDishes = new Dish[consideredDishes.length];
        int numberOfPossibleDishes = 0;
        for (int i = 0; i < numberOfConsideredDishes; i++) {
            long consideredDishId = consideredDishes[i].getId();
            int dishesAvailable = 0;
            Dish dish = thisActivity.datasource.getDish(consideredDishId);
            for (Ingredient dishIngredient : dish.getIngredients()) {
                String ingredientName = dishIngredient.getName();
                int ingredientAmount = dishIngredient.getAmount();

                // check if ingredient is available
                for (int j = 0; j < numberOfAvailableIngredients; j++) {
                    Ingredient ingredient = availableIngredients[j];
                    if (ingredient.getName().equals(ingredientName) && ingredient.getAmount() >= ingredientAmount) {
                        dishesAvailable++;
                        break;
                    }
                }
            }

            // add dish which can be prepared to list (dishes with all ingredients available can be prepared)
            if (dishesAvailable == dish.getIngredients().length) {
                possibleDishes[numberOfPossibleDishes] = dish;
                numberOfPossibleDishes++;
            }
        }

        System.out.println(numberOfPossibleDishes);
        System.out.println("possibleDishes");
        System.out.println(Arrays.toString(possibleDishes));


        Dish chosenDish = new Dish();

        // if not empty, then random select
        if (numberOfPossibleDishes == 1) {
            chosenDish = possibleDishes[0];
        } else if (numberOfPossibleDishes > 1) {
            Random ran = new Random();
            int indexOfDish = ran.nextInt(numberOfPossibleDishes);
            chosenDish = possibleDishes[indexOfDish];
        }

        return chosenDish;
    }
}