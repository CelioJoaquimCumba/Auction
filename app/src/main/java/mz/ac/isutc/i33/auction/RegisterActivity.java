package mz.ac.isutc.i33.auction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mz.ac.isutc.i33.auction.models.User;

public class RegisterActivity extends AppCompatActivity {
    EditText username, email, password, passwordConfirmation;
    Button register_button;

//    FirebaseDatabase rootNode;
//    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.username_register);
        email = findViewById(R.id.email_register);
        password = findViewById(R.id.password_register);
        passwordConfirmation = findViewById(R.id.confirmpassword_register);

        register_button = findViewById(R.id.register_button_register);


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username_txt = username.getText().toString();
                String email_txt = email.getText().toString();
                String password_txt = password.getText().toString();
                String passwordConfirmation_txt = passwordConfirmation.getText().toString();
                if(validFields(
                        username_txt,
                        email_txt,
                        password_txt,
                        passwordConfirmation_txt
                )){
                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://auction-a4883-default-rtdb.firebaseio.com/");
                    DatabaseReference myRef = database.getReference("users");

                    User user = new User(username_txt,email_txt,password_txt);
                    myRef.child(username_txt).setValue(user);
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });


    }
    private  boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    private boolean validFields(String username, String email, String password, String passwordConfirmation){
        if( username.trim().equals("") ){
            return false;
        }
        if( email.trim().equals("") ){
            return false;
        }
        if( password.trim().equals("") ){
            return false;
        }
        if( !password.equals(passwordConfirmation) ){
            return false;
        }

        if( !isValidEmail(email) ) {
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