package com.d4viddf.bookflight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d4viddf.bookflight.clas.City;
import com.d4viddf.bookflight.clas.History;
import com.d4viddf.bookflight.clas.Result;
import com.d4viddf.bookflight.clas.Vuelos;
import com.d4viddf.bookflight.ui.ResultsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class InfoActivity extends AppCompatActivity implements OnMapReadyCallback {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://bookflight-d4viddf-default-rtdb.europe-west1.firebasedatabase.app");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String[] type = new String[]{"Turista", "Premium", "Business"};
    Vuelos vuelos;

    RelativeLayout card;
    MaterialTextView txtCat;

    String desdeI, latI, longI, haciaI, ide, idv,date;
    long pasajeros;
    Result value;
    Date Startdate;

    FloatingActionButton main, edit, delete, noti;
    Float translationYaxis = 100f;
    Boolean open = false;
    OvershootInterpolator interpolator = new OvershootInterpolator();

    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        //Mostrar en mapa
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        MaterialToolbar appbar = findViewById(R.id.topAppBari);
        appbar.setNavigationOnClickListener(view -> finish());

        card = findViewById(R.id.cardclass);
        txtCat = findViewById(R.id.txtCat);

        TextView desde = findViewById(R.id.fromi);
        TextView hacia = findViewById(R.id.toi);
        TextView pasajero = findViewById(R.id.pasajeroi);
        TextView salida = findViewById(R.id.salidai);
        TextView volver = findViewById(R.id.llegadai);
        TextView dinero = findViewById(R.id.moneyi);

        ImageView img = findViewById(R.id.thumbnail);

        String identificador = getIntent().getStringExtra("reserva");

        if (identificador != null) {
            Log.i("id", identificador);
            DatabaseReference myRef2 = database.getReference("users").child(user.getUid()).child("reservas");
            Query query = myRef2.child(identificador);

            query.addValueEventListener(new ValueEventListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    vuelos = snapshot.getValue(Vuelos.class);
                    if (vuelos != null) {
                        desdeI = vuelos.getFrom();
                        haciaI = vuelos.getTo();
                        desde.setText(vuelos.getFrom());
                        hacia.setText(vuelos.getTo());
                        pasajero.setText(String.valueOf(vuelos.getPasajeros()));
                        salida.setText(vuelos.getSalida());
                        volver.setText(vuelos.getVolver());
                        dinero.setText(vuelos.getPrecio());

                        ide = vuelos.getId();
                        idv = vuelos.getIdentificador();
                        pasajeros = vuelos.getPasajeros();
                        date = vuelos.getSalida();

                        txtCat.setText(vuelos.getCategory());

                        appbar.setTitle(vuelos.getFrom() + " - " + vuelos.getTo());
                        if (vuelos.getTipo().equalsIgnoreCase("ida")) {
                            volver.setVisibility(View.GONE);
                        }

                        if (haciaI.equalsIgnoreCase("A Coruña")) {
                            img.setImageResource(R.drawable.coruna);
                        } else if (haciaI.equalsIgnoreCase("Madrid")) {
                            img.setImageResource(R.drawable.madrid);
                        } else if (haciaI.equalsIgnoreCase("París")) {
                            img.setImageResource(R.drawable.paris);
                        } else if (haciaI.equalsIgnoreCase("Barcelona")) {
                            img.setImageResource(R.drawable.barcelona);
                        } else if (haciaI.equalsIgnoreCase("Roma")) {
                            img.setImageResource(R.drawable.roma);
                        } else if (haciaI.equalsIgnoreCase("Nueva york")) {
                            img.setImageResource(R.drawable.nueva_york);
                        }

                        if (vuelos.getCategory().equalsIgnoreCase("Turista")){
                            card.setBackgroundColor(Color.rgb(50,153,168));

                        } else if (vuelos.getCategory().equalsIgnoreCase("Premium")){
                            card.setBackgroundColor(Color.rgb(200,154,34));

                        } else {
                            card.setBackgroundColor(Color.rgb(88,59,184));

                        }

                        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Startdate  = df2.parse(date);
                            Log.i("calendario", Startdate.toString());

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        getCity();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        ShowMenu();
    }

    private void ShowMenu() {
        main = findViewById(R.id.main);
        edit = findViewById(R.id.edit_fab);
        delete = findViewById(R.id.delete_fab);
        noti = findViewById(R.id.noti_fab);

        edit.setAlpha(0f);
        delete.setAlpha(0f);
        noti.setAlpha(0f);

        edit.setTranslationY(translationYaxis);
        delete.setTranslationY(translationYaxis);
        noti.setTranslationY(translationYaxis);

        main.setOnClickListener(view -> {
            if (open) {
                openMenu();
            } else closeMenu();
        });
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(InfoActivity.this);
        delete.setOnClickListener(view -> {

            materialAlertDialogBuilder.setTitle(getString(R.string.dialog_delete_reserve))
                    .setMessage(R.string.dialog_delete_reserve_message)
                    .setPositiveButton(R.string.cancell, (dialogInterface, i) -> {
                        DatabaseReference myRef2 = database.getReference("Vuelos");
                        Query query = myRef2;
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot != null) {
                                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                        value = postSnapshot.getValue(Result.class);
                                        if (value.getIdentificador().equalsIgnoreCase(idv)) {
                                            value.setDisponibles(value.getDisponibles() + pasajeros);

                                        }

                                    }
                                    database.getReference("Vuelos").child(idv).setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        DatabaseReference myRef1 = database.getReference("users").child(user.getUid());
                        myRef1.child("reservas").child(ide).removeValue();
                        finish();
                    }).show();

        });
        noti.setOnClickListener(view -> {
            materialAlertDialogBuilder.setTitle(getString(R.string.dialog_title_reminder))
                    .setMessage(R.string.dialog_message_reminder)
                    .setNegativeButton(R.string.cancell, (dialogInterface, i) -> {
                    })

                    .setPositiveButton(R.string.crear, (dialogInterface, i) -> {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(Startdate);
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);


                        Intent intent = new Intent(Intent.ACTION_INSERT)
                                .setData(CalendarContract.Events.CONTENT_URI)
                                .putExtra(CalendarContract.Events.CALENDAR_ID, UUID.randomUUID())
                                .putExtra(CalendarContract.Events.TITLE, String.valueOf("Viaje a "+haciaI))
                                .putExtra(CalendarContract.Events.DESCRIPTION, "Salida del vuelo hacia "+haciaI)
                                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,cal.getTimeInMillis())
                                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,cal.getTimeInMillis())
                                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                                .putExtra(CalendarContract.Events.EVENT_LOCATION, latI+", "+longI);
                            startActivity(intent);

                    }).show();
        });
        edit.setOnClickListener(view -> {
            materialAlertDialogBuilder.setTitle(getString(R.string.dialog_title_type))
                    .setSingleChoiceItems(type, 0, (dialogInterface, i) -> {
                        if(i == 0) {
                            vuelos.setCategory("Turista");
                        }
                        else if (i == 1) {
                            vuelos.setCategory("Premium");
                        }
                        else if (i == 2) {
                            vuelos.setCategory("Business");
                        }
                    })
                    .setNegativeButton(R.string.cancell, (dialogInterface, i) -> {
                    })
                    .setPositiveButton(R.string.save, (dialogInterface, i) -> {
                        DatabaseReference myRef1 = database.getReference("users").child(user.getUid());
                        myRef1.child("reservas").child(ide).setValue(vuelos);
                    }).show();
        });
    }

    private void closeMenu() {
        open = !open;
        main.setImageResource(R.drawable.ic_round_close_24);
        main.animate().rotation(45f).setDuration(200).start();
        edit.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        edit.setClickable(true);
        edit.setFocusable(true);
        delete.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(400).start();
        delete.setClickable(true);
        noti.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(600).start();
        noti.setClickable(true);
    }

    private void openMenu() {
        open = !open;
        main.setImageResource(R.drawable.add);
        main.animate().rotation(0f).setDuration(200).start();
        edit.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(600).start();
        edit.setClickable(false);
        edit.setFocusable(false);
        delete.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(400).start();
        delete.setClickable(false);
        noti.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        noti.setClickable(false);
    }

    private void getCity() {
        DatabaseReference myRef2 = database.getReference("Citys");
        Query query = myRef2.child(desdeI);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                City city = snapshot.getValue(City.class);
                latI = city.getLatitud();
                longI = city.getLongitud();

                mapFragment.getMapAsync(InfoActivity.this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng loc = new LatLng(Double.valueOf(latI), Double.valueOf(longI));
        googleMap.addMarker(new MarkerOptions()
                .position(loc)
                .title(desdeI));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
    }
}