package tech.diggle.apps.bible.bhaibheridzvenemuchishona.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.BibleDBHelper;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.ReadCursorAdapter;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ReadFragment extends DialogFragment {

    private OnFragmentInteractionListener mListener;
    public String testament;

    private ReadCursorAdapter adapter;
    Cursor verses;
    int startVerse = 0;
    BibleDBHelper db;
    ListView listView;
    String bookName;
    int chapter;
    final String[] from = new String[]{"v", "t"};
    final int[] to = new int[]{R.id.tvVerseNumber};//, R.id.tvVerse};
    private LinearLayout nav;
    //spinner spintoolbar//
    private Toolbar toolbar;
    private Spinner spinner_book;
    private Spinner spinner_chapter;
    FloatingActionButton next;
    FloatingActionButton prev;
    Context context;

    //spinner spintoolbar
    public ReadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_read, container, false);

        Bundle args = getArguments();
        if (args != null) {
            bookName = args.getString("BOOK");
            chapter = args.getInt("CHAPTER");
            startVerse = args.getInt("VERSE");
        } else {
            try {
                this.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        bookName = (bookName == null || bookName.isEmpty()) ? "John" : bookName;
        chapter = chapter < 0 ? 15 : chapter;

        getActivity().setTitle(bookName + " " + chapter);
        context = getContext();

        new LoadVerses().execute();
//        db = new BibleDBHelper(getContext());
        //db.setBibleTextTable(sharedPref.getString(getString(R.string.language_key), "t_kjv"));
        //db.setBooksKeyTable(sharedPref.getString(getString(R.string.books_key), "key_english"));
//        verses = db.getVerses(bookName, chapter);
//        adapter = new ReadCursorAdapter(getContext(),
//                Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB?R.layout.read_view_verse:R.layout.list_item_book,
//                verses,
//                from,
//                to,
//                0);

//        new QueryDBTask().doInBackground(bookName, chapter+"");
        listView = (ListView) view.findViewById(R.id.listViewRead);
        db = new BibleDBHelper(context);
        //<editor-fold desc="Set long item click listener">
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    getActivity().getMenuInflater().inflate(R.menu.cab_read_fragment, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }

                @SuppressLint("NewApi")
                @Override
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                    //<editor-fold desc="Collect the items">
                    SparseBooleanArray checked = listView.getCheckedItemPositions();
                    String toShareItems = bookName + " " + chapter + "\n";
                    int startVerse = 0, endVerse = 0;
                    Boolean isFirst = true;
                    boolean isLast = false;
                    for (int i = 0; i < checked.size(); ++i) {
                        if (checked.valueAt(i)) {
                            if (isFirst) {
                                isFirst = false;
                                startVerse = checked.keyAt(i) + 1;
                            }
                            endVerse = checked.keyAt(i) + 1;
                            int pos = checked.keyAt(i);
                            toShareItems += adapter.getVerse(pos) + "\n";
                        }
                    }
                    //</editor-fold>
                    if (menuItem.getItemId() == R.id.items_share) {
                        //<editor-fold desc="Share The Items">
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, toShareItems);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        actionMode.finish();
                        if (getDialog() != null)
                            getDialog().cancel();
                        //</editor-fold>
                    } else if (menuItem.getItemId() == R.id.items_add_note) {
//                        getActivity().startActivity(new Intent(getContext(), DialogActivity.class));
//                        private Dialog myTextDialog() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        // Get the layout inflater
                        LayoutInflater inflater = getActivity().getLayoutInflater();

                        // Inflate and set the layout for the dialog
                        // Pass null as the parent view because its going in the dialog layout
                        final int finalStartVerse = startVerse;
                        final int finalEndVerse = endVerse;
                        View dialogView = inflater.inflate(R.layout.dialog_add_note, null);
                        final EditText etNoteTitle = (EditText) dialogView.findViewById(R.id.etNoteTitle);
                        final EditText etNote = (EditText) dialogView.findViewById(R.id.etNote);
                        builder.setView(dialogView)
                                // Add action buttons
                                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Add the note
                                        String noteTitle = etNoteTitle.getText().toString();
                                        String note = etNote.getText().toString();
                                        Long saveNote = db.saveNote(
                                                noteTitle,
                                                note,
                                                db.getBookId(bookName),
                                                chapter,
                                                finalStartVerse,
                                                finalEndVerse
                                        );
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (getDialog() != null)
                                            getDialog().cancel();
                                    }
                                })
