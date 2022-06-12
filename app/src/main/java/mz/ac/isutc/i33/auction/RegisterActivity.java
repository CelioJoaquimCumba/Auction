package mz.ac.isutc.i33.auction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import mz.ac.isutc.i33.auction.models.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText username, email, password, passwordConfirmation;
    Button register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.username_register);
        email = findViewById(R.id.email_register);
        password = findViewById(R.id.password_register);
        passwordConfirmation = findViewById(R.id.confirmpassword_register);

        register_button = findViewById(R.id.register_button_register);

        register_button.setOnClickListener(this);


    }
    private  boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    private boolean validFields(EditText username_ET, EditText email_ET, EditText password_ET, EditText passwordConfirmation_ET){
        String username = username_ET.getText().toString().trim();
        String email = email_ET.getText().toString().trim();
        String password = password_ET.getText().toString().trim();
        String passwordConfirmation = passwordConfirmation_ET.getText().toString();
        if( username.equals("") ){
            return false;
        }
        if( email.equals("") ){
            email_ET.setError("Email is empty");
            return false;
        }
        if( password.equals("") ){
            password_ET.setError("Password is empty");
            return false;
        }
        if( !password.equals(passwordConfirmation) ){
            passwordConfirmation_ET.setError("Passwords not matching");
            return false;
        }
        if( !isValidEmail(email) ) {
            email_ET.setError("Email is not valid");
            return false;
        }
        return true;
    }

    public void toLoginPage(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void save(View v){
        //TODO
        String text = username.getText().toString();
        FileOutputStream fos = null;

        try {
            FileOutputStream fileOutputStream = fos = openFileOutput(MainActivity.FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());

            Toast.makeText(getApplicationContext(), fos.toString(),Toast.LENGTH_SHORT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if ( fos != null ){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    @Override
    public void onClick(View v){
        if( v.equals(register_button) ){
            String username_txt = username.getText().toString().trim();
            String email_txt = email.getText().toString().trim();
            String password_txt = password.getText().toString().trim();
            //String passwordConfirmation_txt = passwordConfirmation.getText().toString();
            if(validFields(
                    username,
                    email,
                    password,
                    passwordConfirmation
            )){
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://auction-a4883-default-rtdb.firebaseio.com/");
                DatabaseReference reference = database.getReference("users");
                Query user = reference.orderByChild("username").equalTo(username_txt);
                user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if( dataSnapshot.exists() ) {
                            //exist
                            Toast.makeText(getApplicationContext(),"A user with this username already exists",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //dont exist
                            User _user = new User(username_txt,email_txt,password_txt);
                            reference.child(username_txt).setValue(_user);
                            save(username);
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

}