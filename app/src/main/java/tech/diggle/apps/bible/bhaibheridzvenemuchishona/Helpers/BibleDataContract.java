package tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers;

import android.provider.BaseColumns;

/**
 * Created by DiggeDollarz on 8/12/2016.
 */

public final class BibleDataContract {
    public static final  int    DATABASE_VERSION   = 1;
    public static final  String DATABASE_NAME      = "database.db";
    private static final String TEXT_TYPE          = " TEXT";
    private static final String COMMA_SEP          = ",";
    private static final String INT_TYPE           = " INTEGER";

    private BibleDataContract() {
    }

    public static abstract class Notes implements BaseColumns{
        public static final String START_VERSE = "startVerse";
        public static final String END_VERSE = "endVerse";
        public static final String TABLE_NAME = "notes";
        public static final String NOTE = "note";
        public static final String BOOK = "bookId";
        public static final String CHAPTER = "chapter";
        public static final String TITLE = "title";


        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                BOOK + INT_TYPE + COMMA_SEP +
                CHAPTER + INT_TYPE + COMMA_SEP +
                START_VERSE + INT_TYPE + COMMA_SEP +
                END_VERSE + INT_TYPE + COMMA_SEP +
                TITLE + TEXT_TYPE + COMMA_SEP +
                NOTE + TEXT_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

}
