/**
 * Created by y.belyaeva on 28.05.2015.
 */

package com.example.ENumbersData;

public class ENumber {
    private String code = "";
    private String name = "";
    private String purpose = "";
    private String status = "";
    private String additionalInfo = "";

    public ENumber(String code, String name, String purpose, String status) {
        this.code = code;
        this.name = name;
        this.purpose = purpose;
        this.status = status;
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

    //TODO rename to add_additional_info
    public void AddAdditionalInfo(String info) {
        if (info.equals(""))
        {
            return;
        }

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

    //TODO
    private void reformatAdditionalInfo()
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
    }




}
