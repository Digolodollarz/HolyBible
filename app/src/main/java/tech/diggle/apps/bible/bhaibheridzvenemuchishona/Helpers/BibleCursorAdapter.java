package tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;

/**
 * Created by DiggleDollarz on 4/12/2016.
 */

public class BibleCursorAdapter extends SimpleCursorAdapter {
    public BibleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
    }
}
