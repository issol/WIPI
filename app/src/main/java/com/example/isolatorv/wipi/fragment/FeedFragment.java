package com.example.isolatorv.wipi.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.isolatorv.wipi.R;
import com.example.isolatorv.wipi.Utils.DBHelper;
import com.example.isolatorv.wipi.adapter.FeedListViewAdapter;
import com.example.isolatorv.wipi.adapter.ListViewAdapter;
import com.example.isolatorv.wipi.login.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by tlsdm on 2017-09-11.
 */

public class FeedFragment extends Fragment {

    private OnFragmentInteractionListener listener;


    public static FeedFragment newInstance(){
        return new FeedFragment();
    }


    private String userName;
    private String userEmail;
    private String petName;
    private String unique_id;


    private static final String TAG = "TcpClient";

    private boolean isConnected = false;

    private int sno;
    String mJsonString;
    private String mServerIP = null;
    private Socket mSocket = null;
    private PrintWriter mOut;
    private BufferedReader mIn;
    private Thread mReceiverThread = null;




    static final String[] LISTM_MENU = {"Sun", "Mon", "Tue" , "Wed","Thu", "Fri", "Sat"};
    FloatingActionButton fab;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab3;
    //Animations
    Animation show_fab_1;
    Animation hide_fab_1;
    Animation show_fab_2;
    Animation hide_fab_2;
    Animation show_fab_3;
    Animation hide_fab_3;

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

    private DBHelper dbHelper;
    private List feeds =null;
    private List feeds1 = null;
    private List feeds2 = null;
    private List feeds3 = null;
    private List times = null;
    private List hasival=null;

    private boolean FAB_Status = false;
    private boolean feedcompiltecheck = false;
    private boolean fab1enable =false;
    private boolean fab2enable =false;
    private boolean fab3enable =false;

    private String t1 = null;
    private String t2 = null;
    private String chagedate = null;
    private int id=0;

    SharedPreferences pref;

    private ListView listview=null;
    private FeedListViewAdapter adapter=null;

