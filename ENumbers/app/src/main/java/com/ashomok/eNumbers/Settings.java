package com.ashomok.eNumbers;

/**
 * Created by iuliia on 7/30/16.
 */
public class Settings {
    public static final boolean isAdActive = !BuildConfig.PAID_VERSION;
    public static final boolean isFree = isAdActive;
    public static final String appPackageName_PRO = "com.ashomok.eNumbers_pro";
    public static final String appPackageName = "com.ashomok.eNumbers";
    public static final String PRIVACY_POLICY_LINK = "https://sites.google.com/view/e-numbers-privacy-policy/home";
}
