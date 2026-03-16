# WiFi 共享设备扫描器

一个 Android 应用，用于扫描和显示通过 WiFi 共享连接的设备信息及 IP 地址。

## 功能特性

- 扫描本地 WiFi 网络中的连接设备
- 显示设备名称、IP 地址、MAC 地址和连接时间
- 支持下拉刷新和手动扫描
- Material Design 界面设计

## 权限要求

应用需要以下权限：

- `ACCESS_WIFI_STATE` - 访问 WiFi 状态
- `CHANGE_WIFI_STATE` - 更改 WiFi 状态
- `ACCESS_NETWORK_STATE` - 访问网络状态
- `INTERNET` - 网络访问
- `ACCESS_FINE_LOCATION` - 精确位置（Android 10+ 扫描设备需要）
- `ACCESS_COARSE_LOCATION` - 粗略位置

## 使用说明

1. 确保手机已连接到 WiFi 网络
2. 打开应用
3. 点击扫描按钮或下拉刷新开始扫描
4. 查看已发现的设备列表

## 技术实现

- 使用 Java 语言开发
- 支持最低 Android 7.0 (API 24)
- 使用 Material Components 设计
- 多线程扫描提高性能

## 注意事项

- 某些设备可能无法被检测到，取决于网络配置
- MAC 地址获取可能需要 root 权限
- 扫描速度取决于网络大小和性能
- Android 10+ 需要位置权限才能扫描设备

## 构建说明

1. 确保已安装 Android Studio
2. 使用 Gradle 构建项目
3. 连接 Android 设备或使用模拟器
4. 运行应用

## 项目结构

```
app/
├── src/main/
│   ├── java/com/example/wifishare/
│   │   ├── adapter/          # RecyclerView 适配器
│   │   ├── model/            # 数据模型
│   │   ├── MainActivity.java # 主活动
│   │   ├── NetworkScanner.java # 网络扫描工具
│   │   └── DeviceScanService.java # 后台服务
│   ├── res/
│   │   ├── layout/           # 布局文件
│   │   ├── values/           # 资源值
│   │   ├── menu/             # 菜单
│   │   └── mipmap/           # 图标资源
│   └── AndroidManifest.xml   # 应用清单
```

## 开发环境

- Android Studio Hedgehog | 2023.1.1 或更高版本
- Gradle 8.1.2
- Java 8

## 许可证

MIT License
