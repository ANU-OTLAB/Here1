package com.otlab.here;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ListViewCustomAdapter extends BaseAdapter {
        LayoutInflater inflater;
        ArrayList<SettingItem> arrayList;
        int layout;

        public ListViewCustomAdapter(Context context, int _layout, ArrayList<SettingItem> _arrayList){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = _layout;
            arrayList = _arrayList;
        }

        public int getCount(){
            return arrayList.size();
        }

        public Object getItem(int position){
            return arrayList.get(position);
        }

        public long getItemId(int position){
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = inflater.inflate(layout, parent, false);
            }

            TextView NameText = (TextView) convertView.findViewById(R.id.Name);
            NameText.setText(arrayList.get(position).getSettingName());

            TextView DistanceText = (TextView) convertView.findViewById(R.id.Distance);
            DistanceText.setText(arrayList.get(position).getDistance());

            TextView DestinationText = (TextView)convertView.findViewById(R.id.Destination);
            DestinationText.setText(arrayList.get(position).getDestination());

            TextView TimeText = (TextView)convertView.findViewById(R.id.Time);
            TimeText.setText(arrayList.get(position).getTime());

            return convertView;
        }
    }


