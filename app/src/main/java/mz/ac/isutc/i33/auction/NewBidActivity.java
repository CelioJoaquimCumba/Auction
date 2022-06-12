package mz.ac.isutc.i33.auction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import mz.ac.isutc.i33.auction.models.Bid.Bid;
import mz.ac.isutc.i33.auction.models.Bid.Bid_post;

public class NewBidActivity extends AppCompatActivity implements View.OnClickListener{
    Button load_image_button, register_button, date_button, time_button;
    ImageView imageView;
    EditText date, time,title,description,startingBid;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Uri selectedImage;
    private String imageUri;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_bid);
        //buttons initialization
        load_image_button = findViewById(R.id.load_image_new_bid);
        register_button = findViewById(R.id.register_button_new_bid);
        date_button = findViewById(R.id.date_button_new_bid);
        time_button = findViewById(R.id.time_button_new_bid);
        date = findViewById(R.id.ending_date_new_bid);
        //edit texts initialization
        time = findViewById(R.id.ending_time_new_bid);
        title = findViewById(R.id.bid_title_new_bid);
        description = findViewById(R.id.bid_description_new_bid);
        startingBid = findViewById(R.id.bid_starting_bid_new_bid);
        //firebase components initialization
        storage = FirebaseStorage.getInstance("gs://auction-a4883.appspot.com");
        storageReference = storage.getReference();
        database = FirebaseDatabase.getInstance("https://auction-a4883-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference("bidPosts");

        //buttons action listiners
        register_button.setOnClickListener(this);
        load_image_button.setOnClickListener(this);
        date_button.setOnClickListener(this);
        time_button.setOnClickListener(this);


    }

    //TODO : we need to make some real validation here, for now it only return true no matter what
    private boolean validation(){
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode == RESULT_OK && data != null && data.getData()!=null ){
            selectedImage = data.getData();
            imageView = findViewById(R.id.imageView_new_bid);
            imageView.setImageURI(selectedImage);
        }
    }

    private void uploadPicture() {
        final String randomKey = UUID.randomUUID().toString();
        StorageReference ref = storageReference.child("images/"+randomKey);
//        final ProgressDialog pd = new ProgressDialog(getApplicationContext());
//        pd.setTitle("loading...");
        ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setMessage("uploading bid ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100
        progressBar.show();//displays the progress bar
        ref.putFile(selectedImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //TODO: check the problem to get the image uri in the code below
                        Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                        if ( downloadUri.isSuccessful() ){
                            imageUri = downloadUri.getResult().toString();
                        } else {
                            Toast.makeText(getApplicationContext(), "Registo feito sem sucesso", Toast.LENGTH_SHORT).show();
                        }
                        registerBidPostInfo();
                        progressBar.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        progressBar.dismiss();
                        Toast.makeText(getApplicationContext(), "not saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progressPercent  = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressBar.setProgress((int) progressPercent);
                        Toast.makeText(NewBidActivity.this, progressPercent+"", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerBidPost(){
        uploadPicture();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerBidPostInfo(){
        String product_id = UUID.randomUUID().toString();
        String title_txt = title.getText().toString();
        String description_txt = description.getText().toString();
        String startingBid_txt = startingBid.getText().toString();
        if(startingBid_txt.trim() == "") startingBid_txt = "0";
        String endDate_txt = date.getText().toString();
        String endTime_txt = time.getText().toString();
        //TODO: remove this hardCode value
        String owner = "celio";
        ArrayList<Bid> bids = new ArrayList<Bid>();
        bids.add(new Bid(owner, Double.parseDouble(startingBid_txt),product_id));
        Bid_post bid_post = new Bid_post(owner,title_txt,description_txt, startingBid_txt,endDate_txt,endTime_txt, bids, imageUri);
        databaseReference.child(product_id).setValue(bid_post);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if (register_button.equals(v)) {
            if(validation()){
               // Intent intent = new Intent(NewBidActivity.this, MainActivity.class);
               // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                registerBidPost();
                //uploadPicture();
                //startActivity(intent);
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

                            date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

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

                            time.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}