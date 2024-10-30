package com.example.financemanagment;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, nameEditText;
    private Button signupButton, backButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();

        nameEditText = findViewById(R.id.signup_name);
        emailEditText = findViewById(R.id.signup_email);
        passwordEditText = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        backButton = findViewById(R.id.back_button);

        signupButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String name = nameEditText.getText().toString();

            // Register the user with Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            Log.d("SignupActivity", "User registered with UID: " + user.getUid());
                            this.saveUserToDatabase(user, name);
                            Log.d("SignupActivity", "User data saved to Firestore");
                        } else {
                            // Handle signup failure
                            Toast.makeText(SignupActivity.this, "Signup failed. Try again. booooo",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, OpeningPage.class);
            startActivity(intent);
        });
    }

    // Save user's name and financial information to Firestore
    private void saveUserToDatabase(FirebaseUser user, String name) {
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a map with user's data, including financial info
            Map<String, Object> userData = new HashMap<>();
            userData.put("name", name);
            userData.put("income", 0.0);         // Initialize income as 0
            userData.put("expenses", 0.0);       // Initialize expenses as 0
            userData.put("totalBalance", 0.0);   // Initialize total balance as 0
            Log.d("SignupActivity", "created map");
            // Save data to Firestore
            db.collection("users").document(user.getUid()).set(userData)
                    .addOnSuccessListener(aVoid -> {
                        // After signup, navigate to the main screen or dashboard
                        Log.d("SignupActivity", "User data saved to Firestorehfhfhf");
                        Intent intent = new Intent(SignupActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(SignupActivity.this, "Error saving data: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        Log.e("SignupActivity", "Error saving user data to Firestore", e);
                    });
            Log.d("SignupActivity", "User data not saved to Firestore");
        }
    }
}
