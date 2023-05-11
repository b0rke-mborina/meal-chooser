package android.meal_chooser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChooseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseFragment extends Fragment {

    /**
     * Key for sharing list of navigation items.
     */
    private static final String DEFAULT_TIME = "defaultTime";

    private int defaultTime;

    private EditText mInputTime;

    public ChooseFragment() {
        // Required empty public constructor
    }

    /**
     *
     *
     * @param defaultTime Last selected time limit.
     * @return Instance of the fragment.
     */
    public static ChooseFragment newInstance(int defaultTime) {
        ChooseFragment fragment = new ChooseFragment();
        Bundle args = new Bundle();
        args.putInt(DEFAULT_TIME, defaultTime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            defaultTime = getArguments().getInt(DEFAULT_TIME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose, container, false);

        mInputTime = view.findViewById(R.id.input_time);
        System.out.println(defaultTime);
        mInputTime.setText(String.valueOf(defaultTime));

        return view;
    }
}