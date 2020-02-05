package ca.uqac.mysongs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ListOfSongs listOfSongs;
    private SongsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
    }

    private void setupUI(){
        listView = findViewById(R.id.list);


        listOfSongs = new ListOfSongs(this);
        adapter = new SongsAdapter(this, listOfSongs.getList());
        listView.setAdapter(adapter);
    }
}
