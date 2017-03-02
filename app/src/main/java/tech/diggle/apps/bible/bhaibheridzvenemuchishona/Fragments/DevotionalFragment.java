package tech.diggle.apps.bible.bhaibheridzvenemuchishona.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.BibleDBHelper;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.BibleDataContract;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers.ReadCursorAdapter;
import tech.diggle.apps.bible.bhaibheridzvenemuchishona.R;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DevotionalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DevotionalFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    View nav;
    View view;
    String receivedText;
    int dayOfYear;
    Calendar calendar;
    Context context;
    BibleDBHelper db;
    final String[] from = new String[]{"v", "t"};
    final int[] to = new int[]{R.id.tvVerseNumber};//, R.id.tvVerse};

    public DevotionalFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_devotional, container, false);
        getActivity().setTitle(R.string.devotional);
        if (getArguments() != null) {
            if (getArguments().getString("TEXT") != null) {
                receivedText = getArguments().getString("TEXT");
            }
            if (getArguments().getString("TITLE") != null) {
                ((TextView) view.findViewById(R.id.tvDevotionalTitle)).setText(getArguments().getString("TITLE"));
            } else {
                view.findViewById(R.id.tvDevotionalTitle).setVisibility(GONE);
            }
        }
        calendar = Calendar.getInstance();
        dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        db = new BibleDBHelper(getContext());
        String[] devotional = db.getDevotional(dayOfYear);

        doNotLookInMyCode(view, devotional);

        Button yesterdayDevotional = (Button) view.findViewById(R.id.btnReadYesterday);
        yesterdayDevotional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
//                String[] devotional;
//                if(dayOfYear-1>0)
//                    dayOfYear--;
//                else
//                    dayOfYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);

                calendar.add(Calendar.DATE, -1);
                doNotLookInMyCode(view, db.getDevotional(calendar.get(Calendar.DAY_OF_YEAR)));
            }
        });

        Button readNext = (Button) view.findViewById(R.id.btnReadNext);
        readNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
//                String[] devotional;
//                if(dayOfYear-1>0)
//                    dayOfYear--;
//                else
//                    dayOfYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);

                calendar.add(Calendar.DATE, 1);
                doNotLookInMyCode(view, db.getDevotional(calendar.get(Calendar.DAY_OF_YEAR)));
            }
        });
