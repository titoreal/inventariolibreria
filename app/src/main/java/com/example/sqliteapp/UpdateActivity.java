package com.example.sqliteapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    EditText title_input, author_input, pages_input, price_input;
    Button update_button, delete_button;

    String id, title, author, pages, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        title_input = findViewById(R.id.title_input2);
        author_input = findViewById(R.id.author_input2);
        pages_input = findViewById(R.id.pages_input2);
        price_input = findViewById(R.id.price_input2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);

        //First we call this
        getAndSetIntentData();

        //Set actionbar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
        }

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = title_input.getText().toString().trim();
                author = author_input.getText().toString().trim();

                // Procesar páginas
                String pagesString = pages_input.getText().toString().trim();
                int pagesInt = 0;
                try {
                    pagesString = pagesString.replaceAll("[^0-9]", ""); // Elimina todo excepto números
                    pagesInt = Integer.parseInt(pagesString);
                } catch (NumberFormatException e) {
                    Log.e("UpdateActivity", "Error parsing pages: " + pagesString, e);
                    Toast.makeText(UpdateActivity.this, "Error en el formato de páginas", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Procesar precio
                String priceString = price_input.getText().toString().trim();
                int priceInt = 0;
                try {
                    priceString = priceString.replace("$", "").replaceAll("[^0-9]", ""); // Elimina todo excepto números
                    priceInt = Integer.parseInt(priceString);
                } catch (NumberFormatException e) {
                    Log.e("UpdateActivity", "Error parsing price: " + priceString, e);
                    Toast.makeText(UpdateActivity.this, "Error en el formato de precio", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Log para depuración
                Log.d("UpdateActivity", "Páginas (raw): " + pages_input.getText().toString());
                Log.d("UpdateActivity", "Precio (raw): " + price_input.getText().toString());
                Log.d("UpdateActivity", "Páginas (processed): " + pagesInt);
                Log.d("UpdateActivity", "Precio (processed): " + priceInt);

                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                boolean updateSuccess = myDB.updateData(id, title, author, pagesInt, priceInt);

                if (updateSuccess) {
                    Toast.makeText(UpdateActivity.this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                    // Volver a la actividad principal
                    Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Cierra la actividad actual
                } else {
                    Toast.makeText(UpdateActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });

    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                getIntent().hasExtra("author") && getIntent().hasExtra("pages") && getIntent().hasExtra("price")) {
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            author = getIntent().getStringExtra("author");
            pages = getIntent().getStringExtra("pages");
            price = getIntent().getStringExtra("price");

            //Setting Intent Data
            title_input.setText(title);
            author_input.setText(author);
            pages_input.setText(pages + " páginas");
            price_input.setText("$" + price);
            Log.d("stev", title + " " + author + " " + pages + " " + price);
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Borrar " + title + " ?");
        builder.setMessage("Está seguro de eliminar " + title + " ?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id);

                Toast.makeText(UpdateActivity.this, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                // Volver a la actividad principal
                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Cierra la actividad actual
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // No hacer nada si el usuario elige "No"
            }
        });
        builder.create().show();
    }
}