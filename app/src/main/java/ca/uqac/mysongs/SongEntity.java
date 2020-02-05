package ca.uqac.mysongs;

import android.database.Cursor;
import android.provider.MediaStore;

@SuppressWarnings("unused")
public class SongEntity {
    private String data;
    private String name;
    private String artist;
    private String album;

    public SongEntity(Cursor cursor){
        data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
        artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
