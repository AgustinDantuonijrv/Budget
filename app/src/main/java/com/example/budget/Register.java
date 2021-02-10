package com.example.budget;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Objects;

public class Register extends AppCompatActivity implements View.OnClickListener {
    public Button Registerbtn;
    public Boolean Success;
    EditText editemail, editpassword;
    public Uri filePath;
    private ProgressDialog progressDialog;

    private static final String TAG = "Se envio el email";

    public  Boolean usuarioexistente;
    private FirebaseAuth firebaseAuth;
    public String usuario, email, estado;
    public DatabaseReference presupuesto;

    public EditText editTextEmail, editTextPassword;
    public DatabaseReference databaseReference;

    public DatabaseReference users;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        try {
            initViews();
            Registerbtn.setOnClickListener(this);

            databaseReference = FirebaseDatabase.getInstance().getReference();

            //buttonlog1.setOnClickListener(this);

            progressDialog = new ProgressDialog(this);
            //ImageUserPhoto.setOnClickListener(this);

            firebaseAuth = FirebaseAuth.getInstance();

            //progressDialog = new ProgressDialog(this);

            users = FirebaseDatabase.getInstance().getReference("usuario");

            usuario = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();


        } catch (Exception e) {
            //  Toast.makeText(Register.this, "e" + e, Toast.LENGTH_SHORT).show();
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("firebasekey", "value");
        editor.apply();
    }
    public void initViews(){
       Registerbtn = findViewById(R.id.btnRegister);
       editTextEmail = findViewById(R.id.inputEmail);
       editTextPassword = findViewById(R.id.inputPassword);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    }

    private void registrarUsuario() { // este bloque permite agregar a la seccion de autenticacion un usuario por medio de email y clave para
        //poder tener una vista de los usuarios ingresados y que estos se puedan loguear
        // tambien se le envia un mensaje viamail de confirmacion al usuario del mail ingresado

        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        if (TextUtils.isEmpty(email)) { // para que no quede vacio el edit text destinado al ingreso de email
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();

            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Falta ingresar la contraseÒa", Toast.LENGTH_LONG).show(); // lo mismo que el aterior pero en el caso de la clave

            return;

        } else {
            progressDialog.setMessage("Realizando registro en linea...");
            progressDialog.show();
            usuarioexistente = false; //probando si asi se puede resetear el valor cada vez que se ingresa un usua
            //registramos un nuevo usuario
            firebaseAuth.createUserWithEmailAndPassword(email, password) // usamos las variables creadas para poder tener los datos de los edit text y creamos el usuario
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //checking if success
                            if (task.isSuccessful()) {
                                Success = true;
                                final FirebaseAuth auth = FirebaseAuth.getInstance();
                                FirebaseUser user = auth.getCurrentUser(); // conseguimos el suario ingresado
                                assert user != null; // si no es nula la variable a la que se le asigna el usuario
                                user.sendEmailVerification() // se le envia un mail de verificacion que fue editado en la pagina de firebase
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "Email Enviado."); // se notifica con un onComplete al igual que en el caso anterior que seria e caso de que se haya podido realizar la tarea
                                                }
                                            }
                                        });
                                Toast.makeText(Register.this, "Se ha registrado el usuario con el email: " + editTextEmail.getText(), Toast.LENGTH_LONG).show();
                            } else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(Register.this, "Ese usuario ya existe ", Toast.LENGTH_SHORT).show();
                                    usuarioexistente = true;
                                } else {
                                    Toast.makeText(Register.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                                }
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
    }

    public boolean validate() {
        boolean valid = false;

        //Get values from EditText fields
        String Email = editTextEmail.getText().toString();
        String Password = editTextPassword.getText().toString();

        //Handling validation for Email field
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            valid = false;
            Toast.makeText(Register.this, "Ingrese Un Email Valido!", Toast.LENGTH_SHORT).show();
        } else {
            if (Email.isEmpty()) {
                valid = false;
                Toast.makeText(Register.this, "El Campo Del Email Está Vacío", Toast.LENGTH_SHORT).show();
            }
            valid = true;
            estado = "Estado Valido";
        }

        //Handling validation for Password field
        if (Password.isEmpty()) {
            valid = false;
            Toast.makeText(Register.this, "El Campo De La Contraseña Está Vacío!", Toast.LENGTH_SHORT).show();
        } else {
            if (Password.length() > 5 | Password.length() > 31) {
                valid = true;
            } else {
                valid = false;
                Toast.makeText(Register.this, "La Contraseña debe tener mas de 5 caracteres y menos de 34!", Toast.LENGTH_SHORT).show();
            }
        }

        return valid;
    }
    public void toLogIn(View view) {
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        startActivityForResult(intent, 0);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:

                if (validate()) {
                    registrarUsuario();
                    toLogIn(view);
                }
                break;
        }
    }
}
