package ca.uqac.mysongs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private ListOfSongs mListOfSongs;
    private SongsAdapter mAdapter;

    private Button mButtom;
    private int mPlayerState;

    private final ServiceConnection mServiceConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected( ComponentName name, IBinder service ){
            Messenger messenger = new Messenger(service);
            try {
                Message message = Message.obtain(null, PlayerService.GET_STATE);
                message.replyTo = mMessenger;
                messenger.send(message);
            }
            catch( RemoteException ignore ){}
        }

        @Override
        public void onServiceDisconnected( ComponentName name ){}
    };

    Handler mIncomingHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage( @NonNull Message message ){
            if( message.what == PlayerService.GET_STATE ){
                mPlayerState = message.arg1;
                setupActionButtom();
            }
            return true;
        }
    });

    final Messenger mMessenger = new Messenger(mIncomingHandler);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
        connectService();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unbindService( mServiceConnection );
    }

    private void setupUI(){
        mButtom = findViewById( R.id.btnAction );
        mListView = findViewById( R.id.list );
        mListOfSongs = new ListOfSongs(this) ;
        mAdapter = new SongsAdapter(this, mListOfSongs.getList() );
        mListView.setAdapter( mAdapter );
    }

    private void setupActionButtom(){
        switch( mPlayerState ){
            case PlayerService.STATE_STOPPED:
                mButtom.setText( "play" );
                break;
            case PlayerService.STATE_PLAYING:
                mButtom.setText( "stop" );
                break;
        }
    }

    private void connectService(){
        Intent intent = new Intent( this, PlayerService.class );
        startService( intent );
        bindService( intent, mServiceConnection, 0 );
    }

    public void onHitAction( View view ){
        switch( mPlayerState ){
            case PlayerService.STATE_STOPPED:
                // @TODO
                break;
            case PlayerService.STATE_PLAYING:
                // @TODO
                break;
        }
    }
}