//        ((TextView) view.findViewById(R.id.tvDevotionalTitle)).setText(devotional[0]);
//        receivedText = devotional[1];
//
////        receivedText = "The Devotional Text for Today [John 15 vs 1-2]" +
////                "I am the true vine, and my father is the vine dresser [John 15vs 2]" +
////                "For God so loved this wicked world, that He giveth His only begotten Son," +
////                " that whosoever believeth in him shall not perish, but have and everlasting life ([John 3 vs 16]).";
//
//        StringBuilder sbDevotionalText = new StringBuilder(receivedText);
//        SpannableStringBuilder spannableStringBuilderDevotionalText = new SpannableStringBuilder();
//
//        Pattern pattern = Pattern.compile("\\[\\w+\\s\\d+\\s\\w{2}\\s\\d+]");
//        Pattern pattern1 = Pattern.compile("\\[[^\\d\\W]+\\s\\d+\\s\\w{2}\\s\\d+]");
//        Pattern pattern2 = Pattern.compile("\\[\\d\\w+\\s\\d+\\s\\w{2}\\s\\d+]");
//
////        sbDevotionalText = new StringBuilder("Today's Daily verse is from [John 3 vs 10]. Read and enjoy!" +
////                "and yesterday's was from " +
////                "[1John 3 vs 10]. Read and enjoy!" + "Today's Daily verse is from [John 3 vs 10]. Read and enjoy!" +
////                "and yesterday's was from " +
////                "[1John 3 vs 10]. Read and enjoy!");
//        Matcher matcherAnyVerse = pattern.matcher(sbDevotionalText);
//        Matcher matcherNoNumber = pattern1.matcher(sbDevotionalText);
//        Matcher matcherNumber = pattern2.matcher(sbDevotionalText);
//
////        StringBuilder spannableStringBuilderDevotionalText = new StringBuilder();
//
//        if (matcherAnyVerse.find()) {
//            do {
//                ClickDetails clickDetails = new ClickDetails();
//                try {
//                    int mnnStart, mnStart;
//                    boolean num = matcherNumber.find();
//                    boolean noNum = matcherNoNumber.find();
//
//                    if (noNum)
//                        mnnStart = matcherNoNumber.start();
//                    else
//                        mnnStart = 0;
//
//                    if (num)
//                        mnStart = matcherNumber.start();
//                    else
//                        mnStart = 0;
//
//
//                    System.out.println(
//                            "No Num Start : " + mnnStart +
//                                    "Num Start : " + mnStart
//                    );
//                    if (num && noNum) {
//                        if (mnnStart > mnStart) {
//                            clickDetails.displayText = sbDevotionalText.substring(sbDevotionalText.indexOf("[") + 1, sbDevotionalText.indexOf("[") + 2)
//                                    + " "
//                                    + sbDevotionalText.substring(sbDevotionalText.indexOf("[") + 2, sbDevotionalText.indexOf("]", sbDevotionalText.indexOf("[") + 2));
//                            spannableStringBuilderDevotionalText.append(sbDevotionalText.substring(0, sbDevotionalText.indexOf("[", matcherNumber.start())));
//                            sbDevotionalText.delete(0, sbDevotionalText.indexOf("[", matcherNumber.start()) + 1);
//                            clickDetails.bookName = sbDevotionalText.substring(0, 1) + " " + sbDevotionalText.substring(1, sbDevotionalText.indexOf(" "));
//                            sbDevotionalText.delete(0, sbDevotionalText.indexOf(" ") + 1);
//                            clickDetails.chapter = Integer
//                                    .parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf(" ")));
//                            sbDevotionalText.delete(0, sbDevotionalText.indexOf("vs ") + 3);
//                            clickDetails.startVerse = Integer.parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf("]")));
//                            sbDevotionalText.delete(0, sbDevotionalText.indexOf("]") + 1);
//                        } else {
//                            clickDetails.displayText = sbDevotionalText.substring(sbDevotionalText.indexOf("[") + 1, sbDevotionalText.indexOf("]"));
//                            spannableStringBuilderDevotionalText.append(sbDevotionalText.substring(0, sbDevotionalText.indexOf("[", matcherNoNumber.start())));
//                            sbDevotionalText.delete(0, sbDevotionalText.indexOf("[", matcherNoNumber.start()) + 1);
//                            clickDetails.bookName = sbDevotionalText.substring(0, 1) + sbDevotionalText.substring(1, sbDevotionalText.indexOf(" "));
//                            sbDevotionalText.delete(0, sbDevotionalText.indexOf(" ") + 1);
//                            clickDetails.chapter = Integer
//                                    .parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf(" ")));
//                            sbDevotionalText.delete(0, sbDevotionalText.indexOf("vs ") + 3);
//                            clickDetails.startVerse = Integer.parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf("]")));
//                            sbDevotionalText.delete(0, sbDevotionalText.indexOf("]") + 1);
//                        }
//                    } else if (!noNum && num) {
//                        clickDetails.displayText = sbDevotionalText.substring(sbDevotionalText.indexOf("[") + 1, sbDevotionalText.indexOf("[") + 2)
//                                + " "
//                                + sbDevotionalText.substring(sbDevotionalText.indexOf("[") + 2, sbDevotionalText.indexOf("]", sbDevotionalText.indexOf("[") + 2));
//                        spannableStringBuilderDevotionalText.append(sbDevotionalText.substring(0, sbDevotionalText.indexOf("[", matcherNumber.start())));
//                        sbDevotionalText.delete(0, sbDevotionalText.indexOf("[", matcherNumber.start()) + 1);
//                        clickDetails.bookName = sbDevotionalText.substring(0, 1) + " " + sbDevotionalText.substring(1, sbDevotionalText.indexOf(" "));
//                        sbDevotionalText.delete(0, sbDevotionalText.indexOf(" ") + 1);
//                        clickDetails.chapter = Integer
//                                .parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf(" ")));
//                        sbDevotionalText.delete(0, sbDevotionalText.indexOf("vs ") + 3);
//                        clickDetails.startVerse = Integer.parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf("]")));
//                        sbDevotionalText.delete(0, sbDevotionalText.indexOf("]") + 1);
//                    }
//                    if (noNum && !num) {
//                        clickDetails.displayText = sbDevotionalText.substring(sbDevotionalText.indexOf("[") + 1, sbDevotionalText.indexOf("]"));
//                        spannableStringBuilderDevotionalText.append(sbDevotionalText.substring(0, sbDevotionalText.indexOf("[", matcherNoNumber.start())));
//                        sbDevotionalText.delete(0, sbDevotionalText.indexOf("[", matcherNoNumber.start()) + 1);
//                        clickDetails.bookName = sbDevotionalText.substring(0, 1) + sbDevotionalText.substring(1, sbDevotionalText.indexOf(" "));
//                        sbDevotionalText.delete(0, sbDevotionalText.indexOf(" ") + 1);
//                        clickDetails.chapter = Integer
//                                .parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf(" ")));
//                        sbDevotionalText.delete(0, sbDevotionalText.indexOf("vs ") + 3);
//                        clickDetails.startVerse = Integer.parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf("]")));
//                        sbDevotionalText.delete(0, sbDevotionalText.indexOf("]") + 1);
//                    }
//                    System.out.println(clickDetails.bookName);
//                    final String book = clickDetails.bookName;
//                    System.out.println(clickDetails.chapter);
//                    final int chapter = clickDetails.chapter;
//                    System.out.println(clickDetails.startVerse);
//                    final int startVerse = clickDetails.startVerse;
//                    System.out.println(clickDetails.displayText);
//                    System.out.println(clickDetails.bookId);
//
//
//                    int startIndex = spannableStringBuilderDevotionalText.length();
//                    spannableStringBuilderDevotionalText.append(
//                            clickDetails.displayText
//                    );
//                    int endIndex = spannableStringBuilderDevotionalText.length();
////                sbDevotionalText.delete(sbDevotionalText.indexOf("["), sbDevotionalText.indexOf("]")+1);
//                    ClickableSpan clickableSpan = new ClickableSpan() {
//                        @Override
//                        public void onClick(View textView) {
//                            if (getActivity() == null)
//                                return;
//                            DialogFragment newFragment = ReadFragment.newInstance();
//                            Bundle args = new Bundle();
//                            args.putInt("CHAPTER", chapter);
//                            args.putInt("VERSE", startVerse);
//                            args.putString("BOOK", book);
//                            args.putInt("LAST_VERSE", 0);
//                            newFragment.setArguments(args);
//                            newFragment.show(getActivity().getSupportFragmentManager(), "dialog");
//
//                        }
//                    };
//                    spannableStringBuilderDevotionalText.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    matcherAnyVerse = pattern.matcher(sbDevotionalText);
//                    matcherNoNumber = pattern1.matcher(sbDevotionalText);
//                    matcherNumber = pattern2.matcher(sbDevotionalText);
//
//                    if (sbDevotionalText.indexOf("[") < 0)
//                        spannableStringBuilderDevotionalText.append(sbDevotionalText);
//                }
//
//
//            } while (matcherAnyVerse.find());
//        } else {
//            spannableStringBuilderDevotionalText.append(sbDevotionalText.toString());
//        }
//
//        System.out.println(spannableStringBuilderDevotionalText);
//
//
////        //For Click
////        myString.setSpan(clickableSpan,startIndex,lastIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
////
////        //For UnderLine
////        myString.setSpan(new UnderlineSpan(),startIndex,lastIndex,0);
////
////        //For Bold
////        myString.setSpan(new StyleSpan(Typeface.BOLD),startIndex,lastIndex,0);
//
//        //Finally you can set to textView.
//        TextView tvDevotionalText = (TextView) view.findViewById(R.id.tvDevotionalText);
//        tvDevotionalText.setText(spannableStringBuilderDevotionalText);
//        System.out.println(spannableStringBuilderDevotionalText);
//        tvDevotionalText.setMovementMethod(LinkMovementMethod.getInstance());
        getActivity().setTitle(R.string.devotional);
        db.close();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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

    private static class ClickDetails {
        int startVerse = 0;
        int chapter = 0;
        int bookId = 0;
        String bookName = " ";
        String displayText = " ";

        ClickDetails() {
        }
    }

    private class ShowNav extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            nav.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            nav.setVisibility(GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    private void doNotLookInMyCode(View view, String devotional[]) {
        TextView devotionalTitle = (TextView) view.findViewById(R.id.tvDevotionalTitle);
        devotionalTitle.setText(devotional[0]);
        devotionalTitle.setVisibility(View.VISIBLE);
        receivedText = devotional[1];

//        receivedText = "The Devotional Text for Today [John 15 vs 1-2]" +
//                "I am the true vine, and my father is the vine dresser [John 15vs 2]" +
//                "For God so loved this wicked world, that He giveth His only begotten Son," +
//                " that whosoever believeth in him shall not perish, but have and everlasting life ([John 3 vs 16]).";

        SpannableStringBuilder spannableStringBuilderDevotionalText;
        StringBuilder sbDevotionalText = new StringBuilder(receivedText);
        spannableStringBuilderDevotionalText = new SpannableStringBuilder();

        Pattern pattern = Pattern.compile("\\[\\w+\\s\\d+\\s\\w{2}\\s\\d+]");
        Pattern pattern1 = Pattern.compile("\\[[^\\d\\W]+\\s\\d+\\s\\w{2}\\s\\d+]");
        Pattern pattern2 = Pattern.compile("\\[\\d\\w+\\s\\d+\\s\\w{2}\\s\\d+]");

//        sbDevotionalText = new StringBuilder("Today's Daily verse is from [John 3 vs 10]. Read and enjoy!" +
//                "and yesterday's was from " +
//                "[1John 3 vs 10]. Read and enjoy!" + "Today's Daily verse is from [John 3 vs 10]. Read and enjoy!" +
//                "and yesterday's was from " +
//                "[1John 3 vs 10]. Read and enjoy!");
        Matcher matcherAnyVerse = pattern.matcher(sbDevotionalText);
        Matcher matcherAnyVerse1 = pattern.matcher(sbDevotionalText);
        Matcher matcherNoNumber = pattern1.matcher(sbDevotionalText);
        Matcher matcherNumber = pattern2.matcher(sbDevotionalText);
        if (matcherAnyVerse1.replaceAll("").replaceAll("((?=[^a-z])([^A-Z]))*", "").isEmpty()) {

            if (matcherAnyVerse.find()) {
                do {
                    ClickDetails clickDetails = new ClickDetails();
                    try {
                        int mnnStart, mnStart;
                        boolean num = matcherNumber.find();
                        boolean noNum = matcherNoNumber.find();

                        if (noNum)
                            mnnStart = matcherNoNumber.start();
                        else
                            mnnStart = 0;

                        if (num)
                            mnStart = matcherNumber.start();
                        else
                            mnStart = 0;

                        if (num && noNum) {
                            if (mnnStart > mnStart) {
                                clickDetails.displayText = sbDevotionalText.substring(sbDevotionalText.indexOf("[") + 1, sbDevotionalText.indexOf("[") + 2)
                                        + " "
                                        + sbDevotionalText.substring(sbDevotionalText.indexOf("[") + 2, sbDevotionalText.indexOf("]", sbDevotionalText.indexOf("[") + 2));
                                spannableStringBuilderDevotionalText.append(sbDevotionalText.substring(0, sbDevotionalText.indexOf("[", matcherNumber.start())));
                                sbDevotionalText.delete(0, sbDevotionalText.indexOf("[", matcherNumber.start()) + 1);
                                clickDetails.bookName = sbDevotionalText.substring(0, 1) + " " + sbDevotionalText.substring(1, sbDevotionalText.indexOf(" "));
                                sbDevotionalText.delete(0, sbDevotionalText.indexOf(" ") + 1);
                                clickDetails.chapter = Integer
                                        .parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf(" ")));
                                sbDevotionalText.delete(0, sbDevotionalText.indexOf("vs ") + 3);
                                clickDetails.startVerse = Integer.parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf("]")));
                                sbDevotionalText.delete(0, sbDevotionalText.indexOf("]") + 1);
                            } else {
                                clickDetails.displayText = sbDevotionalText.substring(sbDevotionalText.indexOf("[") + 1, sbDevotionalText.indexOf("]"));
                                spannableStringBuilderDevotionalText.append(sbDevotionalText.substring(0, sbDevotionalText.indexOf("[", matcherNoNumber.start())));
                                sbDevotionalText.delete(0, sbDevotionalText.indexOf("[", matcherNoNumber.start()) + 1);
                                clickDetails.bookName = sbDevotionalText.substring(0, 1) + sbDevotionalText.substring(1, sbDevotionalText.indexOf(" "));
                                sbDevotionalText.delete(0, sbDevotionalText.indexOf(" ") + 1);
                                clickDetails.chapter = Integer
                                        .parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf(" ")));
                                sbDevotionalText.delete(0, sbDevotionalText.indexOf("vs ") + 3);
                                clickDetails.startVerse = Integer.parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf("]")));
                                sbDevotionalText.delete(0, sbDevotionalText.indexOf("]") + 1);
                            }
                        } else if (!noNum && num) {
                            clickDetails.displayText = sbDevotionalText.substring(sbDevotionalText.indexOf("[") + 1, sbDevotionalText.indexOf("[") + 2)
                                    + " "
                                    + sbDevotionalText.substring(sbDevotionalText.indexOf("[") + 2, sbDevotionalText.indexOf("]", sbDevotionalText.indexOf("[") + 2));
                            spannableStringBuilderDevotionalText.append(sbDevotionalText.substring(0, sbDevotionalText.indexOf("[", matcherNumber.start())));
                            sbDevotionalText.delete(0, sbDevotionalText.indexOf("[", matcherNumber.start()) + 1);
                            clickDetails.bookName = sbDevotionalText.substring(0, 1) + " " + sbDevotionalText.substring(1, sbDevotionalText.indexOf(" "));
                            sbDevotionalText.delete(0, sbDevotionalText.indexOf(" ") + 1);
                            clickDetails.chapter = Integer
                                    .parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf(" ")));
                            sbDevotionalText.delete(0, sbDevotionalText.indexOf("vs ") + 3);
                            clickDetails.startVerse = Integer.parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf("]")));
                            sbDevotionalText.delete(0, sbDevotionalText.indexOf("]") + 1);
                        }
                        if (noNum && !num) {
                            clickDetails.displayText = sbDevotionalText.substring(sbDevotionalText.indexOf("[") + 1, sbDevotionalText.indexOf("]"));
                            spannableStringBuilderDevotionalText.append(sbDevotionalText.substring(0, sbDevotionalText.indexOf("[", matcherNoNumber.start())));
                            sbDevotionalText.delete(0, sbDevotionalText.indexOf("[", matcherNoNumber.start()) + 1);
                            clickDetails.bookName = sbDevotionalText.substring(0, 1) + sbDevotionalText.substring(1, sbDevotionalText.indexOf(" "));
                            sbDevotionalText.delete(0, sbDevotionalText.indexOf(" ") + 1);
                            clickDetails.chapter = Integer
                                    .parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf(" ")));
                            sbDevotionalText.delete(0, sbDevotionalText.indexOf("vs ") + 3);
                            clickDetails.startVerse = Integer.parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf("]")));
                            sbDevotionalText.delete(0, sbDevotionalText.indexOf("]") + 1);
                        }
                        final String bookName = clickDetails.bookName;
                        final int book = db.getBookId(bookName);
                        final int chapter = clickDetails.chapter;
                        final int startVerse = clickDetails.startVerse;

                        Button readVerse = (Button) view.findViewById(R.id.btnReadSource);
                        readVerse.setText("Read " +bookName + " " + chapter + " : " + startVerse);
                        readVerse.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DialogFragment newFragment = ShowChapterFragment.newInstance();
                                Bundle args = new Bundle();
                                args.putInt(BibleDataContract.CHAPTER, chapter);
                                args.putInt("START_VERSE", startVerse);
                                args.putInt(BibleDataContract.BOOK, book);
                                args.putString("BOOK_NAME", bookName);
                                args.putInt("LAST_VERSE", 0);
                                newFragment.setArguments(args);
                                newFragment.show(getActivity().getSupportFragmentManager(), "dialog");

