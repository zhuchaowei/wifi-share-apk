#!/bin/bash

# 快速在线构建脚本
echo "=========================================="
echo "WiFi 共享设备扫描器 - 在线构建 APK"
echo "=========================================="
echo ""

PROJECT_DIR="/Users/apple/CodeBuddy/demoapk"
cd "$PROJECT_DIR"

# 检查是否已初始化 Git
if [ ! -d ".git" ]; then
    echo "初始化 Git 仓库..."
    git init
    git add .
    git commit -m "Initial commit: WiFi Share App"
    echo "✓ Git 仓库已初始化"
    echo ""
fi

# 检查是否有远程仓库
if ! git remote get-url origin >/dev/null 2>&1; then
    echo "=========================================="
    echo "下一步操作："
    echo "=========================================="
    echo ""
    echo "1. 访问 GitHub 创建新仓库："
    echo "   https://github.com/new"
    echo ""
    echo "2. 仓库名建议：wifi-share-apk"
    echo "   不要初始化 README"
    echo ""
    echo "3. 创建后，运行以下命令添加远程仓库："
    echo "   git remote add origin https://github.com/你的用户名/wifi-share-apk.git"
    echo ""
    echo "4. 推送代码："
    echo "   git push -u origin main"
    echo ""
    echo "5. 在 GitHub 仓库页面："
    echo "   - 点击 'Actions' 标签"
    echo "   - 选择 'Build APK' 工作流"
    echo "   - 点击 'Run workflow' 按钮"
    echo "   - 选择 'main' 分支，点击 'Run workflow'"
    echo ""
    echo "6. 等待 2-3 分钟后，在构建页面下载 APK"
    echo ""
    echo "详细说明请查看：ONLINE_BUILD_GUIDE.md"
    echo ""
else
    echo "✓ 已检测到远程仓库"
    echo ""
    echo "如果代码有更新，运行："
    echo "  git add ."
    echo "  git commit -m 'update'"
    echo "  git push"
    echo ""
    echo "然后访问 GitHub Actions 触发构建"
    echo ""
fi

echo "=========================================="
echo "提示："
echo "=========================================="
echo ""
echo "• 完整构建指南：ONLINE_BUILD_GUIDE.md"
echo "• 本地构建指南：BUILD_GUIDE.md"
echo "• GitHub Actions 会自动构建 APK"
echo "• 构建完成后下载 Artifact 即可"
echo ""
