package com.example.c202cusirazonactivity4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth cusiRazonAuth;
    FirebaseFirestore cusiRazonFirestore;

    private EditText cusiRazonBook1QuantityField;
    private EditText cusiRazonBook1PriceField;
    private EditText cusiRazonBook2QuantityField;
    private EditText cusiRazonBook2PriceField;
    private EditText cusiRazonBook3QuantityField;
    private EditText cusiRazonBook3PriceField;
    private Button cusiRazonRegisterButton;

    private Button cusiRazonNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cusiRazonAuth = FirebaseAuth.getInstance();
        cusiRazonFirestore = FirebaseFirestore.getInstance();

        cusiRazonBook1QuantityField = findViewById(R.id.cusiRazonBook1Quantity);
        cusiRazonBook1PriceField = findViewById(R.id.cusiRazonBook1Price);
        cusiRazonBook2QuantityField = findViewById(R.id.cusiRazonBook2Quantity);
        cusiRazonBook2PriceField = findViewById(R.id.cusiRazonBook2Price);
        cusiRazonBook3QuantityField = findViewById(R.id.cusiRazonBook3Quantity);
        cusiRazonBook3PriceField = findViewById(R.id.cusiRazonBook3Price);
        cusiRazonRegisterButton = findViewById(R.id.cusiRazonRegisterButton);
        cusiRazonNextButton = findViewById(R.id.cusiRazonNextButton);


        cusiRazonNextButton.setOnClickListener(v -> {
            Intent cusiRazonConfirmationIntent = new Intent(MainActivity.this, SummaryActivity.class);

            String cusiRazonBook1Quantity = String.valueOf(cusiRazonBook1QuantityField.getText());
            String cusiRazonBook1Price = String.valueOf(cusiRazonBook1PriceField.getText());
            String cusiRazonBook2Quantity = String.valueOf(cusiRazonBook2QuantityField.getText());
            String cusiRazonBook2Price = String.valueOf(cusiRazonBook2PriceField.getText());
            String cusiRazonBook3Quantity = String.valueOf(cusiRazonBook3QuantityField.getText());
            String cusiRazonBook3Price = String.valueOf(cusiRazonBook3PriceField.getText());

            cusiRazonConfirmationIntent.putExtra("book1Qty", Integer.parseInt(cusiRazonBook1Quantity));
            cusiRazonConfirmationIntent.putExtra("book1Price", Integer.parseInt(cusiRazonBook1Price));
            cusiRazonConfirmationIntent.putExtra("book2Qty", Integer.parseInt(cusiRazonBook2Quantity));
            cusiRazonConfirmationIntent.putExtra("book2Price", Integer.parseInt(cusiRazonBook2Price));
            cusiRazonConfirmationIntent.putExtra("book3Qty", Integer.parseInt(cusiRazonBook3Quantity));
            cusiRazonConfirmationIntent.putExtra("book3Price", Integer.parseInt(cusiRazonBook3Price));

            startActivity(cusiRazonConfirmationIntent);
        });

        cusiRazonRegisterButton.setOnClickListener(v -> {
            FirebaseUser cusiRazonCurrentUser = cusiRazonAuth.getCurrentUser();
            if (cusiRazonCurrentUser != null) {
                cusiRazonAuth.signOut();

            }

            Intent cusiRazonLoginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(cusiRazonLoginIntent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser cusiRazonCurrentUser = cusiRazonAuth.getCurrentUser();
        if (cusiRazonCurrentUser != null) {
            cusiRazonFirestore.collection("users")
                    .whereEqualTo("userId", cusiRazonCurrentUser.getUid())
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();

                            for (QueryDocumentSnapshot document : result) {
                                StringBuilder cusiRazonDisplayName = new StringBuilder();
                                cusiRazonDisplayName.append(document.get("firstName"));
                                cusiRazonDisplayName.append(" ");
                                cusiRazonDisplayName.append(document.get("lastName"));

                                cusiRazonRegisterButton.setText(cusiRazonDisplayName);
                            }

                            if (result.isEmpty()) {
                                cusiRazonRegisterButton.setOnClickListener(v -> {
                                    Intent cusiRazonRegistrationIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                                    startActivity(cusiRazonRegistrationIntent);
                                });
                            }

                        }
                    });
        }
    }
}