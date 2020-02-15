package ca.uqac.mysongs;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import java.io.File;

public class PlayerService extends Service {

    public final static int STATE_STOPPED = 0;
    public final static int STATE_PLAYING = 1;
    public final static int GET_STATE = 2;
    public final static int PLAY_FILE = 3;
    public final static int STOP = 4;
    public final static String PARAM_SONG = "pSong";

    MediaPlayer mMediaPlayer = new MediaPlayer();

    Handler mIncomingHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(@NonNull Message message){
            Message returnMessage;
            switch( message.what ){
                case STOP:
                    stop();
                    // no break here
                case GET_STATE:
                    int state = mMediaPlayer != null && mMediaPlayer.isPlaying() ? STATE_PLAYING : STATE_STOPPED;
                    returnMessage = Message.obtain(null, GET_STATE, state, 0);
                    sendMessage( message, returnMessage );
                    break;
                case PLAY_FILE:
                    Bundle bundle = message.getData();
                    SongEntity song = bundle.getParcelable( PARAM_SONG );
                    play(song.getData());
                    returnMessage = Message.obtain(null, GET_STATE, STATE_PLAYING, 0);
                    sendMessage( message, returnMessage );
                    break;
            }
            return true;
        }
    });

    private final Messenger mMessenger = new Messenger(mIncomingHandler);

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    void play(String path ){
        stop();
        File file = new File(path);
        Uri uri = Uri.fromFile( file);
        mMediaPlayer = MediaPlayer.create( getApplicationContext(), uri );
        resume();
    }

    void resume(){
        if( !mMediaPlayer.isPlaying() ){
            mMediaPlayer.start();
        }
    }

    void pause() {
        if( mMediaPlayer.isPlaying() ) {
            mMediaPlayer.pause();
        }
    }

    void stop(){
        if( mMediaPlayer != null && mMediaPlayer.isPlaying() ) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void sendMessage( Message message, Message returnMessage ){
        try {
            message.replyTo.send( returnMessage );
        }
        catch( RemoteException ignore ){}

    }
}
