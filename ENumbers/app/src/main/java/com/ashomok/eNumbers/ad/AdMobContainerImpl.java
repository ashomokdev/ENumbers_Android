package com.ashomok.eNumbers.ad;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.Settings;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/**
 * Created by iuliia on 7/26/16.
 */
public class AdMobContainerImpl implements AdContainer {

    private static final String TAG = AdMobContainerImpl.class.getSimpleName();
    private final Activity context;

    public AdMobContainerImpl(Activity context) {
        this.context = context;
    }

    private void initBottomBanner(ViewGroup parent) {
        if (parent instanceof RelativeLayout || parent instanceof LinearLayout) {
            AdView adView = new AdView(context);
            if (parent instanceof RelativeLayout) {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                adView.setLayoutParams(lp);
            } else if (parent instanceof LinearLayout) {
                adView.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.0f));
            }

            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(context.getResources().getString(R.string.banner_ad_unit_id));
            adView.setId(R.id.ad_banner);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            parent.addView(adView);
        } else {
            Log.e(TAG, "Ads can not been loaded programmaticaly. RelativeLayout and LinearLayout are supported as parent.");
        }
    }

    @Override
    public void initAd(ViewGroup parentLayout) {
        initAd(Settings.isAdActive, parentLayout);
    }

    private void initAd(boolean isAdActive, ViewGroup parentLayout) {
        if (isAdActive) {
            if (context.getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
                //init banner
                initBottomBanner(parentLayout);
            }
        }
    }
}
