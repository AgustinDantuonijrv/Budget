package com.example.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Question2 extends AppCompatActivity implements View.OnClickListener {

    public Button sig;
    public EditText cantventanas;
    public DatabaseReference presupuesto;
    public int cont;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions2);
        initViews();
        sig.setOnClickListener(this);
        presupuesto = FirebaseDatabase.getInstance().getReference("presupuesto");
    }
    public void  initViews(){
        sig =findViewById(R.id.botonsiguiente);
        cantventanas = findViewById(R.id.ventanastexto);
    }

    public void CargarInfo(){
        presupuesto.child("contadortotal").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    cont = snapshot.getValue(Integer.class);
                    Toast.makeText(Question2.this, "valor:" + cont, Toast.LENGTH_SHORT).show();

                } catch (Exception e){
                  Toast.makeText(Question2.this, "on data change cargar info" + cont, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Question2.this, "Algo salió mal", Toast.LENGTH_SHORT).show();
            }
        });
        presupuesto.child("usuario").child(String.valueOf(cont + 1)).child("cantidaddeventanas").setValue(cantventanas.getText().toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.botonsiguiente:
                try {
                    CargarInfo();
                    Intent intent = new Intent(Question2.this, Question3.class);
                    startActivity(intent);
                    break;
                }catch (Exception e){
                    Toast.makeText(Question2.this, "error en la parte 2" + e, Toast.LENGTH_SHORT).show();
                }
        }
    }
}
