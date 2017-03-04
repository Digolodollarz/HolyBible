package tech.diggle.apps.bible.bhaibheridzvenemuchishona.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.BibleCursorAdapter;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.BibleDBHelper;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookListFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    private BibleDBHelper db;
    private ListView listView;
    private BibleCursorAdapter adapter;
    final String[] from = new String[] {  "n" };
    final int[] to = new int[] { R.id.tvBook };

    public BookListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookListFragment newInstance(String param1, String param2) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);
        String testament;
        Bundle args = getArguments();
        if(args!=null){
            testament = args.getString("TESTAMENT");//intent.getStringExtra("com.diggle.apps.bhaibheri.BOOK_NAME");
        }else {
            testament = "New";//intent.getStringExtra("com.diggle.apps.bhaibheri.BOOK_NAME");
        }
        getActivity().setTitle(R.string.app_name);
        db = new BibleDBHelper(getContext());
        Cursor cursorBooks;
        if (testament!=null&&!testament.isEmpty()) {
            cursorBooks = db.getBooks();
        } else {
            testament = "New";
            cursorBooks = db.getBooks();
        }

        listView = (ListView) view.findViewById(R.id.lvBookList);
        listView.setEmptyView(view.findViewById(android.R.id.empty));

        adapter = new BibleCursorAdapter(getContext(), R.layout.list_item_book, cursorBooks, from, to, 0);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        if(testament.equals("New"))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                listView.post( new Runnable() {
                    @Override
                    public void run() {
                        //call smooth scroll
                        listView.smoothScrollToPositionFromTop(39, 0);
                    }
                });
            }

        // OnCLickListener For List Items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                String bookName = ((TextView)view.findViewById(R.id.tvBook)).getText().toString();
//                int chapter = Integer.parseInt(((TextView)view.findViewById(R.id.tvChapter)).getText().toString());
                ChapterListFragment newFragment = new ChapterListFragment();
                Bundle args = new Bundle();
                args.putString("BOOK", bookName);
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
        if(testament.equals("New"))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                listView.smoothScrollToPositionFromTop(0,34);
            }
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
