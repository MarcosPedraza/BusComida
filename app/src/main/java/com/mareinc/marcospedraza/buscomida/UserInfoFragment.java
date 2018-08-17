package com.mareinc.marcospedraza.buscomida;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mareinc.marcospedraza.buscomida.models.Platillo;
import com.mareinc.marcospedraza.buscomida.models.User;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserInfoFragment extends Fragment {
    private static final String TAG = "UserInfoFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int CAMERA_REQUEST = 16;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    //var
    boolean permissionCamera = false;

    //widgets
    ImageView img_profile;
    TextView tv_nom_user;
    TextView tv_phone_user;
    TextView tv_email_user;
    Button btnScanner;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    FirebaseAuth mAuth;

    User user_info;


    public UserInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserInfoFragment newInstance(String param1, String param2) {
        UserInfoFragment fragment = new UserInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        //mFirebaseDatabase = FirebaseDatabase.getInstance();

        img_profile = view.findViewById(R.id.img_profile_pic);
        tv_nom_user = view.findViewById(R.id.tv_user_name);
        tv_phone_user = view.findViewById(R.id.tv_telefono);
        tv_email_user = view.findViewById(R.id.tv_email_user);
        btnScanner = view.findViewById(R.id.btn_scanner);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("usuarios").child(mAuth.getUid());


        if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            permissionCamera = false;
        }
        else
        {
            permissionCamera = true;
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user_info = dataSnapshot.getValue(User.class);
                Log.d(TAG, "onDataChange: usuario: " + dataSnapshot.getValue(User.class).getUser_name());
                setUserInfo(user_info);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestPermitions();

                if(permissionCamera) {
                    Intent i = new Intent(getContext(), QRActivity.class);
                    startActivity(i);
                }
                else
                {
                    crearDialog();
                }
            }
        });



        return view;
    }

    private AlertDialog crearDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Permisos de camara")
                .setMessage("Es necesario que otorge permisos para que la aplicacion pueda usar la camara")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .setNegativeButton("CANCELAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

        return builder.create();
    }

    private void RequestPermitions()
    {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.CAMERA))
            {
                Toast.makeText(getContext(), "Tengo que pedir Permiso", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},CAMERA_REQUEST);
            }
            else
            {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},CAMERA_REQUEST);
            }



        }
        else
        {
            permissionCamera = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case CAMERA_REQUEST:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    permissionCamera = true;
                }
                else
                {
                    permissionCamera = false;
                }
                break;
        }
    }

    private void setUserInfo(User user) {

        Glide.with(getContext())
                .load(user.getProfile_url())
                .into(img_profile);

        tv_nom_user.setText(user.getUser_name());
        tv_phone_user.setText(user.getTel());
        tv_email_user.setText(user.getEmail());

    }

}
