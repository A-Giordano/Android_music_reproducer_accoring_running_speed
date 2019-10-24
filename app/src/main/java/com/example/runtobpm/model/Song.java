package com.example.runtobpm.model;

import java.io.File;
import java.io.Serializable;

/** Class that represent a song */
public class Song implements Serializable {
    private File file;
    private String name;
    private int BPM;

    /** Class constructor
     * @param file song's path
     */
    public Song(File file) {
        this.file = file;
        name = file.getName();
        autoStetBPM();
    }

    /** Returns song's path
     * @return File
     */
    public File getFile() {
        return file;
    }

    /** Returns song's name
     * @return String
     */
    public String getName() {
        return name;
    }

    /** Returns song's BPM
     * @return int
     */
    public int getBPM() {
        return BPM;
    }

    /** Automatically sets song's BPM from the song's title.
     * !!! ASSUMPTION: All the audio in the device have their BPM as title.
     * This Will change implementing the ML algorithm to detect BPM
     */
    public void autoStetBPM () {
        BPM = Math.round(Float.valueOf(name.substring(0, name.lastIndexOf("."))));
        System.out.println("bpm: " + String.valueOf(BPM));
    }

}
