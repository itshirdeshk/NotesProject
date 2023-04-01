package com.hirdesh.notesproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnCreateNote;
    FloatingActionButton fabAdd;
    RecyclerView recyclerNotes;
    DataBaseHelper dataBaseHelper;
    LinearLayout llNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVar();

        showNotes();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_note_lay);

                EditText edtTitle, edtDescription;
                Button btnAdd;

                edtTitle = dialog.findViewById(R.id.edtTitle);
                edtDescription = dialog.findViewById(R.id.edtDescription);
                btnAdd = dialog.findViewById(R.id.btnAdd);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = edtTitle.getText().toString();
                        String content = edtDescription.getText().toString();

                        if (!content.equals("")) {
                            dataBaseHelper.noteDao().addNote(new Note(title, content));
                            showNotes();

                            dialog.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this, "Please Enter Something...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });

        btnCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabAdd.performClick();
            }
        });
    }

    private void initVar() {
        btnCreateNote = findViewById(R.id.btnCreate);
        fabAdd = findViewById(R.id.fabAdd);
        recyclerNotes = findViewById(R.id.recyclerNotes);
        llNotes = findViewById(R.id.llNotes);

        recyclerNotes.setLayoutManager(new GridLayoutManager(this, 2));
        dataBaseHelper = DataBaseHelper.getInstance(this);
    }

    public void showNotes() {
        ArrayList<Note> arrNotes = (ArrayList<Note>) dataBaseHelper.noteDao().getNotes();

        if (arrNotes.size() > 0) {
            recyclerNotes.setVisibility(View.VISIBLE);
            llNotes.setVisibility(View.GONE);

            recyclerNotes.setAdapter(new RecyclerNotesAdapter(this, arrNotes, dataBaseHelper));
        } else {
            llNotes.setVisibility(View.VISIBLE);
            recyclerNotes.setVisibility(View.GONE);
        }
    }
}