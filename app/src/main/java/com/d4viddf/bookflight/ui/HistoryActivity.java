package com.d4viddf.bookflight.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.d4viddf.bookflight.R;
import com.d4viddf.bookflight.adapters.HistoryAdapter;
import com.d4viddf.bookflight.clas.History;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://bookflight-d4viddf-default-rtdb.europe-west1.firebasedatabase.app");
    ArrayList<History> lista = new ArrayList<>();
    HistoryAdapter historyAdapter;
    RecyclerView lview;
    RelativeLayout notfound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        MaterialToolbar appbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        appbar.setNavigationOnClickListener(view -> finish());

        appbar.inflateMenu(R.menu.history_menu);

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
        notfound = findViewById(R.id.notfound);

        lista.clear();

        lview = findViewById(R.id.list_history);
        historyAdapter = new HistoryAdapter(HistoryActivity.this, lista);
        lview.setHasFixedSize(true);
        lview.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        lview.setAdapter(historyAdapter);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRef = database.getReference("users").child(currentUser.getUid()).child("history");
        Query query = myRef.orderByChild("timestamp");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    History value = postSnapshot.getValue(History.class);

                    lista.add(value);
                    historyAdapter.notifyDataSetChanged();

                }

                if (lista.isEmpty()) {
                    notfound.setVisibility(View.VISIBLE);
                } else {
                    notfound.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(HistoryActivity.this);
        appbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.remove_history:
                    FirebaseUser currentUser1 = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference myRef1 = database.getReference("users").child(currentUser1.getUid());
                    materialAlertDialogBuilder.setTitle(getString(R.string.delete_history))
                            .setNegativeButton(R.string.cancell, (dialogInterface, i) -> {
                            })
                            .setPositiveButton(R.string.accept, (dialogInterface, i) -> {
                                myRef1.child("history").removeValue();
                                lista.clear();
                                historyAdapter.notifyDataSetChanged();
                            })
                            .show();
                    return true;
                default:
                    return false;

            }
        });
    }
}