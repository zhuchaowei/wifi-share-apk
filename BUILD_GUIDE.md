# APK 打包指南

## 方法一：使用 Android Studio（推荐）

### 1. 安装 Android Studio
下载并安装 Android Studio：https://developer.android.com/studio

### 2. 打开项目
```bash
# 启动 Android Studio
open -a "Android Studio" /Users/apple/CodeBuddy/demoapk
```

### 3. 等待 Gradle 同步
- Android Studio 会自动检测项目结构
- 等待 Gradle 同步完成（首次可能需要几分钟）

### 4. 构建调试版 APK
1. 点击菜单：`Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
2. 等待构建完成
3. 构建完成后会弹出提示框，点击 `locate` 查看 APK 位置

### 5. 导出发布版 APK（签名）
1. 点击菜单：`Build` → `Generate Signed Bundle / APK`
2. 选择 `APK`，点击 `Next`
3. 创建或选择密钥库：
   - 如果是第一次，点击 `Create new...`
   - 填写密钥库信息：
     - Key store path: 选择保存位置
     - Password: 设置密码
     - Key alias: 密钥别名
     - Key password: 密钥密码
     - Validity: 有效期（建议至少 25 年）
     - Certificate: 填写证书信息
4. 选择构建类型：`release`
5. 点击 `Finish`
6. 构建完成后点击 `locate` 查看 APK 位置

## 方法二：使用命令行构建

### 前提条件
需要安装：
- Android SDK
- Java JDK 8 或更高版本
- Gradle

### 1. 设置环境变量
将以下内容添加到 `~/.zshrc`：

```bash
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/tools/bin
export PATH=$PATH:$ANDROID_HOME/platform-tools
export PATH=$PATH:$ANDROID_HOME/build-tools/34.0.0
export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
```

然后执行：
```bash
source ~/.zshrc
```

### 2. 初始化 Gradle Wrapper
```bash
cd /Users/apple/CodeBuddy/demoapk
gradle wrapper --gradle-version 8.1.1
```

### 3. 构建调试版 APK
```bash
./gradlew assembleDebug
```

APK 输出位置：
```
app/build/outputs/apk/debug/app-debug.apk
```

### 4. 构建发布版 APK（需要签名）
```bash
./gradlew assembleRelease
```

APK 输出位置：
```
app/build/outputs/apk/release/app-release-unsigned.apk
```

#### 签名 APK
```bash
# 使用 jarsigner 签名
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 \
  -keystore your-keystore.jks \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  your-key-alias

# 使用 zipalign 优化
zipalign -v 4 app/build/outputs/apk/release/app-release-unsigned.apk \
  app-release-aligned.apk
```

## 方法三：使用命令行工具（无需 Gradle）

如果安装了 Android SDK，可以直接使用构建工具：

```bash
# 设置 Android SDK 路径
export ANDROID_HOME=/path/to/Android/sdk

# 使用 aapt 和 dx 工具手动构建（复杂，不推荐）
```

## APK 输出位置

### Android Studio 生成的 APK：
- **调试版**: `app/build/outputs/apk/debug/app-debug.apk`
- **发布版**: `app/build/outputs/apk/release/app-release.apk`

### 文件大小预估：
- 调试版：约 1-2 MB
- 发布版（已签名和优化）：约 0.8-1.5 MB

## 安装 APK 到设备

### 方法一：使用 ADB
```bash
# 连接设备并启用 USB 调试
adb devices

# 安装 APK
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 方法二：直接传输
1. 将 APK 文件复制到手机
2. 在手机上打开 APK 文件
3. 允许安装未知来源的应用
4. 完成安装

## 常见问题

### 问题 1：Gradle 同步失败
**解决方案**：
- 检查网络连接
- 配置 Gradle 镜像源（阿里云）
- 在 `gradle.properties` 中添加：
```properties
maven.aliyun.com
```

### 问题 2：找不到 Android SDK
**解决方案**：
- 在 Android Studio 中：`File` → `Project Structure` → `SDK Location`
- 或设置环境变量 `ANDROID_HOME`

### 问题 3：构建失败 - SDK 版本不匹配
**解决方案**：
- 安装所需 SDK 版本（API 34）
- 使用 SDK Manager 安装构建工具

### 问题 4：签名时找不到密钥库
**解决方案**：
- 确保密钥库文件路径正确
- 检查密码是否正确

## 快速开始（最快方式）

1. **安装 Android Studio**（如果还没安装）
   ```bash
   # 下载并安装
   # https://developer.android.com/studio
   ```

2. **打开项目**
   ```bash
   cd /Users/apple/CodeBuddy/demoapk
   open -a "Android Studio" .
   ```

3. **等待同步完成**（首次需要下载依赖）

4. **构建 APK**
   - 菜单：`Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`

5. **导出 APK**
   - 构建完成后点击提示框中的 `locate` 按钮

## 项目依赖

本项目的最小 SDK 版本：API 24 (Android 7.0)
目标 SDK 版本：API 34 (Android 14)

## 注意事项

- 调试版 APK 不可发布到应用商店
- 发布版必须使用签名密钥
- 保管好签名密钥，丢失后无法更新应用
- 定期备份密钥库文件

## 更多资源

- Android 官方文档：https://developer.android.com/studio/build
- APK 签名指南：https://developer.android.com/studio/publish/app-signing
