package com.example.financemanagment;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private TextView tvTotalBalance, tvIncome, tvExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        //initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();
        // Initialize TextViews
        tvTotalBalance = findViewById(R.id.tvTotalBalance);
        tvIncome = findViewById(R.id.tvIncome);
        tvExpenses = findViewById(R.id.tvExpenses);

        // Assuming the current user is logged in and we have their userID
        String userId = auth.getInstance().getCurrentUser().getUid();

        // Fetch data from Firebase for the current user
        fetchUserDataFromFirebase(userId);
    }

    private void fetchUserDataFromFirebase(String userId) {
        // Reference to the user's document in the "users" collection
        DocumentReference userRef = db.collection("users").document(userId);

        // Fetch the document
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Retrieve income, expenses, and total balance
                    double income = document.getDouble("income");
                    double expenses = document.getDouble("expenses");
                    double totalBalance = document.getDouble("totalBalance");

                    // Update UI with the retrieved data
                    tvIncome.setText("Income: $" + String.format("%.2f", income));
                    tvExpenses.setText("Expenses: $" + String.format("%.2f", expenses));
                    tvTotalBalance.setText("Total Balance: $" + String.format("%.2f", totalBalance));
                } else {
                    // Handle the case where the document doesn't exist
                    tvTotalBalance.setText("Data not found");
                }
            } else {
                // Handle the error
                tvTotalBalance.setText("Error: " + task.getException().getMessage());
            }
        });
    }
}
