package com.example.runtobpm.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.runtobpm.R;

import java.util.ArrayList;
/** Adapter that allows to display the song collection*/
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private ArrayList<Song> songCollection;
    private OnNoteListener onNoteListener;
    /** ViewHolder inner class*/
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView songNameView;
        public OnNoteListener onNoteListener;
        /** ViewHolder constructor
         * @param itemView
         * @param onNoteListener
         */
        public ViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            songNameView= itemView.findViewById(R.id.songName);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }
        /** Calls the onNoteClick method on the clicked item
         * @param v the View
         * */
        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }
    /** Constructor for the SongAdapter class*/
    public SongAdapter(ArrayList<Song> songCollection, OnNoteListener onNoteListener){
        this.songCollection = songCollection;
        this.onNoteListener = onNoteListener;
    }

    /** Creates the ViewHolder and place it inside the current ViewGroup
     * @param viewGroup
     * @param i the viewType which the holder should be placed in
     * @return the viewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.song_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v, onNoteListener);
        return vh;
    }
    /** Sets the item from the list into a specific holder in the recyclerView
     * @param viewHolder
     * @param i the position within the list
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Song song = songCollection.get(i);
        viewHolder.songNameView.setText(song.getName());

    }
    /** Shows the number of items in the list
     * @return the number of values in the list
     */
    @Override
    public int getItemCount() {
        return songCollection.size();
    }
    /** Interface to set the onClick method in other classes*/

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
