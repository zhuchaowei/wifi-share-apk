package com.example.wifishare.model;

/**
 * 设备信息数据模型
 */
public class DeviceInfo {
    private String name;
    private String ipAddress;
    private String macAddress;
    private String connectedTime;

    public DeviceInfo() {
    }

    public DeviceInfo(String name, String ipAddress, String macAddress, String connectedTime) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.connectedTime = connectedTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getConnectedTime() {
        return connectedTime;
    }

    public void setConnectedTime(String connectedTime) {
        this.connectedTime = connectedTime;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "name='" + name + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", connectedTime='" + connectedTime + '\'' +
                '}';
    }
}
