package com.example.eNumbers.test;

import android.test.IsolatedContext;
import android.test.mock.MockContentResolver;
import com.example.eNumbers.ENumbersSQLiteAssetHelper;
import com.example.eNumbers.MainActivity;

import android.test.ActivityInstrumentationTestCase2;
import junit.framework.Assert;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.example.eNumbers.MainActivityTest \
 * com.example.eNumbers.tests/android.test.InstrumentationTestRunner
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {

        super("com.example.eNumbers", MainActivity.class);
    }

    public void testSQLiteAssetHelper() {

        IsolatedContext _openHelperContext = new IsolatedContext(new MockContentResolver(), getActivity());

        ENumbersSQLiteAssetHelper helper = new ENumbersSQLiteAssetHelper(_openHelperContext);

        Assert.assertTrue(helper.selectRowsByCodes(new String[]{"E100"}).getCount() == 1);

        Assert.assertTrue(helper.selectRowsByCodes(new String[]{"E100", "E123"}).getCount() == 2);
    }
}