package com.d4viddf.bookflight.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.d4viddf.bookflight.R;
import com.d4viddf.bookflight.clas.Result;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {
    Activity activity;
    Context context;
    private ArrayList<Result> lista;
    private ArrayList<Result> checked = new ArrayList<>();

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

        viewHolder.id.setText(viewHolder.resource.getText(R.string.code_fly)+" "+lista.get(position).getIdentificador());
        viewHolder.txtdesde.setText(lista.get(position).getDesde());
        viewHolder.txthacia.setText(lista.get(position).getHacia());
        viewHolder.pasajeros.setText(Long.toString(lista.get(position).getDisponibles()));
        viewHolder.tipo_vuelo.setText(lista.get(position).getTipo());
        viewHolder.money.setText(lista.get(position).getPrecio());
        viewHolder.salida.setText(lista.get(position).getSalida());
        viewHolder.vuelta.setText(lista.get(position).getVuelta());
        if (lista.get(position).getTipo().equalsIgnoreCase("ida")) {
            viewHolder.vuelta.setVisibility(View.GONE);
        }

        viewHolder.check.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) {
                Result r = (Result) lista.get(position);
                checked.add(r);
                Log.i("Lista", String.valueOf(checked.size()));
            } else if (!compoundButton.isChecked()) {
                Result r = (Result) lista.get(position);
                checked.remove(r);
                Log.i("Lista", String.valueOf(checked.size()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        Chip txtdesde;
        Chip txthacia;
        Chip salida;
        Chip vuelta;
        Chip pasajeros;
        Chip tipo_vuelo;
        Chip money;
        MaterialCheckBox check;
        Resources resource;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            resource = itemView.getResources();
            id = (TextView) itemView.findViewById(R.id.code);
            txtdesde = (Chip) itemView.findViewById(R.id.fromh);
            txthacia = (Chip) itemView.findViewById(R.id.toh);
            salida = (Chip) itemView.findViewById(R.id.salidah);
            vuelta = (Chip) itemView.findViewById(R.id.llegadah);
            pasajeros = (Chip) itemView.findViewById(R.id.pasajeroh);
            tipo_vuelo = (Chip) itemView.findViewById(R.id.tipoh);
            money = (Chip) itemView.findViewById(R.id.money);
            check = (MaterialCheckBox) itemView.findViewById(R.id.check);
        }
    }

    public ArrayList<Result> getChecked() {
        return checked;
    }

}
