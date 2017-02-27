package tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers;

import java.util.Calendar;

/**
 * Created by DiggeDollarz on 18/2/2017.
 */

public class Singleton {
    private static Singleton mInstance = null;
    private Boolean isDarkMode;

    private Singleton(){
        isDarkMode = (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 21 || Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 6);
    }

    public static Singleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new Singleton();
        }
        return mInstance;
    }

    public Boolean getDarkMode(){
        return this.isDarkMode;
    }

    public void setDarkMode(Boolean value){
        this.isDarkMode = value;
    }

    public void toggleDarkMode() {
        isDarkMode =! isDarkMode;
    }
}