//                                .setTitle("Add Note")
                                .show();
                        actionMode.finish();
                    }

                    return true;
                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {

                }
            });
        } else {
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    String verse = ((TextView) view.findViewById(R.id.tvBook)).getText().toString();//((TextView)view.findViewById(R.id.tvVerse)).getText().toString();
//                int verseNumber = Integer.parseInt(((TextView)view.findViewById(R.id.tvVerseNumber)).getText().toString());
                    String shareText = bookName + " " + chapter + " : " + verse;

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                    return false;
                }
            });
        }
        //</editor-fold>


        next = (FloatingActionButton) view.findViewById(R.id.fabNextChapter);
        prev = (FloatingActionButton) view.findViewById(R.id.fabPreviousChapter);


        //<editor-fold desc="Navigate through chapters fast">
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NextChapter().execute();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chapter--;
                if (chapter > 0) {
//                    verses = db.getVerses(bookName, chapter);
//                    adapter.swapCursor(verses);
                    new LoadVerses().execute();
                    getActivity().setTitle(bookName + " " + chapter);
                } else {
                    chapter++;
                    Toast.makeText(getContext(), "Start", Toast.LENGTH_SHORT).show();
                }
            }
        });

//</editor-fold>


        //        spinner_book = (Spinner)view.findViewById(R.id.spinnerBook);
//        spinner_chapter = (Spinner)view.findViewById(R.id.spinnerChapter);
//        addItemsToBooksSpinner();

//        Button button = (Button) view.findViewById(R.id.buttonBla);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                Intent dbmanager = new Intent(getActivity(), AndroidDatabaseManager.class);
//                startActivity(dbmanager);
//            }
//        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static DialogFragment newInstance() {
        DialogFragment frag = new ReadFragment();
        frag.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFullSize);
        return frag;
    }

    //<editor-fold desc="Populating the spinners TODO: Actually do that!">
    // add items into spinner dynamically
