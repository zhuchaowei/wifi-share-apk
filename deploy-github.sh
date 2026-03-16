#!/bin/bash

# GitHub Actions 部署脚本

echo "=========================================="
echo "GitHub Actions APK 构建指南"
echo "=========================================="
echo ""

PROJECT_DIR="/Users/apple/CodeBuddy/demoapk"
cd "$PROJECT_DIR"

# 检查 GitHub 仓库配置
if git remote get-url github >/dev/null 2>&1; then
    echo "✓ 已配置 GitHub 远程仓库"
    GITHUB_URL=$(git remote get-url github)
    echo "  URL: $GITHUB_URL"
else
    echo "⚠️  未配置 GitHub 远程仓库"
    echo ""
    echo "请按以下步骤操作："
    echo ""
    echo "1. 访问 GitHub 创建新仓库："
    echo "   https://github.com/new"
    echo ""
    echo "2. 仓库名建议：wifi-share-apk"
    echo "   • Repository name: wifi-share-apk"
    echo "   • Description: WiFi 共享设备扫描器"
    echo "   • 选择 Public 或 Private"
    echo "   • 不要勾选 Initialize this repository"
    echo "   • 点击 Create repository"
    echo ""
    echo "3. 复制仓库 URL，例如："
    echo "   https://github.com/你的用户名/wifi-share-apk.git"
    echo ""
    echo "4. 运行以下命令添加并推送："
    echo ""
    echo "   git remote add github https://github.com/你的用户名/wifi-share-apk.git"
    echo "   git branch -M main"
    echo "   git push -u github main"
    echo ""
    exit 0
fi

echo ""
echo "=========================================="
echo "推送代码到 GitHub"
echo "=========================================="
echo ""

# 确保在 main 分支
git branch -M main

# 推送代码
echo "正在推送代码..."
git push -u github main

if [ $? -eq 0 ]; then
    echo ""
    echo "✓ 代码推送成功！"
    echo ""
    echo "=========================================="
    echo "下一步：触发构建"
    echo "=========================================="
    echo ""
    echo "1. 访问你的 GitHub 仓库："
    GITHUB_REPO=$(git remote get-url github | sed 's|https://github.com/||' | sed 's|.git||')
    echo "   https://github.com/$GITHUB_REPO"
    echo ""
    echo "2. 点击顶部的 'Actions' 标签"
    echo ""
    echo "3. 在左侧选择 'Build APK' 工作流"
    echo ""
    echo "4. 点击右侧的 'Run workflow' 按钮"
    echo ""
    echo "5. 确认分支为 'main'，点击绿色的 'Run workflow' 按钮"
    echo ""
    echo "6. 等待 2-3 分钟构建完成"
    echo ""
    echo "7. 构建完成后，点击构建任务"
    echo ""
    echo "8. 滚动到页面底部，找到 'Artifacts' 部分"
    echo ""
    echo "9. 点击 'app-debug' 下载 ZIP 文件"
    echo ""
    echo "10. 解压 ZIP 文件，得到 app-debug.apk"
    echo ""
    echo "=========================================="
    echo "提示"
    echo "=========================================="
    echo ""
    echo "• 每次推送代码都会触发自动构建"
    echo "• 可以在 Actions 页面查看所有构建历史"
    echo "• APK 保留 30 天"
    echo "• 支持下载和安装"
    echo ""
else
    echo ""
    echo "✗ 推送失败"
    echo ""
    echo "可能的原因："
    echo "1. 网络连接问题"
    echo "2. GitHub 身份验证失败"
    echo "3. 仓库权限问题"
    echo ""
    echo "请检查后重试"
    exit 1
fi
