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
    private OnMyListener mOnMyListener;

    private static final String TAG = "option_example";
    Switch hos_Sc,shop_Sc;

    //Activity에 데이터 전달 인터페이스
    /*OnMyListener*/
    public interface OnMyListener{
        void onReceivedData(Object data);
    }
    /*OnMyListener*/

    //플래그먼트가 액티비티에 붙을때 호출
    /*onAttach*************************************************************************************/
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(getActivity()!=null&&getActivity() instanceof OnMyListener){
            mOnMyListener =(OnMyListener)getActivity();
        }
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

        hos_Sc = (Switch)layout.findViewById(R.id.hospital_Switch);
        shop_Sc = (Switch)layout.findViewById(R.id.dogShop_Switch);

        //병원 체크 버튼 리스너
        hos_Sc.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){

                    if(mOnMyListener !=null){
                        Log.d(TAG,"Option:sendActivity_On");
                        mOnMyListener.onReceivedData("hospitalOn");
                    }
                }else{
                    if(mOnMyListener !=null) {
                        Log.d(TAG,"Option:sendActivity_OFF");
                        mOnMyListener.onReceivedData("hospitalOFF");
                    }
                }
            }
        });

        //용품샵 체크 버튼 리스너
        shop_Sc.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    if(mOnMyListener !=null){
                        Log.d(TAG,"Option:sendActivity_On");
                        mOnMyListener.onReceivedData("shopOn");
                    }
                }else{
                    Log.d(TAG,"Option:sendActivity_OFF");
                    mOnMyListener.onReceivedData("shopOFF");
                }
            }
        });


        return layout;
    }
    /*onCreateView*********************************************************************************/


}
