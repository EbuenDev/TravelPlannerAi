package DataPackage;

import android.app.AlertDialog;
import android.content.Context;

public class ConstructorANDHelperClass {
    private String email;
    private String password;
    private Context context; // Context variable

    // Constructor with context
    public ConstructorANDHelperClass(Context context, String email, String password) {
        this.context = context;
        this.email = email;
        this.password = password;
    }

    // Setters
    public void setEmail(String email) {
        if (isValidEmail(email)) {
            this.email = email;
        } else {
            showAlert("Invalid Email", "Please enter a valid email address.");
        }
    }

    public void setPassword(String password) {
        if (isValidPassword(password)) {
            this.password = password;
        } else {
            showAlert("Invalid Password", "Password must be at least 8 characters long.");
        }
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Email Validation
    public boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    // Password Validation
    public boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    // Helper Method to Show Alert Dialog
    private void showAlert(String title, String message) {
        if (context != null) { // Check if context is available
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


}
