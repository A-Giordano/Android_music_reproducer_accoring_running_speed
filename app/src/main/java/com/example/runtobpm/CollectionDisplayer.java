package com.example.runtobpm;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.runtobpm.model.SongAdapter;

/***
 *  Activity that displays music collection and allows to start a song in it
 */
public class CollectionDisplayer extends AppCompatActivity implements SongAdapter.OnNoteListener {
    protected RunBPMapp model;
    private ImageButton buttonRun;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_displayer);
        hideSystemUI();
        model = (RunBPMapp) getApplication();
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        adapter = new SongAdapter(model.getSongManager().getSongCollection(), this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        buttonRun = findViewById(R.id.buttonRun);
        model.setAnimation((AnimationDrawable) buttonRun.getBackground());
        buttonRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /***
     * Hides navigation bar
     */
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    /** Stets the behaviour of the click on one of the RecyclerView's rows starting the related song
     * @param i the position in the ArrayList
     * */
    @Override
    public void onNoteClick(int i) {
        model.resetMediaPlsyer();
        model.playMusic(model.getSongManager().getSong(i));
        Log.d(TAG, "onNoteClick1: " + model.getSongManager().getSong(i));
        Log.d(TAG, "onNoteClick2: " + model.getPlayedSongName());
    }
}
