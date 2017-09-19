package com.example.isolatorv.wipi.diary.timeline;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isolatorv.wipi.R;
import com.example.isolatorv.wipi.diary.CommonUtils;
import com.example.isolatorv.wipi.diary.DateUtils;
import com.example.isolatorv.wipi.diary.EasyDiaryUtils;
import com.example.isolatorv.wipi.diary.FontUtils;
import com.example.isolatorv.wipi.diary.diaries.DiaryDao;
import com.example.isolatorv.wipi.diary.diaries.DiaryDto;

import org.apache.commons.lang3.StringUtils;

import java.util.List;



/**
 * Created by hanjoong on 2017-07-16.
 */

public class TimelineArrayAdapter extends ArrayAdapter<DiaryDto> {

    private final Context context;
    private final List<DiaryDto> list;
    private final int layoutResourceId;

    public TimelineArrayAdapter(@NonNull Context context, @LayoutRes int layoutResourceId, @NonNull List<DiaryDto> list) {
        super(context, layoutResourceId, list);
        this.context = context;
        this.list = list;
        this.layoutResourceId = layoutResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity)this.context).getLayoutInflater();
            row = inflater.inflate(this.layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.textView1 = (TextView) row.findViewById(R.id.text1);
            holder.textView2 = (TextView) row.findViewById(R.id.text2);
            holder.title = (TextView) row.findViewById(R.id.title);
            holder.horizontalLine1 = row.findViewById(R.id.horizontalLine1);
            holder.horizontalLine2 = row.findViewById(R.id.horizontalLine2);
            holder.titleContainer = (ViewGroup) row.findViewById(R.id.titleContainer);
            holder.weather = (ImageView) row.findViewById(R.id.weather);
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }

        initFontStyle(holder);
        float fontSize = CommonUtils.loadFloatPreference(context, "font_size", 0);
        if (fontSize > 0) {
            holder.textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
            holder.textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
            holder.title.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        }

        DiaryDto diaryDto = list.get(position);
        if (position > 0 && StringUtils.equals(diaryDto.getDateString(), list.get(position - 1).getDateString())) {
            holder.titleContainer.setVisibility(View.GONE);
            holder.weather.setImageResource(0);
        } else {
//            holder.title.setText(diaryDto.getDateString() + " " + DateUtils.timeMillisToDateTime(diaryDto.getCurrentTimeMillis(), "EEEE"));
            holder.title.setText(DateUtils.getFullPatternDate(diaryDto.getCurrentTimeMillis()));
            holder.titleContainer.setVisibility(View.VISIBLE);
            // 현재 날짜의 목록을 조회
            List<DiaryDto> mDiaryList = DiaryDao.readDiaryByDateString(diaryDto.getDateString());
            boolean initWeather = false;
            if (mDiaryList.size() > 0) {
                for (DiaryDto temp : mDiaryList) {
                    if (temp.getWeather() > 0) {
                        initWeather = true;
                        EasyDiaryUtils.initWeatherView(holder.weather, temp.getWeather());
                        break;
                    }
                }
                if (!initWeather) {
                    holder.weather.setVisibility(View.GONE);
                    holder.weather.setImageResource(0);
                }
            } else {
                holder.weather.setVisibility(View.GONE);
                holder.weather.setImageResource(0);
            }
        }

        if (position % 2 == 0) {
            holder.textView1.setVisibility(View.VISIBLE);
            holder.textView2.setVisibility(View.INVISIBLE);
            holder.horizontalLine1.setVisibility(View.VISIBLE);
            holder.horizontalLine2.setVisibility(View.INVISIBLE);
            holder.textView1.setText(DateUtils.timeMillisToDateTime(diaryDto.getCurrentTimeMillis(), DateUtils.TIME_PATTERN) + "\n" + diaryDto.getTitle());
//            holder.textView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            holder.textView1.setVisibility(View.INVISIBLE);
            holder.textView2.setVisibility(View.VISIBLE);
            holder.horizontalLine1.setVisibility(View.INVISIBLE);
            holder.horizontalLine2.setVisibility(View.VISIBLE);
            holder.textView2.setText(DateUtils.timeMillisToDateTime(diaryDto.getCurrentTimeMillis(), DateUtils.TIME_PATTERN) + "\n" + diaryDto.getTitle());
//            holder.textView2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }


        return row;
    }

    private void initFontStyle(ViewHolder holder) {
        FontUtils.setTypeface(context, context.getAssets(), holder.textView1);
        FontUtils.setTypeface(context, context.getAssets(), holder.textView2);
        FontUtils.setTypeface(context, context.getAssets(), holder.title);
    }

    private static class ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView title;
        View horizontalLine1;
        View horizontalLine2;
        ViewGroup titleContainer;
        ImageView weather;
    }
}
