package tech.diggle.apps.bible.bhaibheridzvenemuchishona;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatDelegate;

import java.util.Calendar;
import java.util.Locale;

import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.Singleton;

/**
 * Created by DiggeDollarz on 3/12/2016.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 18 && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 6)
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
//        else
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        AppCompatDelegate.setDefaultNightMode(Singleton.getInstance().getDarkMode()?AppCompatDelegate.MODE_NIGHT_YES:AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        Configuration config = getBaseContext().getResources().getConfiguration();
        String lang = sharedPref.getString("PREFERRED_LOCALE", "sn");
        Locale locale;
        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang))
        {
            locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }
}
