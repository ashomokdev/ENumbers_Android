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

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getTypicalProducts() {
        return typicalProducts;
    }

    public void setTypicalProducts(String typicalProducts) {
        this.typicalProducts = typicalProducts;
    }

    public String getApprovedIn() {
        return approvedIn;
    }

    public void setApprovedIn(String approvedIn) {
        this.approvedIn = approvedIn;
    }

    public String getBannedIn() {
        return bannedIn;
    }

    public void setBannedIn(String bannedIn) {
        this.bannedIn = bannedIn;
    }

    public Boolean getBadForChildren() {
        return badForChildren;
    }

    public void setBadForChildren(Boolean badForChildren) {
        this.badForChildren = badForChildren;
    }
}
