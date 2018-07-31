package com.mareinc.marcospedraza.buscomida;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";


    //widgets
    Button btn_log_in;
    Button btn_register;
    TextInputEditText et_user;
    EditText et_password;
    ProgressBar pg_login;



    //Firebase vars
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        btn_log_in = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.link_reg);
        et_user = findViewById(R.id.et_usuario);
        et_password = findViewById(R.id.et_contraseña);
        pg_login =(ProgressBar) findViewById(R.id.pg_login);




        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null)
                {

                    goToMain();
                    //Toast.makeText(getApplicationContext(),"Login exitoso, pasar a MainActivity",Toast.LENGTH_SHORT).show();
                }

            }
        };

        btn_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_user.getText().toString();
                String pass = et_password.getText().toString();

                if(email.equals("") || pass.equals(""))
                {
                    Toast.makeText(LoginActivity.this, "por favor ingrese su usuario y contraseña", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    logInWhitEmail(email,pass);
                }


            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,RegistoActivity.class);
                startActivity(i);
            }
        });



    }

    public void logInWhitEmail(String email, String password)
    {
        pg_login.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pg_login.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: log in correcto");
                            // Sign in success, update UI with the signed-in user's information

                        } else {
                            // Fallo la autenticacion
                            Log.w(TAG, "logInWhitEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Error de Auntentificacion",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }


    private void goToMain() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthStateListener);


        if(mAuth.getCurrentUser() != null)
        {
            goToMain();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mAuthStateListener != null)
        {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}
