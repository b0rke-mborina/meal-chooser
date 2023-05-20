package android.meal_chooser.fragments;

import android.content.Context;
import android.meal_chooser.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

/**
 * Adapter for creating lists of ingredients and dishes. It's a subclass of BaseAdapter.
 */
public class CustomAdapter extends BaseAdapter {
    /**
     * Data which the adapter shows.
     */
    private List<HashMap<String, String>> data;

    /**
     * Data context of the activity.
     */
    private Context context;

    /**
     * Object for building layout objects.
     */
    private LayoutInflater inflater;

    /**
     * Id of the layout in which the adapter shows data.
     */
    private int layout;

    /**
     * Keys of data which are to be mapped to the view.
     */
    private String[] from;

    /**
     * Ids of the layout elements which the data is mapped to.
     */
    private int[] to;

    /**
     * Long click listener added to items of the view made by the adapter.
     */
    private AdapterView.OnItemLongClickListener onItemLongClickListener;

    /**
     * Change listener added to checkboxes in items of the view made by the adapter.
     */
    private CheckBoxChangeListener checkBoxChangeListener;

    /**
     * Parameterized constructor.
     *
     * @param context Data context of the activity.
     * @param data Data which will be shown by the adapter.
     * @param layout Id of the layout in which the adapter shows data.
     * @param from Keys of data which are to be mapped to the view.
     * @param to Ids of the layout elements which the data is mapped to.
     */
    public CustomAdapter(Context context, List<HashMap<String, String>> data, int layout,
                         String[] from, int[] to) {
        this.context = context;
        this.data = data;
        this.layout = layout;
        this.from = from;
        this.to = to;
        inflater = LayoutInflater.from(context);
    }

    /**
     * Item long click listener setter.
     *
     * @param listener Listener to be set to items.
     */
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    /**
     * Checkbox change listener setter.
     *
     * @param listener Listener to be set to checkboxes in items.
     */
    public void setCheckBoxChangeListener(CheckBoxChangeListener listener) {
        this.checkBoxChangeListener = listener;
    }

    /**
     * Data size getter.
     *
     * @return Size of the data shown by adapter.
     */
    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * Item getter.
     *
     * @param position Position of the item whose data we want within the adapter's data set.
     * @return Item on the specified position in the list.
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    /**
     * Item id getter.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return Id of the item on the specified position in the list.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * View getter.
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return Generated view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        // create view and reference its elements
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();

            // map checkboxes based on layout element id
            if (layout == R.layout.ingredient_list_item) {
                viewHolder.checkBox = convertView.findViewById(R.id.item_is_available);
            } else {
                viewHolder.checkBox = convertView.findViewById(R.id.item_is_considered);
            }

            viewHolder.checkBox.setFocusable(false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // get item and remove listener from checkbox
        HashMap<String, String> item = data.get(position);
        viewHolder.checkBox.setOnCheckedChangeListener(null);

        // set checkboxes check value based on data
        String booleanValue = item.get(from[2]);
        boolean isChecked = Boolean.parseBoolean(booleanValue);
        viewHolder.checkBox.setChecked(isChecked);

        // set change listeners on checkboxes
        viewHolder.checkBox.setOnCheckedChangeListener((buttonView, checked) -> {
            // update the data with the new checkbox state
            item.put(from[2], String.valueOf(checked));

            // notify the listener if set
            if (checkBoxChangeListener != null) {
                checkBoxChangeListener.onCheckBoxChanged(position, checked);
            }
        });

        // set long click listeners on items
        convertView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(
                        null, v, position, getItemId(position));
                return true;
            }
            return false;
        });

        // set values for each view based on the mappings
        for (int i = 0; i < from.length; i++) {
            String key = from[i];
            int viewId = to[i];
            String value = item.get(key);

            if (value != null) {
                TextView textView = convertView.findViewById(viewId);
                textView.setText(value);
            }
        }

        // remove checkbox text by overwriting it
        viewHolder.checkBox.setText("");

        return convertView;
    }

    /**
     * Updates list generated by adapter by setting new data and triggering the list update.
     *
     * @param items New data to be mapped by the adapter.
     */
    public void updateDataList(List<HashMap<String, String>> items) {
        data = items;
        notifyDataSetChanged();
    }

    /**
     * Subclass for checkboxes.
     */
    private static class ViewHolder {
        CheckBox checkBox;
    }

    /**
     * Interface for checkbox listeners.
     */
    public interface CheckBoxChangeListener {
        void onCheckBoxChanged(int position, boolean isChecked);
    }
}