    //onCreate와 같은 매서드
    /*onCreateView*********************************************************************************/
    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dogrice, container, false);
        //Floating Action Buttons
        fab = (FloatingActionButton) layout.findViewById(R.id.feed_fab);
        fab1 = (FloatingActionButton) layout.findViewById(R.id.fab_1);
        fab2 = (FloatingActionButton) layout.findViewById(R.id.fab_2);
        fab3 = (FloatingActionButton) layout.findViewById(R.id.fab_3);

        pref = getActivity().getSharedPreferences("WIPI", 0);
        sno = pref.getInt(Constants.SNO,0);
        Log.d("TAG", String.valueOf(sno));

        hasival =new ArrayList();

        Bundle extra = getArguments();
        userName = extra.getString("name");
        userEmail = extra.getString("email");
        unique_id = extra.getString("unique_id");
        sno = extra.getInt("sno");

        getData();

        adapter = new FeedListViewAdapter(getActivity());

        listview = (ListView) layout.findViewById(R.id.feed_listView);
        listview.setAdapter(adapter);
        listview.setVerticalScrollBarEnabled(false);
        listview.setDivider(null);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!fab1enable&&!fab2enable&&!fab3enable) Toast.makeText(getActivity(),"프로필에 등록을 해주세요",Toast.LENGTH_SHORT).show();
                if (FAB_Status == false) {
                    //Display FAB menu
                    expandFAB();
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    FAB_Status = false;
                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=1;
                feeds =dbHelper.getFeedData(id,String.valueOf(doDayOfWeek()));
                if(feeds.size()==0)dayDistinction(diffOfDate(t1,t2));
                createList();
                if (FAB_Status == false) {
                    //Display FAB menu
                    expandFAB();
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    FAB_Status = false;
                }
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=2;
                feeds =dbHelper.getFeedData(id,doDayOfWeek());
                if(feeds.size()==0)dayDistinction(diffOfDate(t1,t2));
                createList();
                if (FAB_Status == false) {
                    //Display FAB menu
                    expandFAB();
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    FAB_Status = false;
                }
            }

        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=3;
                feeds =dbHelper.getFeedData(id,String.valueOf(doDayOfWeek()));
                if(feeds.size()==0)dayDistinction(diffOfDate(t1,t2));
                createList();
                if (FAB_Status == false) {
                    //Display FAB menu
                    expandFAB();
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    FAB_Status = false;
                }
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s =((TextView)view.findViewById(R.id.list_item1)).getText().toString();
                Log.d(TAG,"lsitviewclick : "+s);
                feeds = dbHelper.getFeedData(id,s);
                String _id=null, push =null ,name=null, date=null, count=null, day=null;
                try{
                    _id = feeds.get(0).toString();
                    push = feeds.get(1).toString();
                    name = feeds.get(2).toString();
                    date = feeds.get(3).toString();
                    day = feeds.get(4).toString();
                    count = feeds.get(5).toString();

                    Log.d(TAG, String.valueOf(hasival.size()));
                    showErrorDialog(name, date, count,day);
                }catch (Exception e){
                    Toast.makeText(getActivity(),"데이터 없음",Toast.LENGTH_LONG).show();
                }
            }
        });


        listview = (ListView) layout.findViewById(R.id.feed_listView);



        listview.setAdapter(adapter);
        listview.setDivider(null);




        Button sendButton = (Button) layout.findViewById(R.id.Feed_send_btn);

        sendButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                new Thread(new ConnectThread("192.168.0.3", 80)).start();
                if (!isConnected) Log.d(TAG,"서버에접속된 후 사용가능");
                else {
                    new Thread(new SenderThread("aa")).start();
                    Log.d(TAG, "데이터 전송" +
                            "");
                }
                Log.d(TAG,doDayOfWeek());
                feeds = dbHelper.getFeedData(id,doDayOfWeek());
                Log.d(TAG,"밥주기 feeds data size : "+feeds.size());
                String day =null,count=null;
                try{
                    day = feeds.get(4).toString();
                    count = feeds.get(5).toString();

                    switch (count){
                        case "0": dbHelper.upDatefeed(id,1,doDayOfWeek());
                            Toast.makeText(getActivity(),"밥주기 1회 완료",Toast.LENGTH_SHORT).show();
                            break;
                        case "1":dbHelper.upDatefeed(id,2,doDayOfWeek());
                            Toast.makeText(getActivity(),"밥주기 2회 완료",Toast.LENGTH_SHORT).show();
                            break;
                        case"2":dbHelper.upDatefeed(id,3,doDayOfWeek());
                            dbHelper.updatefeed2(id,"o",doDayOfWeek());
                            createList();
                            Toast.makeText(getActivity(),"밥주기 3회 완료",Toast.LENGTH_SHORT).show();
                            break;
                        case"3":
                            Toast.makeText(getActivity(),"금일 밥주기를 초과 하였습니다.",Toast.LENGTH_SHORT).show();

                            break;
                    }
                }catch(Exception e){
                    Log.d(TAG,e.getMessage());
                    Toast.makeText(getActivity(),"데이터 없음",Toast.LENGTH_LONG).show();
                }
            }
        });


        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.feed_title);

        return layout;
    }
    /*onCreateView*********************************************************************************/




    //생성될때 초기화 UI는 안됨
    /*onCreate*************************************************************************************/
    @Override
    public void onCreate(Bundle savedInstance){super.onCreate(savedInstance);}
    /*onCreate*************************************************************************************/

    //플래그먼트가 액티비티에 붙을때 호출
    /*onAttach*************************************************************************************/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }


    }
    /*onAttach*************************************************************************************/

    //플래그먼트가 액티비티에 떨어질 때 호출
    /*onStop***************************************************************************************/
    @Override
    public void onStop() {
        super.onStop();
        FAB_Status = false;
    }
    /*onStop***************************************************************************************/

    //플래그먼트가 액티비티에 떨어질 때 호출
    /*onDetach*************************************************************************************/
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        FAB_Status = false;
    }
    /*onDetach*************************************************************************************/

    public interface OnFragmentInteractionListener {
    }

    //기본생성자
    /*FeedFragment*********************************************************************************/
    public FeedFragment(){

    }
    /*FeedFragment*********************************************************************************/

    //플래그먼트가 제거될때 호출
    /*onDestroy************************************************************************************/
    @Override
    public void onDestroy() {
        super.onDestroy();

        isConnected = false;
    }
    /*onDestroy************************************************************************************/

    //하드웨어 연결 하는 메서드
    /*ConnectThread********************************************************************************/
    private class ConnectThread implements Runnable {

        private String serverIP;
        private int serverPort;

        ConnectThread(String ip, int port) {
            serverIP = ip;
            serverPort = port;

            Log.d(TAG,"connecting to "+serverIP+".......");
            }

            @Override
            public void run() {

                try {

                mSocket = new Socket(serverIP, serverPort);
                //ReceiverThread: java.net.SocketTimeoutException: Read timed out 때문에 주석처리
                //mSocket.setSoTimeout(3000);

                mServerIP = mSocket.getRemoteSocketAddress().toString();

            } catch( UnknownHostException e )
            {
                Log.d(TAG,  "ConnectThread: can't find host");
            }
            catch( SocketTimeoutException e )
            {
                Log.d(TAG, "ConnectThread: timeout");
            }
            catch (Exception e) {

                Log.e(TAG, ("ConnectThread:" + e.getMessage()));
            }


            if (mSocket != null) {

                try {

                    mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream(), "UTF-8")), true);
                    mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "UTF-8"));

                    isConnected = true;
                } catch (IOException e) {

                    Log.e(TAG, ("ConnectThread:" + e.getMessage()));
                }
            }

            try{
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        if (isConnected) {

                            Log.d(TAG, "connected to " + serverIP);
                            mReceiverThread = new Thread(new ReceiverThread());
                            mReceiverThread.start();
                        }else{

                            Log.d(TAG, "failed to connect to server " + serverIP);
                        }

                    }
                });
            }catch(Exception e){
                    e.getMessage();
                }

        }
    }
    /*ConnectThread********************************************************************************/

    //하드웨어 데이터 보내는 메서드
    /*SenderThread*********************************************************************************/
    private class SenderThread implements Runnable {

        private String msg;

        SenderThread(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {

            mOut.println(this.msg);
            mOut.flush();


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "send message: " + msg);
                }
            });

    getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "send message: " + msg);
        }
    });
}
    }
    /*SenderThread*********************************************************************************/

    //하드웨어 데이터 받는 메서드
    /*ReceiverThread*******************************************************************************/
    private class ReceiverThread implements Runnable {

        @Override
        public void run() {

            try {

                while (isConnected) {

                    if ( mIn ==  null ) {

                        Log.d(TAG, "ReceiverThread: mIn is null");
                        break;
                    }

                    final String recvMessage =  mIn.readLine();

                    if (recvMessage != null) {

                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                Log.d(TAG, "recv message: "+recvMessage);
                            }
                        });
                    }
                }

                Log.d(TAG, "ReceiverThread: thread has exited");
                    if (mOut != null) {
                        mOut.flush();
                    mOut.close();
                }

                mIn = null;
                mOut = null;

                if (mSocket != null) {
                    try {
                        mSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (IOException e) {

                Log.e(TAG, "ReceiverThread: "+ e);
            }
        }

    }
    /*ReceiverThread*******************************************************************************/

    //하드웨어 연결 에러창
    /*showErrorDialog******************************************************************************/
    public void showErrorDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Error");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    /*showErrorDialog******************************************************************************/

    private void setInit(){
        try{
            //Animations
            show_fab_1 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab1_show);
            hide_fab_1 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab1_hide);
            show_fab_2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab2_show);
            hide_fab_2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab2_hide);
            show_fab_3 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab3_show);
            hide_fab_3 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab3_hide);

            dbHelper = new DBHelper(getActivity(), "myDb", null, 1);
            dbHelper.testDB();


            if(hasival.size()!=0){
                if(hasival.size()==4||hasival.size()==8||hasival.size()==12)id=1;

                if(hasival.size()==4){
                    fab1enable =true;
                }
                if(hasival.size()==8){
                    fab1enable =true;
                    fab2enable =true;
                }
                if(hasival.size()==12){
                    fab1enable =true;
                    fab2enable =true;
                    fab3enable =true;
                }
            }

        }catch (Exception e){
            e.getMessage();
        }

    }

    private void expandFAB() {

        //Floating Action Button 1
        if(fab1enable){
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
            layoutParams.rightMargin += (int) (fab1.getWidth() * 0.2);
            layoutParams.bottomMargin += (int) (fab1.getHeight() * 1.5);
            fab1.setLayoutParams(layoutParams);
            fab1.startAnimation(show_fab_1);
            fab1.setClickable(true);
        }

        if(fab2enable){
            //Floating Action Button 2
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
            layoutParams2.rightMargin += (int) (fab2.getWidth() * 0.2);
            layoutParams2.bottomMargin += (int) (fab2.getHeight() * 2.7);
            fab2.setLayoutParams(layoutParams2);
            fab2.startAnimation(show_fab_2);
            fab2.setClickable(true);
        }

        if(fab3enable){
            //Floating Action Button 3
            FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fab3.getLayoutParams();
            layoutParams3.rightMargin += (int) (fab3.getWidth() * 0.2);
            layoutParams3.bottomMargin += (int) (fab3.getHeight() * 3.9);
            fab3.setLayoutParams(layoutParams3);
            fab3.startAnimation(show_fab_3);
            fab3.setClickable(true);
        }

    }

    private void hideFAB() {
        if(fab1enable){
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
            layoutParams.rightMargin -= (int) (fab1.getWidth() * 0.2);
            layoutParams.bottomMargin -= (int) (fab1.getHeight() * 1.5);
            fab1.setLayoutParams(layoutParams);
            fab1.startAnimation(hide_fab_1);
            fab1.setClickable(false);
            //Floating Action Button 1
        }

        if(fab2enable){
            //Floating Action Button 2
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
            layoutParams2.rightMargin -= (int) (fab2.getWidth() * 0.2);
            layoutParams2.bottomMargin -= (int) (fab2.getHeight() * 2.7);
            fab2.setLayoutParams(layoutParams2);
            fab2.startAnimation(hide_fab_2);
            fab2.setClickable(false);
        }

        if(fab3enable){
            //Floating Action Button 3
            FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fab3.getLayoutParams();
            layoutParams3.rightMargin -= (int) (fab3.getWidth() * 0.2);
            layoutParams3.bottomMargin -= (int) (fab3.getHeight() * 3.9);
            fab3.setLayoutParams(layoutParams3);
            fab3.startAnimation(hide_fab_3);
            fab3.setClickable(false);
        }

    }

    private void db() {

        times = dbHelper.getAllTimeData();
        if (times.size() < 2) {
            dbHelper.addTime(getTime());
            dbHelper.addTime(getTime());
            times =dbHelper.getAllTimeData();
        }

        if (times.size() == 2) {
            dbHelper.upDatetime(getTime(), 2);
            times = dbHelper.getAllTimeData();
        }

        if (times.size() == 2) {
            for (int i = 0; times.size() > i; i++) {
                if(i==0)
                    t1 = times.get(i).toString();
                else
                    t2 = "2017-11-21";
            }
            Log.d(TAG,"t1 : "+t1+" "+"t2 : "+t2);
            dayDistinction(diffOfDate(t1,t2));
            Log.d(TAG,"diffOfDate : "+diffOfDate(t1,t2));

        }



    }

    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        Log.d(TAG, "Mainactivity : 시간 측정 완료");
        return mFormat.format(mDate);
    }

    public void showErrorDialog(String name, String time, String count,String day) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(name);
        builder.setCancelable(false);
        builder.setMessage("요일 : "+String.valueOf(day)+ "\n"+
                "아침 : "+String.valueOf(count.equals("3")?"o":(count.equals("1")?"o":(count.equals("2")?"o":"x")))+
                " 점심 : "+String.valueOf(count.equals("3")?"o":(count.equals("2")?"o":"x"))+
                " 저녁 : "+String.valueOf(count.equals("3")?"o":"x"));



        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private String doDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        String strWeek = null;

        int nWeek = cal.get(Calendar.DAY_OF_WEEK);
        switch (nWeek) {
            case 1:
                strWeek = "Sun";
                break;
            case 2:
                strWeek = "Mon";
                break;
            case 3:
                strWeek = "Tue";
                break;
            case 4:
                strWeek = "Wed";
                break;
            case 5:
                strWeek = "Thu";
                break;
            case 6:
                strWeek = "Fri";
                break;
            case 7:
                strWeek = "Sat";
                break;
        }
        return strWeek;
    }

    public static long diffOfDate(String begin, String end) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = null;
        Date endDate = null;
        long diff;
        try {
            beginDate = format.parse(begin);
            endDate = format.parse(end);
        } catch (Exception e) {
            e.printStackTrace();
        }

        diff = (beginDate.getTime() - endDate.getTime()) / (24 * 60 * 60 * 1000);

        long diffDays = Math.abs(diff);
        return diffDays;
    }

    public void dayDistinction(long day){
        feeds = dbHelper.getFeedData(id,doDayOfWeek());
        feeds1 = dbHelper.getAllFeedData(id);
        Log.d(TAG,"dayDistinciton()_feeds_size : "+feeds.size());
        Log.d(TAG,"feedAlldatasize : "+feeds1.size());
        switch((int) day){
            case 0:
                Log.d(TAG,"diffOfDate : 차이 0");
                if(feeds.size()==0){
                    dbHelper.addFeed(String.valueOf(id),"x",String.valueOf(id==3?hasival.get(8):(id==1?hasival.get(0):hasival.get(4))),getTime(),doDayOfWeek(),0);
                    Log.d(TAG,"dayDistinction실행");
                }
                break;
            case 1:
                Log.d(TAG,"diffOfDate : 차이 1");
                if(feeds.size()==0){
                    dbHelper.addFeed(String.valueOf(id),"x",String.valueOf(id==3?hasival.get(8):(id==1?hasival.get(0):hasival.get(4))),getTime(),doDayOfWeek(),0);
                    Log.d(TAG,"dayDistinction실행");
                }

                break;
            case 2:
                Log.d(TAG,"diffOfDate : 차이 2");
                if(feeds.size()==0){
                    dbHelper.addFeed(String.valueOf(id),"x",String.valueOf(id==3?hasival.get(8):(id==1?hasival.get(0):hasival.get(4))),getTime(),doDayOfWeek(),0);
                    Log.d(TAG,"dayDistinction실행");
                }
                break;
            case 3:
                Log.d(TAG,"diffOfDate : 차이 3");
                if(feeds.size()==0)
                    dbHelper.addFeed(String.valueOf(id),"x",String.valueOf(id==3?hasival.get(8):(id==1?hasival.get(0):hasival.get(4))),getTime(),doDayOfWeek(),0);
                break;
            case 4:
                Log.d(TAG,"diffOfDate : 차이 4");
                if(feeds.size()==0){
                    dbHelper.addFeed(String.valueOf(id),"x",String.valueOf(id==3?hasival.get(8):(id==1?hasival.get(0):hasival.get(4))),getTime(),doDayOfWeek(),0);
                    Log.d(TAG,"dayDistinction실행");
                }
                break;
            case 5:
                Log.d(TAG,"diffOfDate : 차이 5");
                if(feeds.size()==0){
                    dbHelper.addFeed(String.valueOf(id),"x",String.valueOf(id==3?hasival.get(8):(id==1?hasival.get(0):hasival.get(4))),getTime(),doDayOfWeek(),0);
                    Log.d(TAG,"dayDistinction실행");
                }
                break;
            case 6:
                Log.d(TAG,"diffOfDate : 차이 6");
                if(feeds.size()==0){
                    dbHelper.addFeed(String.valueOf(id),"x",String.valueOf(id==3?hasival.get(8):(id==1?hasival.get(0):hasival.get(4))),getTime(),doDayOfWeek(),0);
                    Log.d(TAG,"dayDistinction실행");
                }
                break;
            case 7 :
                Log.d(TAG,"diffOfData : 차이 7");

                if(hasival.size()==4){
                    for(int i=0;i<feeds1.size();i++)
                        dbHelper.deleteList(id);
                }
                if(hasival.size()==8){
                    for(int i=0;i<feeds1.size();i++)
                        dbHelper.deleteList(id);
                    for(int i=0;i<feeds1.size();i++)
                        dbHelper.deleteList(id+1);
                }
                if(hasival.size()==12){
                    for(int i=0;i<feeds1.size();i++)
                        dbHelper.deleteList(id);
                    for(int i=0;i<feeds1.size();i++)
                        dbHelper.deleteList(id+1);
                    for(int i=0;i<feeds1.size();i++)
                        dbHelper.deleteList(id+2);
                }

                dbHelper.upDatetime(times.get(1).toString(),0);
                feeds = dbHelper.getFeedData(id,String.valueOf(doDayOfWeek()));
                if(feeds.size()==0){
                    dbHelper.addFeed(String.valueOf(id),"x",String.valueOf(id==3?hasival.get(8):(id==1?hasival.get(0):hasival.get(4))),getTime(),doDayOfWeek(),0);
                    Log.d(TAG,"dayDistinction실행");
                }
                break;

        }
    }


    public void createList(){
        String ox="x";
        try{
            if(0!=adapter.getCount())
                for(int i=0;i<7;i++){
                    adapter.remove(adapter.getCount()-1);
                }
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }



        for(int i=0;i<7;i++){
            feeds = dbHelper.getFeedData(id,LISTM_MENU[i]);
            try{
                ox = feeds.get(1).toString();
                Log.d(TAG,"OX : "+ox);
            }catch(Exception e){
                Log.d(TAG,e.getMessage());
            }
            adapter.addItem(LISTM_MENU[i],ox);
            ox="x";
        }
    }

    private void getData() {
        class GetData extends AsyncTask<String, Void, String> {
            ProgressDialog progressDialog;
            String errorString = null;
            List<String> petNameList = new ArrayList<String>();
            List<String> petTypeList = new ArrayList<String>();
            List<String> petAgeList = new ArrayList<String>();
            List<String> petImageList = new ArrayList<String>();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                progressDialog.dismiss();

                Log.d(Constants.TAG, "response  - " + result);

                if (result == null) {

                    Log.d(Constants.TAG, errorString);
                } else {
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


                    return sb.toString().trim();


                } catch (Exception e) {
                    Log.d(Constants.TAG, "InsertData: Error ", e);
                    errorString = e.toString();

                    return null;
                }

            }

            private void showResult() {
                try {
                    JSONObject jsonobject = new JSONObject(mJsonString);
                    JSONArray jsonarray = jsonobject.getJSONArray("result");

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject item = jsonarray.getJSONObject(i);

                        if (item.getInt("sno") == sno) {
                            hasival.add(item.getString("pet_name"));
                            hasival.add(item.getString("pet_type"));
                            hasival.add(item.getString("pet_age"));
                            hasival.add(item.getString("pet_image"));

                        }

                    }

                    for(int i = 0; i<hasival.size();i++){
                        Log.d("TAG",String.valueOf(i));
                        Log.d(TAG,hasival.get(i).toString());
                    }

                    setInit();
                    db();
                    createList();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        GetData task = new GetData();
        task.execute("http://13.229.34.115/getPetInfo.php");

    }
}



