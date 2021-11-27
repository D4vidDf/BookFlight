package com.d4viddf.bookflight.ui.reservations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.d4viddf.bookflight.R;
import com.d4viddf.bookflight.adapters.ReservationsAdapter;
import com.d4viddf.bookflight.adapters.ResultsAdapter;
import com.d4viddf.bookflight.clas.Result;
import com.d4viddf.bookflight.clas.Vuelos;
import com.d4viddf.bookflight.databinding.FragmentReservationsBinding;
import com.d4viddf.bookflight.ui.ResultsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReservationsFragment extends Fragment {
    private FragmentReservationsBinding binding;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://bookflight-d4viddf-default-rtdb.europe-west1.firebasedatabase.app");
    ArrayList<Vuelos> lista = new ArrayList<>();

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    ReservationsAdapter reservationsAdapter;
    RecyclerView lview;
    RelativeLayout info;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReservationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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

        lview = root.findViewById(R.id.list_reservas);
        info = root.findViewById(R.id.notreserv);
        reservationsAdapter = new ReservationsAdapter(getActivity(), lista);
        lview.setLayoutManager(new LinearLayoutManager(getContext()));
        lview.setAdapter(reservationsAdapter);
        DatabaseReference myRef = database.getReference("users").child(currentUser.getUid()).child("reservas");
        Query query = myRef.orderByChild("salida");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                lista.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Vuelos value = postSnapshot.getValue(Vuelos.class);
                    if (value != null){
                        lista.add(value);
                    }
                    reservationsAdapter.notifyDataSetChanged();
                    if (lista.isEmpty()) {
                        info.setVisibility(View.VISIBLE);
                    } else {
                        info.setVisibility(View.GONE);
                    }

                }
                reservationsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
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