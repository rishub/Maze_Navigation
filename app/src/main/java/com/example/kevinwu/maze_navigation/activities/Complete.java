package com.example.kevinwu.maze_navigation.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kevinwu.maze_navigation.R;

import java.util.ArrayList;

public class Complete extends AppCompatActivity {

    private ListView scoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        scoreList = (ListView) findViewById(R.id.scoreListView);

        final ArrayList scores = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter(Complete.this, android.R.layout.simple_list_item_1, scores);
        scoreList.setAdapter(adapter);

        // Display the things into the list view
        adapter.add("Imaqtpie");
        adapter.add("nogler777");
        adapter.add("kevin595");
        adapter.add("Retris");
        adapter.add("Diversion");
        adapter.add("CRM Lapras");
        adapter.add("CRM U");
        adapter.add("CVL JDawg");
        adapter.add("Doublelift");
    }

    public void finishButton(View view) {
        Intent intent = new Intent(this, Connection.class);
        startActivity(intent);
    }
}
