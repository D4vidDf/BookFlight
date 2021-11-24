package com.d4viddf.bookflight.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.d4viddf.bookflight.Bottom;
import com.d4viddf.bookflight.R;
import com.d4viddf.bookflight.clas.Result;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {
        Activity activity;
        Context context;
private ArrayList<Result> lista;

public ResultsAdapter(Context context, ArrayList<Result> lista) {
        this.context = context;
        this.lista = lista;
        }

public ResultsAdapter(Activity activity, ArrayList<Result> lista) {
        this.activity = activity;
        this.lista = lista;
        }


@NonNull
@Override
public ResultsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.result_layout, parent, false);
        return new ResultsAdapter.ViewHolder(v);
        }

@Override
public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.txtdesde.setText(lista.get(position).getDesde());
        viewHolder.txthacia.setText(lista.get(position).getHacia());
        viewHolder.pasajeros.setText(Integer.toString(lista.get(position).getAsientos()));
        viewHolder.tipo_vuelo.setText(lista.get(position).getTipo());
        viewHolder.money.setText(lista.get(position).getPrecio());


        }

@Override
public int getItemCount() {
        return lista.size();
        }


public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView txtdesde;
    TextView txthacia;
    Chip salida;
    Chip vuelta;
    Chip pasajeros;
    Chip tipo_vuelo;
    Chip money;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        txtdesde = (TextView) itemView.findViewById(R.id.fromh);
        txthacia = (TextView) itemView.findViewById(R.id.toh);
        salida = (Chip) itemView.findViewById(R.id.salidah);
        vuelta = (Chip) itemView.findViewById(R.id.llegadah);
        pasajeros = (Chip) itemView.findViewById(R.id.pasajeroh);
        tipo_vuelo = (Chip) itemView.findViewById(R.id.tipoh);
        money = (Chip) itemView.findViewById(R.id.money);
    }
}
}
