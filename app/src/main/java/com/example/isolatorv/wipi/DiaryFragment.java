package com.example.isolatorv.wipi;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v4.app.Fragment;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.example.isolatorv.wipi.Base.BaseFragment;
import com.example.isolatorv.wipi.diary.CommonUtils;
import com.example.isolatorv.wipi.diary.DateUtils;
import com.example.isolatorv.wipi.diary.DialogUtils;
import com.example.isolatorv.wipi.diary.DiaryConstants;
import com.example.isolatorv.wipi.diary.FontUtils;
import com.example.isolatorv.wipi.diary.calendar.CalendarActivity;
import com.example.isolatorv.wipi.diary.diaries.CreateDiaryActivity;
import com.example.isolatorv.wipi.diary.diaries.DiaryCardArrayAdapter;
import com.example.isolatorv.wipi.diary.diaries.DiaryDao;
import com.example.isolatorv.wipi.diary.diaries.DiaryDto;

import com.example.isolatorv.wipi.diary.diaries.ReadDiaryDetailActivity;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.yalantis.phoenix.PullToRefreshView;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import retrofit2.http.POST;

/**
 * Created by tlsdm on 2017-09-11.
 */

public class DiaryFragment extends Fragment {

    public DiaryFragment(){

    }
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
        AppCompatActivity activity = (AppCompatActivity) getActivity();
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


    public void onAttach(Context context) {
        super.onAttach(context);


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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;

            case R.id.planner:
                Intent calendarIntent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(calendarIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.read_diary, menu);
        return true;
    }
}


