package com.d4viddf.bookflight.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.d4viddf.bookflight.R;
import com.d4viddf.bookflight.Vuelos;
import com.d4viddf.bookflight.databinding.FragmentHomeBinding;
import com.d4viddf.bookflight.ui.HistoryActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class HomeFragment extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://bookflight-d4viddf-default-rtdb.europe-west1.firebasedatabase.app");
    Button history, search;
    ImageView add, remove;
    TextInputEditText from, to, depart, volver, passanger;
    TextInputLayout re;
    TextView tit, name;

    RadioGroup tigroup, paradagroup;
    RadioButton round, oneway, non, one, more;

    LinearLayout resul;
    ScrollView scroll;


    private FragmentHomeBinding binding;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRef = database.getReference("users").child(currentUser.getUid());

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemGestures());
            // Apply the insets as padding to the view. Here we're setting all of the
            // dimensions, but apply as appropriate to your layout. You could also
            // update the views margin if more appropriate.
            root.setPadding(0,insets.top,0,0);

            // Return CONSUMED if we don't want the window insets to keep being passed
            // down to descendant views.
            return WindowInsetsCompat.CONSUMED;
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //ScrollView
        scroll = root.findViewById(R.id.scroll);
        name = root.findViewById(R.id.name);

        if (user != null){
            String Uname = user.getDisplayName();

            name.setText(new String(getString(R.string.hello)+Uname));
        }

        //RadioGroup
        tigroup = root.findViewById(R.id.tipo);
        paradagroup = root.findViewById(R.id.paradas);

        //InputText
        from = root.findViewById(R.id.from);
        to = root.findViewById(R.id.to);
        depart = root.findViewById(R.id.depart);
        volver = root.findViewById(R.id.returndate);
        passanger = root.findViewById(R.id.pasajero);

        //InputLayout
        re = root.findViewById(R.id.ret);

        //ImageView
        add = root.findViewById(R.id.add);
        remove = root.findViewById(R.id.remove);

        //Button
        history = root.findViewById(R.id.history);
        search = root.findViewById(R.id.buscar);

        //RadioButton
        oneway = root.findViewById(R.id.one);
        round = root.findViewById(R.id.round);
        one = root.findViewById(R.id.onestop);
        non = root.findViewById(R.id.nonstop);
        more = root.findViewById(R.id.more);

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
                myRef.child("history").child(UUID.randomUUID().toString()).setValue(vul);

            }

            //Tipo viaje oneway
            else if (oneway.isChecked()) {
                String fr = Objects.requireNonNull(from.getText()).toString();
                String hacia = Objects.requireNonNull(to.getText()).toString();
                String des = Objects.requireNonNull(depart.getText()).toString();
                String hasta = "";
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
                vul = new Vuelos(tipo, fr, hacia, des,hasta, para, pasa);
                myRef.child("history").child(UUID.randomUUID().toString()).setValue(vul);

            }

        });

        //History Button
        history.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), HistoryActivity.class);
            startActivity(intent);
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


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}