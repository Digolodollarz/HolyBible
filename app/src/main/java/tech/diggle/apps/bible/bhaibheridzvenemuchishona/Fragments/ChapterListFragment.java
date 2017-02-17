package tech.diggle.apps.bible.bhaibheridzvenemuchishona.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.BibleCursorAdapter;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.BibleDBHelper;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.MainActivity;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChapterListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ChapterListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private BibleDBHelper db;
    private ListView listView;
    private BibleCursorAdapter adapter;
    final String[] from = new String[] {  "n" , "c"};
    final int[] to = new int[] { R.id.tvBook, R.id.tvChapter };

    public ChapterListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chapter_list, container, false);

        String book = "John";
        Bundle args = getArguments();
        if(args!=null){
            book = args.getString("BOOK");//intent.getStringExtra("com.diggle.apps.bhaibheri.BOOK_NAME");
        }
        db = new BibleDBHelper(getContext());
        Cursor cursorChapters;
        if (book!=null&&!book.isEmpty()) {
            cursorChapters = db.getChapters(book);
        } else {
            book = "John";
            cursorChapters = db.getChapters(book);
        }

        listView = (ListView) view.findViewById(R.id.lvBookList);
        listView.setEmptyView(view.findViewById(android.R.id.empty));

        adapter = new BibleCursorAdapter(getContext(), R.layout.list_item_chapter, cursorChapters, from, to, 0);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        // OnCLickListener For List Items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                String bookName = ((TextView)view.findViewById(R.id.tvBook)).getText().toString();
                int chapter = Integer.parseInt(((TextView)view.findViewById(R.id.tvChapter)).getText().toString());
//                Intent readIntent = new Intent(getContext(), MainActivity.class);
//                readIntent.putExtra("com.diggle.apps.bhaibheri.BOOK_NAME", bookName);
//                readIntent.putExtra("com.diggle.apps.bhaibheri.CHAPTER", chapter);
//                startActivity(readIntent);

                ReadFragment newFragment = new ReadFragment();
                Bundle args = new Bundle();
                args.putString("BOOK", bookName);
                args.putInt("CHAPTER", chapter);
                newFragment.setArguments(args);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.content_frame, newFragment)
                        .addToBackStack(null);
//            transaction.addToBackStack(null);

// Commit the transaction
                transaction.commit();

            }
        });

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
}
