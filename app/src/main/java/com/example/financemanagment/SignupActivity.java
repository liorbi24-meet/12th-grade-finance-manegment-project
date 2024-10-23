package com.example.financemanagment;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class SignupActivity extends AppCompatActivity {  // Add inheritance

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

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            saveUserName(user, name);
                        } else {
                            // handle signup failure
                        }
                    });
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, OpeningPage.class);
            startActivity(intent);
        });

    }

    private void saveUserName(FirebaseUser user, String name) {
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> userData = new HashMap<>();
            userData.put("name", name);

            db.collection("users").document(user.getUid()).set(userData).addOnSuccessListener(aVoid -> {
                        Toast.makeText(SignupActivity.this, "Name saved successfully.",
                                Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(SignupActivity.this, "Error saving name.",
                                Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
