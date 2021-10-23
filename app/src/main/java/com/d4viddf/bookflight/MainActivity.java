package com.d4viddf.bookflight;

import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button history, search;
    ImageView add, remove;
    TextInputEditText from, to, depart, volver, passanger;
    TextInputLayout re;
    TextView tit, name;

    RadioGroup tigroup, paradagroup;
    RadioButton round, oneway, non, one, more;

    LinearLayout resul;
    ScrollView scroll;

    ArrayList<Vuelos> vuel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RelativeLayout Resultado
        resul = findViewById(R.id.layoutresultado);
        //ScrollView
        scroll = findViewById(R.id.scroll);

        //Text
        tit = findViewById(R.id.tittleres);
        name = findViewById(R.id.name);

        //RadioGroup
        tigroup = findViewById(R.id.tipo);
        paradagroup = findViewById(R.id.paradas);

        //InputText
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        depart = findViewById(R.id.depart);
        volver = findViewById(R.id.returndate);
        passanger = findViewById(R.id.pasajero);

        //InputLayout
        re = findViewById(R.id.ret);

        //ImageView
        add = findViewById(R.id.add);
        remove = findViewById(R.id.remove);

        //Button
        history = findViewById(R.id.history);
        search = findViewById(R.id.buscar);

        //RadioButton
        oneway = findViewById(R.id.one);
        round = findViewById(R.id.round);
        one = findViewById(R.id.onestop);
        non = findViewById(R.id.nonstop);
        more = findViewById(R.id.more);

        //ReltiveLayput Params
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.setMargins(0, 0, 0, 30);

        //Add && Remove
        add.setOnClickListener(view -> {
            if (Integer.parseInt(Objects.requireNonNull(passanger.getText()).toString()) < 0 || Integer.parseInt(passanger.getText().toString()) >= 19) {
                if (Integer.parseInt(passanger.getText().toString()) < 0) {
                    passanger.setText(String.valueOf(0));
                } else if (Integer.parseInt(passanger.getText().toString()) > 19) {
                    passanger.setText(String.valueOf(19));
                } else passanger.setText("1");
            } else {
                int num = Integer.parseInt(passanger.getText().toString()) + 1;
                passanger.setText(String.valueOf(num));
            }
        });

        remove.setOnClickListener(view -> {
            if (Integer.parseInt(Objects.requireNonNull(passanger.getText()).toString()) > 0 && Integer.parseInt(passanger.getText().toString()) <= 19) {
                int num = Integer.parseInt(passanger.getText().toString()) - 1;
                passanger.setText(String.valueOf(num));
            } else if (Integer.parseInt(passanger.getText().toString()) < 0) {
                passanger.setText(String.valueOf(0));
            } else if (Integer.parseInt(passanger.getText().toString()) > 19) {
                passanger.setText(String.valueOf(19));
            } else passanger.setText("19");
        });

        //Hide or show return

        round.setOnClickListener(view -> {
            re.setHint(R.string.returnn);
            volver.setVisibility(View.VISIBLE);
        });
        oneway.setOnClickListener(view -> {
            re.setHint("");
            volver.setVisibility(View.INVISIBLE);
        });

        //Search Flight
        search.setOnClickListener(view -> {
            Vuelos vul;
            String pal = "";
            //Tipo viaje roundtrip
            if (round.isChecked()) {
                String fr = Objects.requireNonNull(from.getText()).toString();
                String hacia = Objects.requireNonNull(to.getText()).toString();
                String des = Objects.requireNonNull(depart.getText()).toString();
                String hasta = Objects.requireNonNull(volver.getText()).toString();
                int pasa = Integer.parseInt(Objects.requireNonNull(passanger.getText()).toString());
                String tipo = "Roundtrip";
                String para = "";
                if (non.isChecked()) {
                    para = "Non Stop";
                } else if (one.isChecked()) {
                    para = "One Stop";
                } else if (more.isChecked()) {
                    para = "2 or more";
                }
                vul = new Vuelos(tipo, fr, hacia, des, hasta, para, pasa);
                vuel.add(vul);
                pal = "Tipo de vuelo: " + vul.getTipo() + "\nDesde: " + vul.getFrom() + "\nHasta: " + vul.getTo() + "\nSalida: " + vul.getSalida() + "\nRegreso: " + vul.getVolver() + "\nNº pasajeros: " + vul.getPasajeros() + "\nNº paradas: " + vul.getParadas();
            }

            //Tipo viaje oneway
            else if (oneway.isChecked()) {
                String fr = Objects.requireNonNull(from.getText()).toString();
                String hacia = Objects.requireNonNull(to.getText()).toString();
                String des = Objects.requireNonNull(depart.getText()).toString();
                int pasa = Integer.parseInt(Objects.requireNonNull(passanger.getText()).toString());
                String tipo = "Roundtrip";
                String para = "";
                if (non.isChecked()) {
                    para = "Non Stop";
                } else if (one.isChecked()) {
                    para = "One Stop";
                } else if (more.isChecked()) {
                    para = "2 or more";
                }
                vul = new Vuelos(tipo, fr, hacia, des, para, pasa);
                vuel.add(vul);
                pal = "Tipo de vuelo: " + vul.getTipo() + "\nDesde: " + vul.getFrom() + "\nHasta: " + vul.getTo() + "\nSalida: " + vul.getSalida() + "\nNº pasajeros: " + vul.getPasajeros() + "\nNº paradas: " + vul.getParadas();

            }

            //Poner titulo
            tit.setText(R.string.resultado);
            tit.setVisibility(View.VISIBLE);
            //Borrar vista
            resul.removeAllViews();
            //Personalizar texto
            TextView text = new TextView(MainActivity.this);
            text.setBackgroundResource(R.drawable.background_white);
            text.setText(pal);
            text.setLayoutParams(param);
            text.setPadding(20, 20, 20, 20);
            //Mostrar texto
            resul.addView(text);
        });

        //History Button
        history.setOnClickListener(view -> {
            resul.removeAllViews();

            tit.setText(R.string.history);
            String r;
            if (vuel.isEmpty()) {
                TextView text = new TextView(MainActivity.this);
                text.setTextColor(ContextCompat.getColor(this, R.color.white));
                text.setTextSize(24);
                text.setText(R.string.notresults);
                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                text.setLayoutParams(param);
                text.setPadding(20, 20, 20, 20);
                //Mostrar texto
                resul.addView(text);
            } else {
                tit.setVisibility(View.VISIBLE);
                for (Vuelos v : vuel) {
                    //Personalizar texto
                    TextView text = new TextView(MainActivity.this);
                    text.setBackgroundResource(R.drawable.background_white);
                    if (v.getVolver() == null) {
                        r = "Tipo de vuelo: " + v.getTipo() + "\nDesde: " + v.getFrom() + "\nHasta: " + v.getTo() + "\nSalida: " + v.getSalida() + "\nNº pasajeros: " + v.getPasajeros() + "\nNº paradas: " + v.getParadas();
                    } else
                        r = "Tipo de vuelo: " + v.getTipo() + "\nDesde: " + v.getFrom() + "\nHasta: " + v.getTo() + "\nSalida: " + v.getSalida() + "\nRegreso: " + v.getVolver() + "\nNº pasajeros: " + v.getPasajeros() + "\nNº paradas: " + v.getParadas();
                    text.setText(r);
                    text.setLayoutParams(param);
                    text.setPadding(20, 20, 20, 20);
                    //Mostrar texto
                    resul.addView(text);
                }
            }
        });

        //Hide title on Scroll
        scroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                switch (i1) {
                    case 2:
                        name.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        name.setVisibility(View.GONE);
                        break;
                    case 0:
                        name.setVisibility(View.VISIBLE);
                        break;
                    default:
                        name.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }
}