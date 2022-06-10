package com.example.sqlite_crud_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    EditText etSName, etDOB;
    RecyclerView recyclerView;
    Button btnInsert;

    MyDBHandler myDBHandler;
    CustomAdaptor customAdaptor;

    DatePickerDialog dp;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rvStud);
        etSName = findViewById(R.id.etSName);
        etDOB = findViewById(R.id.etDOB);
        btnInsert = findViewById(R.id.btnInsert);
        myDBHandler = new MyDBHandler(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        customAdaptor = new CustomAdaptor(this, myDBHandler.getArray());

        if (customAdaptor.jsonArray.length() > 0) {
            recyclerView.setAdapter(customAdaptor);
        } else {
            recyclerView.setAdapter(null);
            Toast.makeText(this, "No Records.", Toast.LENGTH_SHORT).show();
        }


        etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int Day = calendar.get(calendar.DAY_OF_MONTH);
                int Month = calendar.get(calendar.MONTH);
                int Year = calendar.get(calendar.YEAR);
                dp = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        etDOB.setText(i2 + "/" + (i1 + 1) + "/" + i);
                    }
                }, Day, Month, Year);
                dp.show();
            }
        });


        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String S_NAME = etSName.getText().toString();
                String S_DOB = etDOB.getText().toString();

                if (myDBHandler.Insert(S_NAME, S_DOB) == 1) {
                    Toast.makeText(MainActivity.this, "New Record Added.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Something wrong.", Toast.LENGTH_SHORT).show();
                }

                customAdaptor.updateArray(myDBHandler.getArray());
                recyclerView.setAdapter(customAdaptor);

                etSName.setText("");
                etDOB.setText("");
            }
        });
    }
}