//    public void addItemsToBooksSpinner() {
//        Cursor booksCursor = db.getBooks();
//        ArrayList<String> bookNamesArray= new ArrayList<>();
//        if (booksCursor.moveToFirst()) {
//            do {
//                bookNamesArray.add(booksCursor.getString(1)); //<< pass column index here instead of i
//            } while (booksCursor.moveToNext());
//        }
//
//       ArrayAdapter<String> booksCursorAdapter = new ArrayAdapter<>(getActivity(),
//                R.layout.custom_spinner_dropdown, android.R.id.text1, bookNamesArray);
//        booksCursorAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
//        spinner_book.setAdapter(booksCursorAdapter);
//        spinner_book.setSelection(((ArrayAdapter<String>)spinner_book.getAdapter()).getPosition(bookName));
//        spinner_book.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View v,
//                                       int position, long id) {
//                // On selecting a spinner item
//                bookName = adapterView.getItemAtPosition(position).toString();
//                verses = db.getVerses(bookName ,chapter);
//                adapter.swapCursor(verses);
//                adapter.notifyDataSetChanged();
//                // Notify the chapter list that book has changed
//                addItemsToChapterSpinner();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//            }
//        });
//    }
//
//    public void addItemsToChapterSpinner() {
//        Cursor chaptersCursor = db.getChapters(bookName);
//        ArrayList<String> chaptersArray= new ArrayList<>();
//        if (chaptersCursor.moveToFirst()) {
//            do {
//                chaptersArray.add(chaptersCursor.getString(1)); //<< pass column index here instead of i
//            } while (chaptersCursor.moveToNext());
//        }
//        ArrayAdapter<String> chaptersCursorAdapter = new ArrayAdapter<>(getActivity(),
//                R.layout.custom_spinner_dropdown, android.R.id.text1, chaptersArray);
//        chaptersCursorAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
//        spinner_chapter.setAdapter(chaptersCursorAdapter);
//        spinner_chapter.setSelection(((ArrayAdapter<String>)spinner_chapter.getAdapter()).getPosition("" + chapter));
//        spinner_chapter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View v,
//                                       int position, long id) {
//                String item = adapterView.getItemAtPosition(position).toString();
//                chapter = Integer.parseInt(item);
//                verses = db.getVerses(bookName ,chapter);
//                adapter.swapCursor(verses);
//                adapter.notifyDataSetChanged();
//                getActivity().setTitle(bookName + " " + chapter);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//            }
//        });
//    }
//
    //</editor-fold>

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class LoadVerses extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (adapter == null) {
                adapter = new ReadCursorAdapter(getContext(),
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? R.layout.read_view_verse : R.layout.list_item_book,
                        cursor,
                        from,
                        to,
                        0);
                listView.setAdapter(adapter);
            } else adapter.swapCursor(cursor);
            adapter.notifyDataSetChanged();
            listView.post(new Runnable() {
                @Override
                public void run() {
                    //call smooth scroll
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        listView.smoothScrollToPositionFromTop(startVerse - 1, 0);
                    }
                }
            });
            getActivity().setTitle(bookName + " " + chapter);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            BibleDBHelper db = new BibleDBHelper(context);
            Cursor verses = db.getVerses(bookName, chapter);
            db.close();
            return verses;
        }
    }


    private class NextChapter extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {

            if (cursor!=null) {
                if (adapter == null) {
                    adapter = new ReadCursorAdapter(getContext(),
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? R.layout.read_view_verse : R.layout.list_item_book,
                            cursor,
                            from,
                            to,
                            0);
                    listView.setAdapter(adapter);
                } else adapter.swapCursor(cursor);
                adapter.notifyDataSetChanged();
                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        //call smooth scroll
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            listView.smoothScrollToPositionFromTop(startVerse - 1, 0);
                        }
                    }
                });
                getActivity().setTitle(bookName + " " + chapter);
            } else {
                Toast.makeText(getContext(), "End of book", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected Cursor doInBackground(Void... ints) {
            BibleDBHelper db = new BibleDBHelper(context);
            chapter++;
            if (db.getChapters(bookName).getCount() >= chapter) {
                Cursor verses = db.getVerses(bookName, chapter);
                db.close();
                return verses;
            } else {
                chapter--;
                return null;
            }

        }
    }

//    private class ShowNav extends AsyncTask<Void, Void, Void>{
//        @Override
//        protected void onPreExecute() {
//            spinner_book.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    ShowNav.this.cancel(true);
//                    spinner_book.performClick();
//                    return true;
//                }
//            });
//            spinner_chapter.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    ShowNav.this.cancel(true);
//                    spinner_chapter.performClick();
//                    return true;
//                }
//            });
//
//            next.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ShowNav.this.cancel(true);
//                    chapter++;
//                    if(db.getChapters(bookName).getCount()>=chapter){
////                    verses = db.getVerses(bookName, chapter);
////                    adapter.swapCursor(verses);
//                        getActivity().setTitle(bookName + " " + chapter);
//                        spinner_chapter.setSelection(((ArrayAdapter<String>)spinner_chapter.getAdapter()).getPosition("" + chapter));
//
//                    }else {
//                        chapter--;
//                        Toast.makeText(getContext(), "End", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//            prev.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ShowNav.this.cancel(true);
//                    chapter--;
//                    if(chapter>0){
////                    verses = db.getVerses(bookName, chapter);
////                    adapter.swapCursor(verses);
//                        getActivity().setTitle(bookName + " " + chapter);
//                        spinner_chapter.setSelection(((ArrayAdapter<String>)spinner_chapter.getAdapter()).getPosition("" + chapter));
//                    }else {
//                        chapter++;
//                        Toast.makeText(getContext(), "Start", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//            Animation fadeIn = new AlphaAnimation(0, 1);
//            fadeIn.setInterpolator(new AccelerateInterpolator());
//            fadeIn.setDuration(300);
//
//            fadeIn.setAnimationListener(new Animation.AnimationListener()
//            {
//                public void onAnimationEnd(Animation animation)
//                {
//                    nav.setVisibility(View.VISIBLE);
//                }
//                public void onAnimationRepeat(Animation animation) {}
//                public void onAnimationStart(Animation animation) {}
//            });
//            nav.setVisibility(View.VISIBLE);
//            if (this.getStatus()!=Status.RUNNING)
//            nav.startAnimation(fadeIn);
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            Animation fadeOut = new AlphaAnimation(1, 0);
//            fadeOut.setInterpolator(new AccelerateInterpolator());
//            fadeOut.setDuration(300);
//            fadeOut.setAnimationListener(new Animation.AnimationListener()
//            {
//                public void onAnimationEnd(Animation animation)
//                {
//                    nav.setVisibility(View.GONE);
//                }
//                public void onAnimationRepeat(Animation animation) {}
//                public void onAnimationStart(Animation animation) {}
//            });
//            if(!this.isCancelled())
//            nav.startAnimation(fadeOut);
//        }
//
//        @Override
//        protected void onCancelled() {
//            nav.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            try {
//                Thread.sleep(4200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }
}
