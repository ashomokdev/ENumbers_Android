package com.example.ENumbers;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.example.ENumbers.HelloWorldActivityTest \
 * com.example.ENumbers.tests/android.test.InstrumentationTestRunner
 */
public class HelloWorldActivityTest extends ActivityInstrumentationTestCase2<HelloWorldActivity> {

   // private HelloWorldActivity helloWorldActivity;
   // private TextView mFirstTestText;

    public HelloWorldActivityTest() {
        super("com.example.ENumbers", HelloWorldActivity.class);
    }

    @Test
    public void testGetInfoByENumber() throws Exception {
        TestCase test = new TestCase("check getting data by ENumber") {
            public void runTest() {
                testExtractData();
            }
        };
        test.run();
    }

    public void testExtractData() {
        IGetInfoByENumber instance = new HelloWorldActivity();
        String recived = null;
        try {
           // recived = instance.GetInfoByENumber("E123");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(!recived.isEmpty());
    }

    //example of testing UI
//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//        helloWorldActivity = getActivity();
//        mFirstTestText =
//                (TextView) helloWorldActivity
//                        .findViewById(R.id.my_first_test_text_view);
//    }



}
