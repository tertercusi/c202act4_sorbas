package com.example.c202cusirazonactivity4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConfirmationActivity extends AppCompatActivity {

    FirebaseFirestore cusiRazonFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Intent cusiRazonIntent = getIntent();
        String cusiRazonOrderReferenceId = cusiRazonIntent.getStringExtra("referenceId");

        cusiRazonFirestore = FirebaseFirestore.getInstance();

        TextView cusiRazonMessage = findViewById(R.id.cusiRazonConfirmationMessage);
        TextView cusiRazonBook1Quantity = findViewById(R.id.cusiRazonBook1QuantityConfirmation);
        TextView cusiRazonBook1Total = findViewById(R.id.cusiRazonBook1TotalConfirmation);
        TextView cusiRazonBook2Quantity = findViewById(R.id.cusiRazonBook2QuantityConfirmation);
        TextView cusiRazonBook2Total = findViewById(R.id.cusiRazonBook2TotalConfirmation);
        TextView cusiRazonBook3Quantity = findViewById(R.id.cusiRazonBook3QuantityConfirmation);
        TextView cusiRazonBook3Total = findViewById(R.id.cusiRazonBook3TotalConfirmation);
        TextView cusiRazonPriceTotal = findViewById(R.id.cusiRazonTotalPriceConfirmation);

        assert cusiRazonOrderReferenceId != null;
        cusiRazonFirestore.collection("orders").document(cusiRazonOrderReferenceId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot cusiRazonOrder = task.getResult();
                String cusiRazonUserId = String.valueOf(cusiRazonOrder.get("orderedBy"));

                int book1Quantity = cusiRazonOrder.get("book1Quantity", Integer.class);
                int book1Price = cusiRazonOrder.get("book1Price", Integer.class);
                int book1Subtotal = book1Quantity * book1Price;
                int book2Quantity = cusiRazonOrder.get("book2Quantity", Integer.class);
                int book2Price = cusiRazonOrder.get("book2Price", Integer.class);
                int book2Subtotal = book2Quantity * book2Price;
                int book3Quantity = cusiRazonOrder.get("book3Quantity", Integer.class);
                int book3Price = cusiRazonOrder.get("book3Price", Integer.class);
                int book3Subtotal = book3Quantity * book3Price;
                int bookTotal = book1Subtotal + book2Subtotal + book3Subtotal;

                cusiRazonBook1Quantity.setText(String.valueOf(book1Quantity));
                cusiRazonBook1Total.setText(String.valueOf(book1Subtotal));
                cusiRazonBook2Quantity.setText(String.valueOf(book2Quantity));
                cusiRazonBook2Total.setText(String.valueOf(book2Subtotal));
                cusiRazonBook3Quantity.setText(String.valueOf(book3Quantity));
                cusiRazonBook3Total.setText(String.valueOf(book3Subtotal));

                cusiRazonPriceTotal.setText(String.valueOf(bookTotal));

                cusiRazonFirestore.collection("users")
                        .document(cusiRazonUserId)
                        .get().addOnCompleteListener(userTask -> {
                            if (userTask.isSuccessful()) {
                                DocumentSnapshot cusiRazonUserData = userTask.getResult();
                                StringBuilder cusiRazonMessageBuilder = new StringBuilder();

                                cusiRazonMessageBuilder.append("Mr/Ms ");

                                Object cusiRazonFirstName = cusiRazonUserData.get("firstName");
                                cusiRazonMessageBuilder.append(cusiRazonFirstName);

                                cusiRazonMessageBuilder.append(" ");

                                Object cusiRazonLastname = cusiRazonUserData.get("lastName");
                                cusiRazonMessageBuilder.append(cusiRazonLastname);

                                cusiRazonMessageBuilder.append(" your order had been confirm. Please wait for 30 - 40 working days for your orders to be delivered on your address which is ");

                                String cusiRazonAddress = String.valueOf(cusiRazonUserData.get("address"));
                                cusiRazonMessageBuilder.append(cusiRazonAddress);

                                cusiRazonMessage.setText(cusiRazonMessageBuilder);

                            }
                        });

            }
        });
    }
}