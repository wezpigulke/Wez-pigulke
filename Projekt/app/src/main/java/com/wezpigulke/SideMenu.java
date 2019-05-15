package com.wezpigulke;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wezpigulke.go_to.GoToAuthor;
import com.wezpigulke.go_to.GoToDoctors;
import com.wezpigulke.go_to.GoToMeasurement;
import com.wezpigulke.go_to.GoToMedicine;
import com.wezpigulke.go_to.GoToNotes;
import com.wezpigulke.go_to.GoToProfiles;
import com.wezpigulke.go_to.GoToReminder;
import com.wezpigulke.go_to.GoToSettings;
import com.wezpigulke.go_to.GoToToday;
import com.wezpigulke.go_to.GoToTypeMeasurement;
import com.wezpigulke.go_to.GoToVisit;
import com.wezpigulke.go_to.GoToWelcome;
import com.wezpigulke.notification.BootCompletedNotificationReceiver;
import com.wezpigulke.other.OnClearFromRecentService;

public class SideMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Cursor cursor;
    private DatabaseHelper myDb;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        startService(new Intent(getBaseContext(), OnClearFromRecentService.class));

        myDb = new DatabaseHelper(this);

        cursor = myDb.getAllData_UZYTKOWNICY();
        if (cursor.getCount() == 0) {
            Intent cel = new Intent(this, GoToWelcome.class);
            startActivity(cel);
        }
        if (cursor != null) cursor.close();

        cursor = myDb.getStatus_CZYZAMKNIETA();

        if(cursor!=null) {
            cursor.moveToNext();
            if(cursor.getInt(0)==1) {
                Intent intent = new Intent(getApplicationContext(), BootCompletedNotificationReceiver.class);
                getApplicationContext().sendBroadcast(intent);
            }
        }

        myDb.updateStatus_CZYZAMKNIETA(0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView headerName = headerView.findViewById(R.id.isName);
        headerName.setText("Weź\npigułkę");

        cursor = myDb.getAllData_PRZYPOMNIENIE();

        if (cursor.getCount() == 0) {
            displaySelectedScreen(R.id.reminder);
        } else displaySelectedScreen(R.id.today);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {

    }

    @SuppressLint("ShowToast")
    @Override
    protected void onDestroy() {
        if (cursor != null) cursor.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int id) {

        Fragment fragment = null;

        switch (id) {
            case R.id.today:
                fragment = new GoToToday();
                break;
            case R.id.reminder:
                fragment = new GoToReminder();
                break;
            case R.id.listOfMedicine:
                fragment = new GoToMedicine();
                break;
            case R.id.listOfDoctors:
                fragment = new GoToDoctors();
                break;
            case R.id.listOfVisit:
                fragment = new GoToVisit();
                break;
            case R.id.listOfTests:
                fragment = new GoToMeasurement();
                break;
            case R.id.listOfTestsType:
                fragment = new GoToTypeMeasurement();
                break;
            case R.id.listOfNotes:
                fragment = new GoToNotes();
                break;
            case R.id.listOfProfiles:
                fragment = new GoToProfiles();
                break;
            case R.id.settings:
                fragment = new GoToSettings();
                break;
            case R.id.aboutAuthor:
                fragment = new GoToAuthor();
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_main, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

}
