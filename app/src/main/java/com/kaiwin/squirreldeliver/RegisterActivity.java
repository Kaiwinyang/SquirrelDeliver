package com.kaiwin.squirreldeliver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    // Content View Elements

    private TextView textViewMsg;
    private EditText passwordEditText;
    private EditText loginEditText;
    private EditText repeatEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private Button btnRegister;

    // End Of Content View Elements

    private FirebaseAuth mAuth;

    private void bindViews() {

        textViewMsg = (TextView) findViewById(R.id.textVeiwMsg);
        passwordEditText = (EditText) findViewById(R.id.PasswordEditText);
        loginEditText = (EditText) findViewById(R.id.usernameEditText);
        repeatEditText = (EditText) findViewById(R.id.RepeatEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        btnRegister = (Button) findViewById(R.id.btnRegsiter);

        btnRegister.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bindViews();

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnRegsiter:
                if (!Tool.isEmail(emailEditText.getText().toString())) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.invalid_email_input), Toast.LENGTH_SHORT).show();
                    return;
                }
                String email = emailEditText.getText().toString().trim();
                String pwd = passwordEditText.getText().toString().trim();
                String pwdRepeated = repeatEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();


                if (email.isEmpty() || pwd.isEmpty() || pwdRepeated.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.empty_input, Toast.LENGTH_SHORT).show();
                    return;
                } else if (!pwd.equals(pwdRepeated)) {
                    Toast.makeText(getApplicationContext(), R.string.password_is_different, Toast.LENGTH_SHORT)
                            .show();
                    return;
                } else if (pwd.length() < 6) {
                    Toast.makeText(getApplicationContext(), R.string.password_should_be_at_least_6_characters, Toast.LENGTH_SHORT)
                            .show();
                    return;
                }


                mAuth.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                sendEmailVerification(mAuth.getCurrentUser());
                                new AlertDialog.Builder(this)
                                        .setIcon(R.drawable.squirrel)
                                        .setTitle(R.string.register_success)
                                        .setMessage(R.string.register_success_content)
                                        .setPositiveButton("OK", (dialog, w) -> {
                                                    startActivity(new Intent(this, LoginActivity.class)
                                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                                                    );
                                                    finish();
                                                }
                                        )
                                        .show();
                            } else {
                                Snackbar.make(btnRegister, task.getException().toString(), Snackbar.LENGTH_LONG).show();
                                new AlertDialog.Builder(this)
                                        .setTitle("ERROR")
                                        .setMessage(task.getException().toString())
                                        .show();
                            }
                        });
                break;
            default:
                Log.e(Tool.TAG, "default");
        }


        Log.v(Tool.TAG, "Click Event");
    }


    private void updateUserInfo() {
        UserProfileChangeRequest profileUpdates =
                new UserProfileChangeRequest.Builder()
                        .setDisplayName("Jane Q. User")
                        .build();

    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT)
                        .show();
                textViewMsg.setText(task.getException().toString());
            }
        });
    }
}
