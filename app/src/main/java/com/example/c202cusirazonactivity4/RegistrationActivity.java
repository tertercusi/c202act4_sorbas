package com.example.c202cusirazonactivity4;

import android.content.Intent;
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

    private FirebaseAuth cusiRazonAuth;
    private FirebaseFirestore cusiRazonFirestore;

    private EditText cusiRazonUsernameField;
    private EditText cusiRazonFirstnameField;
    private EditText cusiRazonLastnameField;
    private EditText cusiRazonBirthdateField;
    private EditText cusiRazonPasswordField;
    private EditText cusiRazonAddressField;
    private Button cusiRazonDoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        cusiRazonAuth = FirebaseAuth.getInstance();
        cusiRazonFirestore = FirebaseFirestore.getInstance();

        cusiRazonUsernameField = findViewById(R.id.cusiRazonUsername);
        cusiRazonFirstnameField = findViewById(R.id.cusiRazonFirstName);
        cusiRazonLastnameField = findViewById(R.id.cusiRazonLastName);
        cusiRazonBirthdateField = findViewById(R.id.cusiRazonBirthdate);
        cusiRazonPasswordField = findViewById(R.id.cusiRazonPassword);
        cusiRazonDoneButton = findViewById(R.id.cusiRazonRegisterDoneButton);
        cusiRazonAddressField = findViewById(R.id.cusiRazonAddress);

        cusiRazonDoneButton.setOnClickListener(v -> {
            String cusiRazonUsername = String.valueOf(cusiRazonUsernameField.getText());
            String cusiRazonPassword = String.valueOf(cusiRazonPasswordField.getText());
            cusiRazonAuth.createUserWithEmailAndPassword(cusiRazonUsername, cusiRazonPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser cusiRazonCurrentUser = cusiRazonAuth.getCurrentUser();
                    String cusiRazonFirstname = String.valueOf(cusiRazonFirstnameField.getText());
                    String cusiRazonLastname = String.valueOf(cusiRazonLastnameField.getText());
                    String cusiRazonBirthdate = String.valueOf(cusiRazonBirthdateField.getText());
                    String cusiRazonAddress = String.valueOf(cusiRazonAddressField.getText());

                    Map<String, String> cusiRazonNewUserData = new HashMap<>();
                    cusiRazonNewUserData.put("firstName", cusiRazonFirstname);
                    cusiRazonNewUserData.put("lastName", cusiRazonLastname);
                    cusiRazonNewUserData.put("birthDate",  cusiRazonBirthdate);
                    if (cusiRazonCurrentUser != null) {
                        cusiRazonNewUserData.put("userId", cusiRazonCurrentUser.getUid());
                        cusiRazonNewUserData.put("address", cusiRazonAddress);

                        cusiRazonFirestore.collection("users").document(cusiRazonCurrentUser.getUid()).set(cusiRazonNewUserData);

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