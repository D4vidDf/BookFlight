package com.d4viddf.bookflight.ui;

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

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.d4viddf.bookflight.Bottom;
import com.d4viddf.bookflight.R;
import com.d4viddf.bookflight.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    private FirebaseAuth mAuth;
    Bitmap imagen;
    Boolean imagen_selected = false;
    Uri uri;
    Uri mage;
    ShapeableImageView imageView;
    CardView card;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        View view = new View(this);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
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

        mDatabase = FirebaseDatabase.getInstance("https://bookflight-d4viddf-default-rtdb.europe-west1.firebasedatabase.app").getReference();
        imageView = findViewById(R.id.profileImg);
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
        card = findViewById(R.id.load);
        mAuth = FirebaseAuth.getInstance();
        FloatingActionButton edit = findViewById(R.id.edit);
        TextInputEditText em = findViewById(R.id.email);
        TextInputEditText pa = findViewById(R.id.password);
        MaterialButton iniciar = findViewById(R.id.login);
        iniciar.setOnClickListener(view12 -> finish());
        edit.setOnClickListener(view1 -> {
            Intent intentimg = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intentimg.setType("image/*");
            activityResultLauncher.launch(intentimg);
        });
        TextInputEditText usernamer = findViewById(R.id.nombre);
        MaterialButton reg = findViewById(R.id.register);
        reg.setOnClickListener(view13 -> {
            if (em.getText().toString().isEmpty() || pa.getText().toString().isEmpty() || usernamer.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Por favor, rellene todos los campos", Toast.LENGTH_LONG);
            } else if (imagen_selected) {
                card.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(em.getText().toString(), pa.getText().toString())
                        .addOnCompleteListener(RegisterActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithEmail:success");
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                StorageReference ref = storageReference.child("images/" + mAuth.getUid() + UUID.randomUUID().toString());

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
                                }).addOnCompleteListener(task12 -> {
                                    if (task12.isSuccessful()) {
                                        mage = task12.getResult();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(usernamer.getText().toString())
                                                .setPhotoUri(mage)
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()) {
                                                        Log.d("TAG", "User profile updated.");
                                                    }
                                                });
                                        User user1 = new User(usernamer.getText().toString(), em.getText().toString());

                                        mDatabase.child("users").child(mAuth.getUid()).setValue(user1);

                                        Intent act = new Intent(RegisterActivity.this, Bottom.class);
                                        card.setVisibility(View.GONE);
                                        startActivity(act);
                                    } else {
                                    }
                                });


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                card.setVisibility(View.GONE);
                            }
                        });
            } else {
                card.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(em.getText().toString(), pa.getText().toString())
                        .addOnCompleteListener(RegisterActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithEmail:success");
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(usernamer.getText().toString())
                                        .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(task13 -> {
                                            if (task13.isSuccessful()) {
                                                Log.d("TAG", "User profile updated.");
                                            }
                                        });
                                User users = new User(usernamer.getText().toString(), em.getText().toString());

                                mDatabase.child("users").child(mAuth.getUid().toString()).setValue(users);

                                Intent act = new Intent(RegisterActivity.this, Bottom.class);
                                card.setVisibility(View.GONE);
                                startActivity(act);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                card.setVisibility(View.GONE);
                            }
                        });
            }

        });
    }
}