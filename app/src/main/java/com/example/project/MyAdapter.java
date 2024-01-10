package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater = null;
    ArrayList<IngredientData> items = new ArrayList<IngredientData>();

    public MyAdapter(Context context, int simple_list_item_1, ArrayList<IngredientData> data){
        mContext = context;
        items = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public IngredientData getItem(int position) {
        return items.get(position);
    }

    public void addItem(IngredientData item){
        items.add(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mContext = parent.getContext();
        IngredientData listItem = items.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.iImg);
        TextView nameText = convertView.findViewById(R.id.iName);
        TextView dateText = convertView.findViewById(R.id.iDate);

        imageView.setImageResource(listItem.getIngredient());
        nameText.setText(listItem.getName());
        dateText.setText(listItem.getDate());

        return convertView;
    }

    // setItems 메서드 추가
    public void setItems(ArrayList<IngredientData> newItems) {
        items.clear(); // 기존 데이터를 모두 지움
        items.addAll(newItems); // 새로운 데이터를 추가
        notifyDataSetChanged(); // 어댑터에게 데이터 변경
    }
}
