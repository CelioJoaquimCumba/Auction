package mz.ac.isutc.i33.auction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import mz.ac.isutc.i33.auction.controllers.InternetController;
import mz.ac.isutc.i33.auction.models.Bid.Bid;
import mz.ac.isutc.i33.auction.models.Bid.Bid_post;

public class NewBidActivity extends AppCompatActivity implements View.OnClickListener{
    Button load_image_button, register_button, date_button, time_button;
    ImageView imageView;
    EditText end_date, end_time,title,description,startingBid;
    String username;
    TextInputLayout title_TIL, description_TIL, starting_bid_TIL, end_date_TIL, end_time_TIL, image_load_TIL;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Uri selectedImage;
    private String imageUri;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    Object system_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            username = extras.getString(MainActivity.USERNAME);
        } else{
            username = "unknown";
        }
        selectedImage = null;

        setContentView(R.layout.activity_new_bid);
        //image view initialization

        imageView = findViewById(R.id.imageView_new_bid);

        //buttons initialization
        load_image_button = findViewById(R.id.load_image_new_bid);
        register_button = findViewById(R.id.register_button_new_bid);
        date_button = findViewById(R.id.date_button_new_bid);
        time_button = findViewById(R.id.time_button_new_bid);


        title_TIL = findViewById(R.id.bid_title_text_input_new_bid);
        description_TIL = findViewById(R.id.bid_description_text_input_new_bid);
        starting_bid_TIL = findViewById(R.id.bid_starting_bid_text_input_new_bid);
        end_date_TIL = findViewById(R.id.ending_date_text_input_new_bid);
        end_time_TIL = findViewById(R.id.ending_time_text_input_new_bid);
        image_load_TIL = findViewById(R.id.bid_load_image_text_input_new_bid);


        //edit texts initialization
        end_date = findViewById(R.id.ending_date_new_bid);
        end_time = findViewById(R.id.ending_time_new_bid);
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
        system_service = getSystemService(Context.CONNECTIVITY_SERVICE);
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator_layout_new_bid);
        InternetController.getInstance().alertDisconnection(coordinatorLayout, system_service);

    }

    //TODO : we need to make some real validation here, for now it only return true no matter what
    //NOTE : it validates internet conection before publishing the bid
    private boolean validation(){


        String title_txt = title.getText().toString();
        String description_txt = description.getText().toString();
        String starting_bid_txt = startingBid.getText().toString();
        String end_date_txt = end_date.getText().toString();
        String end_time_txt = end_time.getText().toString();
        boolean valid = true;
        final int TOO_SHORT = 8;
        final int TOO_LONG_TITLE = 20;
        final int TOO_LONG_DESCRIPTION = 158;
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator_layout_new_bid);

        title_TIL.setError(null);
        description_TIL.setError(null);
        starting_bid_TIL.setError(null);
        end_date_TIL.setError(null);
        end_time_TIL.setError(null);
        image_load_TIL.setError(null);

        //Validating connection to internet.
        if (!InternetController.getInstance().checkConnection(coordinatorLayout)){
            valid = false;
            system_service = getSystemService(Context.CONNECTIVITY_SERVICE);
            InternetController.getInstance().alertDisconnection(coordinatorLayout, system_service);
        }

        if( title_txt.trim().equals("") ){
            title_TIL.setError("Title is empty");
            valid = false;
        }else if( title_txt.length() < TOO_SHORT ){
            title_TIL.setError("title is too short(need more than "+TOO_SHORT+" characters)");
            valid = false;
        }else if(title_txt.length() > TOO_LONG_TITLE ){
            title_TIL.setError("title is too long (need less than "+TOO_LONG_TITLE+" characters)");
        }
        if( description_txt.trim().equals("") ){
            description_TIL.setError("desription is empty");
            valid = false;
        }else if ( description_txt.length() < TOO_SHORT ){
            description_TIL.setError("description is too short(need more than "+TOO_SHORT+" characters)");
            valid = false;
        }else if( description_txt.length() > TOO_LONG_DESCRIPTION ){
            description_TIL.setError("description is too long(need more than "+TOO_LONG_DESCRIPTION+" characters)");
            valid = false;
        }
        if( starting_bid_txt.trim().equals("") ){
            starting_bid_TIL.setError("starting bid is empty");
            valid = false;
        }else if ( !numberIsValid(starting_bid_txt) ){
            starting_bid_TIL.setError("starting bid must be a number");
            valid = false;
        }else if (starting_bid_txt.length() > 12 ){
            starting_bid_TIL.setError("Damn that's alot of money, give ur dad's card back!");
            valid = false;
        }

        if (end_date_txt.trim().equals("")) {
            end_date_TIL.setError("date is empty");
            valid = false;
        }else if ( !dateIsValid(end_date_txt)) {
            end_date_TIL.setError("date is invalid.(set date to be greater or equal to today)");
            valid = false;
        }
        if(end_time_txt.trim().equals("")){
            end_time_TIL.setError("time is empty");
            valid = false;
        }else if ( !timeIsValid(end_time_txt, end_date_txt) && !end_date_txt.trim().equals("") ){
            end_time_TIL.setError("time is invalid.(if date is today, set time greater than now");
            valid =false;
        }
         if (selectedImage == null){
             image_load_TIL.setError("Select an image");
            valid = false;
        }




        return valid;
    }
    //TODO: make time validation
    private boolean timeIsValid(String time, String date){
        Date date_now = new Date();
        Date date_comparing;
        try {
            SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy HH:mm");
            date_comparing = formatter.parse(date+ " "+time);
        }catch (Exception e){
            return false;
        }
        if( date_comparing.compareTo(date_now) <= 0 ){
            return false;
        }

        return true;
    }
    //TODO: make date validation
    private boolean dateIsValid(String date){
        Date date_now = new Date();
        Date date_comparing;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date_holder = dateFormat.format(date_now);
        try {
            date_now = dateFormat.parse(date_holder);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        try {
            SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
            date_comparing = formatter.parse(date);
        }catch (Exception e){
            return false;
        }
        if( date_now.compareTo(date_comparing) > 0 ){
            return false;
        }

        return true;
    }
    private boolean numberIsValid(String number){
        Double nr;
        try {
            nr = Double.parseDouble(number);
        }catch (NumberFormatException e ){
            return false;
        }


        return nr>=0;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode == RESULT_OK && data != null && data.getData()!=null ){
            selectedImage = data.getData();

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
                        //TODO:Ask professor if it is okay to do this
                        while(! downloadUri.isSuccessful() ){

                        }
                        if ( downloadUri.isSuccessful() ){
                            imageUri = downloadUri.getResult().toString();
                        } else {
                            Toast.makeText(getApplicationContext(), "Registo feito sem sucesso", Toast.LENGTH_SHORT).show();
                        }
                        registerBidPostInfo();
                        progressBar.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Registro do artigo feito com sucesso", Snackbar.LENGTH_LONG).show();
                        Intent intent = new Intent(NewBidActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
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
        String endDate_txt = end_date.getText().toString();
        String endTime_txt = end_time.getText().toString();
        String owner = username;
        ArrayList<Bid> bids = new ArrayList<Bid>();
        bids.add(new Bid(owner, Double.parseDouble(startingBid_txt),product_id));
        Bid_post bid_post = new Bid_post(product_id,owner,title_txt,description_txt, startingBid_txt,endDate_txt,endTime_txt, bids, imageUri);
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

                            end_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

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

                            end_time.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}