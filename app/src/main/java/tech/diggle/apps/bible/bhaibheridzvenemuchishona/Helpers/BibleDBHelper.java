package tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers;

/**
 * Created by DiggleDollarz on 9/8/2016.
 */

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private static final int DATABASE_VERSION = 31;
    private boolean needsUpgrade = false;
    private String bibleTextTable;// = "t_bbe";
    private String booksKeyTable;// = "key_english";
    private String titleColumn = "header";
    private String devotionalColumn = "text";
    private String verseColumn = "verse";
    private String devotionalTable = "devotional";
    private String TEMP_DATABASE_NAME = "bible-data-new.db";
//    private static final String FTS_VIRTUAL_TABLE = "FTS";
//    private String fts_table;

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
        booksKeyTable = sharedPref.getString(context.getString(R.string.books_key), "key_shona");
        bibleTextTable = sharedPref.getString(context.getString(R.string.language_key), "t_shona");
//        TODO: Initialise the FTS4 tables
//        fts_table = sharedPref.getString("fts_key", "fts_t_english");
        Log.d("Initialising :" + bibleTextTable, "key: " + booksKeyTable);
        SQLiteDatabase db = getWritableDatabase();
//        setForcedUpgrade(50);
//        SQLiteDatabase db = getWritableDatabase();
        if (needsUpgrade) {
            upgradeDB();
//            createFtsTable();
        }
