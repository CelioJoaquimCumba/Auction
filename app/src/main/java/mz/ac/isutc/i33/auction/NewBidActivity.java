package mz.ac.isutc.i33.auction;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import java.net.InetSocketAddress;
import java.util.Calendar;

public class NewBidActivity extends AppCompatActivity implements View.OnClickListener{
    Button load_image_button, register_button, date_button, time_button;
    ImageView imageView;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bid);
        load_image_button = findViewById(R.id.load_image_new_bid);

        register_button = findViewById(R.id.register_button_new_bid);

        date_button = findViewById(R.id.date_button_new_bid);
        txtDate = findViewById(R.id.ending_date_new_bid);
        time_button = findViewById(R.id.time_button_new_bid);
        txtTime = findViewById(R.id.ending_time_new_bid);

        register_button.setOnClickListener(this);

        load_image_button.setOnClickListener(this);

        date_button.setOnClickListener(this);

        time_button.setOnClickListener(this);
    }


    private boolean validation(){
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode == RESULT_OK && data != null ){
            Uri selectedImage = data.getData();
            imageView = findViewById(R.id.imageView_new_bid);
            imageView.setImageURI(selectedImage);
        }
    }

    @Override
    public void onClick(View v) {
        if (register_button.equals(v)) {
            if(validation()){
                Intent intent = new Intent(NewBidActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }else if(load_image_button.equals(v)){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 3);
        }else if(date_button.equals(v)){
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }else if (time_button.equals(v)){
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}