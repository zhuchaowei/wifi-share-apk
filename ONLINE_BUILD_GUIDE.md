# 使用在线构建服务打包 APK

由于本地环境没有 Android SDK，我们提供以下几种在线构建方案：

## 方案一：使用 GitHub Actions（推荐，免费）

### 步骤：

1. **初始化 Git 仓库**
```bash
cd /Users/apple/CodeBuddy/demoapk
git init
git add .
git commit -m "Initial commit: WiFi Share App"
```

2. **创建 GitHub 仓库**
   - 访问 https://github.com/new
   - 创建新仓库，命名为 `wifi-share-apk`
   - 不要初始化 README

3. **推送代码到 GitHub**
```bash
git remote add origin https://github.com/你的用户名/wifi-share-apk.git
git branch -M main
git push -u origin main
```

4. **触发构建**
   - 访问你的 GitHub 仓库
   - 点击 `Actions` 标签
   - 点击左侧的 `Build APK`
   - 点击 `Run workflow` 按钮
   - 选择 `main` 分支，点击 `Run workflow`

5. **下载 APK**
   - 等待构建完成（约 2-3 分钟）
   - 点击构建任务
   - 在页面底部找到 `Artifacts` 部分
   - 点击 `app-debug` 下载 ZIP 文件
   - 解压后得到 `app-debug.apk`

## 方案二：使用 GitHub Actions 自动触发

将 `.github/workflows/build.yml` 中的触发条件改为：
```yaml
on:
  workflow_dispatch:
  push:
    branches: [ main, master ]
```

这样每次推送代码都会自动构建。

## 方案三：使用 Cloud Build（其他在线构建服务）

### GitHub Codespaces
1. 访问 GitHub 仓库
2. 点击 `Code` → `Codespaces` → `Create codespace`
3. 在 Codespace 中执行：
```bash
./gradlew assembleDebug
```

### Gitpod
1. 访问 https://gitpod.io/#https://github.com/你的用户名/wifi-share-apk
2. 等待环境加载
3. 执行构建命令

## 方案四：使用 Docker（本地需要 Docker）

```bash
# 使用 Android Docker 镜像构建
docker run --rm -v "$PWD":/project -w /project openjdk:17 bash -c "
  wget https://services.gradle.org/distributions/gradle-8.1.1-bin.zip -q
  unzip -q gradle-8.1.1-bin.zip
  export PATH=$PATH:$PWD/gradle-8.1.1/bin
  ./gradlew wrapper
  ./gradlew assembleDebug
"
```

## 方案五：使用 Android Studio（最简单）

如果你愿意安装 Android Studio：

1. 下载 Android Studio
   - https://developer.android.com/studio
   - 文件大小约 1GB

2. 安装并打开项目

3. 点击 `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`

## 快速获得 APK 的完整步骤

### 使用 GitHub Actions（最快，无需安装任何东西）

```bash
# 1. 进入项目目录
cd /Users/apple/CodeBuddy/demoapk

# 2. 初始化 Git
git init
git add .
git commit -m "Initial commit"

# 3. 创建 GitHub 仓库后，添加远程地址
# git remote add origin https://github.com/你的用户名/wifi-share-apk.git

# 4. 推送
# git push -u origin main

# 5. 在 GitHub 网页上触发构建
# 访问仓库 → Actions → Build APK → Run workflow

# 6. 下载 APK（2-3 分钟后）
```

## APK 文件信息

- **文件名**: `app-debug.apk`
- **大小**: 约 1-2 MB
- **签名**: 调试签名
- **兼容性**: Android 7.0 (API 24) 及以上
- **安装说明**: 允许安装未知来源的应用

## 验证 APK 质量

构建完成后，你可以使用以下工具验证：

### 使用 aapt 查看 APK 信息
```bash
aapt dump badging app-debug.apk
```

### 使用 apkanalyzer 分析 APK
```bash
apkanalyzer apk file-size app-debug.apk
```

## 常见问题

### Q: GitHub Actions 构建失败怎么办？
A: 检查构建日志，通常是依赖下载超时，重新运行即可。

### Q: 如何获得发布版 APK？
A: 在 `build.yml` 中添加：
```yaml
- name: Build Release APK
  run: ./gradlew assembleRelease
```

### Q: 构建需要多长时间？
A: 首次构建约 3-5 分钟，后续 1-2 分钟。

### Q: APK 可以安装到任何设备吗？
A: 需要 Android 7.0 及以上系统。

## 推荐方案

**最快获得 APK**: 使用 GitHub Actions（方案一）
- ✅ 无需本地安装任何工具
- ✅ 免费使用
- ✅ 构建速度快
- ✅ 2-3 分钟即可获得 APK

按照方案一的步骤操作，你很快就能获得可用的 APK 文件！
