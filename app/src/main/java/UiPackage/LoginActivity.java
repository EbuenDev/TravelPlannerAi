package UiPackage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.travelplannerapp.R;
import com.example.travelplannerapp.databinding.ActivityLoginBinding;
import DataPackage.FirebaseHelper;
import com.example.travelplannerapp.MainActivity;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseHelper firebaseHelper;
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

        //Hard coded to set the box stroke color to white
        binding.email.setBoxStrokeColor(getResources().getColor(R.color.white));
        binding.password.setBoxStrokeColor(getResources().getColor(R.color.white));
        binding.email.setBoxStrokeWidth(2);
        binding.password.setBoxStrokeWidth(2);


        if (firebaseHelper.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }


        binding.loginButton.setOnClickListener(v -> attemptLogin());
        binding.registerTextView.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });


    }

    private void attemptLogin() {
        String email = binding.email.getEditText().getText().toString().trim();
        String password = binding.password.getEditText().getText().toString().trim();

        if (validateInputs(email, password)) {
            showProgress(true);
            performLogin(email, password);
        }
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

    private void showProgress(boolean show) {
//        assert binding.progressBar != null;
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.loginButton.setEnabled(!show);
    }


}