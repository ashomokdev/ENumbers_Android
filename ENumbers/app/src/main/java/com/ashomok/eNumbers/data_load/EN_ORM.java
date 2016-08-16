package com.ashomok.eNumbers.data_load;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by iuliia on 8/16/16.
 */
public class EN_ORM {

    private static final String TAG = EN_ORM.class.getSimpleName();
    private static EN_ORM ourInstance;
    private List<EN> allEnumbs;

    private EN_ORM(Context context) {

        ENumbersSQLiteAssetHelper db = new ENumbersSQLiteAssetHelper(context);

        allEnumbs = new ArrayList<>();

        Cursor cursor = db.selectAllData();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                EN enumb = new EN(cursor);
                allEnumbs.add(enumb);
                cursor.moveToNext();
            }
        }
    }

    public static EN_ORM getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new EN_ORM(context);
        }
        return ourInstance;
    }

    /**
     * Get all data.
     *
     * @return
     */
    public List<EN> getAllEnumbs() {
        return allEnumbs;
    }

    /**
     * Get data by codes. Example: E100, E123
     *
     * @param codes
     * @return
     */
    public List<EN> getEnumbsByCodeArray(String[] codes) {
        final List<String> codesList = new ArrayList<>(Arrays.asList(codes));
        List<EN> result = new ArrayList<>();

        for (EN en : allEnumbs) {
            if (codesList.contains(en.getCode())) {
                result.add(en);
            }
        }

        return result;
    }

    /**
     * Get data by code range. Example: 100-150
     *
     * @param startValue
     * @param endValue
     * @return
     */
    public List<EN> getEnumbsByCodeRange(int startValue, int endValue) {
        //todo
      //  return null;
        return getAllEnumbs();
    }

    /**
     * Get data by inputing contains both codes and names. Example E100, Curcumin, Alcanin, E123
     *
     * @param codeOrName
     * @return
     */
    public List<EN> getEnumbsbyCodeAndNameArray(String[] codeOrName) {
        //// TODO: 8/16/16
        return null;
    }
}
