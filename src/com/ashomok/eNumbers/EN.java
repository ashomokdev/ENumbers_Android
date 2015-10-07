package com.ashomok.eNumbers;

import android.database.Cursor;
import android.util.Log;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Iuliia on 15.09.2015.
 */
public class EN implements Serializable {
    public EN (Cursor cursor) {
        try {
            this.setCode(cursor.getString(cursor.getColumnIndex(ENumbersSQLiteAssetHelper.COLUMN_NAME_CODE)));
            this.setName(cursor.getString(cursor.getColumnIndex(ENumbersSQLiteAssetHelper.COLUMN_NAME_NAME)));
            this.setPurpose(cursor.getString(cursor.getColumnIndex(ENumbersSQLiteAssetHelper.COLUMN_NAME_PURPOSE)));
            this.setStatus(cursor.getString(cursor.getColumnIndex(ENumbersSQLiteAssetHelper.COLUMN_NAME_STATUS)));
            this.setAdditionalInfo(cursor.getString(cursor.getColumnIndex(ENumbersSQLiteAssetHelper.COLUMN_NAME_ADDITIONAL_INFO)));
            this.setApprovedIn(cursor.getString(cursor.getColumnIndex(ENumbersSQLiteAssetHelper.COLUMN_NAME_APPROVED_IN)));
            this.setBannedIn(cursor.getString(cursor.getColumnIndex(ENumbersSQLiteAssetHelper.COLUMN_NAME_BANNED_IN)));
            this.setTypicalProducts(cursor.getString(cursor.getColumnIndex(ENumbersSQLiteAssetHelper.COLUMN_NAME_TYPICAL_PRODUCTS)));
            this.setDangerLevel(cursor.getString(cursor.getColumnIndex(ENumbersSQLiteAssetHelper.COLUMN_NAME_DANGER_LEVEL)));
            this.setBadForChildren(cursor.getString(cursor.getColumnIndex(ENumbersSQLiteAssetHelper.COLUMN_NAME_BAD_FOR_CHILDREN)));
        }
        catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage() + e.getStackTrace().toString());
        }
    }

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

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setBadForChildren(String badForChildren) {
        this.badForChildren = badForChildren;
    }

    public void setDangerLevel(String dangerLevel) {
        this.dangerLevel = dangerLevel;
    }


    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    private int id;

    @Expose
    private String code;

    @Expose
    private String name;

    @Expose
    private String purpose;

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

    public String getBadForChildren() {
        return badForChildren;
    }

    public String getDangerLevel() {
        return dangerLevel;
    }

    @Expose
    private String status;

    @Expose
    private String additionalInfo;

    @Expose
    private String typicalProducts;

    @Expose
    private String approvedIn;

    @Expose
    private String bannedIn;

    @Expose
    private String badForChildren;

    @Expose
    private String dangerLevel;
}
