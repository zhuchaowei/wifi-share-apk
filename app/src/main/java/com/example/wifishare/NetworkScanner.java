package com.example.wifishare;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.example.wifishare.model.DeviceInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 网络扫描工具类
 * 用于扫描本地 WiFi 网络中的连接设备
 */
public class NetworkScanner {
    private static final String TAG = "NetworkScanner";
    private static final int SCAN_TIMEOUT = 2000; // 扫描超时时间(毫秒)
    private static final int THREAD_POOL_SIZE = 50; // 并发扫描线程数

    /**
     * 扫描本地网络设备
     */
    public static List<DeviceInfo> scanLocalNetwork(WifiManager wifiManager) {
        List<DeviceInfo> devices = new ArrayList<>();
        
        if (wifiManager == null) {
            Log.e(TAG, "WifiManager is null");
            return devices;
        }

        try {
            // 获取 WiFi 连接信息
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            if (dhcpInfo == null) {
                Log.e(TAG, "DhcpInfo is null");
                return devices;
            }

            // 获取本机 IP 地址
            String localIp = intToIp(dhcpInfo.ipAddress);
            if (localIp == null || localIp.isEmpty()) {
                Log.e(TAG, "Local IP is empty");
                return devices;
            }

            // 添加本机设备
            DeviceInfo localDevice = new DeviceInfo();
            localDevice.setName("本机 (" + getDeviceName() + ")");
            localDevice.setIpAddress(localIp);
            localDevice.setMacAddress(getLocalMacAddress());
            localDevice.setConnectedTime("刚刚");
            devices.add(localDevice);

            // 获取网段
            String networkPrefix = getNetworkPrefix(localIp);
            Log.d(TAG, "Scanning network: " + networkPrefix + ".x");

            // 扫描局域网
            scanSubnet(devices, networkPrefix, wifiManager);

        } catch (Exception e) {
            Log.e(TAG, "Error scanning network", e);
        }

        return devices;
    }

    /**
     * 扫描子网中的设备
     */
    private static void scanSubnet(List<DeviceInfo> devices, String networkPrefix, WifiManager wifiManager) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        CountDownLatch latch = new CountDownLatch(254);

        for (int i = 1; i <= 254; i++) {
            final int host = i;
            executor.execute(() -> {
                try {
                    String ip = networkPrefix + "." + host;
                    if (isReachable(ip)) {
                        DeviceInfo device = new DeviceInfo();
                        device.setName("设备-" + host);
                        device.setIpAddress(ip);
                        
                        // 尝试获取设备名称
                        String hostname = getHostName(ip);
                        if (hostname != null && !hostname.isEmpty()) {
                            device.setName(hostname);
                        }
                        
                        // 尝试获取 MAC 地址
                        String mac = getMacAddress(ip);
                        if (mac != null && !mac.isEmpty()) {
                            device.setMacAddress(mac);
                        }
                        
                        device.setConnectedTime("刚刚");
                        devices.add(device);
                    }
                } catch (Exception e) {
                    // 忽略单个设备的扫描错误
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await(SCAN_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executor.shutdownNow();
    }

    /**
     * 检查 IP 是否可达
     */
    private static boolean isReachable(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.isReachable(SCAN_TIMEOUT);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 获取主机名
     */
    private static String getHostName(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.getHostName();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    /**
     * 获取 MAC 地址（需要 root 权限或特定 API）
     */
    private static String getMacAddress(String ip) {
        try {
            // 尝试通过 ARP 表获取 MAC 地址
            Process p = Runtime.getRuntime().exec("ip neigh show " + ip);
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
            
            if (line != null && line.contains(ip)) {
                String[] parts = line.split("\\s+");
                for (String part : parts) {
                    if (part.matches("([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})")) {
                        return part.toUpperCase();
                    }
                }
            }
        } catch (Exception e) {
            // 忽略错误
        }
        return "未知";
    }

    /**
     * 获取本机 MAC 地址
     */
    private static String getLocalMacAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces == null) {
                return "未知";
            }
            
            for (NetworkInterface networkInterface : Collections.list(interfaces)) {
                if (!networkInterface.isLoopback() && networkInterface.isUp()) {
                    byte[] macBytes = networkInterface.getHardwareAddress();
                    if (macBytes != null && macBytes.length > 0) {
                        StringBuilder mac = new StringBuilder();
                        for (byte b : macBytes) {
                            mac.append(String.format("%02X:", b));
                        }
                        return mac.substring(0, mac.length() - 1);
                    }
                }
            }
        } catch (SocketException e) {
            Log.e(TAG, "Error getting MAC address", e);
        }
        return "未知";
    }

    /**
     * 获取设备名称
     */
    private static String getDeviceName() {
        return android.os.Build.MODEL;
    }

    /**
     * 将整数 IP 转换为字符串
     */
    private static String intToIp(int ipAddress) {
        return ((ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                ((ipAddress >> 24) & 0xFF));
    }

    /**
     * 获取网络前缀
     */
    private static String getNetworkPrefix(String ip) {
        int lastDot = ip.lastIndexOf('.');
        return ip.substring(0, lastDot);
    }
}
