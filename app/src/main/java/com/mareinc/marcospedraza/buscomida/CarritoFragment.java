package com.mareinc.marcospedraza.buscomida;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mareinc.marcospedraza.buscomida.adapters.AdapterCarrito;
import com.mareinc.marcospedraza.buscomida.adapters.AdapterPlatillos;
import com.mareinc.marcospedraza.buscomida.models.ItemCarrito;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarritoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarritoFragment extends Fragment {

    private static final String TAG = "CarritoFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //Firebase vars
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    RecyclerView rvCarrito;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public CarritoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarritoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarritoFragment newInstance(String param1, String param2) {
        CarritoFragment fragment = new CarritoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carrito, container, false);

        rvCarrito = view.findViewById(R.id.rv_carrito);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("carrito")
                .child(mAuth.getCurrentUser().getUid());


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ItemCarrito> items = new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    ItemCarrito carrito = ds.getValue(ItemCarrito.class);
                    Log.d(TAG, "onDataChange: values"+ds.getValue(ItemCarrito.class).getId_platillo());
                    items.add(carrito);

                }

                AdapterCarrito adapter = new AdapterCarrito(items,getContext());
                rvCarrito.setAdapter(adapter);
                rvCarrito.setLayoutManager(new LinearLayoutManager(getContext()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: Value event listener cancelado");

            }
        });



        return view;
    }

}
