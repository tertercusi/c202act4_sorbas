package com.example.c202cusirazonactivity4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth cusiRazonAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cusiRazonAuth = FirebaseAuth.getInstance();

        EditText cusiRazonUsernameField = findViewById(R.id.cusiRazonLoginUsername);
        EditText cusiRazonPasswordField = findViewById(R.id.cusiRazonLoginPassword);
        Button cusiRazonLoginButton = findViewById(R.id.cusiRazonLoginSignInButton);
        Button cusiRazonRegisterButton = findViewById(R.id.cusiRazonLoginRegisterButton);

        cusiRazonLoginButton.setOnClickListener(v -> {
            String cusiRazonUsername = String.valueOf(cusiRazonUsernameField.getText());
            String cusiRazonPassword = String.valueOf(cusiRazonPasswordField.getText());

            cusiRazonAuth.signInWithEmailAndPassword(cusiRazonUsername, cusiRazonPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    finish();
                } else {
                    Toast.makeText(this, "Invalid user or password", Toast.LENGTH_SHORT).show();
                }
            });
        });

        cusiRazonRegisterButton.setOnClickListener(v -> {
            Intent cusiRazonRegistrationIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(cusiRazonRegistrationIntent);
        });
    }
}