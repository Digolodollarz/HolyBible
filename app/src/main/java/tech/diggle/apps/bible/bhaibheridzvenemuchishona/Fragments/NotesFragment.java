package tech.diggle.apps.bible.bhaibheridzvenemuchishona.Fragments;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.BibleDBHelper;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.BibleDataContract;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.NotesCursorAdapter;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {
    ListView lvNotes;
    ArrayList<Long> toDelete;
    BibleDBHelper db;

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        lvNotes = (ListView) view.findViewById(R.id.lvNotes);

        getActivity().setTitle("Notes");

        db = new BibleDBHelper(getContext());
        //db.setBibleTextTable(sharedPref.getString(getString(R.string.language_key), "t_kjv"));
        //db.setBooksKeyTable(sharedPref.getString(getString(R.string.books_key), "key_english"));
        Cursor notes = db.getNotes();

        final String[] from = new String[]{
                BibleDataContract.Notes.BOOK,
                BibleDataContract.Notes.CHAPTER,
                BibleDataContract.Notes.START_VERSE,
                BibleDataContract.Notes.END_VERSE,
                BibleDataContract.Notes.TITLE,
                BibleDataContract.Notes.NOTE};

        final int[] to = new int[]{R.id.tvVerseNumber, R.id.tvVerse};
        final NotesCursorAdapter adapter = new NotesCursorAdapter(getContext(),
                R.layout.list_item_note,
                notes,
                from,
                to,
                0);

        lvNotes.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            lvNotes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            lvNotes.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    getActivity().getMenuInflater().inflate(R.menu.cab_notes_fragment, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }

                @SuppressLint("NewApi")
                @Override
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                    final SparseBooleanArray checked = lvNotes.getCheckedItemPositions();
                    toDelete = new ArrayList<>();
                    for (int i = 0; i < checked.size(); ++i) {
                        if (checked.valueAt(i)) {
//                            if (isFirst) {
//                                isFirst = false;
//                                startVerse = checked.keyAt(i);
//                            }
//                            endVerse = checked.keyAt(i);
                            int pos = checked.keyAt(i);
                            toDelete.add(adapter.getItemId(pos));
                            //doSomethingWith(adapter.getItem(pos));
//                                Log.d("item", adapter.getVerse(pos).toString());
//                            toShareItems += adapter.getVerse(pos) + "\n";
                        }
                    }
//                    final Long[] toDelete = (Long[])toDeleteAr.toArray();
                    if (menuItem.getItemId() == R.id.action_delete) {
//                        getActivity().startActivity(new Intent(getContext(), DialogActivity.class));
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage(R.string.confirm_delete)
                                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // FIRE ZE MISSILES!
//                                        new DeleteItems().execute(toDelete);
                                        BibleDBHelper dbHelper = new BibleDBHelper(getContext());
                                        boolean ret = false;
                                        //NotesFragment.this.checked = lvNotes.getCheckedItemPositions();
                                        for (Long aToDelete : toDelete) {
                                            ret = dbHelper.deleteNoteById(aToDelete);
                                        }
                                        if (ret){
                                            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                            Cursor notes = db.getNotes();
                                            adapter.swapCursor(notes);
                                        }
                                        else
                                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
//                                        adapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                    }
                                })
                                .show();


                        actionMode.finish();
                    }

                    return true;
                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {

                }
            });
//            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//                @Override
//                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//                                               int position, long arg3) {
//                    // TODO Auto-generated method stub
//
//                    listView.setItemChecked(position, !mAdapter.isPositionChecked(position));
//                    return false;
//                }
//            });


        } else {
            lvNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(R.string.confirm_delete)
                            .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // FIRE ZE MISSILES!
//                                        new DeleteItems().execute(toDelete);
                                    BibleDBHelper dbHelper = new BibleDBHelper(getContext());
                                    if (dbHelper.deleteNoteById(adapter.getNoteId(position))){
                                        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                        Cursor notes = db.getNotes();
                                        adapter.swapCursor(notes);
                                    }
                                    else
                                        Toast.makeText(getContext(), "Failed to delete", Toast.LENGTH_SHORT).show();
//                                        adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            })
                            .show();
                    return false;
                }
            });
        }


        return view;
    }

    private class DeleteItems extends AsyncTask<Long, Integer, Boolean> {
//        SparseBooleanArray checked;// = (SparseBooleanArray)notesCursorAdapters[0];
//
//        @Override
//        protected void onPreExecute() {
//            checked = lvNotes.getCheckedItemPositions();
//        }

        @Override
        protected Boolean doInBackground(Long... toDelete) {
            BibleDBHelper dbHelper = new BibleDBHelper(getContext());
            boolean ret = false;
            //NotesFragment.this.checked = lvNotes.getCheckedItemPositions();
            for (Long aToDelete : toDelete) {
                ret = dbHelper.deleteNoteById(aToDelete);
            }
            return ret;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean)
                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
        }
    }

}
