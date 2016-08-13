package com.ashomok.eNumbers.activities.categories;

/**
 * Created by iuliia on 8/8/16.
 */
class Row {

    private int startNumber;
    private int endNumber;
    private int titleResourceID;

    public int getStartNumber() {
        return startNumber;
    }

    public int getEndNumber() {
        return endNumber;
    }

    public int getTitleResourceID() {
        return titleResourceID;
    }

    public Row(int startNumber, int endNumber, int titleResourceID)
    {
        this.startNumber = startNumber;
        this.endNumber = endNumber;
        this.titleResourceID = titleResourceID;
    }
}
