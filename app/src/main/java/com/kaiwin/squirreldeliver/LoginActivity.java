package com.kaiwin.squirreldeliver;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnRegister;
    private TextView textView;
    private TextView usernameText, passwordText;

    // Fire Base Auth requires
    //Add the dependency for Firebase Authentication to your app-level build.gradle file:
    //implementation 'com.google.firebase:firebase-auth:16.0.2'
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("tw");

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegsiter);
        textView = findViewById(R.id.textVeiwMsg);

        usernameText = findViewById(R.id.usernameEditText);
        passwordText = findViewById(R.id.PasswordEditText);

        btnRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));


        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified())
            startActivity(new Intent(this, MajorActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            overridePendingTransition(0,0);//cancel Transition Animation

        btnLogin.setOnClickListener(v -> {
            String username = usernameText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.empty_input, Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Tool.isEmail(username)) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.invalid_email_input), Toast.LENGTH_SHORT).show();
            }

            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            if (isEmailVerified())
                                startActivity(new Intent(this, MajorActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                                );
                        } else
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    });

//            SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.shared_preferences_XML_file_name), MODE_PRIVATE).edit();
//            editor.putString("username", username).putString("password", password).commit();
//            String text = "username:" + username + "," + "password:" + password;
//            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();


        });
    }

    private boolean isEmailVerified() {
        FirebaseUser user  = mAuth.getCurrentUser();
        if (!user.isEmailVerified()) {
            Snackbar.make(btnLogin,R.string.email_not_verified,Snackbar.LENGTH_LONG).show();
            //Toast.makeText(this, R.string.email_not_verified, Toast.LENGTH_SHORT).show();
            user.sendEmailVerification().addOnCompleteListener(
                    task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(this, R.string.email_verification_has_been_sent, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(this, R.string.sending_mail_error_occurred, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            mAuth.signOut();
            return false;
        }
        return true;
    }
}
