package com.example.notesapp.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.Adapters.NotesAdapter;
import com.example.notesapp.Classes.FirebaseCallback;
import com.example.notesapp.Classes.Note;
import com.example.notesapp.Classes.NoteFetcher;
import com.example.notesapp.Classes.NoteManager;
import com.example.notesapp.Classes.NotesResponse;
import com.example.notesapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AfterLoginActivity extends AppCompatActivity {
    private NoteManager noteManager;
    private List<Note> allNotes = new ArrayList<>(); // Lista pentru a stoca toate notițele
    private NotesAdapter notesAdapter; // Adapter pentru RecyclerView
    private RecyclerView notesRecyclerView; // RecyclerView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        noteManager = new NoteManager();

        notesRecyclerView = findViewById(R.id.notesRecyclerView); // Asigură-te că există în layout
        notesAdapter = new NotesAdapter(this, allNotes);
        notesRecyclerView.setAdapter(notesAdapter);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//
        NoteFetcher fetcher = new NoteFetcher();
        fetcher.fetchAllNotes(new FirebaseCallback() {
            @Override
            public void onCallback(String data) {
                updateNotesList(data); // Actualizează lista de note folosind datele parseate
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(AfterLoginActivity.this, "Error loading notes: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });

        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Filtrarea listei de notițe în RecyclerView în funcție de categoria selectată
                String selectedCategory = parent.getItemAtPosition(position).toString();
                filterNotesByCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
    });

    }
    public void filterNotesByCategory(String category) {
        List<Note> filteredNotes = new ArrayList<>();
        for (Note note : allNotes) {
            if (note.getCategory().equals(category)) {
                filteredNotes.add(note);
            }
        }
        notesAdapter.setNotes(filteredNotes);
        notesAdapter.notifyDataSetChanged();
    }

    private void updateNotesList(String jsonData) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Note>>(){}.getType();
        Map<String, Note> noteMap = gson.fromJson(jsonData, type);
        List<Note> notes = new ArrayList<>(noteMap.values()); // Convert map values to list
        runOnUiThread(() -> {
            if (notes != null && !notes.isEmpty()) {
                allNotes.clear();
                allNotes.addAll(notes);
                notesAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(AfterLoginActivity.this, "No notes found in the received data", Toast.LENGTH_LONG).show();
            }
        });
    }




}
