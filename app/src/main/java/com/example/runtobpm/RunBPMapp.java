package com.example.runtobpm;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.widget.Toast;
import com.example.runtobpm.model.Song;
import com.example.runtobpm.model.SongCollectionManager;

/** Application class that hold the Media Player and the songManager*/
public class RunBPMapp extends android.app.Application {
    private SongCollectionManager songManager;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private int spm;
    private String playedSong;
    /** Application class constructor*/
    @Override
    public void onCreate() {
        super.onCreate();
        songManager = new SongCollectionManager();
        mediaPlayer = new MediaPlayer();
        isPlaying = false;
        spm = 0;
        playedSong = "";
    }
    /** Returns the songCollectionManager
     * @return the SongCollectionManager
     */
    public SongCollectionManager getSongManager() {
        return songManager;
    }
    /** Returns the MediaPlayer
     * @return the MediaPlayer
     */
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
    /** Returns if the MediaPlayer is playing
     * @return boolean
     */
    public boolean isPlaying() {
        return isPlaying;
    }
    /** Stops the MediaPlayer*/
    public void stopMusic(){
        mediaPlayer.stop();
        isPlaying=false;
    }
    /** Resets the MediaPlayer*/
    public void resetMediaPlsyer(){
        mediaPlayer.reset();
    }
    /** Returns the Steps per min.
     * @return int
     */
    public int getSpm() {
        return spm;
    }
    /** Returns the song that is playing
     * @return String
     */
    public String getPlayedSongName() {
        return playedSong;
    }
    /** Sets the steps per min.
     * @param spm
     */
    public void setSpm(int spm) {
        this.spm = spm;
    }
    /** Starts selected song reproduction followed by the most appropriate ones
     * @param song Song object
     */
    public void playMusic(Song song){
        try {
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mPlayer) {
                    isPlaying = false;
                    mediaPlayer.reset();
                    playMusic(songManager.getClosestSongToSpm(spm));
                }
            });
            Toast.makeText(getApplicationContext(), song.getName(), Toast.LENGTH_SHORT).show();
            mediaPlayer.setDataSource(song.getFile().toString());
            mediaPlayer.prepare();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception of type : " + e.toString());
            e.printStackTrace();
        }
        mediaPlayer.start();
        playedSong = song.getName();
        isPlaying = true;
        Toast.makeText(getApplicationContext(),"Start new song: " + playedSong, Toast.LENGTH_SHORT).show();
    }

    /** Sets animation of different objects
     * @param a AnimationDrawable
     */
    public void setAnimation(AnimationDrawable a) {
        AnimationDrawable animation = a;
        animation.setExitFadeDuration(2000);
        animation.setEnterFadeDuration(4000);
        animation.start();
    }
}


