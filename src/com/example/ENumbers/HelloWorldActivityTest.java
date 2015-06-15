package com.example.ENumbers;

import junit.framework.Test;
import junit.framework.TestCase;

import java.util.ArrayList;
//import static org.junit.Assert.*;

/**
 * Created by y.belyaeva on 04.06.2015.
 */

public class HelloWorldActivityTest extends TestCase {

//    @Test
//    public void testAdd1Plus1()
//    {
//        int x  = 1 ; int y = 1;
//        assertEquals(2, myClass.add(x,y));
//    }

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
        String recived = instance.GetInfoByENumber("E123");
        assertTrue(!recived.isEmpty());
        System.out.println("i am here");
    }
}
    //Test running startedTest running failed: Unable to find instrumentation info for: ComponentInfo{com.example.ENumbers/android.test.InstrumentationTestRunner}
//public class HelloWorldActivityTest extends TestCase {
//    protected double fValue1;
//    protected double fValue2;
//
//    public HelloWorldActivityTest(String add) {
//
//    }
//
//    protected void setUp() {
//        fValue1= 2.0;
//        fValue2= 3.0;
//    }
//
//    public void testAdd() {
//        double result= fValue1 + fValue2;
//        assertTrue(result == 5.0);
//    }
//
//    public static void main(String[] args) {
//        TestCase test= new HelloWorldActivityTest("add") {
//            public void runTest() {
//                testAdd();
//            }
//        };
//        test.run();
//    }


//}