package com.jonathanfinerty.todo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class TodoActivity extends Activity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        lvItems = (ListView) findViewById(R.id.lvItems);
        loadItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                saveItems();
                return true;
            }
        });
    }

    public void addToDoItem(View view) {
        EditText editText = (EditText) findViewById(R.id.etNewItem);
        itemsAdapter.add(editText.getText().toString());
        editText.setText("");
        saveItems();
    }

    private void loadItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            InputStream inputStream = new FileInputStream(todoFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            items = new ArrayList<String>();
            while ((line = reader.readLine()) != null) {
                items.add(line);
            }
            reader.close();
        } catch (IOException exception) {
            items = new ArrayList<String>();
            exception.printStackTrace();
        }
    }

    private void saveItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            OutputStream inputStream = new FileOutputStream(todoFile);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(inputStream));
            for (String item : items) {
                writer.write(item);
                writer.newLine();
            }
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
