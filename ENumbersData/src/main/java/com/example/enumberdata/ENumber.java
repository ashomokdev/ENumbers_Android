/**
 * Created by y.belyaeva on 28.05.2015.
 */

package com.example.enumberdata;

public class ENumber {


    private String code;
    private String name;
    private String purpose;

    private String status;
    private String additionalInfo;
    private String typicalProducts;
    private String approvedIn;
    private String bannedIn;
    private boolean badForChildren;
    private boolean avoidIt;


    public void setApprovedIn(String approvedIn) {
        this.approvedIn = approvedIn;
    }

    public void setBadForChildren(Boolean badForChildren) {
        this.badForChildren = badForChildren;
    }

    public void setBannedIn(String bannedIn) {
        this.bannedIn = bannedIn;
    }

    public void setTypicalProducts(String typicalProducts) {
        this.typicalProducts = typicalProducts;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAvoidIt(boolean avoidIt) {
        this.avoidIt = avoidIt;
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

    public String getTypicalProducts() {
        return typicalProducts;
    }

    public String getApprovedIn() {
        return approvedIn;
    }

    public String getBannedIn() {
        return bannedIn;
    }

    public Boolean getBadForChildren() {
        return badForChildren;
    }

    public boolean getAvoidIt() {
        return avoidIt;
    }


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
        avoidIt = false;
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


}
