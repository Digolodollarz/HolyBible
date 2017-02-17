package tech.diggle.apps.bible.bhaibheridzvenemuchishona;

import android.Manifest;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;

import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Fragments.BookListFragment;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Fragments.ChapterListFragment;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Fragments.DevotionalFragment;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Fragments.HymnsFragment;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Fragments.NotesFragment;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Fragments.ReadFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ReadFragment.OnFragmentInteractionListener,
        DevotionalFragment.OnFragmentInteractionListener,
        BookListFragment.OnFragmentInteractionListener,
        ChapterListFragment.OnFragmentInteractionListener {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                View thisView = findViewById(android.R.id.content);
                Snackbar.make(thisView, "This is used to automatically set Day / Night mode. We DO NOT read your location",
                        Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                0xA);
                    }
                })
                        .show();


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        0xA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        Bundle args = getIntent().getExtras();

        if (args != null) {
            Fragment fragment;
            int openFragmentId = args.getInt("FRAGMENT", -1);
            if (openFragmentId == 0xA) {
//                FragmentManager fragmentManager = getSupportFragmentManager();
                // Create a new Fragment to be placed in the activity layout
                //DevotionalFragment devotionalFragment
                fragment = new DevotionalFragment();

                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments

            } else if (openFragmentId == 0xB) {
                fragment = new BookListFragment();
            } else if (openFragmentId == 0xC) {
                fragment = new ChapterListFragment();
            } else if (openFragmentId == 0xD) {
                fragment = new HymnsFragment();
            } else if (openFragmentId == 0xE) {
                //read intent
                fragment = new ReadFragment();
            } else if (openFragmentId == 0xF) {
                fragment = new NotesFragment();
            } else {
                fragment = new DevotionalFragment();
            }
            fragment.setArguments(args);
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, fragment).commit();

        } else {
            Fragment fragment = new DevotionalFragment();
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, fragment).commit();
        }

        context = this;

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
            }
            mBackPressed = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }


    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Get the SearchView and set the searchable configuration
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchActivity.class)));
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName())); //TODO: Test
        searchView.setQueryHint(getResources().getString(R.string.hint_search));

        this.setTitle(getString(R.string.app_name));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        int modeType = AppCompatDelegate.getDefaultNightMode();

//        noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Toast.makeText(context, "Settings Clicked", Toast.LENGTH_SHORT).show();
//            return true;
//        } else
//        if (id == R.id.action_night_mode) {
//            if (modeType == AppCompatDelegate.MODE_NIGHT_AUTO || modeType == AppCompatDelegate.MODE_NIGHT_YES) {
//                // Set Default Day Mode
////                Toast.makeText(context, "Night Mode: "+  AppCompatDelegate.getDefaultNightMode(), Toast.LENGTH_SHORT).show();
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                Toast.makeText(context, "Night Mode Disabled", Toast.LENGTH_SHORT).show();
//            } else if (modeType == AppCompatDelegate.MODE_NIGHT_NO) {
//                // Set Default Night Mode
////                Toast.makeText(context, "Night Mode: "+  AppCompatDelegate.getDefaultNightMode(), Toast.LENGTH_SHORT).show();
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                Toast.makeText(context, "Night Mode ON", Toast.LENGTH_SHORT).show();
//            }
//            reCreate();
//        } else
        if (id == R.id.action_language_shona) {
            SharedPreferences sharedPref = getSharedPreferences("mysettings",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.language_key), "t_shona");
            editor.putString(getString(R.string.books_key), "key_shona");
            Configuration config = getBaseContext().getResources().getConfiguration();
            Locale locale = new Locale("sn");
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            editor.putString("PREFERRED_LOCALE", "sn");
            editor.commit();
            Toast.makeText(context, "Mutauro Tachinja", Toast.LENGTH_SHORT).show();
            reCreate();
        } else if (id == R.id.action_language_english) {
            SharedPreferences sharedPref = getSharedPreferences("mysettings",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.language_key), "t_bbe");
            editor.putString(getString(R.string.books_key), "key_english");
            Configuration config = getBaseContext().getResources().getConfiguration();
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            editor.putString("PREFERRED_LOCALE", "en");
            editor.commit();
            Toast.makeText(context, "Language Switched", Toast.LENGTH_SHORT).show();
            reCreate();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        int id = item.getItemId();

        if (id == R.id.nav_bible_nt) {
            // Create fragment and give it an argument specifying the testament it should show
            BookListFragment newFragment = new BookListFragment();
            Bundle args = new Bundle();
            args.putString("TESTAMENT", "New");
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, newFragment);
            transaction.commit();
        } else if (id == R.id.nav_bible_ot) {
            // Create fragment and give it an argument specifying the testament it should show
            BookListFragment newFragment = new BookListFragment();
            Bundle args = new Bundle();
            args.putString("TESTAMENT", "Old");
            newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, newFragment);
            transaction.commit();
        } else if (id == R.id.nav_devotional) {
            // Create fragment and give it an argument specifying the testament it should show
            DevotionalFragment newFragment = new DevotionalFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, newFragment);
            transaction.commit();
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=tech.diggle.apps.bible.bhaibheridzvenemuchishona");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
//        } else if (id == R.id.nav_hymnal) {
//            HymnsFragment newFragment = new HymnsFragment();
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.content_frame, newFragment);
//            transaction.commit();
        } else if (id == R.id.nav_about) {
            Uri uri = Uri.parse("http://apps.diggle.tech/bhaibheri");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (id == R.id.nav_notes) {

            NotesFragment newFragment = new NotesFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, newFragment);
//            transaction.addToBackStack(null);

// Commit the transaction
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0xA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void reCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.recreate();
        } else {
            this.finish();
            startActivity(getIntent());
            startActivity(new Intent(this, this.getClass()));
        }
    }
}
