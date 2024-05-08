package com.example.notesapp.Classes;
import okhttp3.*;

public class NoteManager {
    private OkHttpClient client = new OkHttpClient();
    private String baseUrl = "https://notesapp-a23a3-default-rtdb.firebaseio.com/Notes/";

    public void createNote(String noteId, String category, String content, Callback callback) {
        String jsonData = String.format("{\"category\": \"%s\", \"content\": \"%s\"}", category, content);
        RequestBody body = RequestBody.create(jsonData, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(baseUrl + noteId + ".json")
                .put(body)
                .build();
        client.newCall(request).enqueue(callback);
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
