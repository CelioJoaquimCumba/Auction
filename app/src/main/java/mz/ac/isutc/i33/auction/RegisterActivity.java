package mz.ac.isutc.i33.auction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText username, email, password;
    Button register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.username_register);
        email = findViewById(R.id.email_register);
        password = findViewById(R.id.password_register);

        register_button = findViewById(R.id.register_button_register);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validFields(
                        username.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString()
                )){
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });


    }
    private boolean validFields(String username, String email, String password){
        if( username.trim() != "" || email.trim() != "" || password.trim() != "" ) {
            Toast.makeText(getApplicationContext(),
                    "One of your fields is not well putted. Guess which :)",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void toLoginPage(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}