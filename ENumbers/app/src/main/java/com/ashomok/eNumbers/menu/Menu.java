package com.ashomok.eNumbers.menu;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iuliia on 8/8/16.
 */
public class Menu {

    public static List<Row> getRows() {
        List<Row> menuItems = new ArrayList<>();
        menuItems.add(new Row(R.drawable.ic_view_list_white_36dp, R.string.categories));
        menuItems.add(new Row(R.drawable.ic_search, R.string.search_by_names));
        menuItems.add(new Row(R.drawable.ic_info_outline_white_36dp, R.string.about));
        menuItems.add(new Row(R.drawable.ic_star_white_36dp, R.string.rate));
        if (Settings.isFree) {
            menuItems.add(new Row(R.drawable.ic_attach_money_white_36dp, R.string.adsfree_version));
        }
        return menuItems;
    }
}
