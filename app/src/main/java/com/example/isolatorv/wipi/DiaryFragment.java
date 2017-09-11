package com.example.isolatorv.wipi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by tlsdm on 2017-09-11.
 */

public class DiaryFragment extends Fragment {
    //onCreate와 같은 매서드
    /*onCreateView*********************************************************************************/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.diary,container,false);
        return layout;
    }
    /*onCreateView*********************************************************************************/
}
