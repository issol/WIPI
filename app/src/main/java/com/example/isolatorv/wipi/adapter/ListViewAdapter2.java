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
import com.example.isolatorv.wipi.Utils.LisvtViewItem2;

import java.util.ArrayList;

/**
 * Created by tlsdm on 2017-11-06.
 */

public class ListViewAdapter2 extends BaseAdapter{
    private ArrayList<LisvtViewItem2> listViewItems = new ArrayList<LisvtViewItem2>();
    Context context;

    public ListViewAdapter2(Context context){
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
            convertView = inflater.inflate(R.layout.listview_item2,parent,false);
        }

        TextView day = (TextView) convertView.findViewById(R.id.list_item1);
        TextView  Ox = (TextView) convertView.findViewById(R.id.list_item2);

        LisvtViewItem2 listViewItem = listViewItems.get(position);

        day.setText(listViewItem.getDay());
        Ox.setText(listViewItem.getOx());
        return convertView;
    }

    public void addItem(String day,String ox){
        LisvtViewItem2 item = new LisvtViewItem2();

        item.setDay(day);
        item.setOx(ox);

        listViewItems.add(item);
    }
}