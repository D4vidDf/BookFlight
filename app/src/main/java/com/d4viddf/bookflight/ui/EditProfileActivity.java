package com.d4viddf.bookflight.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.d4viddf.bookflight.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    MaterialToolbar appbar;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Uri uri;
    Uri mage;
    Bitmap imagen;
    CardView card;
    Boolean imagen_selected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        appbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        appbar.setNavigationOnClickListener(view -> finish());
        card =  findViewById(R.id.loadedit);
        View views = new View(this);
        views.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        ViewCompat.setOnApplyWindowInsetsListener(views, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemGestures());
            // Apply the insets as padding to the view. Here we're setting all of the
            // dimensions, but apply as appropriate to your layout. You could also
            // update the views margin if more appropriate.
            views.setPadding(0, insets.top, 0, 0);

            // Return CONSUMED if we don't want the window insets to keep being passed
            // down to descendant views.
            return WindowInsetsCompat.CONSUMED;
        });

        ShapeableImageView imageView = findViewById(R.id.profileImg);

        FloatingActionButton edit = findViewById(R.id.edit);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            if (result.getResultCode() == RESULT_OK) {
                uri = result.getData().getData();
                try {

                    imagen = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    imageView.setImageBitmap(imagen);
                    imagen_selected = true;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        TextInputEditText em = findViewById(R.id.email);
        TextInputEditText um = findViewById(R.id.nombre);
        edit.setOnClickListener(view -> {
            Intent intentimg = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intentimg.setType("image/*");
            activityResultLauncher.launch(intentimg);
        });
        TextInputEditText usernamer = findViewById(R.id.nombre);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String Uname = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            em.setText(email);
            um.setText(Uname);
            Picasso.get().load(photoUrl).placeholder(R.drawable.ic_round_person_24).into(imageView);
        }

        MaterialButton save = findViewById(R.id.save);
        save.setOnClickListener(view -> {
            if (imagen_selected) {
                card.setVisibility(View.VISIBLE);
                StorageReference ref = storageReference.child("images/" + user.getUid() + UUID.randomUUID().toString());

                // adding listeners on upload
                // or failure of image
                UploadTask uploadTask = ref.putFile(uri);
                Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        card.setVisibility(View.GONE);
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mage = task.getResult();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(usernamer.getText().toString())
                                .setPhotoUri(mage)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        card.setVisibility(View.GONE);
                                        Log.d("TAG", "User profile updated.");
                                        finish();
                                    }
                                });
                    } else {
                        card.setVisibility(View.GONE);
                    }
                });
            } else {
                card.setVisibility(View.VISIBLE);
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(usernamer.getText().toString())
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.d("TAG", "User profile updated.");
                                card.setVisibility(View.GONE);
                                finish();
                            }
                        });
            }
        });

        MaterialButton cancell = findViewById(R.id.cancell);
        cancell.setOnClickListener(view -> finish());
    }
}