package com.d4viddf.bookflight.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.d4viddf.bookflight.R;
import com.d4viddf.bookflight.Vuelos;
import com.d4viddf.bookflight.ui.HistoryActivity;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    Activity activity;
    Context context;
    private ArrayList<Vuelos> lista;
    public HistoryAdapter(Context context, ArrayList<Vuelos> lista) {;
        this.context = context;
        this.lista = lista;
    }
    public HistoryAdapter(Activity activity, ArrayList<Vuelos> lista) {;
        this.activity = activity;
        this.lista = lista;
    }


    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.history_layout,parent,false);
        return new HistoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.txtdesde.setText(lista.get(position).getFrom());
        viewHolder.txthacia.setText(lista.get(position).getTo());
        viewHolder.salida.setText(lista.get(position).getSalida());

        viewHolder.pasajeros.setText(Integer.toString(lista.get(position).getPasajeros()));
        viewHolder.tipo_vuelo.setText(lista.get(position).getTipo());
        viewHolder.trasbordos.setText(lista.get(position).getParadas());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtdesde;
        TextView txthacia;
        TextView salida;
        TextView vuelta;
        TextView pasajeros;
        TextView tipo_vuelo;
        TextView trasbordos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdesde = (TextView) itemView.findViewById(R.id.fromh);
            txthacia = (TextView) itemView.findViewById(R.id.toh);
            salida = (TextView) itemView.findViewById(R.id.salidah);
            vuelta = (TextView) itemView.findViewById(R.id.llegadah);
            pasajeros = (TextView) itemView.findViewById(R.id.pasajeroh);
            tipo_vuelo = (TextView) itemView.findViewById(R.id.tipoh);
            trasbordos = (TextView) itemView.findViewById(R.id.transbordosh);
        }
    }
}
