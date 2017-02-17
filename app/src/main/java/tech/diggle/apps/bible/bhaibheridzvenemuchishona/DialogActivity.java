package tech.diggle.apps.bible.bhaibheridzvenemuchishona;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.BibleDBHelper;

import static java.security.AccessController.getContext;

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        Bundle args = getIntent().getExtras();
        int startVerse = getIntent().getIntExtra("START", 2);
        int endVerse = getIntent().getIntExtra("END", 2);
        int chapter = getIntent().getIntExtra("CHAPTER", 2);
        BibleDBHelper db = new BibleDBHelper(this);

//        Long saveNote = db.saveNote(
//                ((TextView)findViewById(R.id.etNote)).getText().toString(),
//                11,
//                chapter,
//                startVerse,
//                endVerse
//        );


//        Toast.makeText(this, saveNote.toString(), Toast.LENGTH_SHORT).show();
    }
}
