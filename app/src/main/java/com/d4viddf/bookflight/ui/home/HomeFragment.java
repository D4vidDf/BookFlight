package com.d4viddf.bookflight.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.d4viddf.bookflight.R;
import com.d4viddf.bookflight.clas.History;
import com.d4viddf.bookflight.clas.Result;
import com.d4viddf.bookflight.clas.Vuelos;
import com.d4viddf.bookflight.databinding.FragmentHomeBinding;
import com.d4viddf.bookflight.ui.HistoryActivity;
import com.d4viddf.bookflight.ui.ResultsActivity;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.net.Inet4Address;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class HomeFragment extends Fragment {
    Boolean states = false;
    String[] citys = new String[]{"A Coruña", "Madrid", "París", "Barcelona", "Roma", "Nueva York"};

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://bookflight-d4viddf-default-rtdb.europe-west1.firebasedatabase.app");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    Button history, search;
    ImageView add, remove;
    TextInputEditText depart, volver, passanger;
    MaterialAutoCompleteTextView from, to;
    TextInputLayout re;
    TextView name;

    String identificador;

    RadioGroup tigroup, paradagroup;
    RadioButton round, oneway, non, one, more;

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
            root.setPadding(0, insets.top, 0, 0);

            // Return CONSUMED if we don't want the window insets to keep being passed
            // down to descendant views.
            return WindowInsetsCompat.CONSUMED;
        });


        //Droplist
        ArrayAdapter<String> adapterFrom = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu, citys);
        ArrayAdapter<String> adapterTo = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu, citys);

        //ScrollView
        scroll = root.findViewById(R.id.scroll);
        name = root.findViewById(R.id.name);

        if (user != null) {
            String Uname = user.getDisplayName();

            name.setText(new String(getString(R.string.hello) + Uname));
        }

        //RadioGroup
        tigroup = root.findViewById(R.id.tipo);
        paradagroup = root.findViewById(R.id.paradas);

        //Autocomplete
        from = root.findViewById(R.id.from);
        to = root.findViewById(R.id.to);

        from.setAdapter(adapterFrom);
        to.setAdapter(adapterTo);

        //InputText
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
            add();
        });

        remove.setOnClickListener(view -> {
            remove();
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
            History history;
            String pal = "";
            //Tipo viaje roundtrip
            if (from.getText().toString().isEmpty() || to.getText().toString().isEmpty() || depart.getText().toString().isEmpty() || passanger.getText().toString().equalsIgnoreCase("0")){
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getContext());
                materialAlertDialogBuilder.setTitle(getString(R.string.not_search))
                        .setMessage(R.string.not_search_message)
                        .setPositiveButton(R.string.accept, (dialogInterface, i) -> {
                        })
                        .show();
            }
            else if (round.isChecked()) {
                String fr = Objects.requireNonNull(from.getText()).toString();
                String hacia = Objects.requireNonNull(to.getText()).toString();
                String des = Objects.requireNonNull(depart.getText()).toString();
                String hasta = Objects.requireNonNull(volver.getText()).toString();
                int pasa = Integer.parseInt(Objects.requireNonNull(passanger.getText()).toString());
                String tipo = "Ida y Vuelta";
                String para = "";
                if (non.isChecked()) {
                    para = "Sin transbordos";
                } else if (one.isChecked()) {
                    para = "Un transbordo";
                } else if (more.isChecked()) {
                    para = "2 o Más";
                }
                String id = UUID.randomUUID().toString();
                history = new History(tipo, fr, hacia, des, hasta, para, id, pasa);
                vul = new Vuelos(tipo, fr, hacia, des, hasta, para, pasa);
                myRef.child("history").child(id).setValue(history);
                myRef.child("history").child(id).child("timestamp").setValue(ServerValue.TIMESTAMP);
                Intent intent = new Intent(getContext(), ResultsActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }

            //Tipo viaje oneway
            else if (oneway.isChecked()) {
                String fr = Objects.requireNonNull(from.getText()).toString();
                String hacia = Objects.requireNonNull(to.getText()).toString();
                String des = Objects.requireNonNull(depart.getText()).toString();
                int pasa = Integer.parseInt(Objects.requireNonNull(passanger.getText()).toString());
                String tipo = "Ida";
                String para = "";
                if (non.isChecked()) {
                    para = "Sin transbordos";
                } else if (one.isChecked()) {
                    para = "Un transbordo";
                } else if (more.isChecked()) {
                    para = "2 o Más";
                }
                String id = UUID.randomUUID().toString();
                history = new History(tipo, fr, hacia, des, para, id, pasa);
                myRef.child("history").child(id).setValue(history);
                myRef.child("history").child(id).child("timestamp").setValue(ServerValue.TIMESTAMP);
                Intent intent = new Intent(getContext(), ResultsActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);

            }

        });

        //History Button
        history.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), HistoryActivity.class);
            startActivity(intent);
        });

        //Hide title on Scroll
        scroll.setOnScrollChangeListener((view, i, i1, i2, i3) -> {
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
        });

        //DatePickerDialog

        depart.setOnClickListener(view -> {
            if (volver.getVisibility() == View.INVISIBLE) {
                volver.getText().clear();
                datePickerDay();
            } else
                datePickerDays();
        });
        volver.setOnClickListener(view -> datePickerDays());
        if (getActivity().getIntent().getBooleanExtra("state", false) == true) {
            identificador = getActivity().getIntent().getStringExtra("identificador");
            states = true;
        }

        if (identificador != null && states == true) {
            Log.i("id", identificador);
            DatabaseReference myRef2 = database.getReference("users").child(currentUser.getUid()).child("history");
            Query query = myRef2.child(identificador);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    History history = snapshot.getValue(History.class);
                    if (history != null) {
                        from.setText(history.getFrom());
                        to.setText(history.getTo());
                        depart.setText(history.getSalida());
                        volver.setText(history.getVolver());
                        passanger.setText(String.valueOf(history.getPasajeros()));
                        if (history.getTipo().equalsIgnoreCase("ida")) {
                            tigroup.check(oneway.getId());
                            re.setHint("");
                            volver.setVisibility(View.INVISIBLE);

                        } else if (history.getTipo().equalsIgnoreCase("ida y vuelta")) {
                            tigroup.check(round.getId());
                            re.setHint(R.string.returnn);
                            volver.setVisibility(View.VISIBLE);
                        }
                        if (history.getParadas().equalsIgnoreCase("Sin transbordos")) {
                            paradagroup.check(non.getId());
                        } else if (history.getParadas().equalsIgnoreCase("Un transbordo")) {
                            paradagroup.check(one.getId());
                        } else if (history.getParadas().equalsIgnoreCase("2 o Más")) {
                            paradagroup.check(more.getId());
                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            states = false;
        }


        return root;
    }

    private void remove() {
        if (Integer.parseInt(Objects.requireNonNull(passanger.getText()).toString()) > 0 && Integer.parseInt(passanger.getText().toString()) <= 19) {
            int num = Integer.parseInt(passanger.getText().toString()) - 1;
            passanger.setText(String.valueOf(num));
        } else if (Integer.parseInt(passanger.getText().toString()) < 0) {
            passanger.setText(String.valueOf(0));
        } else if (Integer.parseInt(passanger.getText().toString()) > 19) {
            passanger.setText(String.valueOf(19));
        } else passanger.setText("19");
    }

    private void add() {
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
    }


    public void datePickerDays() {
        CalendarConstraints.Builder calendarConstraints = new CalendarConstraints.Builder();
        calendarConstraints.setValidator(DateValidatorPointForward.now());
        MaterialDatePicker dateRangePicker =
                MaterialDatePicker.Builder.dateRangePicker()
                        .setCalendarConstraints(calendarConstraints.build())
                        .setTitleText(R.string.select_dates)
                        .setTheme(R.style.Custom_MaterialCalendar_Fullscreen)
                        .build();

        dateRangePicker.show(getActivity().getSupportFragmentManager(), "datepicker");
        dateRangePicker.addOnPositiveButtonClickListener(selection -> {
            String fechas = selection.toString();
            String fechasparser1 = fechas.substring(fechas.indexOf("{") + 1, fechas.indexOf("}"));
            long depar = Long.parseLong(fechasparser1.substring(0, 13));
            long volv = Long.parseLong(fechasparser1.substring(14));
            Date dateDepar = new Date(depar);
            Date datevolv = new Date(volv);
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
            depart.setText(df.format(dateDepar));
            volver.setText(df.format(datevolv));
        });

    }

    public void datePickerDay() {
        CalendarConstraints.Builder calendarConstraints = new CalendarConstraints.Builder();
        calendarConstraints.setValidator(DateValidatorPointForward.now());
        MaterialDatePicker dateRangePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText(R.string.select_dates)
                        .setCalendarConstraints(calendarConstraints.build())
                        .setTheme(R.style.Custom_MaterialCalendar_Fullscreen)
                        .build();

        dateRangePicker.show(getActivity().getSupportFragmentManager(), "datepicker");
        dateRangePicker.addOnPositiveButtonClickListener(selection -> {
            long depar = Long.parseLong(selection.toString());
            Date dateDepar = new Date(depar);
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
            depart.setText(df.format(dateDepar));
        });
    }
}