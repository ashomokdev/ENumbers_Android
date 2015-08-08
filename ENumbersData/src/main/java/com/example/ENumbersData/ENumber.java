/**
 * Created by y.belyaeva on 28.05.2015.
 */

package com.example.ENumbersData;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class ENumber {
    private String code;
    private String name;
    private String purpose;
    private String status;

    private String additionalInfo;
    private String typicalProducts;
    private String approvedIn;
    private String bannedIn;
    private Boolean badForChildren;

    public ENumber(String code, String name, String purpose, String status) {
        this.code = code;
        this.name = name;
        this.purpose = purpose;
        this.status = status;

        additionalInfo = "";
        typicalProducts = "";
        approvedIn = "";
        bannedIn = "";
        badForChildren = false;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getStatus() {
        return status;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void AddAdditionalInfo(String info) {
        if (info.equals(""))
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
            this.additionalInfo += info;
        }
    }

    //TODO
    public void reformatAdditionalInfo()
    {
        extractTypicalProducts();
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

    private void extractTypicalProducts() {
        Collection<String> patterns = new HashSet<String>(Arrays.asList(
                "Typical products include",
                "Typical products are",
                "Typically used in"
        ));

        for(String pattern : patterns)
        {
            if (additionalInfo.toLowerCase().contains(pattern.toLowerCase()))
            {
                String subString = additionalInfo.substring(
                        additionalInfo.indexOf(pattern) + pattern.length(), //after pattern
                        additionalInfo.indexOf(". ", additionalInfo.indexOf(pattern))); //before "."

                typicalProducts = subString;


                String h = "";
            }
        }
    }




}
