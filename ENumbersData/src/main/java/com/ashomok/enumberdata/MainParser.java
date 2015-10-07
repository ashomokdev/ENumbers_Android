package com.ashomok.enumberdata;

import java.util.Arrays;

/**
 * Created by y.belyaeva on 27.05.2015.
 */
public class MainParser {

    public static final String base_url = "http://en.wikipedia.org/wiki/E_number#E400.E2.80.93E499_.28thickeners.2C_stabilizers.2C_emulsifiers.29";
    public static final String url_1 = "http://www.additivealert.com.au/search.php?start=10&end=20&count=298&process=previous&flg=0";
    public static final String url_2 = "http://nac.allergyforum.com/additives/colors100-181.htm";
    public static final String url3 = "http://apcpage.com/food/e100-e181.htm";

    public static void main(String[] args) {
        new URLProcessorImpl(Arrays.asList(base_url, url_1, url_2, url3))
                .init();
    }
}
