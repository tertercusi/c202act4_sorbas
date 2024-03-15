package com.example.c202sorbasactivity4;

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

    FirebaseAuth sorBasAuth;
    FirebaseFirestore sorBasFirestore;

    private EditText sorBasBook1QuantityField;
    private EditText sorBasBook1PriceField;
    private EditText sorBasBook2QuantityField;
    private EditText sorBasBook2PriceField;
    private EditText sorBasBook3QuantityField;
    private EditText sorBasBook3PriceField;
    private Button sorBasRegisterButton;

    private Button sorBasNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sorBasAuth = FirebaseAuth.getInstance();
        sorBasFirestore = FirebaseFirestore.getInstance();

        sorBasBook1QuantityField = findViewById(R.id.sorBasBook1Quantity);
        sorBasBook1PriceField = findViewById(R.id.sorBasBook1Price);
        sorBasBook2QuantityField = findViewById(R.id.sorBasBook2Quantity);
        sorBasBook2PriceField = findViewById(R.id.sorBasBook2Price);
        sorBasBook3QuantityField = findViewById(R.id.sorBasBook3Quantity);
        sorBasBook3PriceField = findViewById(R.id.sorBasBook3Price);
        sorBasRegisterButton = findViewById(R.id.sorBasRegisterButton);
        sorBasNextButton = findViewById(R.id.sorBasNextButton);


        sorBasNextButton.setOnClickListener(v -> {
            Intent sorBasConfirmationIntent = new Intent(MainActivity.this, SummaryActivity.class);

            String sorBasBook1Quantity = String.valueOf(sorBasBook1QuantityField.getText());
            String sorBasBook1Price = String.valueOf(sorBasBook1PriceField.getText());
            String sorBasBook2Quantity = String.valueOf(sorBasBook2QuantityField.getText());
            String sorBasBook2Price = String.valueOf(sorBasBook2PriceField.getText());
            String sorBasBook3Quantity = String.valueOf(sorBasBook3QuantityField.getText());
            String sorBasBook3Price = String.valueOf(sorBasBook3PriceField.getText());

            sorBasConfirmationIntent.putExtra("book1Qty", Integer.parseInt(sorBasBook1Quantity));
            sorBasConfirmationIntent.putExtra("book1Price", Integer.parseInt(sorBasBook1Price));
            sorBasConfirmationIntent.putExtra("book2Qty", Integer.parseInt(sorBasBook2Quantity));
            sorBasConfirmationIntent.putExtra("book2Price", Integer.parseInt(sorBasBook2Price));
            sorBasConfirmationIntent.putExtra("book3Qty", Integer.parseInt(sorBasBook3Quantity));
            sorBasConfirmationIntent.putExtra("book3Price", Integer.parseInt(sorBasBook3Price));

            startActivity(sorBasConfirmationIntent);
        });

        sorBasRegisterButton.setOnClickListener(v -> {
            FirebaseUser sorBasCurrentUser = sorBasAuth.getCurrentUser();
            if (sorBasCurrentUser != null) {
                sorBasAuth.signOut();

            }

            Intent sorBasLoginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(sorBasLoginIntent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser sorBasCurrentUser = sorBasAuth.getCurrentUser();
        if (sorBasCurrentUser != null) {
            sorBasFirestore.collection("users")
                    .whereEqualTo("userId", sorBasCurrentUser.getUid())
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();

                            for (QueryDocumentSnapshot document : result) {
                                StringBuilder sorBasDisplayName = new StringBuilder();
                                sorBasDisplayName.append(document.get("firstName"));
                                sorBasDisplayName.append(" ");
                                sorBasDisplayName.append(document.get("lastName"));

                                sorBasRegisterButton.setText(sorBasDisplayName);
                            }

                            if (result.isEmpty()) {
                                sorBasRegisterButton.setOnClickListener(v -> {
                                    Intent sorBasRegistrationIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                                    startActivity(sorBasRegistrationIntent);
                                });
                            }

                        }
                    });
        }
    }
}