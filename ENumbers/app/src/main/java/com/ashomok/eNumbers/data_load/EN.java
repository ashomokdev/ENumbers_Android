package com.ashomok.eNumbers.data_load;

import android.database.Cursor;
import android.util.Log;

import com.ashomok.eNumbers.tools.LogHelper;

import java.io.Serializable;

/**
 * Created by Iuliia on 15.09.2015.
 */
public class EN implements Serializable {

    public static final String TAG = EN.class.getSimpleName();
    private String code;
    private String name;
    private String purpose;
    private String status;
    private String additionalInfo;
    private String typicalProducts;
    private String approvedIn;
    private String bannedIn;
    private String badForChildren;
    private String dangerLevel;

    public EN(Cursor cursor) {
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
        } catch (Exception e) {
            LogHelper.e(this.getClass().getCanonicalName(), e.getMessage());
        }

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
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

    public String getBadForChildren() {
        return badForChildren;
    }

    public void setBadForChildren(String badForChildren) {
        this.badForChildren = badForChildren;
    }

    public String getDangerLevel() {
        return dangerLevel;
    }

    public void setDangerLevel(String dangerLevel) {
        this.dangerLevel = dangerLevel;
    }
}
