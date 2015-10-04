package com.example.eNumbers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;
import android.util.Log;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.Collections;

/**
 * Created by Iuliia on 06.09.2015.
 */
public class ENumbersSQLiteAssetHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "Enumbers.db";
    private static final int DATABASE_VERSION = 2;

    public static final String COLUMN_NAME_ID = "0 _id"; //It is returning the value of 0 with the column name _id (it's using column name alias). You can return a column that doesn't exist in your table this way.
    public static final String COLUMN_NAME_CODE = "code";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_PURPOSE = "purpose";
    public static final String COLUMN_NAME_STATUS = "status";
    public static final String COLUMN_NAME_ADDITIONAL_INFO = "additionalInfo";
    public static final String COLUMN_NAME_APPROVED_IN = "approvedIn";
    public static final String COLUMN_NAME_BANNED_IN = "bannedIn";
    public static final String COLUMN_NAME_BAD_FOR_CHILDREN = "badForChildren";
    public static final String COLUMN_NAME_TYPICAL_PRODUCTS = "typicalProducts";
    public static final String COLUMN_NAME_DANGER_LEVEL = "dangerLevel";

    private static String[] sqlSelect = {
            COLUMN_NAME_ID,
            COLUMN_NAME_CODE,
            COLUMN_NAME_NAME,
            COLUMN_NAME_PURPOSE,
            COLUMN_NAME_STATUS,
            COLUMN_NAME_ADDITIONAL_INFO,
            COLUMN_NAME_APPROVED_IN,
            COLUMN_NAME_BANNED_IN,
            COLUMN_NAME_BAD_FOR_CHILDREN,
            COLUMN_NAME_TYPICAL_PRODUCTS,
            COLUMN_NAME_DANGER_LEVEL};

    private static String sqlTable = "Enumbers";

    public ENumbersSQLiteAssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }

    public Cursor selectAllData() {
        try {
            SQLiteDatabase db = getReadableDatabase();
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

            qb.setTables(sqlTable);

            Cursor c = qb.query(db, sqlSelect, null, null,
                    null, null, null);

            c.moveToFirst();
            return c;
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage() + e.getStackTrace().toString());
        }
        return null;
    }

    //TODO close connection after method?
    public Cursor selectRowsByCodes(String[] codes) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

            qb.setTables(sqlTable);

            Cursor c = qb.query(db, sqlSelect, COLUMN_NAME_CODE + " IN (" +
                            TextUtils.join(",", Collections.nCopies(codes.length, "?")) +
                            ")", codes,
                    null, null, null); //from http://stackoverflow.com/questions/7418849/android-sqlite-in-clause-and-placeholders
            c.moveToFirst();
            return c;
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage() + e.getStackTrace().toString());
        }
        return null;
    }
}
