package com.example.c202sorbasactivity4;

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

    FirebaseAuth sorBasAuth;

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

        sorBasAuth = FirebaseAuth.getInstance();

        EditText sorBasUsernameField = findViewById(R.id.sorBasLoginUsername);
        EditText sorBasPasswordField = findViewById(R.id.sorBasLoginPassword);
        Button sorBasLoginButton = findViewById(R.id.sorBasLoginSignInButton);
        Button sorBasRegisterButton = findViewById(R.id.sorBasLoginRegisterButton);

        sorBasLoginButton.setOnClickListener(v -> {
            String sorBasUsername = String.valueOf(sorBasUsernameField.getText());
            String sorBasPassword = String.valueOf(sorBasPasswordField.getText());

            sorBasAuth.signInWithEmailAndPassword(sorBasUsername, sorBasPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    finish();
                } else {
                    Toast.makeText(this, "Invalid user or password", Toast.LENGTH_SHORT).show();
                }
            });
        });

        sorBasRegisterButton.setOnClickListener(v -> {
            Intent sorBasRegistrationIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(sorBasRegistrationIntent);
        });
    }
}