//                                ListView listView = new ListView(context);
//                                Cursor verses = db.getVerses(book, chapter);
//                                ReadCursorAdapter adapter = new ReadCursorAdapter(getContext(),
//                                        R.layout.read_view_verse,
//                                        verses,
//                                        from,
//                                        to,
//                                        0);
//                                listView.setAdapter(adapter);
//                                AlertDialog.Builder builder = new
//                                        AlertDialog.Builder(context);
//
//                                builder.setTitle(book);
//                                builder.setCancelable(true);
//                                builder.setPositiveButton("OK", null);
//                                builder.setView(listView);
//                                AlertDialog dialog = builder.create();
////                                dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT,
////                                        ViewGroup.LayoutParams.WRAP_CONTENT);
////                                WindowManager.LayoutParams lp;
////                                try {
////                                    lp = new WindowManager.LayoutParams();
////                                    lp.copyFrom(dialog.getWindow().getAttributes());
////                                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
////                                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
////                                    lp.gravity = Gravity.CENTER;
////                                    dialog.getWindow().setAttributes(lp);
////                                }catch (NullPointerException ex){
////                                    ex.printStackTrace();
////                                }
//
//
//                                dialog.show();
                            }
                        });
                        readVerse.setVisibility(View.VISIBLE);
                        view.findViewById(R.id.tvDevotionalText).setVisibility(View.INVISIBLE);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (matcherAnyVerse.find());
            } else {
                spannableStringBuilderDevotionalText.append(sbDevotionalText.toString());
            }
        } else {

//        StringBuilder spannableStringBuilderDevotionalText = new StringBuilder();
            if (matcherAnyVerse.find()) {
                do {
                    ClickDetails clickDetails = new ClickDetails();
                    try {
                        int mnnStart, mnStart;
                        boolean num = matcherNumber.find();
                        boolean noNum = matcherNoNumber.find();

                        if (noNum)
                            mnnStart = matcherNoNumber.start();
                        else
                            mnnStart = 0;

                        if (num)
                            mnStart = matcherNumber.start();
                        else
                            mnStart = 0;


                        System.out.println(
                                "No Num Start : " + mnnStart +
                                        "Num Start : " + mnStart
                        );
                        if (num && noNum) {
                            if (mnnStart > mnStart) {
                                clickDetails.displayText = sbDevotionalText.substring(sbDevotionalText.indexOf("[") + 1, sbDevotionalText.indexOf("[") + 2)
                                        + " "
                                        + sbDevotionalText.substring(sbDevotionalText.indexOf("[") + 2, sbDevotionalText.indexOf("]", sbDevotionalText.indexOf("[") + 2));
                                spannableStringBuilderDevotionalText.append(sbDevotionalText.substring(0, sbDevotionalText.indexOf("[", matcherNumber.start())));
                                sbDevotionalText.delete(0, sbDevotionalText.indexOf("[", matcherNumber.start()) + 1);
                                clickDetails.bookName = sbDevotionalText.substring(0, 1) + " " + sbDevotionalText.substring(1, sbDevotionalText.indexOf(" "));
                                sbDevotionalText.delete(0, sbDevotionalText.indexOf(" ") + 1);
                                clickDetails.chapter = Integer
                                        .parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf(" ")));
                                sbDevotionalText.delete(0, sbDevotionalText.indexOf("vs ") + 3);
                                clickDetails.startVerse = Integer.parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf("]")));
                                sbDevotionalText.delete(0, sbDevotionalText.indexOf("]") + 1);
                            } else {
                                clickDetails.displayText = sbDevotionalText.substring(sbDevotionalText.indexOf("[") + 1, sbDevotionalText.indexOf("]"));
                                spannableStringBuilderDevotionalText.append(sbDevotionalText.substring(0, sbDevotionalText.indexOf("[", matcherNoNumber.start())));
                                sbDevotionalText.delete(0, sbDevotionalText.indexOf("[", matcherNoNumber.start()) + 1);
                                clickDetails.bookName = sbDevotionalText.substring(0, 1) + sbDevotionalText.substring(1, sbDevotionalText.indexOf(" "));
                                sbDevotionalText.delete(0, sbDevotionalText.indexOf(" ") + 1);
                                clickDetails.chapter = Integer
                                        .parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf(" ")));
                                sbDevotionalText.delete(0, sbDevotionalText.indexOf("vs ") + 3);
                                clickDetails.startVerse = Integer.parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf("]")));
                                sbDevotionalText.delete(0, sbDevotionalText.indexOf("]") + 1);
                            }
                        } else if (!noNum && num) {
                            clickDetails.displayText = sbDevotionalText.substring(sbDevotionalText.indexOf("[") + 1, sbDevotionalText.indexOf("[") + 2)
                                    + " "
                                    + sbDevotionalText.substring(sbDevotionalText.indexOf("[") + 2, sbDevotionalText.indexOf("]", sbDevotionalText.indexOf("[") + 2));
                            spannableStringBuilderDevotionalText.append(sbDevotionalText.substring(0, sbDevotionalText.indexOf("[", matcherNumber.start())));
                            sbDevotionalText.delete(0, sbDevotionalText.indexOf("[", matcherNumber.start()) + 1);
                            clickDetails.bookName = sbDevotionalText.substring(0, 1) + " " + sbDevotionalText.substring(1, sbDevotionalText.indexOf(" "));
                            sbDevotionalText.delete(0, sbDevotionalText.indexOf(" ") + 1);
                            clickDetails.chapter = Integer
                                    .parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf(" ")));
                            sbDevotionalText.delete(0, sbDevotionalText.indexOf("vs ") + 3);
                            clickDetails.startVerse = Integer.parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf("]")));
                            sbDevotionalText.delete(0, sbDevotionalText.indexOf("]") + 1);
                        }
                        if (noNum && !num) {
                            clickDetails.displayText = sbDevotionalText.substring(sbDevotionalText.indexOf("[") + 1, sbDevotionalText.indexOf("]"));
                            spannableStringBuilderDevotionalText.append(sbDevotionalText.substring(0, sbDevotionalText.indexOf("[", matcherNoNumber.start())));
                            sbDevotionalText.delete(0, sbDevotionalText.indexOf("[", matcherNoNumber.start()) + 1);
                            clickDetails.bookName = sbDevotionalText.substring(0, 1) + sbDevotionalText.substring(1, sbDevotionalText.indexOf(" "));
                            sbDevotionalText.delete(0, sbDevotionalText.indexOf(" ") + 1);
                            clickDetails.chapter = Integer
                                    .parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf(" ")));
                            sbDevotionalText.delete(0, sbDevotionalText.indexOf("vs ") + 3);
                            clickDetails.startVerse = Integer.parseInt(sbDevotionalText.substring(0, sbDevotionalText.indexOf("]")));
                            sbDevotionalText.delete(0, sbDevotionalText.indexOf("]") + 1);
                        }
                        final String bookName = clickDetails.bookName;
                        final int book = db.getBookId(bookName);
                        final int chapter = clickDetails.chapter;
                        final int startVerse = clickDetails.startVerse;

                        int startIndex = spannableStringBuilderDevotionalText.length();
                        spannableStringBuilderDevotionalText.append(
                                clickDetails.displayText
                        );
                        int endIndex = spannableStringBuilderDevotionalText.length();
                        //                sbDevotionalText.delete(sbDevotionalText.indexOf("["), sbDevotionalText.indexOf("]")+1);
                        ClickableSpan clickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(View textView) {
                                if (getActivity() == null)
                                    return;
                                DialogFragment newFragment = ReadFragment.newInstance();
                                Bundle args = new Bundle();
                                args.putInt("CHAPTER", chapter);
                                args.putInt("VERSE", startVerse);
                                args.putInt("BOOK", book);
                                args.putString("BOOK_NAME", bookName);
                                args.putInt("LAST_VERSE", 0);
                                newFragment.setArguments(args);
                                newFragment.show(getActivity().getSupportFragmentManager(), "dialog");

                            }
                        };
                        spannableStringBuilderDevotionalText.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        matcherAnyVerse = pattern.matcher(sbDevotionalText);
                        matcherNoNumber = pattern1.matcher(sbDevotionalText);
                        matcherNumber = pattern2.matcher(sbDevotionalText);

                        if (sbDevotionalText.indexOf("[") < 0)
                            spannableStringBuilderDevotionalText.append(sbDevotionalText);
                    }
                } while (matcherAnyVerse.find());
            } else {
                spannableStringBuilderDevotionalText.append(sbDevotionalText.toString());
            }

//        //For Click
//        myString.setSpan(clickableSpan,startIndex,lastIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        //For UnderLine
//        myString.setSpan(new UnderlineSpan(),startIndex,lastIndex,0);
//
//        //For Bold
//        myString.setSpan(new StyleSpan(Typeface.BOLD),startIndex,lastIndex,0);

            //Finally you can set to textView.
            TextView tvDevotionalText = (TextView) view.findViewById(R.id.tvDevotionalText);
            tvDevotionalText.setText(spannableStringBuilderDevotionalText);
            System.out.println(spannableStringBuilderDevotionalText);
            tvDevotionalText.setMovementMethod(LinkMovementMethod.getInstance());
            tvDevotionalText.setVisibility(View.VISIBLE);
            view.findViewById(R.id.btnReadSource).setVisibility(View.INVISIBLE);
        }

    }
}
