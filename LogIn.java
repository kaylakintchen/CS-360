package com.example.my;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;


public class LogIn extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonCreateAccount;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Check if the username and password match any existing user records in the database
                boolean isValidLogin = databaseHelper.checkUserCredentials(username, password);
                if (isValidLogin) {
                    // Login successful, proceed to the main activity or home screen
                    // You can add an Intent here to start a new activity for the logged-in user.
                    Toast.makeText(LogIn.this, "Login successful!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LogIn.this, EventList.class);
                    startActivity(intent);
                    // Finish the current activity to prevent the user from navigating back to the login screen using the back button
                    finish();

                } else {
                    // Login failed, show an error message
                    Toast.makeText(LogIn.this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Perform input validation (e.g., check if username and password are not empty)

                // Insert the new user's information into the database
                long result = databaseHelper.insertUser(username, password);
                if (result != -1) {
                    // Registration successful, show a success message or redirect to the login screen
                    Toast.makeText(LogIn.this, "Account created successfully!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LogIn.this, EventList.class);
                    startActivity(intent);
                    // Finish the current activity to prevent the user from navigating back to the login screen using the back button
                    finish();

                } else {
                    // Registration failed, show an error message
                    Toast.makeText(LogIn.this, "Failed to create account. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
