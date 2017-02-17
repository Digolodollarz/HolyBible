package tech.diggle.apps.bible.bhaibheridzvenemuchishona.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tech.diggle.apps.bible.bhaibheridzvenemuchishona.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNoteDialogFragment extends Fragment {


    public AddNoteDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note_dialog, container, false);
    }

}
