package com.ashomok.eNumbers.activities.categories;

import android.app.Fragment;

import com.ashomok.eNumbers.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iuliia on 8/8/16.
 */
class CategoriesFragmentFactory {

    public static Map<Integer, CategorySettings> settingsList = new HashMap<Integer, CategorySettings>() {{
        put(0, new CategorySettings(100, 199, R.string.colours));
        put(1, new CategorySettings(200, 299, R.string.preservatives));
        put(2, new CategorySettings(300, 399, R.string.antioxidants));
        put(3, new CategorySettings(400, 499, R.string.thickeners));
        put(4, new CategorySettings(500, 599, R.string.pH_regulators));
        put(5, new CategorySettings(600, 699, R.string.flavour_enhancers));
        put(6, new CategorySettings(700, 799, R.string.antibiotics));
        put(7, new CategorySettings(900, 999, R.string.miscellaneous));
        put(8, new CategorySettings(1000, 1599, R.string.additional_chemicals));
    }};

    static Fragment newInstance(int page) {

        Fragment fragment = CategoryFragment.newInstance(
                settingsList.get(page));
        return fragment;
    }
}
