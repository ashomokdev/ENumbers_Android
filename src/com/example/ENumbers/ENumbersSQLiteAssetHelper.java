package com.example.eNumbers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import android.widget.Toast;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Iuliia on 06.09.2015.
 */
public class ENumbersSQLiteAssetHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "Enumbers.db";
    private static final int DATABASE_VERSION = 1;

    public static final String COLUMN_NAME_ID = "0 _id"; //It is returning the value of 0 with the column name _id (it's using column name alias). You can return a column that doesn't exist in your table this way.
    public static final String COLUMN_NAME_CODE = "code";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_PURPOSE = "purpose";
    public static final String COLUMN_NAME_STATUS = "status";
    public static final String COLUMN_NAME_ADDITIONAL_INFO = "additionalInfo";
    public static final String COLUMN_NAME_APPROVED_IN = "approvedIn";
    //  public static final String COLUMN_NAME_AVOID_IT = "avoidIt"; //reduntant
    public static final String COLUMN_NAME_BAD_FOR_CHILDREN = "badForChildren";
    public static final String COLUMN_NAME_BANNED_IN = "bannedIn";
    public static final String COLUMN_NAME_TYPICAL_PRODUCTS = "typicalProducts";
    public static final String COLUMN_NAME_DANGER_LEVEL = "dangerLevel";

    public ENumbersSQLiteAssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor selectAllData() {
        try {
            SQLiteDatabase db = getReadableDatabase();
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

            String[] sqlSelect = {COLUMN_NAME_ID, COLUMN_NAME_CODE, COLUMN_NAME_NAME, COLUMN_NAME_PURPOSE, COLUMN_NAME_STATUS};
            String sqlTables = "Enumbers";

            qb.setTables(sqlTables);

            Cursor c = qb.query(db, sqlSelect, null, null,
                    null, null, null);

            c.moveToFirst();
            return c;
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage() + e.getStackTrace().toString());
        }
        return null;

    }
}
