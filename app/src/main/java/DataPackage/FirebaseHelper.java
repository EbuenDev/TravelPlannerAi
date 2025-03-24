package DataPackage;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;
public class FirebaseHelper {

    private final FirebaseAuth auth;
    private final FirebaseFirestore db;

    public FirebaseHelper() {
        // Initialize Firebase FireStore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
    public interface RegistrationCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    // Register user with email/password
    public Task<AuthResult> registerUser(String email, String password) {
        return auth.createUserWithEmailAndPassword(email, password);
    }
    
    // Add user details to FireStore
    private void addUserToFirestore(String userId, String fullName, String email,
                                    final RegistrationCallback callback) {
        Map<String, Object> user = new HashMap<>();
        user.put("fullName", fullName);
        user.put("email", email);
        user.put("createdAt", FieldValue.serverTimestamp());

        db.collection("users").document(userId)
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure(task.getException() != null ?
                                task.getException().getMessage() : "Failed to save user data");
                    }
                });
    }

    // Combined registration method
    public void completeRegistration(String fullName, String email, String password,
                                     final RegistrationCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            addUserToFirestore(user.getUid(), fullName, email, callback);
                        } else {
                            callback.onFailure("User creation failed");
                        }
                    } else {
                        callback.onFailure(task.getException() != null ?
                                task.getException().getMessage() : "Registration failed");
                    }
                });
    }

}