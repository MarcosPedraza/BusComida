package com.mareinc.marcospedraza.buscomida.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mareinc.marcospedraza.buscomida.R;
import com.mareinc.marcospedraza.buscomida.interfaces.deleteItemListener;
import com.mareinc.marcospedraza.buscomida.models.ItemCarrito;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterCarrito extends RecyclerView.Adapter<AdapterCarrito.ViewHolder> {

    ArrayList<ItemCarrito> items = new ArrayList<>();
    Context context;
    deleteItemListener listener;


    public AdapterCarrito(ArrayList<ItemCarrito> items, Context context,deleteItemListener listener) {
        this.items = items;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pedido,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Glide.with(context)
                .load(items.get(position).getUrl_platillo_img())
                .into(holder.img_carrito);

        holder.tv_cantidad.setText("Cantidad: "+items.get(position).getCantidad());
        holder.sub_total.setText("$: "+ items.get(position).getSub_total());

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearDialog(items.get(position).getId_item(),position).show();


            }
        });


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
        ImageView btn_delete;
        public ViewHolder(View itemView) {
            super(itemView);

            img_carrito = itemView.findViewById(R.id.img_pedido);
            tv_cantidad = itemView.findViewById(R.id.tv_cantidad_pedido);
            sub_total = itemView.findViewById(R.id.tv_precio_pedido);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }



    private AlertDialog crearDialog(final String id_item, final int item_position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Eliminar")
                .setMessage("¿Realmente quiere eliminar este platillo?")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.deleteItem(id_item,item_position);
                            }
                        })
                .setNegativeButton("CANCELAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .setIcon(R.mipmap.ic_question);

        return builder.create();
    }

}
