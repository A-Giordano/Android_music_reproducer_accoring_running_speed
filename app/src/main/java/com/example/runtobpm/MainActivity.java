package com.example.runtobpm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Locale;

/**
 *  Activity that allows to start automatic music reproduction according running speed
 *  */
public class MainActivity extends AppCompatActivity {
    protected RunBPMapp model;
    private ImageButton buttonCollection;
    private ImageButton buttonPlay;
    private ImageButton buttonVoiceOn;
    private SensorManager sensorManager;
    private Sensor stepCounter;
    private SensorEventListener sensorEventListener;
    private int stepCount;
    private TextView playedSongText;
    private static final String TAG = "MainActivity";
    private TextView spmText;
    private Handler stepCountHandler;
    private TextToSpeech stepCounterTTS;
    private boolean stepCounterTTScorrectlySetted = false;
    private boolean voiceEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideSystemUI();
        model = (RunBPMapp) getApplication();
        getExternalStoragePermission();
        setStepCounterTTS();
        playedSongText = findViewById(R.id.playedSong);
        spmText = findViewById(R.id.spm);
        spmText.setText("Steps Per Minute: " + model.getSpm());
        setPlayedSong();
        stepCount = 0;
        stepCountHandler = new Handler();
        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                    Log.d(TAG, "onSensorChanged: step");
                    stepCount++;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorManager.registerListener(sensorEventListener, stepCounter, SensorManager.SENSOR_DELAY_FASTEST);

        buttonVoiceOn = findViewById(R.id.voiceOn);
        model.setAnimation((AnimationDrawable) buttonVoiceOn.getBackground());
        buttonVoiceOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceEnabled = !voiceEnabled;
                setVoiceButton();
            }
        });
        buttonCollection = findViewById(R.id.buttonCollection);
        model.setAnimation((AnimationDrawable) buttonCollection.getBackground());
        buttonCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CollectionDisplayer.class);
                startActivity(intent);
            }
        });
        buttonPlay = findViewById(R.id.play);
        setPlayButtonImg();
        model.setAnimation((AnimationDrawable) buttonPlay.getBackground());
        calculateStepsPerMin();
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Log.d(TAG, "isplayed: " + isPlaying);
                if (!model.isPlaying()) {
                    model.playMusic(model.getSongManager().getClosestSongToSpm(model.getSpm()));
                    playedSongText.setText(model.getPlayedSongName());
                    buttonPlay.setImageResource(R.drawable.stop_button);
                } else {
                    model.stopMusic();
                    playedSongText.setText("");
                    buttonPlay.setImageResource(R.drawable.play_button);
                }
            }
        });
    }

    /** Displays the song name when a song is played*/
    public void setPlayedSong(){
        if(model.isPlaying()){
            playedSongText.setText(model.getPlayedSongName());
        }
    }
    /** Sets the right img. for the play/stop button*/
    public void setPlayButtonImg() {
        if (model.isPlaying()) {
            buttonPlay.setImageResource(R.drawable.stop_button);
        } else {
            buttonPlay.setImageResource(R.drawable.play_button);
        }
    }
    /** Sets the right img. for the voiceOn/Off button*/
    public void setVoiceButton() {
        if (voiceEnabled) {
            buttonVoiceOn.setImageResource(R.drawable.voice_off);
        } else {
            buttonVoiceOn.setImageResource(R.drawable.voice_on);
        }
    }

    /** Requests user permission to  access external storage and if allowed sets the songCollection*/
    public void getExternalStoragePermission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                //            songManager = new SongCollectionManager();
                model.getSongManager().setSongCollection();
            }
            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
            }
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    /** Counts the number of steps in 30 sec. and calculates the steps per minute*/
    public void calculateStepsPerMin() {
        stepCountHandler.postDelayed(stepCountRunnable,30000);
    }

    /** Runnable object that after and every 30 sec resets step count, plus sets, displays and if allowed pronounces the step per minute*/
    private Runnable stepCountRunnable = new Runnable() {
        @Override
        public void run() {
            model.setSpm(stepCount*2);
            Log.d(TAG, "spm: " + String.valueOf(model.getSpm()));
            spmText.setText("Steps Per Minute: " + model.getSpm());
            if (stepCounterTTScorrectlySetted && voiceEnabled){
                pronounceSPM();
            }
            Toast.makeText(MainActivity.this, "spm: " + model.getSpm(), Toast.LENGTH_SHORT).show();
            stepCount = 0;
            stepCountHandler.postDelayed(this, 30000);
        }
    };

    /** Initializes the TextToSpeech if English language available*/
    public void setStepCounterTTS(){
        stepCounterTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = stepCounterTTS.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        stepCounterTTScorrectlySetted = true;
                        voiceEnabled = true;
                        setVoiceButton();
                        buttonVoiceOn.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
    }

    /** Notices the user with his running speed (teps per min.)*/
    private void pronounceSPM() {
        if(voiceEnabled) {
            String text = "your running speed is " + model.getSpm() + "steps per minute";
            stepCounterTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    /**Hides navigation bar*/
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    /** Refreshes the GUI of this activity*/
    @Override
    public void onResume() {
        super.onResume();
        setPlayButtonImg();
        setPlayedSong();
    }

    /** Terminates TextToSpeech and Runnable object at app closure*/
    @Override
    protected void onDestroy() {
        if (stepCounterTTS != null) {
            stepCounterTTS.stop();
            stepCounterTTS.shutdown();
        }
        if (stepCountHandler != null){
            stepCountHandler.removeCallbacks(stepCountRunnable);
        }
        super.onDestroy();
    }

}

