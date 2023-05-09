package android.meal_chooser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    /**
     * Menu reference field.
     */
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Creates option menu in action bar.
     *
     * @param menu The options menu in which you place your items.
     * @return boolean True value.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(android.meal_chooser.R.menu.menu_quiz, menu);
        return true;
    }

    /**
     * Handles visibility of menu in action bar.
     *
     * @param showMenu Value which defines weather to show or hide the menu.
     */
    public void showOverflowMenu(boolean showMenu){
        // shows menu on action bar (3 dots in top right corner of the screen)
        if (menu == null) return;
        menu.setGroupVisible(R.id.main_menu_group, showMenu);
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
}