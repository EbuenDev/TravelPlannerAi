package UiPackage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelplannerapp.R;
import com.example.travelplannerapp.databinding.ActivityRegisterBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ktx.Firebase;

import javax.annotation.Nonnull;

import DataPackage.FirebaseHelper;


public class RegisterActivity extends AppCompatActivity {

    public ActivityRegisterBinding binding;
    private @Nonnull FirebaseHelper firebaseHelper;

//    private OnBackPressedDispatcher onBackPressedDispatcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        firebaseHelper = new FirebaseHelper();

        // Initialize View Binding
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // hard coded colors for text input layout and edit text
        binding.fullNameEditText.setBoxStrokeColor(getResources().getColor(R.color.white));
        binding.emailEditText.setBoxStrokeColor(getResources().getColor(R.color.white));
        binding.passwordEditText.setBoxStrokeColor(getResources().getColor(R.color.white));
        binding.confirmPasswordEditText.setBoxStrokeColor(getResources().getColor(R.color.white));

        // for back button in register activity
        binding.backButton.setOnClickListener( view1 -> {
            onBackPressed();
        });

        // for back button in register activity
        binding.loginTextView.setOnClickListener(v -> {
            onBackPressed();
        });


        // for register button in register activity
        binding.registerButton.setOnClickListener(view1 -> {


            String fullName = binding.fullNameEditText.getEditText().getText().toString().trim();
            String email = binding.emailEditText.getEditText().getText().toString().trim();
            String password = binding.passwordEditText.getEditText().getText().toString().trim();
            String confirmPassword = binding.confirmPasswordEditText.getEditText().getText().toString().trim();

            if (validateInputs(fullName, email, password, confirmPassword)) {
                showProgress(true);
                registerUser(fullName, email, password);
            }

        });

    }

    private void registerUser(String fullName, String email, String password) {
        firebaseHelper.completeRegistration(fullName, email, password,
                new FirebaseHelper.RegistrationCallback() {
                    @Override
                    public void onSuccess() {
                        showProgress(false);
                        Toast.makeText(RegisterActivity.this,
                                "Registration successful!",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        showProgress(false);
                        Toast.makeText(RegisterActivity.this,
                                "Registration failed: " + errorMessage,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showProgress(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.registerButton.setEnabled(!show);
    }

    private boolean validateInputs(String fullName, String email,
                                   String password, String confirmPassword) {
        boolean isValid = true;

        if (fullName.isEmpty()) {
            binding.fullNameEditText.setError("Full name is required");
            isValid = false;
        }

        if (email.isEmpty()) {
            binding.emailEditText.setError("Email is required");
            isValid = false;
        }

        if (password.isEmpty()) {
            binding.passwordEditText.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            binding.passwordEditText.setError("Password must be at least 6 characters");
            isValid = false;
        }

        if (!password.equals(confirmPassword)) {
            binding.confirmPasswordEditText.setError("Passwords don't match");
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // This will close Activity B and return to Activity A
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Clean up binding reference
    }
}