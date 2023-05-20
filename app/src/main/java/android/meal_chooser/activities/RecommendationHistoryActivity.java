package android.meal_chooser.activities;

import android.app.Activity;
import android.content.Intent;
import android.meal_chooser.R;
import android.meal_chooser.database.MealChooserDataSource;
import android.meal_chooser.models.RecommendationItem;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Used for checking and updating recommendation history.
 */
public class RecommendationHistoryActivity extends AppCompatActivity {
    /**
     * Key for sharing list of recommendation history items.
     */
    private static final String RECOMMENDATION_HISTORY_ITEMS = "recommendationHistoryItems";

    /**
     * Storage for list of recommendation history items.
     */
    private RecommendationItem[] items;

    /**
     * Storage for data for adapter of list of recommendation history items for easier updates.
     */
    private List<HashMap<String, String>> listItems;

    /**
     * Adapter for list of recommendation history items.
     */
    private SimpleAdapter itemsAdapter;

    /**
     * Container which is a ListView and contains recommendation history items.
     */
    private ListView mListContainer;

    /**
     * Data source object instance of this activity.
     */
    public MealChooserDataSource datasource;

    /**
     * Runs when activity is created. Displays view of the activity and implements activity
     * functionalities.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.
     *                           <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_history);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // data source object initialization and retrieval of recommendation history items
        datasource = new MealChooserDataSource(this);
        items = (RecommendationItem[]) getIntent()
                .getSerializableExtra(RECOMMENDATION_HISTORY_ITEMS);

        // create data for adapter from fragment argument
        listItems = new ArrayList<>();
        for (RecommendationItem item : items) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("dishName", item.getDishName());
            hashMap.put("dishDate", unixTimestampToString(item.getTimestamp() * 1000));
            listItems.add(hashMap);
        }

        // define adapter with custom mapping
        String[] from = {"dishName", "dishDate"};
        int[] to = {R.id.item_name, R.id.item_time};
        itemsAdapter = new SimpleAdapter(this, listItems,
                R.layout.recommendation_history_list_item, from, to);

        // set adapter li list view
        mListContainer = findViewById(R.id.list_container);
        mListContainer.setAdapter(itemsAdapter);

        // long click listener for items of list
        mListContainer.setOnItemLongClickListener((parent, v, position, id) -> {
            // create a popup menu and inflate it using xml file
            PopupMenu popup = new PopupMenu(RecommendationHistoryActivity.this, v);
            popup.getMenuInflater()
                    .inflate(R.menu.menu_popup_delete, popup.getMenu());

            // click listener for delete item of popup list
            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.delete) {
                    // delete object in database
                    datasource.open();
                    datasource.deleteRecommendationItem(items[position].getId());
                    items = datasource.getAllRecommendationHistoryItems();
                    datasource.close();

                    // update list of items in view
                    listItems.clear();
                    for (RecommendationItem listItem : items) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("dishName", listItem.getDishName());
                        hashMap.put("dishDate",
                                unixTimestampToString(listItem.getTimestamp() * 1000));
                        listItems.add(hashMap);
                    }
                    itemsAdapter.notifyDataSetChanged();
                }
                return false;
            });

            // show popup and let handling continue
            popup.show();
            return true;
        });
    }

    /**
     * Defines listener on back button in action bar.
     *
     * @return boolean True value.
     */
    @Override
    public boolean onSupportNavigateUp() {
        // handle back button (top right corner of the screen) press
        onBackPressed();
        return true;
    }

    /**
     * Defines what happens when back button is clicked. The activity finishes.
     */
    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    /**
     * Converts Unix timestamp to string date.
     *
     * @param unixTimestamp Unix timestamp to be converted.
     * @return Date as a string.
     */
    public String unixTimestampToString(long unixTimestamp) {
        // get local offset, and find timezone id by looping through available timezone ids
        Calendar cal = Calendar.getInstance();
        long milliDiff = cal.get(Calendar.ZONE_OFFSET);
        String [] ids = TimeZone.getAvailableIDs();
        String name = null;
        for (String id : ids) {
            TimeZone tz = TimeZone.getTimeZone(id);
            if (tz.getRawOffset() == milliDiff) {
                name = id;
                break;
            }
        }

        // convert timestamp to string based on timezone and date format
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a 'on' MMMM d, yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone(name));
        return sdf.format(unixTimestamp);
    }
}