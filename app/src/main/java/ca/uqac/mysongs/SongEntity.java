package ca.uqac.mysongs;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

@SuppressWarnings("unused")
public class SongEntity implements Parcelable {
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

    protected SongEntity(Parcel in) {
        data = in.readString();
        name = in.readString();
        artist = in.readString();
        album = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(data);
        dest.writeString(name);
        dest.writeString(artist);
        dest.writeString(album);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SongEntity> CREATOR = new Creator<SongEntity>() {
        @Override
        public SongEntity createFromParcel(Parcel in) {
            return new SongEntity(in);
        }

        @Override
        public SongEntity[] newArray(int size) {
            return new SongEntity[size];
        }
    };

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
