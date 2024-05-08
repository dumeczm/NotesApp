package com.example.notesapp.Classes;

import java.util.List;

public class NotesResponse {
    private List<Note> notes;

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}

