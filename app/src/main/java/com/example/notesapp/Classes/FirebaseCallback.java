package com.example.notesapp.Classes;

public interface FirebaseCallback {
    void onCallback(String data);
    void onError(Exception e);
}
