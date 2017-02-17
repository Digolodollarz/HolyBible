package tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers;

/**
 * Created by DiggleDollarz on 9/8/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipInputStream;

import tech.diggle.apps.bible.bhaibheridzvenemuchishona.R;

import static tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.BibleDataContract.Notes.BOOK;
import static tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.BibleDataContract.Notes.CHAPTER;
import static tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.BibleDataContract.Notes.END_VERSE;
import static tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.BibleDataContract.Notes.NOTE;
import static tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.BibleDataContract.Notes.START_VERSE;
import static tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.BibleDataContract.Notes.TITLE;

public class BibleDBHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "bible-data.db";
    private static final int DATABASE_VERSION = 18;
    private boolean needsUpgrade = false;
    private String bibleTextTable = "t_bbe";
    private String booksKeyTable = "key_english";
    private String titleColumn = "title_en";
    private String devotionalColumn = "en";
    private String devotionalTable = "devotional";
    private String TEMP_DATABASE_NAME = "bible-data-new.db";

    public BibleDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
//        mName = name;
//        mFactory = factory;
//        mNewVersion = version;
        // you can use an alternate constructor to specify a database location
        // (such as a folder on the sd card)
        // you must ensure that this folder is available and you have permission
        // to write to it
        //super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);
        SharedPreferences sharedPref = context.getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        booksKeyTable = sharedPref.getString(context.getString(R.string.books_key), "key_english");
        bibleTextTable = sharedPref.getString(context.getString(R.string.language_key), "t_bbe");
        Log.d("Initialising :" + bibleTextTable, "key: " + booksKeyTable);
//        setForcedUpgrade(50);
        SQLiteDatabase db = getWritableDatabase();
        if (needsUpgrade) {
            upgradeDB();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        super.onUpgrade(db, oldVersion, newVersion);
        needsUpgrade = true;
        Log.i("DiggleTechApps", "Database NOT YET Upgrading to newVersion");
//        SQLiteDatabase dbTemp = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public boolean upgradeDB() {
        Log.i("DiggleTechApps", "Database Upgrading to newVersion");
        SQLiteDatabase db = getWritableDatabase();
        try {
            copyDatabaseFromAssets();
            String path = mContext.getDatabasePath(TEMP_DATABASE_NAME).getPath();
//            db.setTransactionSuccessful();
//            db.endTransaction();
            db.execSQL("ATTACH '" + path + "' AS TEMPo");
//            db.beginTransaction();
            db.execSQL("INSERT or REPLACE INTO devotional SELECT * FROM TEMPo.devotional");
        } catch (SQLiteAssetException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    //    public void setBibleTextTable(String tableName){
//        this.bibleTextTable = tableName;
//    }
//    public void setBooksKeyTable(String tableName){
//        this.booksKeyTable = tableName;
//    }

    //done with this method yay
    public Cursor getBooks() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"0 _id", "n"};
        //String sqlTables = "key_english";
        qb.setTables(booksKeyTable);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);
        c.moveToFirst();
        return c;
    }

    //done with this method yay
    public Cursor getBooks(String testament) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"0 _id", "n"};
        //String sqlTables = "key_english";
        qb.setTables(booksKeyTable);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getVerses(String bookName, int chapter) {
        String localBook = getBookName(getBookId(bookName));
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"0 _id", "v", "t"};
        String whereToGet = booksKeyTable + ".n = '" + localBook + "' AND " +
                bibleTextTable + ".c = '" + chapter + "'";
        Log.d("getVerses Text :" + bibleTextTable, "key: " + booksKeyTable);
        qb.setTables(bibleTextTable + " INNER JOIN " + booksKeyTable + " ON " + bibleTextTable + ".b = " + booksKeyTable + ".b");
        Cursor c = qb.query(db, sqlSelect, whereToGet, null,
                null, null, null);
        c.moveToFirst();
        Log.d("THisIsALOG", "getVerses: " + c);
        return c;
    }

    public Cursor getChapters(String book) {
//        TODO: Use book id

        String localBook = getBookName(getBookId(book));
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setDistinct(true);
        String[] sqlSelect = {"0 _id", "c", booksKeyTable + ".n"};
        String whereToGet = booksKeyTable + ".n = '" + localBook + "'";
        qb.setTables(bibleTextTable + " INNER JOIN " + booksKeyTable + " ON " + bibleTextTable + ".b = " + booksKeyTable + ".b");
        Cursor c = qb.query(db, sqlSelect, whereToGet, null,
                null, null, null);
        c.moveToFirst();
        Log.d("THisIsALOG", "getVerses: " + c);
        return c;
    }

    public Cursor searchVerses(String searchText) {

        String dbName = null; //The table name to compile the query against.
        String[] columnNames = null; //A list of which table columns to return. Passing "null" will return all columns.
        String whereClause = null; //Where-clause, i.e. filter for the selection of data, null will select all data.
        String[] selectionArgs = null; //You may include ?s in the "whereClause"". These placeholders will get replaced by the values from the selectionArgs array.
        String[] groupBy = null; //A filter declaring how to group rows, null will cause the rows to not be grouped.
        String[] having = null; //Filter for the groups, null means no filter.
        String[] orderBy = null; //Table columns which will be used to order the data, null means no ordering.

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"0 _id", "t", "c", "v", "n"};

        String whereToGet = "t LIKE '%" + searchText.trim() + "%'";
        qb.setTables(bibleTextTable + " INNER JOIN " + booksKeyTable + " ON " + bibleTextTable + ".b = " + booksKeyTable + ".b");
        Cursor c =
                qb.query(db, sqlSelect, whereToGet, null,
                        null, null, null);
//                db.rawQuery(" select 0 as _id, t from t_bbe where t like '%Holy%'", null);
        c.moveToFirst();
        return c;
    }

    public String[] getDevotional(int date) {
        String[] devotional = new String[2];
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"0 _id", titleColumn, devotionalColumn};
        String orderBy = "date";

        String whereToGet = "date = '" + (date) + "'";
        qb.setTables(devotionalTable);
        Cursor c =
                qb.query(db, sqlSelect, whereToGet, null,
                        null, null, null, "1");
//                db.rawQuery(" select 0 as _id, title_en, en from devotional where date = 341", null);

        if (c.moveToFirst()) {
            devotional[0] = c.getString(c.getColumnIndex(titleColumn));
            devotional[1] = c.getString(c.getColumnIndex(devotionalColumn));
        } else {
            devotional[0] = "Nothing for Today";
            devotional[1] = "Nothing has been found in the database. " +
                    "Please check the Play Store to get an updated version of the database" +
                    "with a new devotional";
        }
//        c.close();
        return devotional;
    }

    public int getBookId(String bookName) {

        Cursor c = getReadableDatabase().rawQuery("SELECT b from " +
                booksKeyTable +
                " WHERE n = '" + bookName + "'", null);
        int rValue = 0;
        if (c.moveToFirst()) {
            rValue = c.getInt(c.getColumnIndex("b"));
        } else {
            c = getReadableDatabase().rawQuery("SELECT b from " +
                    "key_english" +
                    " WHERE n = '" + bookName + "'", null);
            rValue = !c.moveToFirst() ?
                    0 :
                    c.getInt(c.getColumnIndex("b"));
        }
        c.close();
        return rValue;

    }

    public long saveNote(String noteTitle, String note, int book, int chapter, int startVerse, int endVerse) {
//        String dbName = null; //The table name to compile the query against.
//        String[] columnNames = null; //A list of which table columns to return. Passing "null" will return all columns.
//        String whereClause = null; //Where-clause, i.e. filter for the selection of data, null will select all data.
//        String[] selectionArgs = null; //You may include ?s in the "whereClause"". These placeholders will get replaced by the values from the selectionArgs array.
//        String[] groupBy = null; //A filter declaring how to group rows, null will cause the rows to not be grouped.
//        String[] having = null; //Filter for the groups, null means no filter.
//        String[] orderBy = null; //Table columns which will be used to order the data, null means no ordering.
//
//        SQLiteDatabase db = getReadableDatabase();
//        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//        String[] sqlSelect = {"0 _id", "t", "c", "v", "n"};
//
//        String whereToGet = "t LIKE '%" + searchText + "%'";
//        qb.setTables(bibleTextTable + " INNER JOIN " + booksKeyTable + " ON " + bibleTextTable + ".b = " + booksKeyTable + ".b");
//        Cursor c =
//                qb.query(db, sqlSelect, whereToGet, null,
//                        null, null, null);
////                db.rawQuery(" select 0 as _id, t from t_bbe where t like '%Holy%'", null);
//        c.moveToFirst();
// Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(BibleDataContract.Notes.CREATE_TABLE);


// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BOOK, book);
        values.put(NOTE, note);
        values.put(START_VERSE, startVerse);
        values.put(END_VERSE, endVerse);
        values.put(CHAPTER, chapter);
        values.put(TITLE, noteTitle);

// Insert the new row, returning the primary key value of the new row
        return db.insert(BibleDataContract.Notes.TABLE_NAME, null, values);
    }

    public Cursor getNotes() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {
                BibleDataContract.Notes._ID,
                BOOK,
                CHAPTER,
                START_VERSE,
                END_VERSE,
                TITLE,
                NOTE,};
//        String orderBy = "date";

//        String whereToGet = "date = '" + (date) + "'";
        qb.setTables(BibleDataContract.Notes.TABLE_NAME);

        Cursor notes = null;
        try {
            notes = qb.query(db, sqlSelect, null, null,
                    null, null, null, "50");
        } catch (SQLException e) {
            e.printStackTrace();
            SQLiteDatabase dbReadable = getWritableDatabase();
            dbReadable.execSQL(BibleDataContract.Notes.CREATE_TABLE);
        }


        return notes;
    }

    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"mesage"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }


    }

    public String getBookName(int bookId) {
        Cursor c = getReadableDatabase().rawQuery("SELECT n from " +
                booksKeyTable +
                " WHERE b = " + bookId, null);
        String rValue = !c.moveToFirst() ?
                null :
                c.getString(c.getColumnIndex("n"));
        c.close();
        return rValue;
    }


    public boolean deleteNoteById(long noteId) {
        try {
//            return getReadableDatabase().rawQuery("DELETE FROM " +
//                    BibleDataContract.Notes.TABLE_NAME +
//                    " WHERE _id = " + noteId, null)>0;
            return getWritableDatabase().delete(BibleDataContract.Notes.TABLE_NAME,
                    "_id = " + noteId,
                    null) > 0;


        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static final String TAG = SQLiteAssetHelper.class.getSimpleName();

    private final Context mContext;


    private String mDatabasePath;

    String mAssetPath = "databases" + "/" + DATABASE_NAME;

    private void copyDatabaseFromAssets() throws SQLiteAssetException {
        Log.w(TAG, "copying database from assets...");
        mDatabasePath = mContext.getApplicationInfo().dataDir + "/databases";
        String path = mAssetPath;
        String dest = mDatabasePath + "/" + TEMP_DATABASE_NAME;
        InputStream is;
        boolean isZip = false;

        try {
            // try uncompressed
            is = mContext.getAssets().open(path);
        } catch (IOException e) {
            // try zip
            try {
                is = mContext.getAssets().open(path + ".zip");
                isZip = true;
            } catch (IOException e2) {
                // try gzip
                try {
                    is = mContext.getAssets().open(path + ".gz");
                } catch (IOException e3) {
                    SQLiteAssetException se = new SQLiteAssetException("Missing " + mAssetPath + " file (or .zip, .gz archive) in assets, or target folder not writable");
                    se.setStackTrace(e3.getStackTrace());
                    throw se;
                }
            }
        }

        try {
            File f = new File(mDatabasePath + "/");
            if (!f.exists()) {
                f.mkdir();
            }
            if (isZip) {
                ZipInputStream zis = Utils.getFileFromZip(is);
                if (zis == null) {
                    throw new SQLiteAssetException("Archive is missing a SQLite database file");
                }
                Utils.writeExtractedFileToDisk(zis, new FileOutputStream(dest));
            } else {
                Utils.writeExtractedFileToDisk(is, new FileOutputStream(dest));
            }

            Log.w(TAG, "database copy complete");

        } catch (IOException e) {
            SQLiteAssetException se = new SQLiteAssetException("Unable to write " + dest + " to data directory");
            se.setStackTrace(e.getStackTrace());
            throw se;
        }
    }
}
