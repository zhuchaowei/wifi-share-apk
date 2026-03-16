#!/bin/bash

# 快速打包脚本
echo "=========================================="
echo "WiFi 共享设备扫描器 - 快速打包脚本"
echo "=========================================="
echo ""

# 检查是否安装了 Android Studio
if [ -d "/Applications/Android Studio.app" ]; then
    echo "✓ 检测到 Android Studio 已安装"
    echo ""
    echo "正在打开项目..."
    open -a "Android Studio" /Users/apple/CodeBuddy/demoapk
    echo ""
    echo "请按照以下步骤操作："
    echo "1. 等待 Gradle 同步完成（首次可能需要几分钟）"
    echo "2. 点击菜单：Build → Build Bundle(s) / APK(s) → Build APK(s)"
    echo "3. 等待构建完成"
    echo "4. 构建完成后点击提示框中的 'locate' 按钮"
    echo "5. APK 文件将位于：app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "详细打包指南请查看：BUILD_GUIDE.md"
else
    echo "✗ 未检测到 Android Studio"
    echo ""
    echo "请先安装 Android Studio："
    echo "https://developer.android.com/studio"
    echo ""
    echo "或者查看详细打包指南：BUILD_GUIDE.md"
fi

echo ""
echo "=========================================="
