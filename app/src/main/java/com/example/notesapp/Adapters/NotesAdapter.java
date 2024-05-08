package com.example.notesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.Classes.Note;
import com.example.notesapp.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notes;
    private LayoutInflater inflater;

    public NotesAdapter(Context context, List<Note> notes) {
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note currentNote = notes.get(position);
        holder.categoryTextView.setText(currentNote.getCategory());
        holder.contentTextView.setText(currentNote.getContent());
    }

    public void setNotes(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();  // Notifică RecyclerView-ul că datele au fost actualizate
    }
    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryTextView;
        private TextView contentTextView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.tvCategory);
            contentTextView = itemView.findViewById(R.id.tvContent);
        }
    }

}
