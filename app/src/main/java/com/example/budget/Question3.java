package com.example.budget;

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

public class Question3 extends AppCompatActivity implements View.OnClickListener{
  Spinner spinnselect;
  Button sigboton;
  int cont;

  public DatabaseReference presupuesto;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question3);
        initviews();
        presupuesto = FirebaseDatabase.getInstance().getReference("presupuesto");
    }

    public void initviews(){
     sigboton = findViewById(R.id.botonsiguiente);
     spinnselect = findViewById(R.id.spinnerusuarios);
    }

    public void info() {
        presupuesto.child("contadortotal").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             try {
                cont = snapshot.getValue(Integer.class);
             } catch (Exception e){
                 Toast.makeText(Question3.this, "error" + e, Toast.LENGTH_SHORT).show();
             }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        presupuesto.child("usuario").child(String.valueOf(cont + 1)).child("logyusers").setValue(spinnselect.getSelectedItem().toString());
    }

    @Override
    public void onClick(View view) {

    }
}
