package android.meal_chooser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class RecommendationHistoryActivity extends AppCompatActivity {
    /**
     * Key for sharing list of recommendation history items.
     */
    private static final String RECOMMENDATION_HISTORY_ITEMS = "recommendationHistoryItems";

    private RecommendationItem[] items;

    /**
     * Adapter for list of dishes
     */
    private SimpleAdapter itemsAdapter;

    /**
     * Container which is a ListView and contains dish items.
     */
    private ListView mListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_history);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        items = (RecommendationItem[]) getIntent().getSerializableExtra(RECOMMENDATION_HISTORY_ITEMS);

        // create data for adapter from fragment argument
        List<HashMap<String, String>> dishListItems = new ArrayList<>();
        for (RecommendationItem item : items) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("dishName", item.getDishName());

            // TODO: make format of date better
            // convert seconds to milliseconds
            // Date date = new java.util.Date(item.getTimestamp()*1000L);
            // the format of your date
            // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            // give a timezone reference for formatting (see comment at the bottom)
            // sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
            // String formattedDate = sdf.format(date);
            // .out.println(formattedDate);
            hashMap.put("dishDate", String.valueOf(new Date(item.getTimestamp()*1000)));

            dishListItems.add(hashMap);
        }

        // define adapter with custom mapping
        String[] from = {"dishName", "dishDate"};
        int[] to = {R.id.item_name, R.id.item_time};
        itemsAdapter = new SimpleAdapter(this, dishListItems,
                R.layout.recommendation_history_list_item, from, to);

        mListContainer = findViewById(R.id.list_container);
        mListContainer.setAdapter(itemsAdapter);

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
     * Defines what happens when back button is clicked.
     */
    @Override
    public void onBackPressed() {
        finish();
    }
}