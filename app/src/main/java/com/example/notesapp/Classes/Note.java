package com.example.notesapp.Classes;

public class Note {
    private String noteId; // Unic identifier for each note
    private String category; // Category of the note
    private String title; // Category of the note
    private String content; // Content of the note

    // Constructor
    public Note(String noteId, String category, String content, String title) {
        this.noteId = noteId;
        this.category = category;
        this.content = content;
        this.title = title;
    }

    // Getters and Setters
    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // ToString method for debugging
    @Override
    public String toString() {
        return "Note{" +
                "noteId='" + noteId + '\'' +
                ", category='" + category + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
