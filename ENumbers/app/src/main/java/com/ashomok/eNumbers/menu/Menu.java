package com.ashomok.eNumbers.menu;

import com.ashomok.eNumbers.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iuliia on 8/8/16.
 */
public class Menu {

   private static List<Row> menuItems;

   public static List<Row> getRows()
   {
       menuItems = new ArrayList<>();
       menuItems.add(new Row(R.drawable.ic_view_list_white_36dp, R.string.categories));
       menuItems.add(new Row(R.drawable.ic_info_outline_white_36dp, R.string.about));
       menuItems.add(new Row(R.drawable.ic_star_white_36dp, R.string.rate));
       menuItems.add(new Row(R.drawable.ic_play_icon_36_white, R.string.more_apps));

       //todo
//       menuItems.add(new Row(R.drawable.ic_camera_alt_36dp, R.string.buy_no_ads_version));

       return menuItems;
   }
}
