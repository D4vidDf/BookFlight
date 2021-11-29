package com.d4viddf.bookflight.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.d4viddf.bookflight.InfoActivity;
import com.d4viddf.bookflight.R;
import com.d4viddf.bookflight.clas.Vuelos;

import java.util.ArrayList;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ViewHolder> {
    FragmentActivity activity;
    Context context;
    private ArrayList<Vuelos> lista;

    public ReservationsAdapter(Context context, ArrayList<Vuelos> lista) {
        this.context = context;
        this.lista = lista;
    }

    public ReservationsAdapter(FragmentActivity activity, ArrayList<Vuelos> lista) {
        this.activity = activity;
        this.lista = lista;
    }


    @NonNull
    @Override
    public ReservationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.reserva_layout, parent, false);
        return new ReservationsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        viewHolder.txthacia.setText(lista.get(position).getTo());
        viewHolder.salida.setText(lista.get(position).getSalida());
        viewHolder.vuelta.setText(lista.get(position).getVolver());
        if (lista.get(position).getTipo().equalsIgnoreCase("ida")) {
            viewHolder.vuelta.setVisibility(View.GONE);
        }
        if (lista.get(position).getTo().equalsIgnoreCase("A Coruña")){
            viewHolder.img.setImageResource(R.drawable.coruna);
        }
        else if (lista.get(position).getTo().equalsIgnoreCase("Madrid")){
            viewHolder.img.setImageResource(R.drawable.madrid);
        }
        else if (lista.get(position).getTo().equalsIgnoreCase("París")){
            viewHolder.img.setImageResource(R.drawable.paris);
        }
        else if (lista.get(position).getTo().equalsIgnoreCase("Barcelona")){
            viewHolder.img.setImageResource(R.drawable.barcelona);
        }
        else if (lista.get(position).getTo().equalsIgnoreCase("Roma")){
            viewHolder.img.setImageResource(R.drawable.roma);
        }
        else if (lista.get(position).getTo().equalsIgnoreCase("Nueva york")){
            viewHolder.img.setImageResource(R.drawable.nueva_york);
        }

        viewHolder.img.setOnClickListener(view -> {
            Intent intent = new Intent(activity, InfoActivity.class);
            intent.putExtra("reserva", lista.get(position).getId());
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txthacia;
        TextView salida;
        TextView vuelta;
        ImageView img;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txthacia = (TextView) itemView.findViewById(R.id.tor);
            salida = (TextView) itemView.findViewById(R.id.salidar);
            vuelta = (TextView) itemView.findViewById(R.id.llegadar);
            img = (ImageView) itemView.findViewById(R.id.port);
        }
    }

}
