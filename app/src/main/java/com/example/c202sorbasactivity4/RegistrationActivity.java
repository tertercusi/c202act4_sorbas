package com.example.c202sorbasactivity4;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth sorBasAuth;
    private FirebaseFirestore sorBasFirestore;

    private EditText sorBasUsernameField;
    private EditText sorBasFirstnameField;
    private EditText sorBasLastnameField;
    private EditText sorBasBirthdateField;
    private EditText sorBasPasswordField;
    private EditText sorBasAddressField;
    private Button sorBasDoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        sorBasAuth = FirebaseAuth.getInstance();
        sorBasFirestore = FirebaseFirestore.getInstance();

        sorBasUsernameField = findViewById(R.id.sorBasUsername);
        sorBasFirstnameField = findViewById(R.id.sorBasFirstName);
        sorBasLastnameField = findViewById(R.id.sorBasLastName);
        sorBasBirthdateField = findViewById(R.id.sorBasBirthdate);
        sorBasPasswordField = findViewById(R.id.sorBasPassword);
        sorBasDoneButton = findViewById(R.id.sorBasRegisterDoneButton);
        sorBasAddressField = findViewById(R.id.sorBasAddress);

        sorBasDoneButton.setOnClickListener(v -> {
            String sorBasUsername = String.valueOf(sorBasUsernameField.getText());
            String sorBasPassword = String.valueOf(sorBasPasswordField.getText());
            sorBasAuth.createUserWithEmailAndPassword(sorBasUsername, sorBasPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser sorBasCurrentUser = sorBasAuth.getCurrentUser();
                    String sorBasFirstname = String.valueOf(sorBasFirstnameField.getText());
                    String sorBasLastname = String.valueOf(sorBasLastnameField.getText());
                    String sorBasBirthdate = String.valueOf(sorBasBirthdateField.getText());
                    String sorBasAddress = String.valueOf(sorBasAddressField.getText());

                    Map<String, String> sorBasNewUserData = new HashMap<>();
                    sorBasNewUserData.put("firstName", sorBasFirstname);
                    sorBasNewUserData.put("lastName", sorBasLastname);
                    sorBasNewUserData.put("birthDate",  sorBasBirthdate);
                    if (sorBasCurrentUser != null) {
                        sorBasNewUserData.put("userId", sorBasCurrentUser.getUid());
                        sorBasNewUserData.put("address", sorBasAddress);

                        sorBasFirestore.collection("users").document(sorBasCurrentUser.getUid()).set(sorBasNewUserData);

                    }


                    Toast.makeText(RegistrationActivity.this, "User created successfully.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(RegistrationActivity.this, "User already exists.", Toast.LENGTH_SHORT).show();
                }

                finish();
            });
        });
    }

    @Override
    protected void onStart() {

        super.onStart();

    }

}