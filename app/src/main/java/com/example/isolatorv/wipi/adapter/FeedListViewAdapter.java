package com.example.isolatorv.wipi.adapter;

/**
 * Created by tlsdm on 2017-11-06.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.isolatorv.wipi.R;
import com.example.isolatorv.wipi.Utils.FeedLisvtViewItem;

import java.util.ArrayList;

/**
 * Created by tlsdm on 2017-11-06.
 */

public class FeedListViewAdapter extends BaseAdapter{
    private ArrayList<FeedLisvtViewItem> listViewItems = new ArrayList<FeedLisvtViewItem>();
    Context context;

    public FeedListViewAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return listViewItems.size();
    }

    @Override
    public Object getItem(int i) {
        return listViewItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.feed_listview_item,parent,false);
        }

        TextView day = (TextView) convertView.findViewById(R.id.list_item1);
        TextView  Ox = (TextView) convertView.findViewById(R.id.list_item2);

        FeedLisvtViewItem listViewItem = listViewItems.get(position);

        day.setText(listViewItem.getDay());
        Ox.setText(listViewItem.getOx());
        return convertView;
    }

    public void addItem(String day,String ox){
        FeedLisvtViewItem item = new FeedLisvtViewItem();

        item.setDay(day);
        item.setOx(ox);

        listViewItems.add(item);
    }
}