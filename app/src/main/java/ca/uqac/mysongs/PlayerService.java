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

    MediaPlayer mMediaPlayer = new MediaPlayer();

    Handler mIncomingHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(@NonNull Message message){
            switch( message.what ){
                case GET_STATE:
                    try {
                        int state = mMediaPlayer.isPlaying() ? STATE_PLAYING : STATE_STOPPED;
                        Message returnMessage = Message.obtain(null, GET_STATE, state, 0);
                        final Bundle bundle = new Bundle();
                        returnMessage.setData( bundle );
                        message.replyTo.send( returnMessage );
                    }
                    catch( RemoteException ignore ){}
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


    void play( String path ){
        stop();
        File file = new File(path);
        Uri uri = Uri.fromFile( file);
        mMediaPlayer = MediaPlayer.create( getApplicationContext(), uri );
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
        if( mMediaPlayer.isPlaying() ) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }
}
