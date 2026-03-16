# GitHub Actions 构建 APK 快速指南

## 🚀 三步获得 APK

### 第 1 步：创建 GitHub 仓库

1. 访问：https://github.com/new
2. 填写信息：
   - **Repository name**: `wifi-share-apk`
   - **Description**: `WiFi 共享设备扫描器`
   - 选择 **Public** 或 **Private** 都可以
   - **不要**勾选 "Initialize this repository"
   - 点击 **Create repository**

### 第 2 步：推送代码

在终端执行以下命令（将 `你的用户名` 替换为你的 GitHub 用户名）：

```bash
cd /Users/apple/CodeBuddy/demoapk
git remote add github https://github.com/你的用户名/wifi-share-apk.git
git branch -M main
git push -u github main
```

或者直接运行：

```bash
./deploy-github.sh
```

按照提示操作即可。

### 第 3 步：触发构建

1. 访问你的 GitHub 仓库
2. 点击顶部的 **Actions** 标签
3. 在左侧选择 **Build APK** 工作流
4. 点击右侧绿色的 **Run workflow** 按钮
5. 确认分支为 `main`，点击 **Run workflow**
6. 等待 **2-3 分钟** 构建完成
7. 点击构建任务，滚动到底部
8. 在 **Artifacts** 部分点击 **app-debug** 下载
9. 解压 ZIP 文件，得到 **app-debug.apk**

## 📱 安装 APK 到手机

### 方法 1：使用 USB
```bash
adb install app-debug.apk
```

### 方法 2：直接安装
1. 将 APK 传到手机
2. 在手机上打开 APK 文件
3. 允许安装未知来源的应用
4. 点击安装

## ⚙️ 工作流配置说明

已配置的 `.github/workflows/build.yml` 会：

- ✅ 自动在每次推送时触发构建
- ✅ 支持手动触发构建
- ✅ 使用 Ubuntu 最新环境
- ✅ 使用 JDK 17
- ✅ 保留构建产物 30 天
- ✅ 自动上传 APK 作为 Artifact

## 🔧 触发构建的方式

### 方式 1：自动触发（推荐）
每次推送到 `main` 或 `master` 分支会自动构建：
```bash
git add .
git commit -m "update"
git push github main
```

### 方式 2：手动触发
在 GitHub 网页上手动运行工作流。

## 📊 构建时间

- 首次构建：**3-5 分钟**
- 后续构建：**2-3 分钟**

## 💡 提示

- 构建 URL 格式：`https://github.com/你的用户名/wifi-share-apk/actions`
- APK 位置：`app/build/outputs/apk/debug/app-debug.apk`
- 可以在 Actions 页面查看所有构建历史
- 每个构建都可以独立下载

## ❓ 常见问题

### Q: 推送时提示 authentication failed
A: 需要配置 GitHub 身份验证：
```bash
# 使用 Personal Access Token
git remote set-url github https://YOUR_TOKEN@github.com/你的用户名/wifi-share-apk.git
```

### Q: 构建失败怎么办
A:
1. 查看构建日志
2. 检查代码是否有语法错误
3. 重新运行工作流

### Q: 如何构建发布版 APK
A: 修改 `.github/workflows/build.yml`，将 `assembleDebug` 改为 `assembleRelease`：
```yaml
- name: Build with Gradle
  run: ./gradlew assembleRelease
```

## 🎯 快速命令总结

```bash
# 推送代码到 GitHub
git push github main

# 查看远程仓库
git remote -v

# 查看构建状态
# 访问：https://github.com/你的用户名/wifi-share-apk/actions
```

现在就开始吧！按照上面的三步操作，2-3 分钟就能拿到 APK！
