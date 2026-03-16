package com.example.wifishare;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wifishare.adapter.DeviceAdapter;
import com.example.wifishare.model.DeviceInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private RecyclerView deviceList;
    private SwipeRefreshLayout swipeRefresh;
    private FloatingActionButton scanButton;
    private DeviceAdapter adapter;
    private List<DeviceInfo> devices;
    private boolean isScanning = false;
    private WifiManager wifiManager;
    private DeviceScanService scanService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        requestPermissions();
    }

    private void initViews() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        deviceList = findViewById(R.id.deviceList);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        scanButton = findViewById(R.id.scanButton);
        
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.purple_500),
            ContextCompat.getColor(this, R.color.teal_200)
        );

        scanButton.setOnClickListener(v -> toggleScan());

        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        devices = new ArrayList<>();
    }

    private void setupRecyclerView() {
        adapter = new DeviceAdapter(devices);
        deviceList.setLayoutManager(new LinearLayoutManager(this));
        deviceList.setAdapter(adapter);
    }

    private void requestPermissions() {
        if (!checkPermissions()) {
            new AlertDialog.Builder(this)
                .setTitle(getString(R.string.permission_required))
                .setMessage(getString(R.string.permission_desc))
                .setPositiveButton(getString(R.string.grant_permission), (dialog, which) -> {
                    // 在运行时请求权限
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
        }
    }

    private boolean checkPermissions() {
        // 简化的权限检查
        return true;
    }

    private void toggleScan() {
        if (isScanning) {
            stopScan();
        } else {
            startScan();
        }
    }

    private void startScan() {
        if (!checkPermissions()) {
            return;
        }

        isScanning = true;
        scanButton.setImageResource(android.R.drawable.ic_media_pause);
        scanButton.setContentDescription(getString(R.string.stop_scan));
        
        devices.clear();
        adapter.notifyDataSetChanged();
        
        scanConnectedDevices();
    }

    private void stopScan() {
        isScanning = false;
        scanButton.setImageResource(android.R.drawable.ic_menu_search);
        scanButton.setContentDescription(getString(R.string.scan_devices));
        swipeRefresh.setRefreshing(false);
    }

    private void scanConnectedDevices() {
        new Thread(() -> {
            List<DeviceInfo> foundDevices = NetworkScanner.scanLocalNetwork(wifiManager);
            
            runOnUiThread(() -> {
                devices.clear();
                devices.addAll(foundDevices);
                adapter.notifyDataSetChanged();
                stopScan();
                
                int count = devices.size();
                getSupportActionBar().setSubtitle(count > 0 
                    ? getString(R.string.device_count, count) 
                    : getString(R.string.no_devices));
            });
        }).start();
    }

    @Override
    public void onRefresh() {
        if (!isScanning) {
            startScan();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            if (!isScanning) {
                startScan();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
