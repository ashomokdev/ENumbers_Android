package com.ashomok.eNumbers.activities.categories;

import java.io.Serializable;

/**
 * Created by iuliia on 8/8/16.
 */
class Row implements Serializable {

    public static final String TAG = Row.class.getSimpleName();

    private int startNumber;
    private int endNumber;
    private int titleResourceID;

    int getStartNumber() {
        return startNumber;
    }

    int getEndNumber() {
        return endNumber;
    }

    int getTitleResourceID() {
        return titleResourceID;
    }

    public Row(int startNumber, int endNumber, int titleResourceID)
    {
        this.startNumber = startNumber;
        this.endNumber = endNumber;
        this.titleResourceID = titleResourceID;
    }
}
