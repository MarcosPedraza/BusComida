package com.mareinc.marcospedraza.buscomida.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mareinc.marcospedraza.buscomida.R;
import com.mareinc.marcospedraza.buscomida.models.ItemCarrito;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterCarrito extends RecyclerView.Adapter<AdapterCarrito.ViewHolder> {

    ArrayList<ItemCarrito> items = new ArrayList<>();
    Context context;

    public AdapterCarrito(ArrayList<ItemCarrito> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pedido,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Glide.with(context)
                .load(items.get(position).getUrl_platillo_img())
                .into(holder.img_carrito);

        holder.tv_cantidad.setText("Cantidad: "+items.get(position).getCantidad());
        holder.sub_total.setText("$: "+ items.get(position).getSub_total());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView img_carrito;
        TextView tv_cantidad;
        TextView sub_total;
        public ViewHolder(View itemView) {
            super(itemView);

            img_carrito = itemView.findViewById(R.id.img_pedido);
            tv_cantidad = itemView.findViewById(R.id.tv_cantidad_pedido);
            sub_total = itemView.findViewById(R.id.tv_precio_pedido);
        }
    }

}
