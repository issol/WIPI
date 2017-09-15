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

import com.example.isolatorv.wipi.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

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


    //FaceBook
    String userName = "";
    String userId ="";
    String userGender ="";

    private SharedPreferences pref;


    //private List<String> permsissonsNeeds = Arrays.asList("email");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.login);


        pref = getPreferences(0);
        initFragment();


    }

    private void initFragment(){

        Fragment fragment;
        Intent intent;


        if(pref.getBoolean(Constants.IS_LOGGED_IN, false)){
            fragment = new ProfileFragment();

        }
        else {
            fragment = new LoginFragment_wipi();
        }

        FragmentTransaction ft =  getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();

    }

    public static void downKeyboard(Context context, Button button) {

        InputMethodManager mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);

        mInputMethodManager.hideSoftInputFromWindow(button.getWindowToken(), 0);

    }






}
