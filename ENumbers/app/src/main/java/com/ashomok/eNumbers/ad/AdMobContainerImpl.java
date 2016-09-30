package com.ashomok.eNumbers.ad;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.Settings;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by iuliia on 7/26/16.
 */
public class AdMobContainerImpl implements AdContainer {


    public static final String appID = "ca-app-pub-5221781428763224~7737821599";

    private static final String TAG = AdMobContainerImpl.class.getSimpleName();
    private final Activity context;
    private InterstitialAd mInterstitialAd;

    public AdMobContainerImpl(Activity context) {
        this.context = context;

    }

    @Override
    public void initAd(boolean isAdActive) {
        if (isAdActive) {
            if (context.getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
                //init banner

                MobileAds.initialize(context.getApplicationContext(), appID);
                AdView mAdView = (AdView) context.findViewById(R.id.adBannerView);

                AdRequest adRequest;

                adRequest = new AdRequest.Builder().build();

                mAdView.loadAd(adRequest);
            }
            else
            {
                AdView mAdView = (AdView) context.findViewById(R.id.adBannerView);
                mAdView.setVisibility(View.GONE);
            }

//            //init interstitial
//            mInterstitialAd = new InterstitialAd(context);
//            mInterstitialAd.setAdUnitId(context.getResources().getString(R.string.interstitial_ad_unit_id));
//
//            mInterstitialAd.setAdListener(new AdListener() {
//                @Override
//                public void onAdClosed() {
//                    requestNewInterstitial();
//                }
//            });
//
//            requestNewInterstitial();
        }
    }

    @Override
    public void init() {
        initAd(Settings.isAdActive);
    }

    private void requestNewInterstitial() {

        AdRequest adRequest;

        adRequest = new AdRequest.Builder().build();

        mInterstitialAd.loadAd(adRequest);
    }
}
