package com.ashomok.eNumbers.ad;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.util.Log;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.activities.ENDetailsActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by iuliia on 1/27/17.
 */
public class AdMobContainerImplTest {
    private static final String TAG = AdMobContainerImplTest.class.getSimpleName();
    @Rule
    public ActivityScenarioRule<ENDetailsActivity> mActivityRule = new ActivityScenarioRule<>(
            ENDetailsActivity.class);

    @Test
    public void testAdVisibility() {
        mActivityRule.getScenario().onActivity(activity -> {
            AdView ad = activity.findViewById(R.id.ad_banner);
            ad.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    Log.i(TAG, "ad loaded");
                    Assert.assertNotNull(ad);
                    onView(withId(R.id.ad_banner)).check(matches(isDisplayed()));
                }
            });
        });
    }
}