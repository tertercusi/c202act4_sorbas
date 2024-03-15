package com.example.c202sorbasactivity4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CheckoutActivity extends AppCompatActivity {

    FirebaseFirestore sorBasFirestore;
    Button sorBasProceedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Intent sorBasIntent = getIntent();
        String sorBasOrderReferenceId = sorBasIntent.getStringExtra("orderReference");

        sorBasFirestore = FirebaseFirestore.getInstance();
        sorBasProceedButton = findViewById(R.id.sorBasProceedButton);
        EditText sorBasCcvField = findViewById(R.id.sorBasCCV);
        Button sorBasToMainButton = findViewById(R.id.sorBasMainButton);

        TextView sorBasFullname = findViewById(R.id.sorBasFullname);
        TextView sorBasAddress = findViewById(R.id.sorBasAddressDisplay);

        assert sorBasOrderReferenceId != null;
        sorBasFirestore.collection("orders").document(sorBasOrderReferenceId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot sorBasOrder = task.getResult();
                String sorBasUserId = String.valueOf(sorBasOrder.get("orderedBy"));
                sorBasFirestore.collection("users")
                        .document(sorBasUserId)
                        .get().addOnCompleteListener(userTask -> {
                            if (userTask.isSuccessful()) {
                                DocumentSnapshot sorBasUserData = userTask.getResult();
                                StringBuilder sorBasFullnameBuilder = new StringBuilder();

                                Object sorBasFirstName = sorBasUserData.get("firstName");
                                sorBasFullnameBuilder.append(sorBasFirstName);

                                sorBasFullnameBuilder.append(" ");

                                Object sorBasLastname = sorBasUserData.get("lastName");
                                sorBasFullnameBuilder.append(sorBasLastname);

                                sorBasFullname.setText(sorBasFullnameBuilder);
                                Object sorBasAddressData = sorBasUserData.get("address");
                                sorBasAddress.setText(String.valueOf(sorBasAddressData));

                            }
                        });

            }
        });

        sorBasProceedButton.setOnClickListener(v -> {
            int sorBasCvvCode = Integer.parseInt(String.valueOf(sorBasCcvField.getText()));
            if (sorBasCvvCode == 456) {
                Intent sorBasConfirmationIntent = new Intent(CheckoutActivity.this, ConfirmationActivity.class);
                sorBasConfirmationIntent.putExtra("referenceId", sorBasOrderReferenceId);
                startActivity(sorBasConfirmationIntent);

            } else {
                Toast.makeText(this, "Wrong CCV!", Toast.LENGTH_SHORT).show();
            }
        });

        sorBasToMainButton.setOnClickListener(v -> {
            Intent sorBasResetIntent = new Intent(CheckoutActivity.this, MainActivity.class);
            startActivity(sorBasResetIntent);
        });
    }
}