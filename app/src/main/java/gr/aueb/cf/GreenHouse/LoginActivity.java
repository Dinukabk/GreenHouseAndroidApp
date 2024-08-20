package gr.aueb.cf.agronitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import gr.aueb.cf.agronitor.apiclient.ApiClient;
import gr.aueb.cf.agronitor.apiclient.IApiService;
import gr.aueb.cf.agronitor.apiclient.requests.LoginRequest;
import gr.aueb.cf.agronitor.apiclient.responses.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity for user authentication.
 */
public class LoginActivity extends AppCompatActivity {

    private TextInputEditText usernameET;
    private TextInputEditText passwordET;
    private AppCompatButton loginBtn;
    private AppCompatButton registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        usernameET = findViewById(R.id.usernameTextInputEditText);
        passwordET = findViewById(R.id.passwordTextInputEditText);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Objects.requireNonNull(usernameET.getText()).toString().trim();
                String password = Objects.requireNonNull(passwordET.getText()).toString().trim();

                loginUser();


            }

        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {

        Intent intent = new Intent(LoginActivity.this, ManagementActivity.class);
        startActivity(intent);
    }
}