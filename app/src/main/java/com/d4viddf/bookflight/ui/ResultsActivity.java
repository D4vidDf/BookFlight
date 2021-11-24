package com.d4viddf.bookflight.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.d4viddf.bookflight.R;
import com.d4viddf.bookflight.adapters.HistoryAdapter;
import com.d4viddf.bookflight.adapters.ResultsAdapter;
import com.d4viddf.bookflight.clas.History;
import com.d4viddf.bookflight.clas.Result;
import com.d4viddf.bookflight.clas.Vuelos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class ResultsActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://bookflight-d4viddf-default-rtdb.europe-west1.firebasedatabase.app");
    ArrayList<Result> lista = new ArrayList<>();

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    ResultsAdapter resultsAdapter;
    RecyclerView lview;
    ExtendedFloatingActionButton fab;
    String from, to , ti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        MaterialToolbar appbar = (MaterialToolbar) findViewById(R.id.topAppBarr);
        appbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        View view = new View(this);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemGestures());
            // Apply the insets as padding to the view. Here we're setting all of the
            // dimensions, but apply as appropriate to your layout. You could also
            // update the views margin if more appropriate.
            view.setPadding(0, insets.top, 0, 0);

            // Return CONSUMED if we don't want the window insets to keep being passed
            // down to descendant views.
            return WindowInsetsCompat.CONSUMED;
        });

        lista.clear();

        String id = getIntent().getStringExtra("id");
        Log.i("identificador", id);

        DatabaseReference my = database.getReference("users").child(currentUser.getUid()).child("history");
        Query q = my.orderByChild("timestamp");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                   History histor = postSnapshot.getValue(History.class);
                    if (histor != null && histor.getIdentificador().equalsIgnoreCase(id)){
                        from = histor.getFrom();
                        to = histor.getTo();
                        ti = histor.getTipo();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });



        lview = findViewById(R.id.list_resultsr);
        resultsAdapter = new ResultsAdapter(ResultsActivity.this, lista);
        lview.setLayoutManager(new LinearLayoutManager(ResultsActivity.this));
        lview.setAdapter(resultsAdapter);
        DatabaseReference myRef = database.getReference("Vuelos");
        Query query = myRef;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Result value = postSnapshot.getValue(Result.class);
                    if (value.getDesde().equalsIgnoreCase(String.valueOf(from)) && value.getHacia().equalsIgnoreCase(to) && value.getTipo().equalsIgnoreCase(ti)) {
                        lista.add(value);
                    }
                    resultsAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        fab = findViewById(R.id.buy);

        lview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    fab.shrink();
                } else if (dy < 0) {
                    fab.extend();
                }
            }


        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = UUID.randomUUID().toString();
                Vuelos vuelo = new Vuelos();
                //vul = new Vuelos(tipo, fr, hacia, des, para, pasa);
                DatabaseReference myReff = database.getReference("users").child(currentUser.getUid());
                myReff.child("reservas").child(id).setValue(vuelo);
            }
        });

    }
}