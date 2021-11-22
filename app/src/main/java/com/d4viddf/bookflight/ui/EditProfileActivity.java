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
    Boolean imagen_selected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        appbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        appbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ShapeableImageView imageView = findViewById(R.id.profileImg);

        FloatingActionButton edit = findViewById(R.id.edit);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

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

            }
        });

        TextInputEditText em = findViewById(R.id.email);
        TextInputEditText um = findViewById(R.id.nombre);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentimg = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intentimg.setType("image/*");
                activityResultLauncher.launch(intentimg);
            }
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
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imagen_selected) {
                    StorageReference ref = storageReference.child("images/" + user.getUid() + UUID.randomUUID().toString());

                    // adding listeners on upload
                    // or failure of image
                    UploadTask uploadTask = ref.putFile(uri);
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                mage = task.getResult();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(usernamer.getText().toString())
                                        .setPhotoUri(mage)
                                        .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Log.d("TAG", "User profile updated.");
                                                finish();
                                            }
                                        });
                            } else {
                            }
                        }
                    });
                } else {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(usernamer.getText().toString())
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Log.d("TAG", "User profile updated.");
                                    finish();
                                }
                            });
                }
            }
        });

        MaterialButton cancell = findViewById(R.id.cancell);
        cancell.setOnClickListener(view -> finish());
    }
}