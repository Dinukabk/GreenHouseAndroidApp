package gr.aueb.cf.agronitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import gr.aueb.cf.agronitor.apiclient.ApiClient;
import gr.aueb.cf.agronitor.apiclient.IApiService;
import gr.aueb.cf.agronitor.apiclient.requests.RegisterRequest;
import gr.aueb.cf.agronitor.apiclient.responses.RegisterResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity for user registration
 */
public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText emailET;
    private TextInputEditText usernameET;
    private TextInputEditText passwordET;
    private TextInputEditText confirmPasswordET;
    private AppCompatButton registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailET = findViewById(R.id.emailTextInputEditText);
        usernameET = findViewById(R.id.usernameTextInputEditText);
        passwordET = findViewById(R.id.passwordTextInputEditText);
        confirmPasswordET = findViewById(R.id.confirmPasswordTextInputEditText);
        registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameET.getText().toString().trim();
                String email = emailET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                String passwordConf = confirmPasswordET.getText().toString().trim();

                if (username.isEmpty()) {
                    usernameET.setError("Username is required");
                    usernameET.requestFocus();
                    return;
                } else if (password.isEmpty()) {
                    passwordET.setError("Password is required");
                    passwordET.requestFocus();
                    return;
                } else if (passwordConf.isEmpty()) {
                    confirmPasswordET.setError("Confirm your password");
                    confirmPasswordET.requestFocus();
                    return;
                } else if (!password.equals(passwordConf)) {
                    confirmPasswordET.setError("Confirm your password");
                    confirmPasswordET.requestFocus();
                    return;
                } else if (email.isEmpty()) {
                    emailET.setError("Email is required");
                    emailET.requestFocus();
                }
                registerUser();
            }
        });
    }

    private void registerUser() {
        RegisterRequest registerRequest = new RegisterRequest(usernameET.getText().toString(),
                                                              passwordET.getText().toString(),
                                                              emailET.getText().toString());
        IApiService apiService;
        apiService = ApiClient.getApiClient().create(IApiService.class);
        Call<RegisterResponse> call = apiService.registerUser(registerRequest);
        call.enqueue(new Callback<RegisterResponse>() {

            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    String username = usernameET.getText().toString();
                    Toast.makeText(RegisterActivity.this, "Hi " + username + "!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}