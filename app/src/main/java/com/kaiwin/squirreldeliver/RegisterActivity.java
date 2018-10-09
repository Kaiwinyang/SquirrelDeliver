package com.kaiwin.squirreldeliver;

import android.content.Intent;
import android.support.design.widget.Snackbar;
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

    private TextView textVeiwMsg;
    private EditText passwordEditText;
    private EditText loginEditText;
    private EditText repeatEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private Button btnRegsiter;

    // End Of Content View Elements

    private FirebaseAuth mAuth;

    private void bindViews() {

        textVeiwMsg = (TextView) findViewById(R.id.textVeiwMsg);
        passwordEditText = (EditText) findViewById(R.id.PasswordEditText);
        loginEditText = (EditText) findViewById(R.id.loginEditText);
        repeatEditText = (EditText) findViewById(R.id.RepeatEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        btnRegsiter = (Button) findViewById(R.id.btnRegsiter);

        btnRegsiter.setOnClickListener(this);
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
                                Toast.makeText(getApplicationContext(), R.string.register_success, Toast.LENGTH_SHORT)
                                        .show();
                                startActivity(new Intent(this, LoginActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                                );
                                sendEmailVerification(mAuth.getCurrentUser());
                            } else {
                                Snackbar.make(btnRegsiter, task.getException().toString(), Snackbar.LENGTH_LONG).show();
                                new AlertDialog.Builder(this)
                                        .setTitle("ERROR")
                                        .setMessage(task.getException().toString())
                                        .show();
                            }
                        });
                break;
            default:
                Log.w("uu", "default");
        }


        //Log.i(view.getTag().toString() + "has been pressed.", "Click Event");
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
                textVeiwMsg.setText(task.getException().toString());
            }
        });
    }
}
