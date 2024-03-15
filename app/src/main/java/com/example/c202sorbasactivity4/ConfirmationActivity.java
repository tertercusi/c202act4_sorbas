package com.example.c202sorbasactivity4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConfirmationActivity extends AppCompatActivity {

    FirebaseFirestore sorBasFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Intent sorBasIntent = getIntent();
        String sorBasOrderReferenceId = sorBasIntent.getStringExtra("referenceId");

        sorBasFirestore = FirebaseFirestore.getInstance();

        TextView sorBasMessage = findViewById(R.id.sorBasConfirmationMessage);
        TextView sorBasBook1Quantity = findViewById(R.id.sorBasBook1QuantityConfirmation);
        TextView sorBasBook1Total = findViewById(R.id.sorBasBook1TotalConfirmation);
        TextView sorBasBook2Quantity = findViewById(R.id.sorBasBook2QuantityConfirmation);
        TextView sorBasBook2Total = findViewById(R.id.sorBasBook2TotalConfirmation);
        TextView sorBasBook3Quantity = findViewById(R.id.sorBasBook3QuantityConfirmation);
        TextView sorBasBook3Total = findViewById(R.id.sorBasBook3TotalConfirmation);
        TextView sorBasPriceTotal = findViewById(R.id.sorBasTotalPriceConfirmation);

        assert sorBasOrderReferenceId != null;
        sorBasFirestore.collection("orders").document(sorBasOrderReferenceId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot sorBasOrder = task.getResult();
                String sorBasUserId = String.valueOf(sorBasOrder.get("orderedBy"));

                int book1Quantity = sorBasOrder.get("book1Quantity", Integer.class);
                int book1Price = sorBasOrder.get("book1Price", Integer.class);
                int book1Subtotal = book1Quantity * book1Price;
                int book2Quantity = sorBasOrder.get("book2Quantity", Integer.class);
                int book2Price = sorBasOrder.get("book2Price", Integer.class);
                int book2Subtotal = book2Quantity * book2Price;
                int book3Quantity = sorBasOrder.get("book3Quantity", Integer.class);
                int book3Price = sorBasOrder.get("book3Price", Integer.class);
                int book3Subtotal = book3Quantity * book3Price;
                int bookTotal = book1Subtotal + book2Subtotal + book3Subtotal;

                sorBasBook1Quantity.setText(String.valueOf(book1Quantity));
                sorBasBook1Total.setText(String.valueOf(book1Subtotal));
                sorBasBook2Quantity.setText(String.valueOf(book2Quantity));
                sorBasBook2Total.setText(String.valueOf(book2Subtotal));
                sorBasBook3Quantity.setText(String.valueOf(book3Quantity));
                sorBasBook3Total.setText(String.valueOf(book3Subtotal));

                sorBasPriceTotal.setText(String.valueOf(bookTotal));

                sorBasFirestore.collection("users")
                        .document(sorBasUserId)
                        .get().addOnCompleteListener(userTask -> {
                            if (userTask.isSuccessful()) {
                                DocumentSnapshot sorBasUserData = userTask.getResult();
                                StringBuilder sorBasMessageBuilder = new StringBuilder();

                                sorBasMessageBuilder.append("Mr/Ms ");

                                Object sorBasFirstName = sorBasUserData.get("firstName");
                                sorBasMessageBuilder.append(sorBasFirstName);

                                sorBasMessageBuilder.append(" ");

                                Object sorBasLastname = sorBasUserData.get("lastName");
                                sorBasMessageBuilder.append(sorBasLastname);

                                sorBasMessageBuilder.append(" your order had been confirm. Please wait for 30 - 40 working days for your orders to be delivered on your address which is ");

                                String sorBasAddress = String.valueOf(sorBasUserData.get("address"));
                                sorBasMessageBuilder.append(sorBasAddress);

                                sorBasMessage.setText(sorBasMessageBuilder);

                            }
                        });

            }
        });
    }
}