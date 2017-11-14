package com.example.isolatorv.wipi.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.isolatorv.wipi.ListViewItem;
import com.example.isolatorv.wipi.R;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by isolatorv on 2017. 11. 2..
 */

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
    Context context;


    public ListViewAdapter(Context context){
        this.context = context;

    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return listViewItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        final Context context = viewGroup.getContext();

        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_item, viewGroup, false);
        }

        ImageView pet_image= (ImageView) view.findViewById(R.id.petImage);
        TextView pet_name = (TextView)view.findViewById(R.id.petName);
        TextView pet_type = (TextView)view.findViewById(R.id.petType);
        TextView pet_age = (TextView)view.findViewById(R.id.petAge);
        TextView pet_weight = (TextView)view.findViewById(R.id.petWeight);

        ListViewItem listViewItem = listViewItemList.get(i);

        Glide.with(context)
                .load(listViewItemList.get(i).getPet_image())
                .centerCrop()
                .crossFade()
                .bitmapTransform(new CropCircleTransformation(context))
                .override(90,90)
                .placeholder(R.drawable.edit)
                .into(pet_image);

        pet_name.setText(listViewItem.getPet_name());
        pet_type.setText(listViewItem.getPet_type());
        pet_age.setText(listViewItem.getPet_age());
        pet_weight.setText(listViewItem.getPet_size());

        return view;
    }

    public void addItem(String pet_image, String pet_name, String pet_type, String pet_age, String pet_size) {
        ListViewItem item = new ListViewItem();

        item.setPet_image(pet_image);
        item.setPet_name(pet_name);
        item.setPet_type(pet_type);
        item.setPet_age(pet_age);
        item.setPet_size(pet_size);


        listViewItemList.add(item);
    }



}
