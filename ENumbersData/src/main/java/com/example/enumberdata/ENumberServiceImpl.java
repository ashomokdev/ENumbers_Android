package com.example.enumberdata;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Iuliia on 08.08.2015.
 */
public class ENumberServiceImpl implements ENumberService {

    public void addAdditionalInfo(ENumber  eNumber, String info) {
        if (info.isEmpty())
        {
            return;
        }
        else {

            if (info.endsWith(". ")) {
                //ok
            } else if (info.endsWith(".")) {
                info += " ";
            } else {
                info += ". ";
            }

            if (Character.isUpperCase(info.charAt(0))) {
                //ok
            } else {
                //to capitalize the first letter of word in a string
                info = info.substring(0, 1).toUpperCase() + info.substring(1);
            }
            eNumber.getAdditionalInfo().concat(info);
        }
    }

    //TODO
    public void reformatAdditionalInfo(ENumber eNumber)
    {
        extractTypicalProducts(eNumber);
        extractBadForChildrenItems(); //HACSG , ets
        deleteDublicateInfo(); //colors ,  not permited, ets
        addInfoInsteadSeeXXX(); //see 554 should be replaced by text
    }

    private void addInfoInsteadSeeXXX() {

    }

    private void deleteDublicateInfo() {

    }

    private void extractBadForChildrenItems() {
    }

    private void extractTypicalProducts(ENumber  eNumber) {
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


}
