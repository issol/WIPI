package com.example.isolatorv.wipi.diary.diaries;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isolatorv.wipi.R;
import com.example.isolatorv.wipi.diary.Utils.CommonUtils;
import com.example.isolatorv.wipi.diary.Utils.EasyDiaryUtils;
import com.example.isolatorv.wipi.diary.Utils.FontUtils;

import java.util.List;


public class DiaryWeatherArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> list;
    private final int layoutResourceId;

    public DiaryWeatherArrayAdapter(Context context, int layoutResourceId, List<String> list) {
        super(context, layoutResourceId, list);
        this.context = context;
        this.list = list;
        this.layoutResourceId = layoutResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return initRow(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initRow(position, convertView, parent);
    }

    private View initRow(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity)this.context).getLayoutInflater();
            row = inflater.inflate(this.layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.textView1 = ((TextView)row.findViewById(R.id.text1));
            holder.imageView1 = ((ImageView) row.findViewById(R.id.imageView1));
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }

       // initFontStyle(holder);
        float fontSize = CommonUtils.loadFloatPreference(context, "font_size", 0);
        if (fontSize > 0) {
            holder.textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        }
        EasyDiaryUtils.initWeatherView(holder.imageView1, position, true);

        holder.textView1.setText(list.get(position));

        return row;
    }

    private void initFontStyle(ViewHolder holder) {
        FontUtils.setTypeface(context, context.getAssets(), holder.textView1);
    }

    private static class ViewHolder {
        TextView textView1;
        ImageView imageView1;
    }
}
