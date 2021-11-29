package com.d4viddf.bookflight.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.d4viddf.bookflight.Bottom;
import com.d4viddf.bookflight.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        mAuth = FirebaseAuth.getInstance();

        CardView card = (CardView) findViewById(R.id.loadl);

        TextInputLayout emt = findViewById(R.id.emp);
        TextInputLayout pas = findViewById(R.id.pam);

        TextInputEditText em = findViewById(R.id.email);
        TextInputEditText pa = findViewById(R.id.password);

        MaterialButton iniciar = findViewById(R.id.login);
        iniciar.setOnClickListener(view12 -> {
            if (!isEmail(String.valueOf(em.getText()))){
                emt.setError(getString(R.string.invalid_email));
                if (!isPasswordValid(String.valueOf(pa.getText()))){
                    pas.setErrorEnabled(true);
                    pas.setError(getString(R.string.invalid_passwords));
                } else pas.setErrorEnabled(false);
            }
            else if (!isPasswordValid(String.valueOf(pa.getText()))){
                emt.setErrorEnabled(false);
                pas.setErrorEnabled(true);
                pas.setError(getString(R.string.invalid_password));
            }
            else {
                card.setVisibility(View.VISIBLE);
                emt.setErrorEnabled(false);
                pas.setErrorEnabled(false);
                mAuth.signInWithEmailAndPassword(em.getText().toString(), pa.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent act = new Intent(LoginActivity.this, Bottom.class);
                                card.setVisibility(View.GONE);
                                startActivity(act);
                            } else {
                                card.setVisibility(View.GONE);
                                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                                materialAlertDialogBuilder.setTitle(getString(R.string.error_login_tittle))
                                        .setMessage(R.string.error_login_message)
                                        .setPositiveButton(R.string.accept, (dialogInterface, i) -> {

                                        })
                                        .show();
                            }

                        });
            }
        });
        MaterialButton reg = findViewById(R.id.register);
        reg.setOnClickListener(view1 -> {
            Intent act = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(act);
        });
    }

    private boolean isPasswordValid(String password) {
        if (password.length() >=6) return true;
        return false;
    }

    private boolean isEmail(String text) {
        if (text.contains("@")) return true;
        else
        return false;
    }

}