package com.example.isolatorv.wipi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.isolatorv.wipi.Base.BaseFragment;

/**
 * Created by isolatorv on 2017. 9. 24..
 */
public class Option3Fragment extends BaseFragment {

    public Option3Fragment(){

    }
    public static Option3Fragment create() {
        Option3Fragment fragment = new Option3Fragment();

        return fragment;
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
