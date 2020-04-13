package com.xdjd.distribution.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;

import java.util.ArrayList;

public class MyBluetoothDeviceAdapter extends BaseAdapter {

    ArrayList<BluetoothDevice> list;

    public MyBluetoothDeviceAdapter(ArrayList<BluetoothDevice> list) {
        this.list = list;
    }

    public void setData(ArrayList<BluetoothDevice> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_bluetooth, null);
        }
        TextView deviceName = (TextView) convertView
                .findViewById(R.id.device_name);
        TextView deviceAddress = (TextView) convertView
                .findViewById(R.id.device_adress);

        deviceAddress.setText(list.get(position).getAddress());
        if (list.get(position).getName() != null)
            deviceName.setText("(" + list.get(position).getName() + ")");
        return convertView;
    }

}
