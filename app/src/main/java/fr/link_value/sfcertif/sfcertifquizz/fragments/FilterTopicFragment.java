package fr.link_value.sfcertif.sfcertifquizz.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.link_value.sfcertif.sfcertifquizz.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilterTopicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterTopicFragment extends Fragment {

    public static final String ARG_TITLE = "title";
    private String mTitle;
    private TextView mTitleView;


    public FilterTopicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Parameter 1.
     * @return A new instance of fragment FilterTopicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterTopicFragment newInstance(String title) {
        FilterTopicFragment fragment = new FilterTopicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter_topic, container, false);
        mTitleView = (TextView) view.findViewById(R.id.topic_title);
        mTitleView.setText(mTitle);

        return view;
    }
}
