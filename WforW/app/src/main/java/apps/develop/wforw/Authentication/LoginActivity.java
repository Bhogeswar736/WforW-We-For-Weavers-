package apps.develop.wforw.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import apps.develop.wforw.MainActivity;
import apps.develop.wforw.PreferenceManager;
import apps.develop.wforw.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {


    ActivityLoginBinding binding;

    FirebaseAuth auth;

    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);

        auth = FirebaseAuth.getInstance();

        if (preferenceManager.getBoolean("signed")){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }



        binding.btnLogin.setOnClickListener(view -> {
            String email = binding.inputEmail.getText().toString();
            String password = binding.inputPassword.getText().toString();
            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Required all fields", Toast.LENGTH_SHORT).show();
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(this, "Enter valid email address", Toast.LENGTH_SHORT).show();
            }else {
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Logging...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        Intent intent;
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        preferenceManager.putBoolean("signed",true);
                        finish();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

        binding.txtSignUp.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class)));

    }
}