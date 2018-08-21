package com.mareinc.marcospedraza.buscomida;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.mareinc.marcospedraza.buscomida.models.SaldoUsuario;
import com.mareinc.marcospedraza.buscomida.models.Tarjeta;
import com.mareinc.marcospedraza.buscomida.models.User;

public class QRActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;

    private static final String TAG = "QRActivity";

    private DatabaseReference myRef;
    private DatabaseReference myRefSaldoUser;
    private FirebaseAuth mAuth;


    private Tarjeta tarjeta_actual;
    private SaldoUsuario saldoUsuario;

    private String id_tarjeta_actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);




        mAuth = FirebaseAuth.getInstance();

        myRef = FirebaseDatabase.getInstance().getReference("prepagos_tarjeta");
        myRefSaldoUser = FirebaseDatabase.getInstance().getReference().child("saldo_usuarios").child(mAuth.getUid());
        getSaldoUser();

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
                        checkValid(result.getText());
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

    }

    private void getSaldoUser()
    {
        myRefSaldoUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                saldoUsuario = dataSnapshot.getValue(SaldoUsuario.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkValid(final String id_tarjeta)
    {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               tarjeta_actual = dataSnapshot.child(id_tarjeta).getValue(Tarjeta.class);

               if(tarjeta_actual != null)
               {
                   id_tarjeta_actual = id_tarjeta;
                   checkCobro(tarjeta_actual);
               }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //crear AlertDialog para notificar al usuario
    private void crearDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Agregar saldo")
                .setMessage("¿Desea agregar $"+tarjeta_actual.getSaldo()+" de saldo a su cuenta?")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(QRActivity.this, "click en si", Toast.LENGTH_SHORT).show();
                                addSaldo();
                                finish();
                            }
                        })
                .setNegativeButton("CANCELAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Log.d(TAG, "onClick: le dio que no");
                            }
                        })
                .setIcon(R.mipmap.ic_question);

        builder.show();
    }

    private void dinamicDialog(String titulo ,String mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(QRActivity.this, "click en si", Toast.LENGTH_SHORT).show();
                            }
                        })
                .setIcon(R.mipmap.ic_question);

        builder.show();
    }

    private void checkCobro(Tarjeta tarjeta)
    {
        if(tarjeta.isCobrada())
        {
            dinamicDialog("Info","La tarjeta ya ha sido cobrada");
            return;
        }
        if(tarjeta.isActiva() == false)
        {
            dinamicDialog("Info","La tarjeta no se encuentra activa");
            return;
        }

        crearDialog();

    }

    private void addSaldo()
    {
      double saldoNuevo = saldoUsuario.getSaldo() + tarjeta_actual.getSaldo();
      saldoUsuario.setSaldo(saldoNuevo);
      tarjeta_actual.setCobrada(true);
      tarjeta_actual.setActiva(false);
      myRefSaldoUser.setValue(saldoUsuario);
      myRef.child(id_tarjeta_actual).setValue(tarjeta_actual);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }



}
