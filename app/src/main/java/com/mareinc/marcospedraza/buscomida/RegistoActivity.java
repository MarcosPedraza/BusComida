package com.mareinc.marcospedraza.buscomida;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mareinc.marcospedraza.buscomida.models.SaldoUsuario;
import com.mareinc.marcospedraza.buscomida.models.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class RegistoActivity extends AppCompatActivity {


    private static final String TAG = "RegistoActivity";
    
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private DatabaseReference myRefSaldo;
    StorageReference mStorageRef;

    private static final int PICK_IMAGE_REQ = 8;
    Bitmap bitmap;

    EditText et_name,et_correo,et_pass,et_confirm_pass,et_phone;
    ImageView pic_select;
    Button btn_registro;
    ProgressBar progressBar;

    //image data

    Uri imageHoldUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);


        et_name = findViewById(R.id.et_registro_username);
        et_correo = findViewById(R.id.et_registro_correo);
        et_pass = findViewById(R.id.et_registro_pass);
        et_confirm_pass = findViewById(R.id.et_registro_pass_confirm);
        pic_select = findViewById(R.id.img_selecter);
        et_phone = findViewById(R.id.et_registro_registro_phone);
        btn_registro = findViewById(R.id.btn_registro);
        progressBar =findViewById(R.id.pb_registro);

        //firebase references
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        myRefSaldo = FirebaseDatabase.getInstance().getReference().child("saldo_usuarios");
        mAuth = FirebaseAuth.getInstance();

        pic_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i.createChooser(i,"Seleccione una foto de Perfil"),PICK_IMAGE_REQ);
            }
        });

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRegistration();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQ
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null)
        {
            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                pic_select.setImageBitmap(bitmap);

                CropImage.activity(uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);

            }catch (IOException e)
            {
                e.printStackTrace();
            }

        }//acaba if del request


        //cachar el Intent del crop de la imagen(libreria)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();
                Log.d(TAG, "onActivityResult: uri_imagen: " +imageHoldUri.toString());
                pic_select.setImageURI(imageHoldUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void UserRegistration()
    {

        final String email = et_correo.getText().toString();
        final String password = et_pass.getText().toString();
        String passrep = et_confirm_pass.getText().toString();
        final String phone = et_phone.getText().toString();
        final String user_name = et_name.getText().toString();

        if(email.equals(""))
        {
            et_correo.setError("Necesita llenar el campo");
            et_correo.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            et_correo.setError("ingresa un email valido");
            et_correo.requestFocus();
            return;
        }

        if(password.length() > 6)
        {
            et_pass.setError("Su contraseña es demasiado corta");
            et_pass.requestFocus();
            return;
        }

        if (password.equals("")&& passrep.equals(""))
        {
            et_pass.setError("necesita llenar los campos");
            et_pass.requestFocus();
            return;
        }

        if(!password.equals(passrep))
        {
            et_confirm_pass.setError("la Contraseña no coincide");
            et_confirm_pass.requestFocus();
            return;
        }

        if(phone.equals(""))
        {
           et_phone.setError("ingrese un numero de telefono");
           et_phone.requestFocus();
           return;
        }

        if(user_name.equals(""))
        {
            et_name.setError("ingrese un nombre de usuario");
            et_name.requestFocus();
            return;
        }

        if(imageHoldUri == null)
        {
            Toast.makeText(this, "Por favor seleccione una imagen de perfil", Toast.LENGTH_SHORT).show();
            return;
        }


        StorageReference mChildStorage = mStorageRef.child("profile_pictures").child(imageHoldUri.getLastPathSegment());
        String profilePicUrl = imageHoldUri.getLastPathSegment();

        mChildStorage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                final Uri imageUrl = taskSnapshot.getDownloadUrl();

                User user = new User();

                user.setUser_name(user_name);
                user.setEmail(email);
                user.setTel(phone);
                user.setProfile_url(imageUrl.toString());

                addUserToAuth(email,password,user);
            }
        });
        progressBar.setVisibility(View.VISIBLE);



    }//acaba UserRegistration


    public void addUserToAuth(String email,String password,User user)
    {

        final User mUser = user;
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful())
                {
                    Log.d(TAG, "onComplete: usuario registrado con exito");
                    User user = mUser;
                    SaldoUsuario saldo_new = new SaldoUsuario(0.0,true);
                    myRef.child("usuarios").child(mAuth.getCurrentUser().getUid()).setValue(user);
                    myRefSaldo.child(mAuth.getCurrentUser().getUid()).setValue(saldo_new);

                    Toast.makeText(getApplicationContext(),"Usuario Creado",Toast.LENGTH_LONG).show();
                    goToLogIn();


                }else {
                    //fallo al crear el nuevo usuario
                    Log.d(TAG, "onComplete: registro de usuario fallido");

                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        //el usuario ya existe
                        Toast.makeText(getApplicationContext(),"El correo ya ha sido utilizado",Toast.LENGTH_LONG).show();
                    }else {

                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
    }

    public void goToLogIn()
    {
        //startActivity(new Intent(LoginActivity.this,MainActivity.class));
        Intent intent = new Intent(RegistoActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
