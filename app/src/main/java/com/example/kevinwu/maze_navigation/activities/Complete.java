package com.example.kevinwu.maze_navigation.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kevinwu.maze_navigation.R;
import com.example.kevinwu.maze_navigation.models.HttpGetRequest;
import com.example.kevinwu.maze_navigation.views.GameView;

import org.json.JSONArray;
import org.json.JSONObject;

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

        String url = "http://mazenavigation.herokuapp.com/add/win/" + GameView.character.getUsername();

        HttpGetRequest getRequest = new HttpGetRequest();
        try {
            getRequest.execute(url).get();
        }
        catch (Exception e) {}

        // Display the things into the list view
        url = "http://mazenavigation.herokuapp.com/winners";
        String result = "";
        getRequest = new HttpGetRequest();
        try {
            result = getRequest.execute(url).get();
            JSONArray obj = new JSONArray(result);
            JSONArray arr = obj.getJSONArray(1);
            for (int i = 0; i < arr.length(); i++) {
                adapter.add(arr.getJSONObject(i).get("username") + " wins: " + arr.getJSONObject(i).get("wins"));
            }
        }
        catch (Exception e) {}
    }

    public void finishButton(View view) {
        Intent intent = new Intent(this, Connection.class);
        startActivity(intent);
    }
}
