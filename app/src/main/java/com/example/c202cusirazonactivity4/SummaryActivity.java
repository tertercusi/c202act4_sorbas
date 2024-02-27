package com.example.c202cusirazonactivity4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SummaryActivity extends AppCompatActivity {

    FirebaseFirestore cusiRazonFirestore;
    FirebaseAuth cusiRazonAuth;

    Button cusiRazonRegisterButton;
    Button cusiRazonCheckoutButton;
    private int cusiRazonBook1Quantity;
    private int cusiRazonBook1Price;
    private int cusiRazonBook1Subtotal;
    private int cusiRazonBook2Quantity;
    private int cusiRazonBook2Price;
    private int cusiRazonBook2Subtotal;
    private int cusiRazonBook3Quantity;
    private int cusiRazonBook3Price;
    private int cusiRazonBook3Subtotal;
    private int cusiRazonTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Intent cusiRazonIntent = getIntent();

        cusiRazonFirestore = FirebaseFirestore.getInstance();
        cusiRazonAuth = FirebaseAuth.getInstance();

        cusiRazonBook1Quantity = cusiRazonIntent.getIntExtra("book1Qty", 0);
        cusiRazonBook1Price = cusiRazonIntent.getIntExtra("book1Price", 0);
        cusiRazonBook1Subtotal = cusiRazonBook1Price * cusiRazonBook1Quantity;
        cusiRazonBook2Quantity = cusiRazonIntent.getIntExtra("book2Qty", 0);
        cusiRazonBook2Price = cusiRazonIntent.getIntExtra("book2Price", 0);
        cusiRazonBook2Subtotal = cusiRazonBook2Price * cusiRazonBook2Quantity;
        cusiRazonBook3Quantity = cusiRazonIntent.getIntExtra("book3Qty", 0);
        cusiRazonBook3Price = cusiRazonIntent.getIntExtra("book3Price", 0);
        cusiRazonBook3Subtotal = cusiRazonBook3Price * cusiRazonBook3Quantity;
        cusiRazonTotal = cusiRazonBook1Subtotal + cusiRazonBook2Subtotal + cusiRazonBook3Subtotal;

        TextView cusiRazonBook1QuantityDisplay = findViewById(R.id.cusiRazonBook1QuantityDisplay);
        TextView cusiRazonBook1SubtotalDisplay = findViewById(R.id.cusiRazonBook1Subtotal);
        TextView cusiRazonBook2QuantityDisplay = findViewById(R.id.cusiRazonBook2QuantityDisplay);
        TextView cusiRazonBook2SubtotalDisplay = findViewById(R.id.cusiRazonBook2Subtotal);
        TextView cusiRazonBook3QuantityDisplay = findViewById(R.id.cusiRazonBook3QuantityDisplay);
        TextView cusiRazonBook3SubtotalDisplay = findViewById(R.id.cusiRazonBook3Subtotal);
        TextView cusiRazonTotalDisplay = findViewById(R.id.cusiRazonTotal);
        cusiRazonRegisterButton = findViewById(R.id.cusiRazonConfirmRegisterButton);
        cusiRazonCheckoutButton = findViewById(R.id.cusiRazonCheckOutButton);

        cusiRazonBook1QuantityDisplay.setText(String.valueOf(cusiRazonBook1Quantity));
        cusiRazonBook1SubtotalDisplay.setText(String.valueOf(cusiRazonBook1Subtotal));
        cusiRazonBook2QuantityDisplay.setText(String.valueOf(cusiRazonBook2Quantity));
        cusiRazonBook2SubtotalDisplay.setText(String.valueOf(cusiRazonBook2Subtotal));
        cusiRazonBook3QuantityDisplay.setText(String.valueOf(cusiRazonBook3Quantity));
        cusiRazonBook3SubtotalDisplay.setText(String.valueOf(cusiRazonBook3Subtotal));
        cusiRazonTotalDisplay.setText(String.valueOf(cusiRazonTotal));


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser cusiRazonCurrentUser = cusiRazonAuth.getCurrentUser();
        if (cusiRazonCurrentUser != null) {
            cusiRazonFirestore.collection("users").document(cusiRazonCurrentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot result = task.getResult();

                            StringBuilder cusiRazonDisplayName = new StringBuilder();
                            cusiRazonDisplayName.append(result.get("firstName"));
                            cusiRazonDisplayName.append(" ");
                            cusiRazonDisplayName.append(result.get("lastName"));

                            cusiRazonRegisterButton.setText(cusiRazonDisplayName);

                            if (result.exists()) {
                                cusiRazonRegisterButton.setOnClickListener(v -> {
                                    Intent cusiRazonRegistrationIntent = new Intent(SummaryActivity.this, RegistrationActivity.class);
                                    startActivity(cusiRazonRegistrationIntent);
                                });
                            }

                        }
                    });


        }

        cusiRazonCheckoutButton.setOnClickListener(v -> {
            Map<String, Object> cusiRazonOrderContents = new HashMap<>();
            assert cusiRazonCurrentUser != null;
            cusiRazonOrderContents.put("orderedBy", cusiRazonCurrentUser.getUid());
            cusiRazonOrderContents.put("book1Quantity", cusiRazonBook1Quantity);
            cusiRazonOrderContents.put("book1Price", cusiRazonBook1Price);
            cusiRazonOrderContents.put("book2Quantity", cusiRazonBook2Quantity);
            cusiRazonOrderContents.put("book2Price", cusiRazonBook2Price);
            cusiRazonOrderContents.put("book3Quantity", cusiRazonBook3Quantity);
            cusiRazonOrderContents.put("book3Price", cusiRazonBook3Price);

            cusiRazonFirestore.collection("orders").add(cusiRazonOrderContents).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentReference cusiRazonOrderReference = task.getResult();
                    Intent cusiRazonCheckOutIntent = new Intent(SummaryActivity.this, CheckoutActivity.class);
                    String cusiRazonReferenceId = cusiRazonOrderReference.getId();
                    cusiRazonCheckOutIntent.putExtra("orderReference", cusiRazonReferenceId);

                    startActivity(cusiRazonCheckOutIntent);
                }
            });
        });


    }
}