package com.example.isolatorv.wipi.diary.timeline;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.isolatorv.wipi.R;
import com.example.isolatorv.wipi.diary.FontUtils;
import com.example.isolatorv.wipi.diary.diaries.DiaryDao;
import com.example.isolatorv.wipi.diary.diaries.DiaryDto;
import com.example.isolatorv.wipi.diary.diaries.ReadDiaryDetailActivity;
import com.example.isolatorv.wipi.diary.helper.EasyDiaryActivity;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by hanjoong on 2017-07-16.
 */

public class TimelineActivity extends EasyDiaryActivity {

    private TimelineArrayAdapter mTimelineArrayAdapter;
    private List<DiaryDto> mDiaryList;

    @BindView(R.id.timelineList)
    ListView mTimelineListView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_diary);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.timeline_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FontUtils.setToolbarTypeface(toolbar, Typeface.DEFAULT);

        mDiaryList = DiaryDao.readDiary(null);
        Collections.reverse(mDiaryList);
        mTimelineArrayAdapter = new TimelineArrayAdapter(this, R.layout.list_item_diary_time_line_array_adapter, mDiaryList);
        mTimelineListView.setAdapter(mTimelineArrayAdapter);
        mTimelineListView.setSelection(mDiaryList.size() - 1);

        mTimelineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DiaryDto diaryDto = (DiaryDto)adapterView.getAdapter().getItem(i);
                Intent detailIntent = new Intent(TimelineActivity.this, ReadDiaryDetailActivity.class);
                detailIntent.putExtra("sequence", diaryDto.getSequence());
                startActivity(detailIntent);
            }
        });
    }

    public void refreshList() {
        mDiaryList.clear();
        mDiaryList.addAll(DiaryDao.readDiary(null));
        Collections.reverse(mDiaryList);
        mTimelineArrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
