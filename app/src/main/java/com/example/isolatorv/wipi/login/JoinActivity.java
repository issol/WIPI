package com.example.isolatorv.wipi.login;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.isolatorv.wipi.MainActivity;
import com.example.isolatorv.wipi.ProfileData;
import com.example.isolatorv.wipi.R;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.Arrays;


/**
 * Created by isolatorv on 2017. 9. 1..
 */

public class JoinActivity extends Activity {

    private static String TAG = "phptest_JoinActivity";

    //getJSON
    private static final String TAG_JSON = "webnautes";
    private static final String TAG_ID = "id";
    private static final String TAG_NICK = "nick";
    private static final String TAG_PASSWD = "passwd";

    //Login
    private EditText mEditTextId;
    private EditText mEditTextPasswd;
    private Button mLoginBtn;
    private String str_id;
    private String str_passwd;
    private int isLogged;


    //FaceBook
    String userName = "";
    String userId ="";
    String userGender ="";

    private SharedPreferences pref;


    //private List<String> permsissonsNeeds = Arrays.asList("email");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);


        pref = getSharedPreferences("WIPI",0);


        initFragment();


    }

    private void initFragment(){

        Fragment fragment;



        if(pref.getBoolean(Constants.IS_LOGGED_IN, false)){
            ProfileData profile = new ProfileData(pref.getInt(Constants.SNO,0),pref.getString(Constants.NAME,""),pref.getString(Constants.EMAIL,""),pref.getString(Constants.UNIQUE_ID,""));
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("userInfo", profile);

            startActivity(intent);


        }
        else {
            fragment = new LoginFragment_wipi();
            FragmentTransaction ft =  getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_frame, fragment);
            ft.commit();

        }



    }

    public static void downKeyboard(Context context, Button button) {

        InputMethodManager mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);

        mInputMethodManager.hideSoftInputFromWindow(button.getWindowToken(), 0);

    }

}
