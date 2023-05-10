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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create data for fragment
        Bundle navData = new Bundle();
        navData.putSerializable(NAV_ITEMS, navigationItems);

        // add fragment to placeholder
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        NavigationFragment navigationFragment = new NavigationFragment();
        navigationFragment.setArguments(navData);
        fragmentTransaction.add(
                R.id.placeholder_fragment_navigation,
                navigationFragment,
                "navigation_fragment"
        );

        ChooseFragment chooseFragment = new ChooseFragment();
        // navigationFragment.setArguments(navData);
        fragmentTransaction.add(
                R.id.placeholder_fragment_content,
                chooseFragment,
                "content_fragment"
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(
                R.id.placeholder_fragment_content, fragment, tag
        );
        fragmentTransaction.commit();
    }
}