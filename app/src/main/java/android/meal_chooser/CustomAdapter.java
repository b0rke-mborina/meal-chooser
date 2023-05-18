package android.meal_chooser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CustomAdapter extends BaseAdapter {
    private List<HashMap<String, String>> data;
    private Context context;
    private LayoutInflater inflater;
    private int layout;
    private String[] from;
    private int[] to;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;
    private CheckBoxChangeListener checkBoxChangeListener;

    public CustomAdapter(Context context, List<HashMap<String, String>> data, int layout, String[] from, int[] to) {
        this.context = context;
        this.data = data;
        this.layout = layout;
        this.from = from;
        this.to = to;
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    public void setCheckBoxChangeListener(CheckBoxChangeListener listener) {
        this.checkBoxChangeListener = listener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();

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

        HashMap<String, String> item = data.get(position);

        viewHolder.checkBox.setOnCheckedChangeListener(null);

        String booleanValue = item.get(from[2]);
        boolean isChecked = Boolean.parseBoolean(booleanValue);
        viewHolder.checkBox.setChecked(isChecked);

        viewHolder.checkBox.setOnCheckedChangeListener((buttonView, checked) -> {
            // Update the data with the new checkbox state
            item.put(from[2], String.valueOf(checked));

            // Notify the listener if set
            if (checkBoxChangeListener != null) {
                checkBoxChangeListener.onCheckBoxChanged(position, checked);
            }
        });

        convertView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(null, v, position, getItemId(position));
                return true;
            }
            return false;
        });

        // Set values for each view based on the mappings
        for (int i = 0; i < from.length; i++) {
            String key = from[i];
            int viewId = to[i];
            String value = item.get(key);

            if (value != null) {
                TextView textView = convertView.findViewById(viewId);
                textView.setText(value);
            }
        }

        viewHolder.checkBox.setText("");

        return convertView;
    }

    private static class ViewHolder {
        CheckBox checkBox;
    }

    public interface CheckBoxChangeListener {
        void onCheckBoxChanged(int position, boolean isChecked);
    }
}
