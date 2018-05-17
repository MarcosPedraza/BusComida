package com.mareinc.marcospedraza.buscomida.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mareinc.marcospedraza.buscomida.DetalleActivity;
import com.mareinc.marcospedraza.buscomida.R;
import com.mareinc.marcospedraza.buscomida.models.Platillo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class AdapterPlatillos extends RecyclerView.Adapter<AdapterPlatillos.ViewHolder> {

    private static final String TAG = "AdapterPlatillos";
    private ArrayList<Platillo> platillos = new ArrayList<>();
    private ArrayList<String> ids_platillos = new ArrayList<>();
    private Context mContext;

    public AdapterPlatillos(ArrayList<String> ids_platillos,ArrayList<Platillo> platillos, Context mContext) {
        this.ids_platillos = ids_platillos;
        this.platillos = platillos;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_comida,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: se llamo onBind" + position);

        Glide.with(mContext)
                .load(platillos.get(position).getImg_url())
                .into(holder.imagen);

        holder.nombre_platillo.setText(platillos.get(position).getNom_platillo());
        holder.precio.setText(""+platillos.get(position).getPrecio());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(mContext, DetalleActivity.class);
                i.putExtra("id_platillo",ids_platillos.get(position));
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return platillos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imagen;
        TextView nombre_platillo;
        TextView precio;
        CardView cardView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            imagen = itemView.findViewById(R.id.img_com_row);
            nombre_platillo = itemView.findViewById(R.id.tv_platillo);
            precio = itemView.findViewById(R.id.tv_precio);
            cardView = itemView.findViewById(R.id.card_platillo);
        }

    }
}
