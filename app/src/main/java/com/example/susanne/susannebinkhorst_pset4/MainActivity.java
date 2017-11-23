package com.example.susanne.susannebinkhorst_pset4;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TodoDatabase Database;
    Button add;
    ListView list;
    EditText todo;
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todo = findViewById(R.id.todo);
        add = findViewById(R.id.add);
        list = findViewById(R.id.list);

        Database = TodoDatabase.getInstance(getApplicationContext());
        Cursor cursor = Database.selectAll();
        adapter = new TodoAdapter(getApplicationContext(), cursor);

        list.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = todo.getText().toString();
                if (item.length() != 0){
                    addItem(item, false);
                    todo.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Please add a to do", Toast.LENGTH_SHORT).show();
                }
                updateData();
            }
        });

        list.setOnItemClickListener(new ListOnItemClickListener());
        list.setOnItemLongClickListener(new ListOnItemLongClickListener());
    }

    public void addItem(String title, Boolean completed) {
        Database = TodoDatabase.getInstance(this);
        boolean addItem = Database.insert(title, completed);

        if (addItem){
            Toast.makeText(this, "Item added to your to do list", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Item could not be added to your to do list", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateData(){
        Cursor new_cursor = Database.selectAll();
        adapter = new TodoAdapter(getApplicationContext(), new_cursor);
        list.setAdapter(adapter);
    }

    private class ListOnItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CheckBox checkBox = view.findViewById(R.id.checkBox);

            Boolean status = checkBox.isChecked();
            Database.update(id, status);

            updateData();
        }
    }

    private class ListOnItemLongClickListener implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Database.delete(id);
            TextView text = view.findViewById(R.id.textView);
            String message = text.getText().toString() + " deleted from your list";
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            updateData();
            return true;
        }
    }


}
