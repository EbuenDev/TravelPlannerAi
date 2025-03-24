package UiPackage;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelplannerapp.R;
import com.example.travelplannerapp.databinding.ActivityLoginBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import DataPackage.ConstructorANDHelperClass;
import DataPackage.FirebaseHelper;


import com.example.travelplannerapp.MainActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private  TextInputLayout textInputLayout;
    private MaterialButton materialButton;
    private FirebaseHelper firebaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.email.setBoxStrokeColor(getResources().getColor(R.color.white));
        binding.password.setBoxStrokeColor(getResources().getColor(R.color.white));
        binding.email.setBoxStrokeWidth(2);
        binding.password.setBoxStrokeWidth(2);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle login button click

                //creating object for firesbase helper class
//                FirebaseHelper firebaseHelper = new FirebaseHelper();
//                firebaseHelper.fireStoreDB();
//

            }
        });

        binding.registerTextView.setOnClickListener(view1 -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        binding.email.setOnClickListener(view1 -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        binding.loginButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });





    }

}