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
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ListOfSongs listOfSongs;
    private SongsAdapter adapter;

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
                int state = message.arg1;
                switch( state ){
                    case PlayerService.STATE_STOPPED:
                        // @TODO
                        break;
                    case PlayerService.STATE_PLAYING:
                        // @TODO
                        break;
                }
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
        listView = findViewById(R.id.list);
        listOfSongs = new ListOfSongs(this);
        adapter = new SongsAdapter(this, listOfSongs.getList());
        listView.setAdapter(adapter);
    }

    private void connectService(){
        Intent intent = new Intent( this, PlayerService.class );
        startService( intent );
        bindService( intent, mServiceConnection, 0 );
    }
}
