package com.vimal.commrade;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class AddMember extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton actionButton;

    DatabaseHandler database;
    ArrayList<String> name, relation, contact, id;
    RecycleViewAdapter recycleViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        recyclerView = findViewById(R.id.recycleviewMemberadd);
        actionButton = findViewById(R.id.addMemberBtn);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddMember.this, MemberInput.class);
                startActivity(intent);
            }
        });

        database = new DatabaseHandler(AddMember.this);
        name = new ArrayList<>();
        id = new ArrayList<>();
        relation = new ArrayList<>();
        contact = new ArrayList<>();
        displayMember();

        recycleViewAdapter = new RecycleViewAdapter(AddMember.this, id, name, relation, contact);
        recyclerView.setAdapter(recycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddMember.this));

    }

    void displayMember() {
        Cursor cursor = database.readData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Member Added", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                relation.add(cursor.getString(2));
                contact.add(cursor.getString(3));
            }
        }
    }


}