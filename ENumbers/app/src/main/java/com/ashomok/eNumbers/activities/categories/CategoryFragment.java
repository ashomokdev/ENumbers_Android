package com.ashomok.eNumbers.activities.categories;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ashomok.eNumbers.R;

/**
 * Created by iuliia on 8/8/16.
 */
public class CategoryFragment extends Fragment {

    private static final String ARGUMENT_START_NUMBER = "arg_start_number";
    private static final String ARGUMENT_END_NUMBER = "arg_end_number";
    private static final String ARGUMENT_TITLE_RESOURCE_ID = "arg_title_resource_id";

    private int startNumber;
    private int endNumber;
    private int titleResourceID;


    public static Fragment newInstance(CategorySettings settings) {
        CategoryFragment categoryFragment = new CategoryFragment();

        Bundle arguments = new Bundle();

        arguments.putInt(ARGUMENT_START_NUMBER, settings.getStartNumber());
        arguments.putInt(ARGUMENT_END_NUMBER, settings.getEndNumber());
        arguments.putInt(ARGUMENT_TITLE_RESOURCE_ID, settings.getTitleResourceID());

        categoryFragment.setArguments(arguments);
        return categoryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startNumber = getArguments().getInt(ARGUMENT_START_NUMBER);
        endNumber = getArguments().getInt(ARGUMENT_END_NUMBER);
        titleResourceID = getArguments().getInt(ARGUMENT_TITLE_RESOURCE_ID);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        //set background
//        View view = inflater.inflate(R.layout.music_fragment, null);
//        view.findViewById(R.id.music_fragment).setBackgroundColor(getResources().getColor(backgroundPattern));
//
//        //set image
//        ImageView imageView = (ImageView) view.findViewById(R.id.image);
//        imageView.setImageDrawable(getResources().getDrawable(image));
//
//        return view;
//    }
}
