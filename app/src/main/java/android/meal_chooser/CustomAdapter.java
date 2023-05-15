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

public class CustomAdapter extends BaseAdapter {
    private List<HashMap<String, String>> data;
    private Context context;
    private LayoutInflater inflater;
    private int layout;
    private String[] from;
    private int[] to;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;

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

            viewHolder.checkBox.setOnCheckedChangeListener(null);
            viewHolder.checkBox.setFocusable(false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> item = data.get(position);

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

        String booleanValue;
        if (layout == R.layout.ingredient_list_item) {
            booleanValue = item.get("ingredientIsAvailable");
        } else {
            booleanValue = item.get("dishIsConsidered");
        }
        boolean isChecked = Boolean.parseBoolean(booleanValue);
        viewHolder.checkBox.setChecked(isChecked);
        viewHolder.checkBox.setText("");

        viewHolder.checkBox.setOnCheckedChangeListener((buttonView, checked) -> {
            if (layout == R.layout.ingredient_list_item) {
                System.out.println(item.get("ingredientId") + " " + checked);
                item.put("ingredientIsAvailable", String.valueOf(checked));
            } else {
                System.out.println(item.get("dishId") + " " + checked);
                item.put("dishIsConsidered", String.valueOf(checked));
            }
        });

        convertView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(null, v, position, getItemId(position));
                return true;
            }
            return false;
        });

        return convertView;
    }

    private static class ViewHolder {
        CheckBox checkBox;
    }
}
