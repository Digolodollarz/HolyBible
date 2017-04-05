package tech.diggle.apps.bible.bhaibheridzvenemuchishona.Helpers;

/**
 * Created by DiggeDollarz on 18/12/2016.
 */


import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Utils {

    private static final String TAG = SQLiteAssetHelper.class.getSimpleName();

    public static List<String> splitSqlScript(String script, char delim) {
        List<String> statements = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        boolean inLiteral = false;
        char[] content = script.toCharArray();
        for (int i = 0; i < script.length(); i++) {
            if (content[i] == '"') {
                inLiteral = !inLiteral;
            }
            if (content[i] == delim && !inLiteral) {
                if (sb.length() > 0) {
                    statements.add(sb.toString().trim());
                    sb = new StringBuilder();
                }
            } else {
                sb.append(content[i]);
            }
        }
        if (sb.length() > 0) {
            statements.add(sb.toString().trim());
        }
        return statements;
    }

    public static void writeExtractedFileToDisk(InputStream in, OutputStream outs) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer))>0){
            outs.write(buffer, 0, length);
        }
        outs.flush();
        outs.close();
        in.close();
    }

    public static ZipInputStream getFileFromZip(InputStream zipFileStream) throws IOException {
        ZipInputStream zis = new ZipInputStream(zipFileStream);
        ZipEntry ze;
        while ((ze = zis.getNextEntry()) != null) {
            Log.w(TAG, "extracting file: '" + ze.getName() + "'...");
            return zis;
        }
        return null;
    }

    public static String convertStreamToString(InputStream is) {
        return new Scanner(is).useDelimiter("\\A").next();
    }

    public static String sanitiseBookName(String bookName){
        Pattern pattern = Pattern.compile("^[a-zA-Z]");
        Pattern pattern1 = Pattern.compile("^\\d[a-zA-Z]+");
        Matcher matcher = pattern.matcher(bookName);
        Matcher matcher1 = pattern1.matcher(bookName);
        if(matcher.find()){
            return bookName.substring(0, 1).toUpperCase() + bookName.substring(1).toLowerCase();
        }else if(matcher1.find()){
            Log.d(TAG, "sanitiseBookName: " + bookName.substring(0, 1)+ " " + bookName.substring(1, 2).toUpperCase() + bookName.substring(2).toLowerCase());
            return bookName.substring(0, 1)+ " " + bookName.substring(1, 2).toUpperCase() + bookName.substring(2).toLowerCase();
        }
        return null;
    }

}
