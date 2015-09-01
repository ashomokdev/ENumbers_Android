package com.example.eNumbers;

import android.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;

/**
 * Created by Iuliia on 30.08.2015.
 */
public class AboutFragment extends Fragment{
    private TextView mTextView_appName;
    private TextView mTextView_developer;
    private TextView mTextView_version;



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.about_fragment, container, false);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 200;
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                //"Right to Left"
                                getActivity().getFragmentManager().popBackStack();
                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                //"Left to Right"
                                getActivity().getFragmentManager().popBackStack();
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });


        mTextView_appName = (TextView) view.findViewById(R.id.appName);
        mTextView_appName.setText(R.string.appName);

        mTextView_developer = (TextView) view.findViewById(R.id.developer);
        mTextView_developer.setText(R.string.developer);

        mTextView_version = (TextView) view.findViewById(R.id.version);
        mTextView_version.setText(R.string.version);
    }

    @Override
    public  void onPause()
    {
        super.onPause();
    }
}
