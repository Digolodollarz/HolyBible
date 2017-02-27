package tech.diggle.apps.bible.bhaibheridzvenemuchishona.Fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.BibleDBHelper;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.BibleDataContract;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.ReadCursorAdapter;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowChapterFragment extends DialogFragment {
    int book;
    int chapter;
    int startVerse;
    Context context;
    ListView listView;
    private String bookName;

    public ShowChapterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_show_chapter, container, false);
        Bundle args = getArguments();
        if (args == null) {
            return null;
        } else {
            book = args.getInt(BibleDataContract.BOOK);
            chapter = args.getInt(BibleDataContract.CHAPTER);
            startVerse = args.getInt("START_VERSE");
            bookName = args.getString("BOOK_NAME");
            if (book == 0 || chapter == 0)
                return null;
        }

        context = getContext();
        listView = (ListView) view.findViewById(R.id.listViewRead);
        new GetVerses().execute();
        ((TextView)view.findViewById(R.id.tvTitle)).setText(bookName+" "+chapter);
        (view.findViewById(R.id.buttonClose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });

        return view;
    }

    private class GetVerses extends AsyncTask<Void, Void, Cursor> {
        final String[] from = new String[]{"v", "t"};
        final int[] to = new int[]{R.id.tvVerseNumber, R.id.tvVerse};

        @Override
        protected Cursor doInBackground(Void... contexts) {
            BibleDBHelper db = new BibleDBHelper(context);
            return db.getVersesByInt(book, chapter);
        }

        @Override
        protected void onPostExecute(Cursor verses) {
            ReadCursorAdapter adapter = new ReadCursorAdapter(getContext(),
                    R.layout.read_view_verse,
                    verses,
                    from,
                    to,
                    0);
            listView.setAdapter(adapter);

            listView.post(new Runnable() {
                @Override
                public void run() {
                    //call smooth scroll
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        listView.smoothScrollToPositionFromTop(startVerse - 1, 0);
                    }
                }
            });
        }


    }

    public static DialogFragment newInstance() {
        DialogFragment frag = new ShowChapterFragment();
        frag.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialogFullSize);
        return frag;
    }

}
