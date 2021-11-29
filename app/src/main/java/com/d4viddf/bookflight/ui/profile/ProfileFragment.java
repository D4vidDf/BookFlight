package com.d4viddf.bookflight.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.d4viddf.bookflight.Bottom;
import com.d4viddf.bookflight.R;
import com.d4viddf.bookflight.databinding.FragmentProfileBinding;
import com.d4viddf.bookflight.ui.EditProfileActivity;
import com.d4viddf.bookflight.ui.HistoryActivity;
import com.d4viddf.bookflight.ui.LoginActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    MaterialTextView username;
    MaterialButton logout, edit, history, reservation;
    private FragmentProfileBinding binding;

    ShapeableImageView imagen;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        username = root.findViewById(R.id.nombre);
        imagen = root.findViewById(R.id.profileImg);

        history = root.findViewById(R.id.history);

        history.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), HistoryActivity.class);
            startActivity(intent);
        });

        edit = root.findViewById(R.id.edit);

        edit.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        reservation = root.findViewById(R.id.resevas);

        reservation.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), Bottom.class);
            intent.putExtra("frg", true);
            startActivity(intent);
        });

        logout = root.findViewById(R.id.logout);

        logout.setOnClickListener(view -> {
            mAuth.signOut();
            Intent inte = new Intent(getContext(), LoginActivity.class);
            startActivity(inte);
        });

        if (user != null) {
            String Uname = user.getDisplayName();
            Uri photoUrl = user.getPhotoUrl();
            username.setText(getString(R.string.hello) + Uname);
            Picasso.get().load(photoUrl).placeholder(R.drawable.ic_round_person_24).into(imagen);

        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}