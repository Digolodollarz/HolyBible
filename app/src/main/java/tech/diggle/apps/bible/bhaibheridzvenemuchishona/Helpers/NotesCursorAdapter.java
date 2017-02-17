package tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.TextView;

import tech.diggle.apps.bible.bhaibheridzvenemuchishona.R;

/**
 * Created by DiggeDollarz on 8/12/2016.
 */
public class NotesCursorAdapter extends SimpleCursorAdapter {
    public NotesCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewTitle = (TextView)view.findViewById(R.id.tvTitle);
        TextView textViewNote = (TextView) view.findViewById(R.id.tvNote);

        String title = cursor.getString(cursor.getColumnIndex(BibleDataContract.Notes.TITLE))+
                " " +
                (new BibleDBHelper(context)).getBookName(cursor.getInt(cursor.getColumnIndex(BibleDataContract.Notes.BOOK)))+
                " " +
                cursor.getString(cursor.getColumnIndex(BibleDataContract.Notes.CHAPTER))+
                " vs " +
                cursor.getString(cursor.getColumnIndex(BibleDataContract.Notes.START_VERSE))+
                "-" +
                cursor.getString(cursor.getColumnIndex(BibleDataContract.Notes.END_VERSE))
                ;
        String note = cursor.getString(cursor.getColumnIndex(BibleDataContract.Notes.NOTE));


        textViewTitle.setText(title);
        textViewNote.setText(note);
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public long getNoteId(int position){
        return 87;
    }
}
