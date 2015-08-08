package com.example.enumberdata;

import org.testng.TestNG;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;

/**
 * Created by Iuliia on 08.08.2015.
 */

@Test
public class ENumbersServiceTest extends TestNG {

    private ArrayList<ENumber> eNumbers;
    private ENumbersServiceImpl eNumberService;

    @BeforeClass
    public void init() {
        eNumbers = new ArrayList<ENumber>();
        ENumber eNumber = new ENumber(
                "100",
                "Curcumin (from turmeric)",
                "Color Yellow-orange",
                "Approved in the EU.");
        eNumber.AddAdditionalInfo("Animal genes damage, possible risk to conception and cancer. JECFA still evaluating re reproductive toxicity. Orange-yellow colour; derived from the root of the curcuma plant, but can be artificially produced; used in cheese, margarine, baked sweets and fish fingers It has beneficial effect on the blood sugar in diabetics. It can increase the liver's secretion of bile and protect the liver from toxic substances. Curcumin may be used to compensate for fading of natural colouring in pre-packed foods. Recognised as an anti-carcinogenic agent during laboratory tests. Typical products include fish fingers, fizzy drinks, butter and other dairy produce, cakes and biscuits, margarine, processed cheese, curry powder, cooking oil, sweets, cereals and sauces. Orange-yellow color, derived from turmeric, a member of the ginger family.");
        eNumbers.add(eNumber);
        eNumberService = new ENumbersServiceImpl(eNumbers);
    }

    @Test
    public void testInitURLProcessor() {
//TODO test somithing here
        eNumberService.extractTypicalProducts();

    }
}