//        if (sharedPref.getBoolean("databaseNotInitialised", true)) {
//            try {
//                createFtsTable();
//                sharedPref.edit().putBoolean("databaseNotInitialised", false).apply();
//            } catch (Exception e) {
//                Log.d("BibleDBHelper","Error Initialising database");
//                e.printStackTrace();
//            }
//        }
//        mAssetPath = "databases" + "/" + DATABASE_NAME;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Save the database as needing upgrade, then upgrade later.
        needsUpgrade = true;
        Log.i("DiggleTechApps", "Database NOT YET Upgrading to new version");
    }

    private boolean upgradeDB() {
        Log.i("DiggleTechApps", "Database Upgrading to newVersion");
        SQLiteDatabase db = getWritableDatabase();
        try {
            copyDatabaseFromAssets();
            String path = mContext.getDatabasePath(TEMP_DATABASE_NAME).getPath();
//            db.setTransactionSuccessful();
//            db.endTransaction();
            db.execSQL("ATTACH '" + path + "' AS TEMPo");
//            db.beginTransaction();
            db.delete("devotional", null, null);
            db.execSQL("INSERT or REPLACE INTO devotional SELECT * FROM TEMPo.devotional");
            db.delete("t_bbe", null, null);
            db.execSQL("INSERT OR REPLACE INTO t_bbe SELECT * FROM TEMPo.t_bbe");
            db.delete("t_shona", null, null);
            db.execSQL("INSERT OR REPLACE INTO t_shona SELECT * FROM TEMPo.t_shona");
        } catch (SQLiteAssetException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
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
        try {
            String[] sqlSelect = {"0 _id", "n"};
            //String sqlTables = "key_english";
            qb.setTables(booksKeyTable);
            Cursor c = qb.query(db, sqlSelect, null, null,
                    null, null, null);
            c.moveToFirst();
            return c;
        } catch (SQLiteAssetException e) {
            Log.d("getBooks", "SQLiteAsset error");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            Log.d("getBooks", "Exception thrown");
            e.printStackTrace();
            return null;
        }
    }

    public Cursor getVerses(String bookName, int chapter) {
        return getVersesByInt(getBookId(bookName), chapter);
    }

    public Cursor getVersesByInt(int book, int chapter) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"0 _id", "v", "t"};
        String whereToGet = "b = '" + book + "' AND c = '" + chapter + "'";
        Log.d("getVerses Text: " + bibleTextTable, "key: " + booksKeyTable);
        qb.setTables(bibleTextTable);

        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor c = qb.query(db, sqlSelect, whereToGet, null,
                    null, null, null);
            Log.d("getVerses", c.moveToFirst() ? "Successfully Retrieved" : "Error Retrieving Verses");
            return c;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            db.close();
        }

    }

    public Cursor getChapters(String book) {
//        TODO: Use book id
        return (getChaptersByInt(getBookId(book)));
    }

    public Cursor getChaptersByInt(int book) {
//        TODO: Use book id

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setDistinct(true);
        String[] sqlSelect = {"0 _id", "c", booksKeyTable + ".n"};
        String whereToGet = bibleTextTable + ".b = '" + book + "'";// booksKeyTable + ".n = '" + localBook + "'";
        qb.setTables(bibleTextTable + " INNER JOIN " + booksKeyTable + " ON " + bibleTextTable + ".b = " + booksKeyTable + ".b");
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor c = qb.query(db, sqlSelect, whereToGet, null,
                    null, null, null);
            Log.d("getChapterByInt", c.moveToFirst() ? "Chapters loaded" : "Error loading chapters");
            return c;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
        try {
            Cursor c =
                    qb.query(db, sqlSelect, whereToGet, null,
                            null, null, null);
            c.moveToFirst();
            return c;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] getDevotional(int date) {
        String[] devotional = new String[3];
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"0 _id", titleColumn, verseColumn, devotionalColumn};
        String orderBy = "date";

        String whereToGet = "date = '" + (date) + "'";
        qb.setTables(devotionalTable);
        Cursor c = new Cursor() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public int getPosition() {
                return 0;
            }

            @Override
            public boolean move(int i) {
                return false;
            }

            @Override
            public boolean moveToPosition(int i) {
                return false;
            }

            @Override
            public boolean moveToFirst() {
                return false;
            }

            @Override
            public boolean moveToLast() {
                return false;
            }

            @Override
            public boolean moveToNext() {
                return false;
            }

            @Override
            public boolean moveToPrevious() {
                return false;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean isBeforeFirst() {
                return false;
            }

            @Override
            public boolean isAfterLast() {
                return false;
            }

            @Override
            public int getColumnIndex(String s) {
                return 0;
            }

            @Override
            public int getColumnIndexOrThrow(String s) throws IllegalArgumentException {
                return 0;
            }

            @Override
            public String getColumnName(int i) {
                return null;
            }

            @Override
            public String[] getColumnNames() {
                return new String[0];
            }

            @Override
            public int getColumnCount() {
                return 0;
            }

            @Override
            public byte[] getBlob(int i) {
                return new byte[0];
            }

            @Override
            public String getString(int i) {
                return null;
            }

            @Override
            public void copyStringToBuffer(int i, CharArrayBuffer charArrayBuffer) {

            }

            @Override
            public short getShort(int i) {
                return 0;
            }

            @Override
            public int getInt(int i) {
                return 0;
            }

            @Override
            public long getLong(int i) {
                return 0;
            }

            @Override
            public float getFloat(int i) {
                return 0;
            }

            @Override
            public double getDouble(int i) {
                return 0;
            }

            @Override
            public int getType(int i) {
                return 0;
            }

            @Override
            public boolean isNull(int i) {
                return false;
            }

            @Override
            public void deactivate() {

            }

            @Override
            public boolean requery() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public void registerContentObserver(ContentObserver contentObserver) {

            }

            @Override
            public void unregisterContentObserver(ContentObserver contentObserver) {

            }

            @Override
            public void registerDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public void setNotificationUri(ContentResolver contentResolver, Uri uri) {

            }

            @Override
            public Uri getNotificationUri() {
                return null;
            }

            @Override
            public boolean getWantsAllOnMoveCalls() {
                return false;
            }

            @Override
            public void setExtras(Bundle bundle) {

            }

            @Override
            public Bundle getExtras() {
                return null;
            }

            @Override
            public Bundle respond(Bundle bundle) {
                return null;
            }
        };
        try {
            c = qb.query(db, sqlSelect, whereToGet, null,
                    null, null, null, "1");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c.moveToFirst()) {
                devotional[0] = c.getString(c.getColumnIndex(titleColumn));
                devotional[1] = c.getString(c.getColumnIndex(verseColumn));
                devotional[2] = c.getString(c.getColumnIndex(devotionalColumn));
            } else {
                devotional[0] = "Nothing for Today";
                devotional[1] = "Nothing has been found in the database. " +
                        "Please check the Play Store to get an updated version of the database" +
                        "with a new devotional";
            }
        }
        return devotional;
    }

    public int getBookId(String bookName) {
        SQLiteDatabase db = getReadableDatabase();
        int rValue;
        try {
            Cursor c = db.rawQuery("SELECT b from " +
                    booksKeyTable +
                    " WHERE n = '" + bookName + "'", null);
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
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            db.close();
        }

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
// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BOOK, book);
        values.put(NOTE, note);
        values.put(START_VERSE, startVerse);
        values.put(END_VERSE, endVerse);
        values.put(CHAPTER, chapter);
        values.put(TITLE, noteTitle);

// Insert the new row, returning the primary key value of the new row
        try {
            db.execSQL(BibleDataContract.Notes.CREATE_TABLE);
            return db.insert(BibleDataContract.Notes.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            db.close();
        }
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
            return notes;
        } catch (SQLException e) {
            e.printStackTrace();
            SQLiteDatabase dbReadable = getWritableDatabase();
            dbReadable.execSQL(BibleDataContract.Notes.CREATE_TABLE);
            return null;
        }


    }

    String getBookName(int bookId) {
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
    private String mAssetPath1;

    private void copyDatabaseFromAssets() throws SQLiteAssetException {
        Log.w(TAG, "copying database from assets...");
        String mDatabasePath = mContext.getApplicationInfo().dataDir + "/databases";
        String sourcePath = "databases" + "/" + DATABASE_NAME;
        String dest = mDatabasePath + "/" + TEMP_DATABASE_NAME;
        InputStream is;
        boolean isZip = false;

        try {
            // try uncompressed
            is = mContext.getAssets().open(sourcePath);
        } catch (IOException e) {
            // try zip
            try {
                is = mContext.getAssets().open(sourcePath + ".zip");
                isZip = true;
            } catch (IOException e2) {
                // try gzip
                try {
                    is = mContext.getAssets().open(sourcePath + ".gz");
                } catch (IOException e3) {
                    SQLiteAssetException se = new SQLiteAssetException("Missing " + sourcePath + " file (or .zip, .gz archive) in assets, or target folder not writable");
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

    //<editor-fold desc="Create and search from FTS4 table">
    //    Provide FTS4 on devices with API level greater than 11 only
//    TODO: Add the search functionality to devices below the API. Probably never gonna happen ;)
    private void createFtsTable() {
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("CREATE VIRTUAL TABLE fts_t_shona USING fts4 (content='t_shona', v)");
//        db.execSQL("INSERT INTO fts_t_shona(fts_t_shona) VALUES('rebuild')");
//        db.execSQL("CREATE VIRTUAL TABLE fts_t_english USING fts4 (content='t_bbe', v)");
//        db.execSQL("INSERT INTO fts_t_english(fts_t_english) VALUES('rebuild')");
    }

//    public Cursor fullTextSearch(String search) {
//        SQLiteDatabase db = getReadableDatabase();
//        String queryArgs[] = {search.trim().replaceAll(" ", " NEAR/2 ")};
//        Log.d("Search Term :", queryArgs[0]);
////        StringBuilder queryArgsSb = new StringBuilder("");
////        String[] searchTerms = search.split(" ");
////        if (searchTerms.length > 1) {
////            queryArgsSb.append(searchTerms[0]);
////            List<String> thisPne = Arrays.asList(searchTerms);
////            thisPne.remove(0);
////            searchTerms = (thisPne).toArray(new String[0]);
////            for (String searchTerm : searchTerms) {
////                queryArgsSb.append(searchTerm);
////            }
////        } else {
////            queryArgs = search;
////        }
//        Cursor cursor = db.rawQuery("SELECT n , v FROM "
//                            +fts_table+ " WHERE " +fts_table+
//                            " MATCH ?", queryArgs);
//        cursor.close();
//        return cursor;
//    }
    //</editor-fold>
}
