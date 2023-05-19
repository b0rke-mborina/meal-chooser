package android.meal_chooser;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

    public Ingredient[] ingredients;

    /**
     * Key for sharing list of dishes.
     */
    private static final String DISHES = "dishes";

    public Dish[] dishes;

    /**
     * Key for sharing list of recommendation history items.
     */
    private static final String RECOMMENDATION_HISTORY_ITEMS = "recommendationHistoryItems";

    /**
     * Request code for starting recommendation history activity.
     */
    private static final int RECOMMENDATION_HISTORY_ACTIVITY_REQUEST_CODE = 1;

    /**
     * Request code for starting ingredient activity.
     */
    private static final int INGREDIENT_ACTIVITY_REQUEST_CODE = 2;

    /**
     * Request code for starting dish activity.
     */
    private static final int DISH_ACTIVITY_REQUEST_CODE = 3;

    public RecommendationItem[] recommendationItems;

    public MealChooserDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new MealChooserDataSource(this);
        datasource.open();

        generateData();

        // System.out.println(Arrays.toString(datasource.getAllIngredients()));
        ingredients = datasource.getAllIngredients();
        // System.out.println(Arrays.toString(ingredients));
        dishes = datasource.getAllDishes();
        // System.out.println(Arrays.toString(dishes));
        recommendationItems = datasource.getAllRecommendationHistoryItems();
        // System.out.println(Arrays.toString(recommendationItems));

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
        chooseData.putSerializable(RECOMMENDATION_HISTORY_ITEMS, getRecommendationItems());

        ChooseFragment chooseFragment = new ChooseFragment();
        chooseFragment.setArguments(chooseData);
        fragmentTransaction.add(
                R.id.placeholder_fragment_content,
                chooseFragment,
                "choose_fragment"
        );

        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        datasource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        datasource.close();
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
        switch (id) {
            case R.id.settings: // android.meal_chooser.R.id.settings
                System.out.println("Chosen: 'Settings'");
                break;
            case R.id.import_data:
                System.out.println("Chosen: 'Import data'");
                break;
            case R.id.export_data:
                System.out.println("Chosen: 'Export data'");
                break;
            case R.id.about:
                System.out.println("Chosen: 'About'");
                break;
            case R.id.help:
                System.out.println("Chosen: 'Help'");
                break;
        }
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
                data.putSerializable(INGREDIENTS, getIngredients());
                break;
            case "choose_fragment":
                data.putInt(DEFAULT_TIME, defaultTime);
                data.putSerializable(RECOMMENDATION_HISTORY_ITEMS, getRecommendationItems());
                break;
            case "dishes_fragment":
                data.putSerializable(DISHES, getDishes());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RECOMMENDATION_HISTORY_ACTIVITY_REQUEST_CODE) {
            System.out.println("closed history");
            datasource.open();
            setRecommendationItems(datasource.getAllRecommendationHistoryItems());
            datasource.close();
        } else if (requestCode == INGREDIENT_ACTIVITY_REQUEST_CODE) {
            System.out.println("closed ingredient");
            datasource.open();
            setIngredients(datasource.getAllIngredients());
            datasource.close();

            IngredientsFragment currentFragment = ((IngredientsFragment) getSupportFragmentManager().findFragmentByTag("ingredients_fragment"));
            if (currentFragment != null) {
                currentFragment.updateList(getIngredients());
            }
        } else if (requestCode == DISH_ACTIVITY_REQUEST_CODE) {
            System.out.println("closed dish");
            datasource.open();
            setDishes(datasource.getAllDishes());
            datasource.close();

            DishesFragment currentFragment = ((DishesFragment) getSupportFragmentManager().findFragmentByTag("dishes_fragment"));
            if (currentFragment != null) {
                currentFragment.updateList(getDishes());
            }
        }
    }

    private void generateData() {
        datasource.database.delete(MealChooserDbHelper.TABLE_INGREDIENT, null, null);
        datasource.database.delete(MealChooserDbHelper.TABLE_DISH, null, null);
        datasource.database.delete(MealChooserDbHelper.TABLE_DISH_INGREDIENT, null, null);
        datasource.database.delete(MealChooserDbHelper.TABLE_RECOMMENDATION_HISTORY, null, null);

        // generate ingredients
        Ingredient[] ingredients = new Ingredient[]{
                new Ingredient(11, "Rice", 3, true, false),
                new Ingredient(12, "Sausage", 1, true, false),
                new Ingredient(13, "Potato", 2, true, false),
                new Ingredient(14, "Cheese", 1, false, false),
                new Ingredient(15, "Apple", 2, true, false),
                new Ingredient(16, "Bread", 1, false, false),
                new Ingredient(17, "Strawberry", 3, false, false),
                new Ingredient(18, "Sour cream", 1, true, false),
                new Ingredient(19, "Mayonnaise", 1, true, false)
        };
        Ingredient ingredientLast = new Ingredient();
        for (Ingredient ingredient : ingredients) {
            ingredientLast = datasource.createIngredient(ingredient);
        }
        /*System.out.println(ingredientLast.getId());
        System.out.println(ingredientLast.getName());
        System.out.println(ingredientLast.getAmount());
        System.out.println(ingredientLast.isAvailable());
        System.out.println(ingredientLast.belongsToDish());*/

        // generate dishes
        Dish[] dishes = new Dish[]{
                new Dish(101, "Pasta carbonara", 20, true,
                        new Ingredient[]{
                                new Ingredient(21, "Pasta", 1, null, true),
                                new Ingredient(22, "Bacon", 1, null, true)
                        }
                ),
                new Dish(102, "Risotto", 35, true,
                        new Ingredient[]{
                                new Ingredient(23, "Rice", 1, null, true),
                                new Ingredient(24, "Salsa", 1,null , true)
                        }
                ),
                new Dish(103, "Toast", 8, false,
                        new Ingredient[]{
                                new Ingredient(25, "Bread", 1, null, true),
                                new Ingredient(26, "Cheese", 1, null, true)
                        }
                ),
                new Dish(104, "Tortillas", 1, true,
                        new Ingredient[]{
                                new Ingredient(27, "Tortilla", 2, null, true),
                                new Ingredient(28, "Chicken meat", 1, null, true)
                        }
                ),
                new Dish(105, "Fruit salad", 5, false,
                        new Ingredient[]{
                                new Ingredient(29, "Apple", 2, null, true),
                                new Ingredient(30, "Strawberry", 3, null, true)
                        }
                ),
        };
        Dish dishLast = new Dish();
        for (Dish dish : dishes) {
            dishLast = datasource.createDish(dish);
        }
        /*System.out.println(dishLast.getId());
        System.out.println(dishLast.getName());
        System.out.println(dishLast.getTimeToMakeInMinutes());
        System.out.println(dishLast.isConsidered());
        System.out.println(Arrays.toString(dishLast.getIngredients()));*/

        // generate recommendation history
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
        RecommendationItem recommendationItemLast = new RecommendationItem();
        for (RecommendationItem recommendationItem : recommendationItems) {
            recommendationItemLast = datasource.createRecommendationItem(recommendationItem);
        }
        /*System.out.println(recommendationItemLast.getId());
        System.out.println(recommendationItemLast.getDishId());
        System.out.println(recommendationItemLast.getDishName());
        System.out.println(recommendationItemLast.getTimestamp());*/
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public void setDishes(Dish[] dishes) {
        this.dishes = dishes;
    }

    public void setRecommendationItems(RecommendationItem[] recommendationItems) {
        this.recommendationItems = recommendationItems;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public Dish[] getDishes() {
        return dishes;
    }

    public RecommendationItem[] getRecommendationItems() {
        return recommendationItems;
    }
}