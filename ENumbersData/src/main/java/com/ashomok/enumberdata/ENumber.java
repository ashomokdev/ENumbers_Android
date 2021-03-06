/**
 * Created by y.belyaeva on 28.05.2015.
 */

package com.ashomok.enumberdata;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Enumbers")
public class ENumber {

    @DatabaseField(generatedId = true, columnName = "_id")
    int id;

    @DatabaseField(canBeNull = false)
    private String code;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField
    private String purpose;

    @DatabaseField
    private String status;

    @DatabaseField
    private String additionalInfo;

    @DatabaseField
    private String typicalProducts;

    @DatabaseField
    private String approvedIn;

    @DatabaseField
    private String bannedIn;

    @DatabaseField
    private Boolean badForChildren;

    public DangerLevel getDangerLevel() {
        return dangerLevel;
    }

    @DatabaseField
    private DangerLevel dangerLevel;

    private Boolean avoidIt;

    public enum DangerLevel { safe, medium, hight, unknown}

    public void setApprovedIn(String approvedIn) {
        this.approvedIn = approvedIn;
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

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setBadForChildren(boolean badForChildren) {
        this.badForChildren = badForChildren;
    }

    public void setDangerLevel(DangerLevel dangerLevel) {
        this.dangerLevel = dangerLevel;
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

    public Boolean getAvoidIt() {
        return avoidIt;
    }

    public ENumber() {
        // ORMLite needs a no-arg constructor
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
