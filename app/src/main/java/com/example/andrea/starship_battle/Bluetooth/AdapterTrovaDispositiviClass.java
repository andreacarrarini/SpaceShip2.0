package com.example.andrea.starship_battle.Bluetooth;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.andrea.starship_battle.R;

import java.util.List;

public class AdapterTrovaDispositiviClass extends BaseAdapter{

    private List<BluetoothDevice> myListDevice;
    private LayoutInflater myLayoutInflater;

    public AdapterTrovaDispositiviClass(Context context) {
        myLayoutInflater = LayoutInflater.from(context);
    }
    public void setData(List<BluetoothDevice> data) {
        myListDevice = data;
    }

    @Override
    public int getCount() {
        return (myListDevice == null) ? 0 : myListDevice.size();
    }


    @Override
    public Object getItem(int position) {
        return myListDevice.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    static class ViewHolder {
        TextView txtName;
        TextView txtAdress;
    }



    ViewHolder holder;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView	=  myLayoutInflater.inflate(R.layout.item_device, null);
            holder = new ViewHolder();
            holder.txtName = (TextView)convertView.findViewById(R.id.txtName);
            holder.txtAdress = (TextView) convertView.findViewById(R.id.txtAdress);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        BluetoothDevice device = myListDevice.get(position);

        holder.txtName.setText(device.getName());
        holder.txtAdress.setText(device.getAddress());

        return convertView;
    }
}

