package com.example.budget;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Questions1 extends AppCompatActivity implements View.OnClickListener {
    public Button siguiente;
    public Spinner androidios;
    public int cont;
    public EditText cantdventanas;
    public TextView cantventanas, operativo ;
    public String Email;
    public DatabaseReference presupuesto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        initviews();
        presupuesto = FirebaseDatabase.getInstance().getReference("presupuesto");
        Email = getIntent().getStringExtra("email");
        siguiente.setOnClickListener(this);
    }

    public void initviews(){
        siguiente = findViewById(R.id.botonsiguiente);
        androidios = findViewById(R.id.spinnerandroidios);
        operativo = findViewById(R.id.textoandroidios);
    }

    public void Cargarrespuesta(){
        presupuesto.child("contadortotal").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             cont = snapshot.getValue(Integer.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Questions1.this, "Algo salió mal", Toast.LENGTH_SHORT).show();
            }
        });
        presupuesto.child("usuario").child(String.valueOf(cont + 1)).child("plataformas").setValue(androidios.getSelectedItem());
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
                case R.id.botonsiguiente:
                    try{
                    Cargarrespuesta();
                    Intent intent = new Intent(Questions1.this, Question2.class);
                    startActivity(intent);
                    break;
            } catch(Exception e) {
                Toast.makeText(Questions1.this, "error en pregunta 1", Toast.LENGTH_SHORT);
            }
        }
    }
    //we have to manage visibility and send the data when button next is pressed and bach has only one function and is change the visibility again
    //Necesitamos un sistema de contadores

}
