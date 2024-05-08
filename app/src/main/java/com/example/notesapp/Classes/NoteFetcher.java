package com.example.notesapp.Classes;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.Callback;
import java.io.IOException;
public class NoteFetcher {
    private OkHttpClient client;
    private String baseUrl;

    public NoteFetcher() {
        client = new OkHttpClient();
        baseUrl = "https://[PROJECT-ID].firebaseio.com/notes.json";  // Înlocuiește [PROJECT-ID] cu ID-ul tău real de proiect Firebase.
    }

    public void fetchAllNotes(FirebaseCallback callback) {
        Request request = new Request.Builder()
                .url(baseUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Notifică despre eșec
                callback.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError(new IOException("Unexpected code " + response));
                } else {
                    // Returnează datele printr-un callback
                    String responseData = response.body().string();
                    callback.onCallback(responseData);
                }
            }
        });
    }
}
