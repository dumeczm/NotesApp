package com.example.notesapp.Activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notesapp.Classes.FirebaseCallback;
import com.example.notesapp.Classes.NoteFetcher;
import com.example.notesapp.Classes.NoteManager;
import com.example.notesapp.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AfterLoginActivity extends AppCompatActivity {
    private NoteManager noteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_after_login);
        noteManager = new NoteManager();

        // Exemplu de adăugare a unei notițe
        noteManager.createNote("noteID2", "Munca", "Acesta este conținutul notiței.", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();
                    // Actualizează UI cu răspunsul dacă este necesar
                }
            }
        });

        NoteFetcher fetcher = new NoteFetcher();
        fetcher.fetchAllNotes(new FirebaseCallback() {
            @Override
            public void onCallback(String data) {
                // Gestionează datele JSON aici
                // De exemplu, actualizează UI-ul sau parsează JSON-ul
                runOnUiThread(() -> {
                    // Actualizează UI-ul aici
                });
            }

            @Override
            public void onError(Exception e) {
                // Gestionează eroarea aici
                runOnUiThread(() -> {
                    // Afișează un mesaj de eroare sau tratează eroarea
                });
            }
        });
    }
}