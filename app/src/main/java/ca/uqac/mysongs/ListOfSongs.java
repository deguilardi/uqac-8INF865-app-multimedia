package ca.uqac.mysongs;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

public class ListOfSongs {

    private ArrayList<String> songs = new ArrayList<>();

    ListOfSongs(Context context){
        ContentResolver contentResolver = context.getContentResolver();
        Uri contentURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String condition = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = contentResolver.query(contentURI, new String[]{
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME
        }, null, null, sortOrder);

        cursor.moveToFirst();
        while(cursor.moveToNext()){
            songs.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
        }

        int a = 1;
    }

}
