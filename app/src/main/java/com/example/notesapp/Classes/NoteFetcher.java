package com.example.notesapp.Classes;

import android.util.Log;

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
        baseUrl = "https://notesapp-a23a3-default-rtdb.firebaseio.com/Notes.json";
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
                    Log.d("NoteFetcher", "Data received: " + responseData); // Log pentru debugging
                    callback.onCallback(responseData);
                }
            }
        });
    }
}
