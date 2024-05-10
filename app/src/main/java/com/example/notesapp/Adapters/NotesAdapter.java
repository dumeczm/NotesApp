package com.example.notesapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.Classes.Note;
import com.example.notesapp.Classes.NoteInteractionListener;
import com.example.notesapp.R;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notes;
    private LayoutInflater inflater;
    private NoteInteractionListener listener;

    public NotesAdapter(Context context, List<Note> notes, NoteInteractionListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
        this.listener=listener;
    }
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(itemView);
    }
    public void setNotes(List<Note> newNotes) {
        notes.clear();
        notes.addAll(newNotes);
        notifyDataSetChanged();
    }
    public void setNotesWithoutNotify(List<Note> newNotes) {
        notes.clear();
        notes.addAll(newNotes);
    }
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.titleTextView.setText(note.getTitle());
        holder.contentTextView.setText(note.getContent());

        // Setează listener pentru apăsare lungă
        holder.itemView.setOnLongClickListener(view -> {
            showOptionsDialog(note);
            return true;
        });
    }
    private void showOptionsDialog(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());
        builder.setTitle("Select Option");
        builder.setItems(new CharSequence[]{"Update", "Delete", "Cancel"}, (dialog, which) -> {
            switch (which) {
                case 0: // Update
                    listener.onUpdateNote(note);
                    break;
                case 1: // Delete
                    listener.onDeleteNote(note);
                    break;
                case 2: // Cancel
                    dialog.dismiss();
                    break;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView contentTextView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tvTitle);
            contentTextView = itemView.findViewById(R.id.tvContent);
        }
    }



}
