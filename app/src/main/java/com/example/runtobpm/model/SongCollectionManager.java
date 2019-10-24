package com.example.runtobpm.model;

import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import static android.content.ContentValues.TAG;

/** Class that represent the song's collection in the device.*/
public class SongCollectionManager {
    private ArrayList<Song> songCollection;

    /** Class constructor*/
    public SongCollectionManager() {
        songCollection = new ArrayList<>();
    }

    /** For all the songs in the device creates Song object and add to the collection*/
    public void setSongCollection() {
        ArrayList<File> audioFiles = readOnlyAudioFiles(Environment.getExternalStorageDirectory());
        Log.d(TAG, "setSongCollection: audioFIles " + audioFiles);
        Log.d(TAG, "setSongCollection:  songCollection" + songCollection);
        for (File f : audioFiles) {
            Song song = new Song(f);
            Log.d(TAG, "setSongCollection: song " + song);
            songCollection.add(song);
            Log.d(TAG, "setSongCollection:  songCollection" + songCollection.size());
        }
        Log.d(TAG, "setSongCollection:  END!!!!");
    }
    /** Scans all the audios in the device, except for hidden folders and files and returns song's collection
     * @param file file in the device
     * @return ArrayList
     */
    public ArrayList readOnlyAudioFiles(File file) {
        ArrayList<File> audioList = new ArrayList<>();
        File[] allFiles = file.listFiles();
        for (File f : allFiles) {
            if (f.isDirectory() && !f.isHidden()) {
                audioList.addAll(readOnlyAudioFiles(f));
                 Log.d(TAG, "readOnlyAudioFiles: " + f);
            } else {
                if (!f.isHidden()){
                    if (f.getName().endsWith(".mp3") || f.getName().endsWith(".wav") || f.getName().endsWith(".wma")){
                        audioList.add(f);
                    }
                }
            }
        }
        return audioList;
    }
    /** Returns the song with the closest BPM to SPM, the first one will be the slowest
     * @param spm steps per minute
     * @return Song
     */
    public Song getClosestSongToSpm(int spm){
        Log.d(TAG, "getClosestSongToSpm: 0 step");

        int distance = Math.abs(songCollection.get(0).getBPM() - spm);
        Log.d(TAG, "getClosestSongToSpm: 1 step");
        int idx = 0;
        for(int c = 1; c < songCollection.size(); c++){
            int cdistance = Math.abs(songCollection.get(c).getBPM() - spm);
            if(cdistance < distance){
                idx = c;
                distance = cdistance;
            }
        }
        Log.d(TAG, "getClosestSongToSpm: 2 step");
        Song song = songCollection.get(idx);
        return song;
    }
    /** Returns song's collection
     * @return ArrayList
     */
    public ArrayList<Song> getSongCollection() {
        return songCollection;
    }

    /** Returns song at specific position
     * @param i position
     * @return Song
     */
    public Song getSong(int i){
        return songCollection.get(i);
    }
}
