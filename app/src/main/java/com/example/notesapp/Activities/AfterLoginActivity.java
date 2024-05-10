package com.example.notesapp.Activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.Adapters.NotesAdapter;
import com.example.notesapp.Classes.FirebaseCallback;
import com.example.notesapp.Classes.Note;
import com.example.notesapp.Classes.NoteInteractionListener;
import com.example.notesapp.Classes.NoteManager;
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

public class AfterLoginActivity extends AppCompatActivity implements NoteInteractionListener{
    private NoteManager noteManager;
    private List<Note> allNotes = new ArrayList<>(); // Lista pentru a stoca toate notițele
    private NotesAdapter notesAdapter; // Adapter pentru RecyclerView
    private RecyclerView notesRecyclerView; // RecyclerView
    String category = "";
    Button addNoteButton;

    boolean userInteracted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        noteManager = new NoteManager();
        notesRecyclerView = findViewById(R.id.notesRecyclerView); // Asigură-te că există în layout
        notesAdapter = new NotesAdapter(this, allNotes,this);
        notesRecyclerView.setAdapter(notesAdapter);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        noteManager.readAllNotes(new FirebaseCallback() {
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
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(userInteracted){
                    category = parent.getItemAtPosition(position).toString();
                    filterNotesByCategory(category);
                    noteManager.readAllNotes(new FirebaseCallback() {
                        @Override
                        public void onCallback(String data) {
                            runOnUiThread(() -> {
                                updateNotesListWithoutNotify(data);
                            });
                        }
                        @Override
                        public void onError(Exception e) {
                            runOnUiThread(() -> {
                                Toast.makeText(AfterLoginActivity.this, "Error loading notes: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });
                        }
                    });
                }
                userInteracted=true;
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

    }
    public void filterNotesByCategory(String category) {
        List<Note> filteredNotes = new ArrayList<>();
        for (Note note : allNotes) {
            if (note.getCategory().equals(category)) {
                filteredNotes.add(note);
            }
        }
        notesAdapter.setNotes(filteredNotes);
    }
    private void updateNotesListCategory(String jsonData, String filterCategory) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Note>>() {
        }.getType();
        Map<String, Note> noteMap = gson.fromJson(jsonData, type);
        List<Note> filteredNotes = new ArrayList<>();
        for (Note note : noteMap.values()) {
            if (note.getCategory() != null && note.getCategory().equals(filterCategory)) {
                filteredNotes.add(note);
            }
        }
        runOnUiThread(() -> {
            if (!filteredNotes.isEmpty()) {
                notesAdapter.setNotes(filteredNotes);
            } else {
                Toast.makeText(AfterLoginActivity.this, "No notes found for category: " + filterCategory, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void updateNotesList(String jsonData) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Note>>() {
        }.getType();
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
    private void updateNotesListWithoutNotify(String jsonData) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Note>>() {
        }.getType();
        Map<String, Note> noteMap = gson.fromJson(jsonData, type);
        List<Note> notes = new ArrayList<>(noteMap.values()); // Convert map values to list
        runOnUiThread(() -> {
            if (notes != null && !notes.isEmpty()) {
                allNotes.clear();
                allNotes.addAll(notes);
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

        EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
        EditText editTextContent = dialogView.findViewById(R.id.editTextContent);
        Spinner spinner = dialogView.findViewById(R.id.categorySpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setPositiveButton("Adaugă", (dialog, id) -> {
            String title = editTextTitle.getText().toString().trim();
            String category = spinner.getSelectedItem().toString().trim();
            String content = editTextContent.getText().toString().trim();
            String noteId = UUID.randomUUID().toString();

            noteManager.createNote(noteId, title, category, content, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Eroare la adăugarea notiței", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        runOnUiThread(() -> {
                            noteManager.readAllNotes(new FirebaseCallback() {
                                @Override
                                public void onCallback(String data) {
                                    updateNotesListCategory(data, category);
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

        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = (int) (displayMetrics.widthPixels * 0.9);  // Setează lățimea la 90% din lățimea ecranului
            window.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        dialog.show();
    }
    private void showUpdateNoteDialog(@NonNull Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_note, null);
        builder.setView(dialogView);

        EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
        EditText editTextContent = dialogView.findViewById(R.id.editTextContent);

        editTextTitle.setText(note.getTitle());
        editTextContent.setText(note.getContent());

        builder.setPositiveButton("Update", (dialog, which) -> {
            String updatedTitle = editTextTitle.getText().toString();
            String updatedContent = editTextContent.getText().toString();
            String updatedCategory = category;

            noteManager.updateNote(note.getNoteId(), updatedTitle, updatedCategory, updatedContent, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Error updating note: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(Call call, Response response) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Note updated successfully", Toast.LENGTH_SHORT).show();
                        Log.d("UpdateNote", "Updating note with ID: " + note.getNoteId());
                        noteManager.readAllNotes(new FirebaseCallback() {
                            @Override
                            public void onCallback(String data) {
                                runOnUiThread(() ->  updateNotesListCategory(data, category));
                            }

                            @Override
                            public void onError(Exception e) {
                                runOnUiThread(() -> Toast.makeText(AfterLoginActivity.this, "Error loading notes: " + e.getMessage(), Toast.LENGTH_LONG).show());
                            }
                        });
                    });
                }
            });
        });

        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public void onDeleteNote(@NonNull Note note) {
        noteManager.deleteNote(note.getNoteId(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to delete note: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Note deleted successfully", Toast.LENGTH_SHORT).show();
                        noteManager.readAllNotes(new FirebaseCallback() {
                            @Override
                            public void onCallback(String data) {
                                runOnUiThread(() -> updateNotesListCategory(data, category));
                            }
                            @Override
                            public void onError(Exception e) {
                                runOnUiThread(() -> Toast.makeText(AfterLoginActivity.this, "Error loading notes: " + e.getMessage(), Toast.LENGTH_LONG).show());
                            }
                        });
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to delete note", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
    @Override
    public void onUpdateNote(Note note) {
        showUpdateNoteDialog(note);
    }
}
