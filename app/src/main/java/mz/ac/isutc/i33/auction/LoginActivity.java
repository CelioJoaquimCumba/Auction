package mz.ac.isutc.i33.auction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText username=null, password=null;
    Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username_login);
        password = findViewById(R.id.password_login);
        login_button = findViewById(R.id.login_button_login);
        login_button.setOnClickListener(this);
    }
    //Validade is all the fields are field and the input of every each is valid
    //probably will need more enhancement
    private boolean validFields(EditText username_ET, EditText password_ET){
        String username = username_ET.getText().toString().trim();
        String password = password_ET.getText().toString().trim();
        if(
                username.equals("")
        ) {
            username_ET.setError("Insira o username");
            return false;
        }
        if( password.equals("")){
            password_ET.setError("Insira a password");
            return false;
        }
        return true;
    }

    private boolean userExists(String username, String password){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://auction-a4883-default-rtdb.firebaseio.com/").getReference("users");
        Query user = reference.orderByChild("username").equalTo(username);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if( dataSnapshot.exists() ){
                    String passwordFromDB = dataSnapshot.child(username).child("password").getValue(String.class);

                    if(passwordFromDB.equals(password)){
                        save(null);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Credentials don't match",Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(getApplicationContext(), "This user is inexistent, register your account",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return false;
    }

    public void toRegisterPage(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void save(View v){
        //TODO
        String text = "user";
        FileOutputStream fos = null;

        try {
            FileOutputStream fileOutputStream = fos = openFileOutput(MainActivity.FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());

            Toast.makeText(getApplicationContext(), "Saved",Toast.LENGTH_SHORT);
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


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    @Override
    public void onClick(View v) {
        if ( v.equals(login_button) ){
            if ( validFields(
                    username,
                    password)
                    && userExists(
                    username.getText().toString().trim(),
                    password.getText().toString().trim() )
            ){

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}