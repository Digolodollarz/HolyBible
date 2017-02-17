package tech.diggle.apps.bible.bhaibheridzvenemuchishona;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.BibleDBHelper;

public class SearchActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener{
    private SimpleCursorAdapter adapter;
    Cursor employees;
    BibleDBHelper db;
    ListView listView;
    String query = "Finde";
    SharedPreferences sharedPref;

    final String[] from = new String[] { "n",
            "c", "v", "t"};
    final int[] to = new int[] { R.id.tvBook, R.id.tvChapter, R.id.tvVerseNumber, R.id.tvVerseText };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        sharedPref = getApplicationContext().getSharedPreferences("mysettings",
//                Context.MODE_PRIVATE);
//        if(sharedPref.getBoolean("com.diggle.apps.themeKey", true)){
//            this.setTheme(R.style.AppThemeLight);
//        }else {
//            this.setTheme(R.style.AppTheme);
//        }
        db = new BibleDBHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Searching by: " + query, Toast.LENGTH_SHORT).show();
            employees = db.searchVerses(query); // you would not typically call this on the main thread
        }
        else{
            query = intent.getStringExtra("com.diggle.apps.bhaibheri.searchText");
            employees = db.searchVerses(query); // you would not typically call this on the main thread
        }

        this.setTitle(query);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
//        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        adapter = new SimpleCursorAdapter(this,
                R.layout.search_result_list_item,
                employees,
                from,
                to,
                0);
        listView = (ListView) findViewById(R.id.listViewSearch);
        listView.setAdapter(adapter);

        // OnCLickListener For List Items (done)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                String bookName = ((TextView)view.findViewById(R.id.tvBook)).getText().toString();
                int chapter = Integer.parseInt(((TextView)view.findViewById(R.id.tvChapter)).getText().toString());
                int verse = Integer.parseInt(((TextView)view.findViewById(R.id.tvVerseNumber)).getText().toString());
                Intent readIntent = new Intent(getApplicationContext(), MainActivity.class);
                Bundle args = new Bundle();
                args.putInt("FRAGMENT", 0xE);
                args.putString("BOOK", bookName);
                args.putInt("CHAPTER", chapter);
                args.putInt("VERSE", verse);
                readIntent.putExtras(args);
                readIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(readIntent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, SearchActivity.class)));
        searchView.setIconifiedByDefault(false);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.languageEnglish){
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putString(getString(R.string.language_key), "t_bbe");
//            editor.putString(getString(R.string.books_key), "key_english");
//            Configuration config = getBaseContext().getResources().getConfiguration();
//            Locale locale = new Locale("en");
//            Locale.setDefault(locale);
//            config.locale = locale;
//            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//            editor.putString("tech.diggle.apps.bhaibher.PREFFERED_LOCALE", "en");
//            editor.commit();
//            updateDb();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                this.recreate();
//            }
//        }else if(item.getItemId() == R.id.languageShona){
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putString(getString(R.string.language_key), "t_shona");
//            editor.putString(getString(R.string.books_key), "key_shona");
//
//            Configuration config = getBaseContext().getResources().getConfiguration();
//            Locale locale = new Locale("sn");
//            Locale.setDefault(locale);
//            config.locale = locale;
//            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//            editor.putString("tech.diggle.apps.bhaibher.PREFFERED_LOCALE", "sn");
//            editor.commit();
//            updateDb();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                this.recreate();
//            }
//        } else if(item.getItemId() == R.id.languageNdebele){
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putString(getString(R.string.language_key), "t_kjv");
//            editor.putString(getString(R.string.books_key), "key_english");
//            Configuration config = getBaseContext().getResources().getConfiguration();
//            Locale locale = new Locale("en");
//            Locale.setDefault(locale);
//            config.locale = locale;
//            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//            editor.putString("tech.diggle.apps.bhaibher.PREFFERED_LOCALE", "en");
//            editor.commit();
//            updateDb();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                this.recreate();
//            }
//        }else if(item.getItemId() == R.id.switchTheme){
//            if(((String) item.getTitle()).contains("Light")){
//                item.setTitle("NIGHT MODE");
//                SharedPreferences.Editor editor = sharedPref.edit();
//                editor.putBoolean("com.diggle.apps.themeKey", true);
//                editor.commit();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                    this.recreate();
//                }
//            }else  if(((String) item.getTitle()).contains("Dark")){
//                item.setTitle("LIGHT");
//                SharedPreferences.Editor editor = sharedPref.edit();
//                editor.putBoolean("com.diggle.apps.themeKey", false);
//                editor.commit();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                    this.recreate();
//                }
//            }
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        employees = db.searchVerses(query);
        adapter.swapCursor(employees);
        adapter.notifyDataSetChanged();
        this.query = query;
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Searching by: " + query, Toast.LENGTH_SHORT).show();
            employees = db.searchVerses(query);
            return;
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Searching by: " + query, Toast.LENGTH_SHORT).show();
            employees = db.searchVerses(query);
            return;
        }
    }

    void updateDb(){
        db = new BibleDBHelper(this);
        Cursor cursor = db.searchVerses(query);
        adapter.swapCursor(cursor);
        adapter.notifyDataSetChanged();
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuItem someMenuItem = menu.findItem(R.id.switchTheme);
//        someMenuItem.setTitle(sharedPref.getBoolean("com.diggle.apps.themeKey", false) ? "Dark Theme":"Light Theme");
//        return super.onPrepareOptionsMenu(menu);
//    }
}

