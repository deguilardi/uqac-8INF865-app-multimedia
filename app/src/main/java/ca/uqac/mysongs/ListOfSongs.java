package ca.uqac.mysongs;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

class ListOfSongs {

    private ArrayList<SongEntity> songs = new ArrayList<>();

    ListOfSongs(Context context){
        ContentResolver contentResolver = context.getContentResolver();
        Uri contentURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String condition = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = contentResolver.query(contentURI, new String[]{
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST
        }, condition, null, sortOrder);


        if(cursor == null){
            // @TODO must inform user
        }
        else {
            if(cursor.getCount() == 0){
                // @TODO must inform user
            }
            else {
                while(cursor.moveToNext()){
                    songs.add(new SongEntity(cursor));
                }
            }
            cursor.close();
        }
    }

    ArrayList<SongEntity> getList(){
        return songs;
    }
}
