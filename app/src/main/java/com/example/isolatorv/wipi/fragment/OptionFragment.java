package com.example.isolatorv.wipi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.example.isolatorv.wipi.R;


/**
 * Created by tlsdm on 2017-09-06.
 */

public class OptionFragment extends Fragment {


    private static final String TAG = "option_example";
    Switch hos_Sc,shop_Sc;

    //플래그먼트가 액티비티에 붙을때 호출
    /*onAttach*************************************************************************************/
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

    }
    /*onAttach*************************************************************************************/
    public static OptionFragment newInstance() {
        return new OptionFragment();
    }
    //onCreate와 같은 매서드
    /*onCreateView*********************************************************************************/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.option,container,false);


        return layout;
    }
    /*onCreateView*********************************************************************************/


}
