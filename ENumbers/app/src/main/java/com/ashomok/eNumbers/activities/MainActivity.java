package com.ashomok.eNumbers.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.Settings;
import com.ashomok.eNumbers.ad.AdContainer;
import com.ashomok.eNumbers.ad.AdMobContainerImpl;
import com.ashomok.eNumbers.menu.ItemClickListener;
import com.ashomok.eNumbers.menu.Menu;
import com.ashomok.eNumbers.menu.Row;
import com.ashomok.eNumbers.menu.RowsAdapter;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActionBarDrawerToggle toggle;

    private ListView mDrawerList;

    private DrawerLayout mDrawerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity_layout);

        ViewGroup parent = findViewById(R.id.start_activity_parent);
        AdContainer adContainer = new AdMobContainerImpl(this);
        adContainer.initAd(parent);

        ImageView menuHeader = findViewById(R.id.image_menu);
        if (!Settings.isFree) {
            menuHeader.setImageResource(R.drawable.menu_header_premium);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Left menu settings
        try {
            mDrawerLayout = findViewById(R.id.drawer_layout);
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

            mDrawerList = findViewById(R.id.lv_navigation_drawer);

            List<Row> menuItems = Menu.getRows();

            mDrawerList.setAdapter(new RowsAdapter(this, menuItems));

            mDrawerList.setOnItemClickListener(new DrawerItemClickListener(this));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
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
        Log.d(TAG, "onBackPressed()" + getFragmentManager().getBackStackEntryCount());
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else {
            ExitDialogFragment exitDialogFragment = ExitDialogFragment.newInstance(R.string.exit_dialog_title);

            exitDialogFragment.show(getFragmentManager(), "dialog");
        }

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        private ItemClickListener itemClickListener;


        DrawerItemClickListener(Context context) {
            itemClickListener = new ItemClickListener(context);
        }

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {

            itemClickListener.onRowClicked(position);

            // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);
            RelativeLayout layout = findViewById(R.id.menu);
            mDrawerLayout.closeDrawer(layout);
        }
    }
}