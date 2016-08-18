package com.ashomok.eNumbers.menu;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.Settings;
import com.ashomok.eNumbers.activities.AboutActivity;
import com.ashomok.eNumbers.activities.categories.CategoriesListActivity;

/**
 * Created by iuliia on 8/8/16.
 */
public class RowClickListener {
    private Context context;

    public RowClickListener(Context context) {
        this.context = context;
    }

    private void leaveFeedback() {
        String appPackageName = context.getPackageName();
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
        try {
            context.startActivity(marketIntent);
        } catch (ActivityNotFoundException exception) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void onRowClicked(int position) {
        switch (position) {
            case 0:
                //Categories
                context.startActivity(
                        new Intent(context, CategoriesListActivity.class));
                break;
            case 1:
                //About
                context.startActivity(
                        new Intent(context, AboutActivity.class));
                break;
            case 2:
                //Rate
                leaveFeedback();
                break;
            case 3:
                //More our apps
                moreApps();
                break;
            case 4:
                //todo
                //Support & Buy ads-free version
                break;
            default:
                break;
        }
    }

    private void moreApps() {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://developer?id=" + Settings.devName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=" + Settings.devName)));
            } catch (android.content.ActivityNotFoundException e) {
                Toast.makeText(context, R.string.cant_open, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
