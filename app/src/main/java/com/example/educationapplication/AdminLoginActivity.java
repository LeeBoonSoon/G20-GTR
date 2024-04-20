package com.example.educationapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        emailEditText = findViewById(R.id.adminEmail);
        passwordEditText = findViewById(R.id.adminPassword);
        loginButton = findViewById(R.id.loginBtn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Check if email is admin
                if (email.equals("admin@gtquiz.com") && password.equals("admin123")) {
                    // Admin login
                    Intent intent = new Intent(AdminLoginActivity.this, AdminMainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Normal user login
                    // Add your login logic for normal users here
                    Toast.makeText(AdminLoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.returnBtn).setOnClickListener(v ->
                startActivity(new Intent(AdminLoginActivity.this, LoginActivity.class)));

    }
}