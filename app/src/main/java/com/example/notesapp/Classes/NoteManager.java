package com.example.notesapp.Classes;
import android.util.Log;

import java.io.IOException;

import okhttp3.*;

public class NoteManager {
    private OkHttpClient client = new OkHttpClient();
    private String baseUrl = "https://notesapp-a23a3-default-rtdb.firebaseio.com/Notes/";

    public void createNote(String noteId, String category, String content, Callback callback) {
        String jsonData = String.format("{\"category\": \"%s\", \"content\": \"%s\"}", category, content);
        RequestBody body = RequestBody.create(jsonData, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(baseUrl + noteId + ".json")
                .put(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("NoteManager", "Failed to create note: " + e.getMessage());
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("NoteManager", "Response for creating note: " + response.body().string());
                callback.onResponse(call, response);
            }
        });
    }

    public void readNote(String noteId, Callback callback) {
        Request request = new Request.Builder()
                .url(baseUrl + noteId + ".json")
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void updateNote(String noteId, String category, String content, Callback callback) {
        String jsonData = String.format("{\"category\": \"%s\", \"content\": \"%s\"}", category, content);
        RequestBody body = RequestBody.create(jsonData, MediaType.get("application/json; charset=utf-8"));
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
        client.newCall(request).enqueue(callback);
    }
}
