package com.krozetapps.fitnessapp;

import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginPage extends AppCompatActivity {
    private Button button_login_login;
    private EditText editText_login_username;
    private EditText editText_login_password;
    private String username;
    private String password;
    private String baseUrl;
    private OkHttpClient client;
    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        baseUrl = "http://gopath.tk/login.php";

        editText_login_username = findViewById(R.id.editText_login_username);
        editText_login_password = findViewById(R.id.editText_login_password);
        button_login_login = (Button) findViewById(R.id.button_login_login);

        button_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    username = editText_login_username.getText().toString();
                    password = editText_login_password.getText().toString();

                    client = new OkHttpClient();

                    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                    StringBuilder content = new StringBuilder("email=" + username + "&password=" + password);
                    RequestBody body = RequestBody.create(mediaType, content.toString());

                    request = new Request.Builder().url(baseUrl).post(body)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .build();
//                    findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.i("Fail", e.getMessage());
//                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.i("Success", response.body().string());
                            // Hide the progress bar.
//                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                            if (response.priorResponse() == null) {
                                goToLoginSuccessful();
                            }
                            // Login Failure
                            else {
                                Looper.prepare();
                                Toast.makeText(LoginPage.this, "Login Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } catch (Exception ex) {
                    Log.i("Exception", ex.getMessage());
                }
            }
        });
    }

    /**
     * Open a new activity window.
     */
    private void goToLoginSuccessful() {
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("password", password);
        bundle.putString("baseUrl", baseUrl);

        Intent intent = new Intent(this, LoginSuccessful.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
