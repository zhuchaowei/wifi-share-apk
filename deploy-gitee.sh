#!/bin/bash

# Gitee 部署脚本

echo "=========================================="
echo "Gitee 部署 APK 构建方案"
echo "=========================================="
echo ""

PROJECT_DIR="/Users/apple/CodeBuddy/demoapk"
cd "$PROJECT_DIR"

# 检查 Gitee 远程仓库
if git remote get-url gitee >/dev/null 2>&1; then
    echo "✓ 已配置 Gitee 远程仓库"
    GITEE_URL=$(git remote get-url gitee)
    echo "  URL: $GITEE_URL"
else
    echo "⚠️  未配置 Gitee 远程仓库"
    echo ""
    echo "请按以下步骤操作："
    echo ""
    echo "1. 访问 Gitee 创建新仓库："
    echo "   https://gitee.com/projects/new"
    echo ""
    echo "2. 仓库名建议：wifi-share-apk"
    echo "   • 仓库名称：wifi-share-apk"
    echo "   • 仓库介绍：WiFi 共享设备扫描器"
    echo "   • 是否公开：公开或私有"
    echo "   • 不要初始化 README"
    echo "   • 点击 创建"
    echo ""
    echo "3. 复制仓库 URL，例如："
    echo "   https://gitee.com/zhuchaowei/wifi-share-apk.git"
    echo ""
    echo "4. 运行以下命令添加并推送："
    echo ""
    echo "   git remote add gitee https://gitee.com/zhuchaowei/wifi-share-apk.git"
    echo "   git push -u gitee main"
    echo ""
    exit 0
fi

echo ""
echo "=========================================="
echo "推送代码到 Gitee"
echo "=========================================="
echo ""

# 推送代码
echo "正在推送代码..."
git push -u gitee main

if [ $? -eq 0 ]; then
    echo ""
    echo "✓ 代码推送成功！"
    echo ""
    echo "=========================================="
    echo "下一步：在线构建"
    echo "=========================================="
    echo ""
    echo "由于 Gitee 原生不支持免费 CI/CD，有以下选择："
    echo ""
    echo "方案 A：使用 GitHub Actions（推荐）"
    echo "  1. 代码已同步到 GitHub（如果推送成功）"
    echo "  2. 访问 GitHub 仓库 Actions 触发构建"
    echo ""
    echo "方案 B：使用 GitLab CI"
    echo "  1. 访问 https://gitlab.com/projects/new"
    echo "  2. 创建仓库"
    echo "  3. 推送代码：git remote add gitlab https://gitlab.com/用户名/wifi-share-apk.git"
    echo "  4. git push gitlab main"
    echo "  5. 自动触发构建"
    echo ""
    echo "方案 C：使用 Gitee + 第三方 CI"
    echo "  1. 代码托管在 Gitee"
    echo "  2. 使用 Travis CI 或 CircleCI 构建"
    echo ""
    echo "方案 D：等待网络恢复后使用 GitHub"
    echo "  1. 等待网络稳定"
    echo "  2. 再次尝试：git push github main"
    echo ""
else
    echo ""
    echo "✗ 推送失败"
    echo ""
    echo "请检查后重试"
    exit 1
fi
