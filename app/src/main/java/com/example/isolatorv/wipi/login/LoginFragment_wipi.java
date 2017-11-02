package com.example.isolatorv.wipi.login;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.isolatorv.wipi.MainActivity;
import com.example.isolatorv.wipi.ProfileData;
import com.example.isolatorv.wipi.R;
import com.example.isolatorv.wipi.RequestHandler;
import com.example.isolatorv.wipi.SetLoginHandler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


/**
 * Created by isolatorv on 2017. 9. 10..
 */


public class LoginFragment_wipi extends Fragment implements View.OnClickListener{

    private AppCompatButton btn_login;
    private EditText et_email,et_password;
    private TextView tv_register;
    private ProgressBar progress;
    private SharedPreferences pref;

    public static final String UPLOAD_URL = "http://13.229.34.115/setLogined.php";

    private String unique_id;

    private int isLogined;
    private int sno;
    private String test;

    String mJsonString;

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public int getIsLogined() {
        return isLogined;
    }

    public void setIsLogined(int isLogined) {
        this.isLogined = isLogined;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login,container,false);


        initViews(view);
        return view;


    }


    private void initViews(View view) {

        //pref = getActivity().getPreferences(0);
        pref = getActivity().getSharedPreferences("WIPI",0);

        btn_login = (AppCompatButton) view.findViewById(R.id.btn_login);
        tv_register = (TextView) view.findViewById(R.id.tv_register);
        et_email = (EditText) view.findViewById(R.id.et_email);
        et_password = (EditText) view.findViewById(R.id.et_password);

        progress = (ProgressBar) view.findViewById(R.id.progress);

        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.tv_register:
                goToRegister();
                break;

            case R.id.btn_login:
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()) {

                    progress.setVisibility(View.VISIBLE);
                    hideKeyboard();

                    loginProcess(email,password);
                    Log.d("TAGDDFEDD", String.valueOf(isLogined));

                } else {

                    Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                }
                break;


        }

    }
    private void loginProcess(String email,String password){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.LOGIN_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                if(resp.getResult().equals(Constants.SUCCESS)){
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(Constants.IS_LOGGED_IN,true);
                    editor.putString(Constants.EMAIL,resp.getUser().getEmail());
                    editor.putString(Constants.NAME,resp.getUser().getName());
                    editor.putString(Constants.UNIQUE_ID,resp.getUser().getUnique_id());

                    unique_id = resp.getUser().getUnique_id();


                    setLogin(unique_id);

                    getData(unique_id);

                    Log.d("TTTTTAAAAG", String.valueOf(isLogined));
                    Log.d("TTTTTAAAA@", String.valueOf(sno));
                    editor.putInt(Constants.SNO, sno);
                    editor.apply();
                    if(isLogined == 1){
                        ProfileData profile = new ProfileData(sno,resp.getUser().getName(),resp.getUser().getEmail(),resp.getUser().getUnique_id());
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("userInfo", profile);

                        startActivity(intent);

                    }else if(isLogined == 0){
                        ProfileData profile = new ProfileData(resp.getUser().getName(),resp.getUser().getEmail(),resp.getUser().getUnique_id());
                        Intent intent = new Intent(getActivity(), RegisterPet.class);
                        intent.putExtra("userInfo", profile);

                        startActivity(intent);
                    }


                }
                progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                progress.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG,"failed");
                Log.d(Constants.TAG, t.getLocalizedMessage());
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

    private void goToRegister(){

        Fragment register = new RegisterFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,register);
        ft.commit();
    }

    private void goToProfile(){

        Fragment profile = new ProfileFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,profile);
        ft.commit();
    }
    private void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_email.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(et_password.getWindowToken(), 0);

    }

    private void setLogin(final String unique_id){
        String uniq = unique_id;
        class SetLogin extends AsyncTask<String,Void,String> {

            ProgressDialog loading;
            SetLoginHandler rh = new SetLoginHandler();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                String uniqueId = params[0];
                Log.d("TAG3", uniqueId);
                String result = rh.sendPostRequest(UPLOAD_URL,uniqueId);

                return result;
            }
        }

        SetLogin ui = new SetLogin();
        ui.execute(uniq);
    }

    private void getData(String unique_id) {

        String uniq = unique_id;

        class GetData extends AsyncTask<String, Void, Integer> {
            ProgressDialog progressDialog;
            String errorString = null;
            String uniq;
            int userLogined;
            int sno;

            public int getSno() {
                return sno;
            }

            public int getUserLogined() {
                return this.userLogined;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);

                progressDialog.dismiss();

                Log.d(Constants.TAG, "response  - " + result);

                if (result == null) {

                    Log.d(Constants.TAG, errorString);
                } else {
                   // mJsonString = result;
                  //  showResult();


                }
            }

            @Override
            protected Integer doInBackground(String... params) {

                String serverURL = params[0];
                uniq = params[1];

                try {

                    URL url = new URL(serverURL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.connect();


                    int responseStatusCode = httpURLConnection.getResponseCode();
                    Log.d(Constants.TAG, "response code - " + responseStatusCode);

                    InputStream inputStream;
                    if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                        inputStream = httpURLConnection.getInputStream();
                    } else {
                        inputStream = httpURLConnection.getErrorStream();
                    }


                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }

                    bufferedReader.close();

                    try {
                        JSONObject jsonobject = new JSONObject(sb.toString().trim());
                        JSONArray jsonarray = jsonobject.getJSONArray("result");

                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject item = jsonarray.getJSONObject(i);
                            if (item.getString("unique_id").equals(uniq)) {
                                userLogined = item.getInt("user_logined");
                                sno = item.getInt("sno");



                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    return userLogined;


                } catch (Exception e) {
                    Log.d(Constants.TAG, "InsertData: Error ", e);
                    errorString = e.toString();

                    return null;
                }

            }


        }
        GetData ui = new GetData();
        ui.execute("http://13.229.34.115/getSno.php",uniq);
        try {
            isLogined= ui.get();
            sno = ui.getSno();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


}