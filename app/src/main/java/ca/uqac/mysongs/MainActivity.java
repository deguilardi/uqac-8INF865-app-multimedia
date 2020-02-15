package ca.uqac.mysongs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
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
import android.widget.Toast;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ListOfSongs mListOfSongs;
    private SongsAdapter mAdapter;

    private Button mButtom;
    private int mPlayerState;

    private final ServiceConnection mServiceConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected( ComponentName name, IBinder service ){
            mMessengerSender = new Messenger(service);
            Message message = Message.obtain(null, PlayerService.GET_STATE);
            sendMessage( message );
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

    final Messenger mMessengerReceiver = new Messenger(mIncomingHandler);
    Messenger mMessengerSender = null;


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
        mRecyclerView = findViewById( R.id.list );
        mListOfSongs = new ListOfSongs(this) ;
        final Context context = this;
        mAdapter = new SongsAdapter(this, mListOfSongs.getList(), new SongsAdapter.OnClickHandler(){

            @Override
            public void onClick(SongEntity song, SongsAdapter.ViewHolder adapterViewHolder) {
                Message message = Message.obtain(null, PlayerService.PLAY_FILE);
                message.replyTo = mMessengerReceiver;
                final Bundle bundle = new Bundle();
                bundle.putParcelable( PlayerService.PARAM_SONG, song );
                message.setData( bundle );
                sendMessage( message );
            }
        });
        mRecyclerView.setAdapter( mAdapter );
    }

    private void setupActionButtom(){
        switch( mPlayerState ){
            case PlayerService.STATE_STOPPED:
                mButtom.setText( "select a song" );
                mButtom.setEnabled(false);
                break;
            case PlayerService.STATE_PLAYING:
                mButtom.setText( "stop" );
                mButtom.setEnabled(true);
                break;
        }
    }

    private void connectService(){
        Intent intent = new Intent( this, PlayerService.class );
        startService( intent );
        if(bindService( intent, mServiceConnection, 0 )){
            Toast.makeText(this, "Initialization error", Toast.LENGTH_SHORT).show();
        }
    }

    public void onHitAction( View view ){
        switch( mPlayerState ){
            case PlayerService.STATE_STOPPED:
                // @TODO
                break;
            case PlayerService.STATE_PLAYING:
                Message message = Message.obtain(null, PlayerService.STOP);
                sendMessage( message );
                break;
        }
    }

    private void sendMessage( Message message ){
        message.replyTo = mMessengerReceiver;
        try {
            mMessengerSender.send( message );
        }
        catch( RemoteException e ){
            Toast.makeText(this, "Couldn't communicate to service", Toast.LENGTH_SHORT).show();
        }

    }
}
