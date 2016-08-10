package com.ashomok.eNumbers.activities.categories;

import android.app.Fragment;

import com.ashomok.eNumbers.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iuliia on 8/8/16.
 */
class CategoriesFragmentFactory {

    public static List<CategorySettings> settingsList = new ArrayList<CategorySettings>(){{
        add(new CategorySettings(100, 199, R.string.colours));
        add(new CategorySettings(200, 299, R.string.preservatives));
        add(new CategorySettings(300, 399, R.string.antioxidants));
        add(new CategorySettings(400, 499, R.string.thickeners));
        add(new CategorySettings(500, 599, R.string.pH_regulators));
        add(new CategorySettings(600, 699, R.string.flavour_enhancers));
        add(new CategorySettings(700, 799, R.string.antibiotics));
        add(new CategorySettings(900, 999, R.string.miscellaneous));
        add(new CategorySettings(1000, 1599, R.string.additional_chemicals));
    }};

    static Fragment newInstance(int page) {


        Fragment fragment = CategoryFragment.newInstance(
                settingsList.get(page));
        return fragment;
    }
}
