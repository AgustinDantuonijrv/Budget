package com.example.budget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public Button btntoregister;
    public Button btntoquestions;
    public Button btntoreset;
    public TextView IngresaGoogle;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    public String usuario;
    public TextView googlelog;
    public boolean abrirmain = true;
    public boolean validuser = false;
    //Declaration EditTexts
    public boolean loged = false;
    EditText editTextEmail;
    public String userapasar;
    public ImageView ImageUserPhoto;
    EditText editTextPassword;
    public String userstring;
    EditText editTextusername;
    EditText editTextdni;
    public String email, password;
    public String Id;
    public DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            initviews();
            btntoquestions.setOnClickListener(this);
            btntoregister.setOnClickListener(this);
            btntoreset.setOnClickListener(this);
            IngresaGoogle.setOnClickListener(this);

            progressDialog = new ProgressDialog(this);

            firebaseAuth = FirebaseAuth.getInstance();

            googlelog.setOnClickListener(this);
            //progressDialog = new ProgressDialog(this);

            users = FirebaseDatabase.getInstance().getReference("usuario");
            try {
                email = getIntent().getStringExtra("email");
                if (email != null) {
                    editTextEmail.setText(email);
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
            Toast.makeText(this, "Er Main" + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void loguearUsuario() {  // es similar a la anterior pero utiliza el objeto FirebaseAuth para poder permitirle al usuario ingresar sus datos y en caso de que exista el usuario wn la parte de autenticacion le permite ingresar

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        // consigue el usuario
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Falta ingresar la contraseña", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Realizando consulta en linea...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    // la misma funcion que en el caso anterior pero para poder ingresar
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) { //el oncomplete es para tener la comprobacion sea del campo del login o el singup

                        if (task.isSuccessful()) {
                            // si el usuario se encontraba creado entonces la tarea es exitosa

                            int pos = email.indexOf("@");
                            userstring = email.substring(0, pos); //consigue el nombre del usuario
                            Toast.makeText(MainActivity.this, "Bienvenido: " + editTextEmail.getText(), Toast.LENGTH_LONG).show(); // consigue el email que habia sido asignado a la variable TextEmail
                            Intent intencion = new Intent(getApplication(), Questions1.class); // nueva actividad
                            intencion.putExtra("user", email); // le pasa como dato al usuario ingresado a la proxima actividad
                            startActivity(intencion); //hay que hacer el get del usuario en la parte principal
                            // userapasar = user;
                            loged = true;

                        } else {
                            loged = false;

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisiÛn

                                Toast.makeText(MainActivity.this, "Procesando", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {

        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                String userId = firebaseUser.getUid();
                String userEmail = firebaseUser.getEmail();

                SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor5 = sharedPrefs.edit();
                editor5.putString("firebasekey", userId);
                editor5.apply();
            }
        }
    };

    public boolean validate() {

        boolean valid = false;

        //Get values from EditText fields
        String Email = editTextEmail.getText().toString();
        String Password = editTextPassword.getText().toString();

        //Handling validation for Email field
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            valid = false;
            Toast.makeText(MainActivity.this, "Ingrese Un Email Valido!", Toast.LENGTH_SHORT).show();
            abrirmain = false;
        } else {
            if (Email.isEmpty()) {
                valid = false;
                Toast.makeText(MainActivity.this, "El Campo Del Email Está Vacío", Toast.LENGTH_SHORT).show();
            }
            valid = true;
            abrirmain = true;
        }

        //Handling validation for Password field
        if (Password.isEmpty()) {
            valid = false;
            Toast.makeText(MainActivity.this, "El Campo De La Contraseña Está Vacío!", Toast.LENGTH_SHORT).show();
        } else {
            if (Password.length() > 5 | Password.length() > 31) {
                valid = true;
            } else {
                valid = false;
                Toast.makeText(MainActivity.this, "La Contraseña debe tener mas de 5 caracteres y menos de 34!", Toast.LENGTH_SHORT).show();
            }
        }

        return valid;
    }


    public void initviews(){

        editTextEmail = findViewById(R.id.inputEmail);
        editTextPassword = findViewById(R.id.inputPassword);
        btntoregister = findViewById(R.id.gotoRegister);
        btntoquestions = findViewById(R.id.btnLogin);
        btntoreset = (Button) findViewById(R.id.forgotPassword);
        IngresaGoogle = findViewById(R.id.textogoogle);
    }

    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.gotoRegister:

                    Intent intent = new Intent(MainActivity.this, Register.class);
                    startActivity(intent);
                    break;

                case R.id.forgotPassword:
                    loadreset();
                    break;

                case R.id.btnLogin:
                    if (validate()) {
                        loguearUsuario();
                        if (loged) {
                            ToMain();
                        }
                    }
                    break;
                case R.id.textogoogle:
                    LogGoogle();
                    break;
            }
    }
    public void loadreset(){
        try {
            Intent intent1 = new Intent(MainActivity.this, ResetPassword.class);
            startActivity(intent1);
        }catch (Exception e)
        {
            Toast.makeText(MainActivity.this, "Error:" + e, Toast.LENGTH_SHORT).show();
        }
    }

    public void ToMain(){
        Intent intent1 = new Intent(MainActivity.this, Questions1.class);
        intent1.putExtra("email",email);
        startActivity(intent1);
    }
    public void LogGoogle(){

    }
}
