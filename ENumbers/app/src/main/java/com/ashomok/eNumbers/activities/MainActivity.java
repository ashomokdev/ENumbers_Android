package com.ashomok.eNumbers.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ashomok.eNumbers.R;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle toggle;

    private ListView mDrawerList;

    private DrawerLayout mDrawerLayout;

    private CharSequence mTitle;

    private String[] mMenuArray;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.start_activity_layout);

        Fragment fragment = new MainFragment();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        //Left menu settings
        try {
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            toggle = new ActionBarDrawerToggle(
                    this,
                    mDrawerLayout,
                    toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close) {
            };

            toggle.setDrawerIndicatorEnabled(true);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setHomeButtonEnabled(true);
            }

            // Set the drawer toggle as the DrawerListener
            mDrawerLayout.setDrawerListener(toggle);

            mMenuArray = getResources().getStringArray(R.array.main_menu_array);
            mDrawerList = (ListView) findViewById(R.id.lv_navigation_drawer);
            mDrawerList.setAdapter(new ArrayAdapter<>(
                    this,
                    R.layout.drawer_list_item,
                    mMenuArray));

            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mTitle);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void selectItem(int position) {
        switch (position) {
            case 0:
                //leave feedback
                leaveFeedback();
                break;
            case 1:
                //About
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void leaveFeedback() {
        String appPackageName = getPackageName();
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
        //Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.gm"));
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(marketIntent);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {

            selectItem(position);

            // Highlight the selected item, update the title, and close the drawer
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.start_activity_layout_linear);
            mDrawerLayout.closeDrawer(linearLayout);
        }
    }
}



