package com.example.enumberdata;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Iuliia on 08.08.2015.
 */
public class ENumbersServiceImpl implements ENumbersService {

    private Collection<ENumber> data;

    public ENumbersServiceImpl (Collection<ENumber> data)
    {
        this.data = data;
    }

    @Override
    public void reformatAdditionalInfo()
    {
        extractTypicalProducts();

        //TODO
        extractBadForChildrenItems(); //HACSG , ets
        deleteDublicateInfo(); //colors ,  not permited, ets
        addInfoInsteadSeeXXX(); //see 554 should be replaced by text
    }

    @Override
    public void extractTypicalProducts() {

        for (ENumber item : data)
        {
            extractTypicalProducts(item);
        }
    }

    public void extractTypicalProducts(ENumber  eNumber) {
        Collection<String> patterns = new HashSet<String>(Arrays.asList(
                "Typical products include",
                "Typical products are",
                "Typically used in"
        ));

        String typicalProducts;

        for(String pattern : patterns)
        {
            if (eNumber.getAdditionalInfo().toLowerCase().contains(pattern.toLowerCase()))
            {
                String subString = eNumber.getAdditionalInfo().substring(
                        eNumber.getAdditionalInfo().indexOf(pattern) + pattern.length(), //after pattern
                        eNumber.getAdditionalInfo().indexOf(". ", eNumber.getAdditionalInfo().indexOf(pattern))); //before "."

                typicalProducts = subString;


                String h = "";
            }
        }
    }
    private void addInfoInsteadSeeXXX() {

    }

    private void deleteDublicateInfo() {

    }

    private void extractBadForChildrenItems() {
    }





}
