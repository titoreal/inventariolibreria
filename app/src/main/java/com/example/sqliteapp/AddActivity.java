package com.example.sqliteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    EditText title_input, author_input, pages_input, price_input;
    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title_input = findViewById(R.id.title_input);
        author_input = findViewById(R.id.author_input);
        pages_input = findViewById(R.id.pages_input);
        price_input = findViewById(R.id.price_input);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);

                String pagesString = pages_input.getText().toString().trim();
                int pagesInt = Integer.parseInt(pagesString.replaceAll("\\D+", ""));

                String priceString = price_input.getText().toString().trim();
                priceString = priceString.replace("$", "").trim();
                float priceFloat = Float.parseFloat(priceString);
                int priceInt = Math.round(priceFloat);

                myDB.addBook(
                        title_input.getText().toString().trim(),
                        author_input.getText().toString().trim(),
                        pagesInt,
                        priceInt
                );
                Log.d("AddActivity", "Páginas: " + pagesInt + ", Precio: " + priceInt);
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}