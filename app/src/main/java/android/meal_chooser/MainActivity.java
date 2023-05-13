package android.meal_chooser;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    /**
     * Menu reference field.
     */
    Menu actionMenu;

    /**
     * Key for sharing list of navigation items.
     */
    private static final String NAV_ITEMS = "navigationItems";

    private final NavigationItem[] navigationItems = new NavigationItem[]{
            new NavigationItem(R.drawable.icon_ingredients, R.string.title_ingredients),
            new NavigationItem(R.drawable.icon_choose, R.string.title_choose),
            new NavigationItem(R.drawable.icon_eat, R.string.title_dishes)
    };

    /**
     * Key for sharing list of navigation items.
     */
    private static final String DEFAULT_TIME = "defaultTime";

    private int defaultTime = 20;

    /**
     * Key for sharing list of ingredients.
     */
    private static final String INGREDIENTS = "ingredients";

    private Ingredient[] ingredients = new Ingredient[]{
        new Ingredient("Rice", 3, true, false),
        new Ingredient("Sausage", 1, true, false),
        new Ingredient("Potato", 2, true, false),
        new Ingredient("Cheese", 1, false, false),
        new Ingredient("Apple", 2, true, false),
        new Ingredient("Bread", 1, false, false),
        new Ingredient("Strawberry", 3, false, false),
        new Ingredient("Sour cream", 1, true, false),
        new Ingredient("Mayonnaise", 1, true, false)
    };

    /**
     * Key for sharing list of dishes.
     */
    private static final String DISHES = "dishes";

    private Dish[] dishes = new Dish[]{
        new Dish("Pasta carbonara", 20, true,
            new Ingredient[]{
                new Ingredient("Pasta", 1, null, true),
                new Ingredient("Bacon", 1, null, true)
            }
        ),
        new Dish("Risotto", 35, true,
            new Ingredient[]{
                new Ingredient("Rice", 1, null, true),
                new Ingredient("Salsa", 1,null , true)
            }
        ),
        new Dish("Toast", 8, true,
            new Ingredient[]{
                new Ingredient("Bread", 1, null, true),
                new Ingredient("Cheese", 1, null, true)
            }
        ),
        new Dish("Tortillas", 1, true,
            new Ingredient[]{
                new Ingredient("Tortilla", 2, null, true),
                new Ingredient("Chicken meat", 1, null, true)
            }
        ),
        new Dish("Fruit salad", 5, true,
            new Ingredient[]{
                new Ingredient("Apple", 2, null, true),
                new Ingredient("Strawberry", 3, null, true)
            }
        ),
    };

    /**
     * Key for sharing list of recommendation history items.
     */
    private static final String RECOMMENDATION_HISTORY_ITEMS = "recommendationHistoryItems";

    RecommendationItem[] recommendationItems = new RecommendationItem[]{
            new RecommendationItem(1, 8, "Doner kebab", 1683904471),
            new RecommendationItem(2, 1, "Pizza capricciosa", 1683911916),
            new RecommendationItem(3, 10, "Cheeseburger", 1683911671),
            new RecommendationItem(4, 3, "Pizza margherita", 1683911959),
            new RecommendationItem(5, 7, "Mexican food", 1683911969),
            new RecommendationItem(6, 4, "Pasta carbonara", 1683911979),
            new RecommendationItem(7, 2, "Hot dog", 1683911992),
            new RecommendationItem(8, 9, "Sausages", 1683912001),
            new RecommendationItem(9, 6, "Toast", 1683912010),
            new RecommendationItem(10, 5, "Croissant", 1683912034),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // add fragment to placeholder
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // create data for fragment
        Bundle navData = new Bundle();
        navData.putSerializable(NAV_ITEMS, navigationItems);

        NavigationFragment navigationFragment = new NavigationFragment();
        navigationFragment.setArguments(navData);
        fragmentTransaction.add(
                R.id.placeholder_fragment_navigation,
                navigationFragment,
                "navigation_fragment"
        );

        // create data for fragment
        Bundle chooseData = new Bundle();
        chooseData.putInt(DEFAULT_TIME, defaultTime);
        chooseData.putSerializable(RECOMMENDATION_HISTORY_ITEMS, recommendationItems);

        // TODO: replace this with changeContentFragment(...) call
        ChooseFragment chooseFragment = new ChooseFragment();
        chooseFragment.setArguments(chooseData);
        fragmentTransaction.add(
                R.id.placeholder_fragment_content,
                chooseFragment,
                "choose_fragment"
        );

        fragmentTransaction.commit();
    }

    /**
     * Creates option menu in action bar.
     *
     * @param menu The options menu in which you place your items.
     * @return boolean True value.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.actionMenu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(android.meal_chooser.R.menu.menu_action, menu);
        return true;
    }

    /**
     * Handles visibility of menu in action bar.
     *
     * @param showMenu Value which defines weather to show or hide the menu.
     */
    public void showOverflowMenu(boolean showMenu){
        // shows menu on action bar (3 dots in top right corner of the screen)
        if (actionMenu == null) return;
        actionMenu.setGroupVisible(R.id.main_menu_group, showMenu);
    }

    /**
     * Handles what happens when menu item is clicked.
     *
     * @param item The menu item that was selected.
     * @return boolean Weather to allow normal menu processing to proceed.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.meal_chooser.R.id.settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeContentFragment(Fragment fragment, String tag) {
        // create data for fragment
        Bundle data = new Bundle();
        switch (tag) {
            case "ingredients_fragment":
                data.putSerializable(INGREDIENTS, ingredients);
                break;
            case "choose_fragment":
                data.putInt(DEFAULT_TIME, defaultTime);
                data.putSerializable(RECOMMENDATION_HISTORY_ITEMS, recommendationItems);
                break;
            case "dishes_fragment":
                data.putSerializable(DISHES, dishes);
                break;
        }

        // change fragment using fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment.setArguments(data);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(
                R.id.placeholder_fragment_content, fragment, tag
        );
        fragmentTransaction.commit();
    }
}