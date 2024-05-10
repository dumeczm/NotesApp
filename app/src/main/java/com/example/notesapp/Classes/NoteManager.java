package com.example.notesapp.Classes;
import android.util.Log;

import java.io.IOException;

import okhttp3.*;

public class NoteManager {
    private OkHttpClient client = new OkHttpClient();
    private String baseUrl = "https://notesapp-a23a3-default-rtdb.firebaseio.com/Notes/";
    private String notesFetcherUrl ="https://notesapp-a23a3-default-rtdb.firebaseio.com/Notes.json";

    public void createNote(String noteId, String title, String category, String content, Callback callback) {
        // Include title în datele JSON
        String jsonData = String.format("{\"id\": \"%s\",\"title\": \"%s\", \"category\": \"%s\", \"content\": \"%s\"}",noteId, title, category, content);
        RequestBody body = RequestBody.create(jsonData, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(baseUrl + noteId + ".json")
                .put(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("NoteManager", "Failed to create note: " + e.getMessage());
                if (callback != null) {
                    callback.onFailure(call, e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i("NoteManager", "Note created successfully: " + response.body().string());
                    if (callback != null) {
                        callback.onResponse(call, response);
                    }
                } else {
                    Log.e("NoteManager", "Failed to create note: " + response.code());
                    if (callback != null) {
                        callback.onFailure(call, new IOException("Failed to create note: " + response.code() + " - " + response.message()));
                    }
                }
            }
        });
    }
    public void readAllNotes(FirebaseCallback callback) {
        Request request = new Request.Builder()
                .url(notesFetcherUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                callback.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError(new IOException("Unexpected code " + response));
                } else {
                    // Returnează datele printr-un callback
                    String responseData = response.body().string();
                    Log.d("NoteFetcher", "Data received: " + responseData); // Log pentru debugging
                    callback.onCallback(responseData);
                }
            }
        });
    }
    public void updateNote(String noteId, String title, String category, String content, Callback callback) {
        String jsonData = String.format("{\"id\": \"%s\",\"title\": \"%s\", \"category\": \"%s\", \"content\": \"%s\"}",noteId, title, category, content);
        RequestBody body = RequestBody.create(jsonData, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(baseUrl + noteId + ".json")
                .patch(body)
                .build();

        client.newCall(request).enqueue(callback);
    }
    public void deleteNote(String noteId, Callback callback) {
        Request request = new Request.Builder()
                .url(baseUrl + noteId + ".json")
                .delete()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onFailure(call, e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callback != null) {
                    callback.onResponse(call, response);
                }
            }
        });
    }

}
