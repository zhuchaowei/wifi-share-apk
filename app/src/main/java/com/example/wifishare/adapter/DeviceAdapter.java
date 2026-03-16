package com.example.wifishare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wifishare.R;
import com.example.wifishare.model.DeviceInfo;

import java.util.List;

/**
 * 设备列表适配器
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    private List<DeviceInfo> devices;

    public DeviceAdapter(List<DeviceInfo> devices) {
        this.devices = devices;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        DeviceInfo device = devices.get(position);
        holder.bind(device);
    }

    @Override
    public int getItemCount() {
        return devices != null ? devices.size() : 0;
    }

    public void updateDevices(List<DeviceInfo> newDevices) {
        this.devices = newDevices;
        notifyDataSetChanged();
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {
        private TextView deviceName;
        private TextView deviceIp;
        private TextView deviceMac;
        private TextView connectedTime;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.deviceName);
            deviceIp = itemView.findViewById(R.id.deviceIp);
            deviceMac = itemView.findViewById(R.id.deviceMac);
            connectedTime = itemView.findViewById(R.id.connectedTime);
        }

        public void bind(DeviceInfo device) {
            deviceName.setText(device.getName());
            deviceIp.setText("IP: " + device.getIpAddress());
            deviceMac.setText("MAC: " + device.getMacAddress());
            connectedTime.setText(device.getConnectedTime());
        }
    }
}
