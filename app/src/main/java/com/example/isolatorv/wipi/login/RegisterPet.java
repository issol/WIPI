package com.example.isolatorv.wipi.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.isolatorv.wipi.CircleImageView;
import com.example.isolatorv.wipi.MainActivity;
import com.example.isolatorv.wipi.ProfileData;
import com.example.isolatorv.wipi.R;
import com.example.isolatorv.wipi.RequestHandler;
import com.example.isolatorv.wipi.SetLoginHandler;
import com.example.isolatorv.wipi.UserData;
import com.example.isolatorv.wipi.login.Constants;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by isolatorv on 2017. 10. 24..
 */

public class RegisterPet extends AppCompatActivity implements View.OnClickListener{

    public static final String UPLOAD_URL = "http://13.229.34.115/uploadtest.php";
    public static final String UPLOAD_KEY = "pet_image";
    private static final String TAG_JSON="result";


    private int PICK_IMAGE_REQUEST = 1;


    private AppCompatButton buttonUpload;
    private EditText pet_name, pet_type, pet_age,pet_weight;


    private ImageView imageView;
    private CircleImageView imagView_profile;

    private String unique_id;
    private int sno;
    private int id;
    private String p_name, p_type, p_age;
    private int p_weight;
    private String u_name, u_email;
    private int isLogined;

    private Bitmap bitmap;
    private Bitmap thumbImages;

    private Uri filePath;

    String mJsonString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        ProfileData profile = extras.getParcelable("userInfo");
        unique_id = profile.getUniqueID();
        u_name = profile.getUserName();
        u_email = profile.getUserEmail();
        Log.d(Constants.TAG, "onCreate");

        setContentView(R.layout.activity_register_pet);

        //buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (AppCompatButton) findViewById(R.id.buttonUpload);
        // buttonView = (Button) findViewById(R.id.buttonViewImage);

        //imageView = (ImageView) findViewById(R.id.imageView);
        imagView_profile = (CircleImageView) findViewById(R.id.profile_image);
        pet_name = (EditText) findViewById(R.id.pet_name);
        pet_type = (EditText) findViewById(R.id.pet_type);
        pet_age = (EditText) findViewById(R.id.pet_age);
        pet_weight = (EditText) findViewById(R.id.pet_weight);

        GetData task = new GetData();
        task.execute("http://13.229.34.115/getSno.php");

        //buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        // buttonView.setOnClickListener(this);
        imagView_profile.setOnClickListener(this);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                imagView_profile.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){

        p_name = pet_name.getText().toString();
        p_type = pet_type.getText().toString();
        p_age= pet_age.getText().toString();
        p_weight = Integer.parseInt(pet_weight.getText().toString());
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RegisterPet.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();


                data.put(UPLOAD_KEY, uploadImage);
                String result = rh.sendPostRequest(UPLOAD_URL,data, sno, p_name, p_type, p_age, p_weight);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    @Override
    public void onClick(View v) {


        if(v == buttonUpload){

            uploadImage();
            setLogin(unique_id);
            ProfileData userData = new ProfileData(sno,u_name, u_email,unique_id);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("userInfo", userData);
            startActivity(intent);


        }

       /* if(v == buttonView){
            //viewImage();
            Log.d(Constants.TAG, String.valueOf(sno));
            Glide.with(RegisterPet.this)
                    .load("http://13.229.34.115/images/"+sno+".png")
                    .centerCrop()
                    .crossFade()
                    .bitmapTransform(new CropCircleTransformation(RegisterPet.this))
                    .override(96,96)
                    .into((ImageView)imagView_profile);
        }*/
        if(v==imagView_profile){
            showFileChooser();
        }
    }


    private void setLogin(String unique_id){
        String uniq = unique_id;
        class SetLogin extends AsyncTask<String,Void,String> {

            ProgressDialog loading;
            SetLoginHandler rh = new SetLoginHandler();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RegisterPet.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

            }

            @Override
            protected String doInBackground(String... params) {
                String uniqueId = params[0];
                Log.d("TAG3", uniqueId);
                String result = rh.sendPostRequest("http://13.229.34.115/setLogined.php",uniqueId);

                return result;
            }
        }

        SetLogin ui = new SetLogin();
        ui.execute(uniq);
    }

    private class GetData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String errorString =null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(RegisterPet.this,"Please Wait",null,true,true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Log.d(Constants.TAG, "response  - " +  result);

            if (result == null){

                Log.d(Constants.TAG,errorString);
            }
            else {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(Constants.TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {
                Log.d(Constants.TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }

        private void showResult(){
            try{
                JSONObject jsonobject = new JSONObject(mJsonString);
                JSONArray jsonarray = jsonobject.getJSONArray(TAG_JSON);

                for(int i = 0; i <jsonarray.length(); i++){
                    JSONObject item = jsonarray.getJSONObject(i);

                    if(item.getString("unique_id").equals(unique_id)){
                        sno = item.getInt("sno");
                        isLogined = item.getInt("user_logined");

                        break;
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

}
