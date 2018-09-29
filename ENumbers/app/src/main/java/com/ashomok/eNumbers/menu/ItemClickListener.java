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

    @SuppressWarnings("deprecation")
    public void moreApps() {
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

    public void rate() {
        Toast.makeText(context, R.string.thank_you_for_your_support, Toast.LENGTH_SHORT).show();
        String appPackageName = context.getPackageName();
        openPackageInMarket(appPackageName);
    }

    @SuppressWarnings("deprecation")
    public void share()
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        Resources res = context.getResources();
        String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
        String sharedBody = String.format(res.getString(R.string.share_message), link);

        Spanned styledText;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            styledText = Html.fromHtml(sharedBody,Html.FROM_HTML_MODE_LEGACY);
        } else {
            styledText = Html.fromHtml(sharedBody);
        }

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, res.getString(R.string.i_want_to_recommend));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, styledText);
        context.startActivity(Intent.createChooser(sharingIntent, res.getString(R.string.share_via)));
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
