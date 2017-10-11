package com.example.isolatorv.wipi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.isolatorv.wipi.R;

import com.example.isolatorv.wipi.diary.Utils.CommonUtils;
import com.example.isolatorv.wipi.diary.Utils.DateUtils;
import com.example.isolatorv.wipi.diary.diaries.CreateDiaryActivity;
import com.example.isolatorv.wipi.diary.diaries.DiaryCardArrayAdapter;
import com.example.isolatorv.wipi.diary.diaries.DiaryDao;
import com.example.isolatorv.wipi.diary.diaries.DiaryDto;

import com.example.isolatorv.wipi.diary.diaries.ReadDiaryDetailActivity;
import com.example.isolatorv.wipi.diary.helper.DiaryConstants;

import java.util.List;
import butterknife.ButterKnife;
import io.realm.Realm;


public class DiaryFragment extends Fragment {

    public DiaryFragment(){}

    public static DiaryFragment newInstance() {
        return new DiaryFragment();
    }

    private long mCurrentTimeMills;
    private DiaryCardArrayAdapter mDiaryCardArrayAdapter;
    private List<DiaryDto> mDiaryList;

    ListView mDiaryListView;

    FloatingActionButton mInsertDiaryButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstancesState){
        View layout= inflater.inflate(R.layout.activity_read_diary, container, false);

        mDiaryListView = (ListView) layout.findViewById(R.id.diaryList);

        mInsertDiaryButton = (FloatingActionButton) layout.findViewById(R.id.insertDiaryButton);

        ButterKnife.bind(getActivity());

        Realm.init(getActivity());


        if(getActivity().getIntent().getBooleanExtra("app_finish", false)){
            getActivity().finish();
        }

        mCurrentTimeMills = System.currentTimeMillis();

        mDiaryList = DiaryDao.readDiary(null);
        mDiaryCardArrayAdapter = new DiaryCardArrayAdapter(getActivity(), R.layout.list_item_diary_card_array_adapter, this.mDiaryList);
        mDiaryListView.setAdapter(mDiaryCardArrayAdapter);


        mInsertDiaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createDiary = new Intent(getActivity(), CreateDiaryActivity.class);
                startActivity(createDiary);
            }
        });



        bindEvent();

        return layout;
    }


    private void bindEvent(){

        mDiaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l ){
                DiaryDto diaryDto = (DiaryDto)adapterView.getAdapter().getItem(i);
                Intent detailIntent = new Intent(getActivity(), ReadDiaryDetailActivity.class);
                detailIntent.putExtra("sequence", diaryDto.getSequence());
                detailIntent.putExtra("title", diaryDto.getTitle());
                detailIntent.putExtra("contents", diaryDto.getContents());
                detailIntent.putExtra("date", DateUtils.timeMillisToDateTime(diaryDto.getCurrentTimeMillis()));
                detailIntent.putExtra("current_time_millis", diaryDto.getCurrentTimeMillis());
                detailIntent.putExtra("weather", diaryDto.getWeather());
                detailIntent.putExtra("query", mDiaryCardArrayAdapter.getCurrentQuery());
                startActivity(detailIntent);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        mDiaryList =  DiaryDao.readDiary(null);
        mDiaryCardArrayAdapter = new DiaryCardArrayAdapter(getActivity(), R.layout.list_item_diary_card_array_adapter, this.mDiaryList);
        mDiaryListView.setAdapter(mDiaryCardArrayAdapter);
        AppCompatActivity appActivity = (AppCompatActivity) getActivity();
        appActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        int previousActivity = CommonUtils.loadIntPreference(getActivity(), DiaryConstants.PREVIOUS_ACTIVITY, -1);
        if(previousActivity == DiaryConstants.PREVIOUS_ACTIVITY_CREATE){
            mDiaryListView.smoothScrollToPosition(0);
            CommonUtils.saveIntPreference(getActivity(), DiaryConstants.PREVIOUS_ACTIVITY, -1);

        }
    }
}


