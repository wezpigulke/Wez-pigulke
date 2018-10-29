package com.wezpigulke;

import android.annotation.SuppressLint;
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

public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        DatabaseHelper myDb = new DatabaseHelper(this);

        Cursor res = myDb.getAllName_UZYTKOWNICY();
        res.moveToFirst();
        String nazwaUzytkownika = res.getString(0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView twojeImie = headerView.findViewById(R.id.isName);
        twojeImie.setText("Witaj\n" + nazwaUzytkownika);

        displaySelectedScreen(R.id.today);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int id) {

        Fragment fragment = null;

        switch(id) {
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

        if(fragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_main, fragment);
            fragmentTransaction.commit();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        switch(id) {
            case R.id.listOfProfiles:

                break;
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

}
