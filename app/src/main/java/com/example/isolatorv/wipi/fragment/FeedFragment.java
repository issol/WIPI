package com.example.isolatorv.wipi.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;


import com.example.isolatorv.wipi.R;
import com.example.isolatorv.wipi.adapter.FeedListViewAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by tlsdm on 2017-09-11.
 */

public class FeedFragment extends Fragment {

    private OnFragmentInteractionListener listener;

    public FeedFragment(){

    }
    public static FeedFragment newInstance(){
        return new FeedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstance){super.onCreate(savedInstance);}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {
    }




    private static final String TAG = "TcpClient";
    private boolean isConnected = false;

    private String mServerIP = null;
    private Socket mSocket = null;
    private PrintWriter mOut;
    private BufferedReader mIn;
    private Thread mReceiverThread = null;
    ListView listview;
    FeedListViewAdapter adapter;

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dogrice, container, false);



        adapter = new FeedListViewAdapter(getActivity());

        listview = (ListView) layout.findViewById(R.id.feed_listView);

        // 첫 번째 아이템 추가.
        adapter.addItem("Sun","x");
        // 첫 번째 아이템 추가.
        adapter.addItem("Mon","o");
        // 첫 번째 아이템 추가.
        adapter.addItem("Tue","o");
        // 첫 번째 아이템 추가.
        adapter.addItem("Wed","o");
        // 첫 번째 아이템 추가.
        adapter.addItem("Thu","o");
        // 첫 번째 아이템 추가.
        adapter.addItem("Fri","o");
        // 첫 번째 아이템 추가.
        adapter.addItem("Sat","o");

        listview.setAdapter(adapter);
        listview.setDivider(null);




        Button sendButton = (Button) layout.findViewById(R.id.Feed_send_btn);

        sendButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new Thread(new ConnectThread("192.168.0.13", 80)).start();

                if (!isConnected) showErrorDialog("서버로 접속된후 다시 해보세요.");
                else {
                    new Thread(new SenderThread("aa")).start();
                    //Log.d(TAG, mInputEditText.getText().toString());

                }
            }
        });


        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.feed_title);
        return layout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        isConnected = false;
    }

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
        }
    }

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

}



