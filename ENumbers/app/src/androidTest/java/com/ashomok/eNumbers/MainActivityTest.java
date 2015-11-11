package com.ashomok.eNumbers;

import android.test.ActivityInstrumentationTestCase2;
import android.test.IsolatedContext;
import android.test.mock.MockContentResolver;

import com.ashomok.eNumbers.activities.MainActivity;
import com.ashomok.eNumbers.sql.ENumbersSQLiteAssetHelper;

import junit.framework.Assert;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {

        super("com.ashomok.eNumbers", MainActivity.class);
    }

    public void testSQLiteAssetHelper() {

        IsolatedContext _openHelperContext = new IsolatedContext(new MockContentResolver(), getActivity());

        ENumbersSQLiteAssetHelper helper = new ENumbersSQLiteAssetHelper(_openHelperContext);

        Assert.assertTrue(helper.selectRowsByCodes(new String[]{"E100"}).getCount() == 1);

        Assert.assertTrue(helper.selectRowsByCodes(new String[]{"E100", "E123"}).getCount() == 2);
    }

}