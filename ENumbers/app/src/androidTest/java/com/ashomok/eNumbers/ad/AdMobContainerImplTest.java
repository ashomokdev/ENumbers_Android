package com.ashomok.eNumbers.ad;

import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.activities.ENDetailsActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by iuliia on 1/27/17.
 */
public class AdMobContainerImplTest {
    private static final String TAG = AdMobContainerImplTest.class.getSimpleName();
    @Rule
    public ActivityTestRule<ENDetailsActivity> mActivityRule = new ActivityTestRule<>(
            ENDetailsActivity.class, true, true);

    @Test
    public void testAdVisibility() {

        AdView ad = mActivityRule.getActivity().findViewById(R.id.ad_banner);
        ad.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.i(TAG, "ad loaded");
                Assert.assertNotNull(ad);
                onView(withId(R.id.ad_banner)).check(matches(isDisplayed()));
            }
        });

    }
}