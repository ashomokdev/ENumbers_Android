package com.ashomok.eNumbers.menu;

/**
 * Created by iuliia on 8/8/16.
 */
public class Row {
    private int icon;
    private int text;

    public Row(int icon, int text) {
        this.icon = icon;
        this.text = text;
    }

    int getIconResourceID() {
        return icon;
    }

    int getTextResourceID() {
        return text;
    }
}
