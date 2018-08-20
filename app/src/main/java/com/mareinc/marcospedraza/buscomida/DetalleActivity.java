package com.mareinc.marcospedraza.buscomida;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mareinc.marcospedraza.buscomida.dialogs_clases.MyDialogCarrito;
import com.mareinc.marcospedraza.buscomida.models.ItemCarrito;
import com.mareinc.marcospedraza.buscomida.models.Platillo;

public class DetalleActivity extends AppCompatActivity implements MyDialogCarrito.OnInputSelected {
    private static final String TAG = "DetalleActivity";

    private DatabaseReference mDatabase;
    private DatabaseReference carritoDataRef;
    FirebaseAuth mAuth;

    //widgets
    TextView nombre_platillo;
    TextView desc_platillo;
    TextView precio_platillo;
    ImageView img_platillo;
    FloatingActionButton btn_add_carrito;
    Platillo platillo_detalle;
    String id_platillo;


    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);


        toolbar = (Toolbar) findViewById(R.id.toolbar_detalle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        img_platillo = findViewById(R.id.img_detalle);
        nombre_platillo = findViewById(R.id.tv_nombre_detalle);
        desc_platillo = findViewById(R.id.tv_desc_detalle);
        precio_platillo = findViewById(R.id.tv_precio_detalle);
        btn_add_carrito = findViewById(R.id.fab_carrito);



        //Platillo mPlatillo = (Platillo) getIntent().getSerializableExtra("platillo");
        //Log.d(TAG, "onCreate: nombre del platillo" + mPlatillo.getNom_platillo());
         id_platillo = getIntent().getStringExtra("id_platillo");
        Log.d(TAG, "onCreate: id_platillo: "+ id_platillo);
        mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("platillos").child(id_platillo);

        carritoDataRef = FirebaseDatabase.getInstance().getReference()
                .child("carrito");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                platillo_detalle = dataSnapshot.getValue(Platillo.class);
                Log.d(TAG, "onDataChange: platillo: " + dataSnapshot.getValue(Platillo.class).getNom_platillo());

                setData(platillo_detalle);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btn_add_carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialogCarrito dialog_add = new MyDialogCarrito();
                dialog_add.show(getSupportFragmentManager(),"DialogAddCarrito");
            }
        });


    }

    private void setData(Platillo platillo_detalle) {

        Glide.with(this)
                .load(platillo_detalle.getImg_url())
                .into(img_platillo);

        nombre_platillo.setText(platillo_detalle.getNom_platillo());
        desc_platillo.setText(platillo_detalle.getDesc());
        precio_platillo.setText("Precio: $"+platillo_detalle.getPrecio());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void sendInput(String input) {

        if(!input.equals(""))
        {

                int cantidad = Integer.valueOf(input);
                double sub_total = cantidad * platillo_detalle.getPrecio();
                Log.d(TAG, "sendInput: cantidad=" + cantidad);
                Log.d(TAG, "sendInput: sub total=" + sub_total);

                ItemCarrito itemCarrito = new ItemCarrito();

                itemCarrito.setId_platillo(id_platillo);
                itemCarrito.setCantidad(Integer.valueOf(input));
                itemCarrito.setSub_total(sub_total);
                itemCarrito.setUrl_platillo_img(platillo_detalle.getImg_url());


                String key = carritoDataRef.child(mAuth.getUid()).push().getKey();
                itemCarrito.setId_item(key);

                carritoDataRef.child(mAuth.getUid()).child(key).setValue(itemCarrito);
                //1carritoDataRef.child(mAuth.getUid()).push().setValue(itemCarrito);
                Log.d(TAG, "sendInput: agregado al carrito");

        }

    }
}
