package com.ashomok.eNumbers.menu;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.Settings;
import com.ashomok.eNumbers.activities.AboutActivity;
import com.ashomok.eNumbers.activities.SearchByNamesActivity;
import com.ashomok.eNumbers.activities.categories.CategoriesListActivity;

import static com.ashomok.eNumbers.Settings.appPackageName;
import static com.ashomok.eNumbers.Settings.appPackageName_PRO;

/**
 * Created by iuliia on 8/8/16.
 */
public class ItemClickListener {
    private final Context context;

    public ItemClickListener(Context context) {
        this.context = context;
    }

    public void onRowClicked(int position) {
        switch (position) {
            case 0:
                //Categories
                context.startActivity(
                        new Intent(context, CategoriesListActivity.class));
                break;
            case 1:
                //Search by name
                context.startActivity(
                        new Intent(context, SearchByNamesActivity.class));
                break;
            case 2:
                //About
                context.startActivity(
                        new Intent(context, AboutActivity.class));
                break;
            case 3:
                //Rate
                rate();
                break;
            case 4:
                //Support & Buy ads-free version
                buyPaidVersion();
                break;
            default:
                break;
        }
    }

    public void buyPaidVersion() {
        Toast.makeText(context, R.string.thank_you_for_your_support, Toast.LENGTH_SHORT).show();
        openPackageInMarket(appPackageName_PRO);
    }

    public void rate() {
        Toast.makeText(context, R.string.thank_you_for_your_support, Toast.LENGTH_SHORT).show();
        String appPackageName = context.getPackageName();
        openPackageInMarket(appPackageName);
    }

    private void openPackageInMarket(String appPackageName) {
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
        try {
            context.startActivity(marketIntent);
        } catch (ActivityNotFoundException exception) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
