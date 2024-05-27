package com.example.sabroremote;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;

public class SidePanelDrawer extends AppCompatActivity {

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_panel_drawer);

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav= findViewById(R.id.navmenu);

        //nav.setItemIconTintList(null);

        drawerLayout= findViewById(R.id.drawer);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomePage()).commit();
        nav.setCheckedItem(R.id.home);

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            Fragment temp;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.home:
                        temp=new HomePage();
                        break;
                    case R.id.search_device:
                        temp=new SearchDevice();
                        break;
                    case R.id.settings:
                        temp=new Settings();
                        break;
                }
                if (temp != null)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, temp).commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else
                {
                    Log.e(TAG, "Fragment instance is null");
                }

                /*getSupportFragmentManager().beginTransaction().replace(R.id.container,temp).commit();
                drawerLayout.closeDrawer(GravityCompat.START);*/
                return true;
            }
        });

    }//ON CREATE() END;

}//MAIN END