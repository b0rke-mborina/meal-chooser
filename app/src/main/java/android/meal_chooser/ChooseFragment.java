package android.meal_chooser;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.StringJoiner;

/**
 * Main fragment of the app. Shows and implements main functionality of the application. Is shown
 * when the app starts.
 *
 * A simple {@link Fragment} subclass.
 * Use the {@link ChooseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseFragment extends Fragment {

    /**
     * Key for sharing default time value.
     */
    private static final String DEFAULT_TIME = "defaultTime";

    /**
     * Default time limit value for choosing dish.
     */
    private int defaultTime;

    /**
     * Key for sharing list of recommendation history items.
     */
    private static final String RECOMMENDATION_HISTORY_ITEMS = "recommendationHistoryItems";

    /**
     * Items from recommendation history.
     */
    private RecommendationItem[] recommendationItems;

    /**
     * Request code for starting recommendation history activity.
     */
    private static final int RECOMMENDATION_HISTORY_ACTIVITY_REQUEST_CODE = 1;

    /**
     * Time limit input reference.
     */
    private EditText mInputTime;

    /**
     * Reference of button for changing ingredients. Changes main content fragment to list of
     * ingredients.
     */
    private Button mButtonChangeIngredients;

    /**
     * Reference of button for changing dishes. Changes main content fragment to list of dishes.
     */
    private Button mButtonChangeDishes;

    /**
     * Reference of button for choosing what to eat. Main function of the app.
     */
    private Button mButtonChooseDish;

    /**
     * Reference of button for showing recommendation history. Starts recommendation history
     * activity.
     */
    private ImageButton mIconButtonHistory;

    /**
     * Required empty public constructor.
     */
    public ChooseFragment() {}

    /**
     * Creates new instance of the fragment with added arguments.
     *
     * @param defaultTime Last selected time limit.
     * @return A new instance of fragment NavigationFragment.
     */
    public static ChooseFragment newInstance(int defaultTime,
                                             RecommendationItem[] recommendationItems) {
        ChooseFragment fragment = new ChooseFragment();
        Bundle args = new Bundle();
        args.putInt(DEFAULT_TIME, defaultTime);
        args.putSerializable(RECOMMENDATION_HISTORY_ITEMS, recommendationItems);
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
            defaultTime = getArguments().getInt(DEFAULT_TIME);
            recommendationItems = (RecommendationItem[]) getArguments()
                    .getSerializable(RECOMMENDATION_HISTORY_ITEMS);
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
        View view = inflater.inflate(R.layout.fragment_choose, container, false);

        // time input referencing and setting initial value
        mInputTime = view.findViewById(R.id.input_time);
        mInputTime.setText(String.valueOf(defaultTime));

        // change ingredients button referencing and setting listener
        mButtonChangeIngredients = view.findViewById(R.id.button_change_ingredients);
        mButtonChangeIngredients.setOnClickListener(v -> {
            // change main content fragment to list of ingredients
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ((MainActivity) Objects.requireNonNull(getActivity()))
                    .changeContentFragment(ingredientsFragment, "ingredients_fragment");
        });

        // change dishes button referencing and setting listener
        mButtonChangeDishes = view.findViewById(R.id.button_change_dishes);
        mButtonChangeDishes.setOnClickListener(v -> {
            // change main content fragment to list of dishes
            DishesFragment dishesFragment = new DishesFragment();
            ((MainActivity) Objects.requireNonNull(getActivity()))
                    .changeContentFragment(dishesFragment, "dishes_fragment");
        });

        // choose dish button referencing and setting listener
        mButtonChooseDish = view.findViewById(R.id.button_choose_dish);
        mButtonChooseDish.setOnClickListener(v -> {
            // main activity reference for accessing data
            MainActivity thisActivity = (MainActivity) Objects.requireNonNull(getActivity());

            // recommend dish (can be empty)
            Dish dish = chooseDish();

            // create the object of dialog and set cancelable true
            // (when the user clicks on the outside the dialog then it will close)
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
                builder.setMessage("You should eat this.\n\nIt takes "
                        + dish.getTimeToMakeInMinutes()
                        + "min to make.\n\nRequired ingredients:\n" + ingredientList);

                // positive button click behaviour
                builder.setPositiveButton("Yes, I'll eat this", (dialog, which) -> {
                    // add new item to the database and update data
                    RecommendationItem recommendationItem = new RecommendationItem();
                    recommendationItem.setDishId(dish.getId());
                    recommendationItem.setDishName(dish.getName());
                    thisActivity.datasource.createRecommendationItem(recommendationItem);
                    thisActivity.setRecommendationItems(thisActivity.datasource
                            .getAllRecommendationHistoryItems());
                });

                // negative button click behaviour (close dialog box)
                builder.setNegativeButton("Not this, thank you",
                        (dialog, which) -> dialog.cancel());
            } else {
                // set dialog texts and buttons to show choosing not successful
                builder.setTitle("Dish could not be chosen");
                builder.setMessage("No dish could be chosen based on this selection.\n\n"
                        + "Change some of this and try again:\n"
                        + "- increase time limit (take some more time to make food)\n"
                        + "- considered dishes (be less picky)\n"
                        + "- available ingredients (buy ingredients)");

                // negative button click behaviour (close dialog box)
                builder.setNegativeButton("OK", (dialog, which) -> dialog.cancel());
            }

            // create and show the dialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        // recommendation history button referencing and setting listener
        mIconButtonHistory = view.findViewById(R.id.button_history);
        mIconButtonHistory.setOnClickListener(v -> {
            // start recommendation history activity which shows recommendation history
            MainActivity thisActivity = (MainActivity) Objects.requireNonNull(getActivity());
            Intent intent = new Intent(thisActivity, RecommendationHistoryActivity.class);
            intent.putExtra(RECOMMENDATION_HISTORY_ITEMS, thisActivity.getRecommendationItems());
            thisActivity.startActivityForResult(intent,
                    RECOMMENDATION_HISTORY_ACTIVITY_REQUEST_CODE);
        });

        return view;
    }

    /**
     * Implements main functionality of the application. Recommends a dish to the user.
     *
     * @return Dish which is recommended to the user.
     */
    public Dish chooseDish() {
        MainActivity thisActivity = (MainActivity) Objects.requireNonNull(getActivity());

        // get all dishes and time limit
        Dish[] dishes = thisActivity.datasource.getAllDishes();
        double timeLimit;
        try {
            timeLimit = Double.parseDouble(String.valueOf(mInputTime.getText()));
        } catch (Exception e) {
            e.printStackTrace();
            timeLimit = defaultTime;
        }

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
                    if (ingredient.getName().equals(ingredientName)
                            && ingredient.getAmount() >= ingredientAmount) {
                        dishesAvailable++;
                        break;
                    }
                }
            }

            // add dish which can be prepared to list
            // (dishes with all ingredients available can be prepared)
            if (dishesAvailable == dish.getIngredients().length) {
                possibleDishes[numberOfPossibleDishes] = dish;
                numberOfPossibleDishes++;
            }
        }

        System.out.println(numberOfPossibleDishes);
        System.out.println("possibleDishes");
        System.out.println(Arrays.toString(possibleDishes));


        // dish which will be result
        Dish chosenDish = new Dish();

        // if not empty, then overwrite dish with random selected dish
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