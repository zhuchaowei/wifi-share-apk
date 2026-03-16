# Gitee 在线构建 APK 方案

## Gitee 原生构建支持情况

Gitee **不直接提供** 类似 GitHub Actions 的免费 CI/CD 服务，但有以下替代方案：

## 方案一：Gitee Go（Gitee 的 CI/CD 服务）

### 可用性
- Gitee Go 需要付费或企业版
- 免费版用户可能无法使用
- 支持 Docker 镜像构建

### 配置步骤（如果可用）

1. 创建 `.gitlab-ci.yml` 文件（已创建）
2. 在 Gitee 仓库中启用 Gitee Go
3. 提交代码触发构建
4. 下载构建产物

## 方案二：Gitee Pages（不推荐）

- **不支持**：Gitee Pages 仅支持静态网站
- 无法用于构建 Android APK

## 方案三：使用第三方 CI/CD 服务（推荐）

### 3.1 Jenkins（自建）

如果你有服务器，可以部署 Jenkins：

```bash
# Docker 快速部署 Jenkins
docker run -d -p 8080:8080 -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  --name jenkins jenkins/jenkins:lts
```

配置 Jenkinsfile：
```groovy
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './gradlew assembleDebug'
            }
        }
        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'app/build/outputs/apk/debug/*.apk'
            }
        }
    }
}
```

### 3.2 Travis CI

1. 访问：https://travis-ci.org
2. 使用 GitHub 账号登录
3. 添加 Gitee 仓库
4. 创建 `.travis.yml`：

```yaml
language: java
jdk: openjdk-17
before_script:
  - wget https://services.gradle.org/distributions/gradle-8.1.1-bin.zip
  - unzip -q gradle-8.1.1-bin.zip
  - export PATH=$PATH:$PWD/gradle-8.1.1/bin
script:
  - ./gradlew assembleDebug
after_success:
  - ls -la app/build/outputs/apk/debug/
```

### 3.3 CircleCI

1. 访问：https://circleci.com
2. 连接 GitHub 仓库
3. 创建 `.circleci/config.yml`：

```yaml
version: 2.1
jobs:
  build:
    docker:
      - image: openjdk:17
    steps:
      - checkout
      - run:
          name: Install Gradle
          command: |
            wget https://services.gradle.org/distributions/gradle-8.1.1-bin.zip
            unzip gradle-8.1.1-bin.zip
      - run:
          name: Build APK
          command: ./gradle-8.1.1/bin/gradle assembleDebug
      - store_artifacts:
          path: app/build/outputs/apk/debug/
          destination: apk
```

## 方案四：混合方案（最实用）

### 使用 GitHub Actions + Gitee

由于 GitHub Actions 是免费的，可以：

1. **在 GitHub 上托管仓库并构建**
2. **从 GitHub 下载 APK**
3. **代码镜像到 Gitee（可选）**

#### 配置 GitHub Actions（已完成）

已创建 `.github/workflows/build.yml`

#### 同步到 Gitee

在 Gitee 中：
1. 选择 `从 GitHub / GitLab 导入仓库`
2. 选择你的 GitHub 仓库
3. 点击导入

这样代码会自动同步到 Gitee，但构建在 GitHub 进行。

## 方案五：使用 Gitee + 第三方构建服务

### AppVeyor

1. 访问：https://www.appveyor.com
2. 连接 GitHub 账号
3. 添加项目
4. 配置构建脚本

### Drone CI

1. 访问：https://cloud.drone.io
2. 连接 GitHub/GitLab/Gitee
3. 配置 `.drone.yml`：

```yaml
kind: pipeline
type: docker
name: default

steps:
  - name: build
    image: openjdk:17
    commands:
      - apt-get update && apt-get install -y wget unzip
      - wget https://services.gradle.org/distributions/gradle-8.1.1-bin.zip
      - unzip gradle-8.1.1-bin.zip
      - ./gradle-8.1.1/bin/gradle assembleDebug

  - name: upload
    image: plugins/s3
    settings:
      bucket: your-bucket
      source: app/build/outputs/apk/debug/app-debug.apk
```

## 推荐方案对比

| 方案 | 成本 | 易用性 | 构建速度 | 稳定性 |
|------|------|--------|----------|--------|
| GitHub Actions | 免费 | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| GitLab CI | 免费 | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| Gitee Go | 付费 | ⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐ |
| Travis CI | 免费 | ⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐ |
| CircleCI | 免费 | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| Jenkins | 自建 | ⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |

## 最佳实践建议

### 如果必须在 Gitee

**方案 A：使用 Gitee Go（付费）**
- 联系 Gitee 开通 Gitee Go 服务
- 使用 `.gitlab-ci.yml` 配置构建

**方案 B：使用自建 Jenkins**
- 适合有服务器的用户
- 完全掌控构建流程

**方案 C：代码在 Gitee，构建在其他平台**
- 代码托管在 Gitee
- 使用 GitHub Actions 或其他免费 CI/CD
- 手动同步触发构建

### 如果只需要 APK

**推荐使用 GitHub Actions**（最简单）
1. 在 GitHub 创建仓库
2. 推送代码
3. 自动构建并下载 APK
4. 无需任何费用

## 快速获得 APK 的操作

### 使用 GitHub Actions（2-3 分钟）

```bash
# 1. 在 GitHub 创建仓库
# 访问：https://github.com/new

# 2. 推送代码到 GitHub
git remote add github https://github.com/你的用户名/wifi-share-apk.git
git push github main

# 3. 触发构建
# 访问 GitHub 仓库 → Actions → Run workflow

# 4. 下载 APK（2-3 分钟后）
```

### 使用 GitLab（如果已有账号）

```bash
# 1. 在 GitLab 创建仓库
# 访问：https://gitlab.com/projects/new

# 2. 推送代码
git remote add gitlab https://gitlab.com/你的用户名/wifi-share-apk.git
git push gitlab main

# 3. 自动触发构建（CI/CD 自动运行）
# 在 CI/CD → Pipelines 查看构建
# 下载 Artifacts
```

## 总结

**Gitee 原生不支持免费在线构建 APK**，但有以下选择：

1. ✅ **GitHub Actions**（推荐，免费，最快）
2. ✅ **GitLab CI**（免费，支持好）
3. ✅ **Gitee Go**（付费，企业版）
4. ✅ **第三方 CI/CD**（Travis, CircleCI 等）
5. ✅ **自建 Jenkins**（需要服务器）

**最快获得 APK 的方案：使用 GitHub Actions**，只需 2-3 分钟！

需要我帮你配置哪个方案？
