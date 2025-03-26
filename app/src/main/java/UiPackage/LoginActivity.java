package UiPackage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.travelplannerapp.R;
import com.example.travelplannerapp.databinding.ActivityLoginBinding;
import DataPackage.FirebaseHelper;
import DataPackage.NetworkUtils;

import com.example.travelplannerapp.MainActivity;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseHelper firebaseHelper;
    private Handler notificationHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Initialize FirebaseHelper instance
        firebaseHelper = new FirebaseHelper();
        notificationHandler = new Handler();

        //Hard coded to set the box stroke color to white
        binding.email.setBoxStrokeColor(getResources().getColor(R.color.white));
        binding.password.setBoxStrokeColor(getResources().getColor(R.color.white));
        binding.email.setBoxStrokeWidth(2);
        binding.password.setBoxStrokeWidth(2);

        // Check if user is already logged in
        if (firebaseHelper.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        // Set click listener for login button
        binding.loginButton.setOnClickListener(v -> attemptLogin());
        binding.registerTextView.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        // Clear error on focus
        binding.email.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.email.setError(null);
            }
        });

/// Revalidate when focus is lost
        binding.email.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                // Get both email and password values for validation
                String email = binding.email.getEditText().getText().toString().trim();
                String password = binding.password.getEditText().getText().toString().trim();
                validateInputs(email, password); // Pass both parameters
            }
        });

// For password field (optional but recommended)
        binding.password.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.password.setError(null); // Clear password error
            }
        });
    }

    // Method to show network notification
    private void showNetworkNotification() {
        // Cancel any pending hide operations
        notificationHandler.removeCallbacksAndMessages(null);


        // Make the notification visible
        binding.internetNotificationTextview.setVisibility(View.VISIBLE);


        // Fade in animation
        binding.internetNotificationTextview.setAlpha(0f);
        binding.internetNotificationTextview.animate()
                .alpha(1f)
                .setDuration(FADE_IN_DURATION)
                .start();

        // Hide after duration with fade out
        notificationHandler.postDelayed(() -> {
            binding.internetNotificationTextview.animate()
                    .alpha(0f)
                    .setDuration(FADE_IN_DURATION)
                    .withEndAction(() -> binding.internetNotificationTextview.setVisibility(View.GONE))
                    .start();
        }, FADE_OUT_START);

    }
    // Method to handle login attempt
    private void attemptLogin() {
        String email = binding.email.getEditText().getText().toString().trim();
        String password = binding.password.getEditText().getText().toString().trim();

        // Check for network connection
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showNetworkNotification();
            return;
        }
        // Validate inputs
        if (validateInputs(email, password)) {
            showProgress(true);
            performLogin(email, password);
        }
    }
    // Constants for animation timing
    private boolean validateInputs(String email, String password) {
        boolean valid = true;

        if (email.isEmpty()) {
            binding.email.setError("Email required");
            valid = false;
        } else {
            binding.email.setError(null);
        }

        if (password.isEmpty()) {
            binding.password.setError("Password required");
            valid = false;
        } else if (password.length() < 6) {
            binding.password.setError("Minimum 6 characters");
            valid = false;
        } else {
            binding.password.setError(null);
        }

        return valid;
    }

    private void performLogin(String email, String password) {
        firebaseHelper.loginUser(email, password, new FirebaseHelper.LoginCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                runOnUiThread(() -> {
                    showProgress(false);
                    Toast.makeText(LoginActivity.this,
                            "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> {
                    showProgress(false);
                    Toast.makeText(LoginActivity.this,
                            "Login failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }


    private void clearEmailError() {
        binding.email.setError(null);
        binding.email.setErrorEnabled(false);
    }
    private static final int FADE_IN_DURATION = 200;
    private static final int FADE_OUT_START = 800; // Start fade out at 800ms
    private void showProgress(boolean show) {
//        assert binding.progressBar != null;
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.loginButton.setEnabled(!show);
    }
}