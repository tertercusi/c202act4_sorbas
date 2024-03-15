package com.example.c202sorbasactivity4;

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

import java.util.HashMap;
import java.util.Map;

public class SummaryActivity extends AppCompatActivity {

    FirebaseFirestore sorBasFirestore;
    FirebaseAuth sorBasAuth;

    Button sorBasRegisterButton;
    Button sorBasCheckoutButton;
    private int sorBasBook1Quantity;
    private int sorBasBook1Price;
    private int sorBasBook1Subtotal;
    private int sorBasBook2Quantity;
    private int sorBasBook2Price;
    private int sorBasBook2Subtotal;
    private int sorBasBook3Quantity;
    private int sorBasBook3Price;
    private int sorBasBook3Subtotal;
    private int sorBasTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Intent sorBasIntent = getIntent();

        sorBasFirestore = FirebaseFirestore.getInstance();
        sorBasAuth = FirebaseAuth.getInstance();

        sorBasBook1Quantity = sorBasIntent.getIntExtra("book1Qty", 0);
        sorBasBook1Price = sorBasIntent.getIntExtra("book1Price", 0);
        sorBasBook1Subtotal = sorBasBook1Price * sorBasBook1Quantity;
        sorBasBook2Quantity = sorBasIntent.getIntExtra("book2Qty", 0);
        sorBasBook2Price = sorBasIntent.getIntExtra("book2Price", 0);
        sorBasBook2Subtotal = sorBasBook2Price * sorBasBook2Quantity;
        sorBasBook3Quantity = sorBasIntent.getIntExtra("book3Qty", 0);
        sorBasBook3Price = sorBasIntent.getIntExtra("book3Price", 0);
        sorBasBook3Subtotal = sorBasBook3Price * sorBasBook3Quantity;
        sorBasTotal = sorBasBook1Subtotal + sorBasBook2Subtotal + sorBasBook3Subtotal;

        TextView sorBasBook1QuantityDisplay = findViewById(R.id.sorBasBook1QuantityDisplay);
        TextView sorBasBook1SubtotalDisplay = findViewById(R.id.sorBasBook1Subtotal);
        TextView sorBasBook2QuantityDisplay = findViewById(R.id.sorBasBook2QuantityDisplay);
        TextView sorBasBook2SubtotalDisplay = findViewById(R.id.sorBasBook2Subtotal);
        TextView sorBasBook3QuantityDisplay = findViewById(R.id.sorBasBook3QuantityDisplay);
        TextView sorBasBook3SubtotalDisplay = findViewById(R.id.sorBasBook3Subtotal);
        TextView sorBasTotalDisplay = findViewById(R.id.sorBasTotal);
        sorBasRegisterButton = findViewById(R.id.sorBasConfirmRegisterButton);
        sorBasCheckoutButton = findViewById(R.id.sorBasCheckOutButton);

        sorBasBook1QuantityDisplay.setText(String.valueOf(sorBasBook1Quantity));
        sorBasBook1SubtotalDisplay.setText(String.valueOf(sorBasBook1Subtotal));
        sorBasBook2QuantityDisplay.setText(String.valueOf(sorBasBook2Quantity));
        sorBasBook2SubtotalDisplay.setText(String.valueOf(sorBasBook2Subtotal));
        sorBasBook3QuantityDisplay.setText(String.valueOf(sorBasBook3Quantity));
        sorBasBook3SubtotalDisplay.setText(String.valueOf(sorBasBook3Subtotal));
        sorBasTotalDisplay.setText(String.valueOf(sorBasTotal));


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser sorBasCurrentUser = sorBasAuth.getCurrentUser();
        if (sorBasCurrentUser != null) {
            sorBasFirestore.collection("users").document(sorBasCurrentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot result = task.getResult();

                            StringBuilder sorBasDisplayName = new StringBuilder();
                            sorBasDisplayName.append(result.get("firstName"));
                            sorBasDisplayName.append(" ");
                            sorBasDisplayName.append(result.get("lastName"));

                            sorBasRegisterButton.setText(sorBasDisplayName);

                            if (result.exists()) {
                                sorBasRegisterButton.setOnClickListener(v -> {
                                    Intent sorBasRegistrationIntent = new Intent(SummaryActivity.this, RegistrationActivity.class);
                                    startActivity(sorBasRegistrationIntent);
                                });
                            }

                        }
                    });


        }

        sorBasCheckoutButton.setOnClickListener(v -> {
            Map<String, Object> sorBasOrderContents = new HashMap<>();
            assert sorBasCurrentUser != null;
            sorBasOrderContents.put("orderedBy", sorBasCurrentUser.getUid());
            sorBasOrderContents.put("book1Quantity", sorBasBook1Quantity);
            sorBasOrderContents.put("book1Price", sorBasBook1Price);
            sorBasOrderContents.put("book2Quantity", sorBasBook2Quantity);
            sorBasOrderContents.put("book2Price", sorBasBook2Price);
            sorBasOrderContents.put("book3Quantity", sorBasBook3Quantity);
            sorBasOrderContents.put("book3Price", sorBasBook3Price);

            sorBasFirestore.collection("orders").add(sorBasOrderContents).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentReference sorBasOrderReference = task.getResult();
                    Intent sorBasCheckOutIntent = new Intent(SummaryActivity.this, CheckoutActivity.class);
                    String sorBasReferenceId = sorBasOrderReference.getId();
                    sorBasCheckOutIntent.putExtra("orderReference", sorBasReferenceId);

                    startActivity(sorBasCheckOutIntent);
                }
            });
        });


    }
}