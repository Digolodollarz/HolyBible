package tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

import tech.diggle.apps.bible.bhaibheridzvenemuchishona.R;

/**
 * Created by DiggeDollarz on 4/12/2016.
 */

public class ReadCursorAdapter extends SimpleCursorAdapter {
    private final String[] from;

    public ReadCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.from = from;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB?
                (TextView) view.findViewById(R.id.tvVerseNumber):
                (TextView) view.findViewById(R.id.tvBook);
        String text = "";
        for (String field : from) {
            if (text.isEmpty())
                text += cursor.getString(cursor.getColumnIndex(field));
            else
                text += ". " + cursor.getString(cursor.getColumnIndex(field));
        }
        textView.setText(text);

    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    public String getVerse(int position){
        Cursor cursor = (Cursor) super.getItem(position);
//        SparseArrayCompat<String> returnArray = new SparseArrayCompat<>();
//        returnArray.put((int)cursor.getShort(cursor.getColumnIndex(from[0])),
//                cursor.getString(cursor.getColumnIndex(from[1])));
        return cursor.getShort(cursor.getColumnIndex(from[0]))+cursor.getString(cursor.getColumnIndex(from[1]));
    }

//    public SparseArrayCompat getVerse(int position){
//        Cursor cursor = (Cursor) super.getItem(position);
//        SparseArrayCompat<String> returnArray = new SparseArrayCompat<>();
//        returnArray.put((int)cursor.getShort(cursor.getColumnIndex(from[0])),
//                cursor.getString(cursor.getColumnIndex(from[1])));
//        return returnArray;
//    }
}
