package com.example.notesapp.Activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AfterLoginActivity extends AppCompatActivity {
    private NoteManager noteManager;
    private List<Note> allNotes = new ArrayList<>(); // Lista pentru a stoca toate notițele
    private NotesAdapter notesAdapter; // Adapter pentru RecyclerView
    private RecyclerView notesRecyclerView; // RecyclerView
    NoteFetcher fetcher;
    String category="";
    Button addNoteButton;
    Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        noteManager = new NoteManager();
        notesRecyclerView = findViewById(R.id.notesRecyclerView); // Asigură-te că există în layout
        notesAdapter = new NotesAdapter(this, allNotes);
        notesRecyclerView.setAdapter(notesAdapter);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetcher = new NoteFetcher();
        fetcher.fetchAllNotes(new FirebaseCallback() {
            @Override
            public void onCallback(String data) {
                runOnUiThread(() -> {
                    updateNotesList(data);
                });

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
                category = parent.getItemAtPosition(position).toString();
                filterNotesByCategory(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
    });

        addNoteButton = findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddNoteDialog();
            }
        });

        refreshButton = findViewById(R.id.refreshRecyclerViewButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterNotesByCategory(category);
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
    private void updateNotesListCategory(String jsonData, String filterCategory) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Note>>(){}.getType();
        Map<String, Note> noteMap = gson.fromJson(jsonData, type);
        List<Note> filteredNotes = new ArrayList<>();
        for (Note note : noteMap.values()) {
            if (note.getCategory() != null && note.getCategory().equals(filterCategory)) {
                filteredNotes.add(note);
            }
        }
        runOnUiThread(() -> {
            if (!filteredNotes.isEmpty()) {
                allNotes.clear();
                allNotes.addAll(filteredNotes);
                notesAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(AfterLoginActivity.this, "No notes found for category: " + filterCategory, Toast.LENGTH_LONG).show();
            }
        });
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
    private void showAddNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_note, null);
        builder.setView(dialogView);

        EditText editTextCategory = dialogView.findViewById(R.id.editTextCategory);
        EditText editTextContent = dialogView.findViewById(R.id.editTextContent);

        builder.setPositiveButton("Adaugă", (dialog, id) -> {
            String category = editTextCategory.getText().toString().trim();
            String content = editTextContent.getText().toString().trim();
            String noteId = UUID.randomUUID().toString();

            noteManager.createNote(noteId, category, content, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Eroare la adăugarea notiței", Toast.LENGTH_SHORT).show());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        runOnUiThread(() -> {
                            fetcher.fetchAllNotes(new FirebaseCallback() {
                                @Override
                                public void onCallback(String data) {
                                    updateNotesListCategory(data,category); // Aici se actualizează lista
                                }

                                @Override
                                public void onError(Exception e) {
                                    Toast.makeText(AfterLoginActivity.this, "Error loading notes: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                            Toast.makeText(getApplicationContext(), "Notiță adăugată cu succes", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Eroare la adăugarea notiței", Toast.LENGTH_SHORT).show());
                    }
                }
            });
        });

        builder.setNegativeButton("Anulează", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
