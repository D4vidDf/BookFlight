package com.d4viddf.bookflight.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.d4viddf.bookflight.Bottom;
import com.d4viddf.bookflight.R;
import com.d4viddf.bookflight.clas.History;
import com.d4viddf.bookflight.clas.Vuelos;
import com.d4viddf.bookflight.ui.ResultsActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    Activity activity;
    Context context;
    private ArrayList<History> lista;

    public HistoryAdapter(Context context, ArrayList<History> lista) {
        this.context = context;
        this.lista = lista;
    }

    public HistoryAdapter(Activity activity, ArrayList<History> lista) {
        this.activity = activity;
        this.lista = lista;
    }


    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.history_layout, parent, false);
        return new HistoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.txtdesde.setText(lista.get(position).getFrom());
        viewHolder.txthacia.setText(lista.get(position).getTo());
        viewHolder.salida.setText(lista.get(position).getSalida());
        if (lista.get(position).getVolver() == null) {
            viewHolder.vuelta.setVisibility(View.INVISIBLE);
        } else{
            viewHolder.vuelta.setText(lista.get(position).getVolver());
        viewHolder.vuelta.setVisibility(View.VISIBLE);}
        viewHolder.pasajeros.setText(Integer.toString(lista.get(position).getPasajeros()));
        viewHolder.tipo_vuelo.setText(lista.get(position).getTipo());
        viewHolder.trasbordos.setText(lista.get(position).getParadas());

        viewHolder.search.setOnClickListener(view -> {
            Intent intent = new Intent(activity.getApplicationContext(), ResultsActivity.class);
            intent.putExtra("id", lista.get(position).getIdentificador());
            activity.startActivity(intent);
        });

        viewHolder.edit.setOnClickListener(view -> {
            Intent intent = new Intent(activity.getApplicationContext(), Bottom.class);
            intent.putExtra("state",true);
            intent.putExtra("identificador", lista.get(position).getIdentificador());
            activity.startActivity(intent);
            activity.finish();
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        Chip txtdesde;
        Chip txthacia;
        Chip salida;
        Chip vuelta;
        Chip pasajeros;
        Chip tipo_vuelo;
        Chip trasbordos;
        MaterialButton search;
        MaterialButton edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            search = (MaterialButton) itemView.findViewById(R.id.search_but);
            txtdesde = (Chip) itemView.findViewById(R.id.fromh);
            txthacia = (Chip) itemView.findViewById(R.id.toh);
            salida = (Chip) itemView.findViewById(R.id.salidah);
            vuelta = (Chip) itemView.findViewById(R.id.llegadah);
            pasajeros = (Chip) itemView.findViewById(R.id.pasajeroh);
            tipo_vuelo = (Chip) itemView.findViewById(R.id.tipoh);
            trasbordos = (Chip) itemView.findViewById(R.id.transbordosh);
            edit = (MaterialButton) itemView.findViewById(R.id.edit_but);
        }
    }
}
