# 移除过时的MaxPermSize JVM选项

## 问题识别

在GitHub Actions构建过程中，我们遇到了以下错误：

```
Unrecognized VM option 'MaxPermSize=512m'
Error: Could not create the Java Virtual Machine.
Error: A fatal exception has occurred. Program will exit.
```

## 原因分析

这个错误是因为：
1. 我们在项目的`gradle.properties`文件中设置了`-XX:MaxPermSize=512m` JVM选项
2. 这个选项在Java 8及之前版本中用于配置永久代(Permanent Generation)的大小
3. 从Java 8开始，永久代被元空间(Metaspace)取代，`MaxPermSize`选项被移除
4. 我们现在使用的是Java 17，所以这个选项不再被支持

## 解决方案

1. **修改gradle.properties文件**
   - 文件路径：`gradle.properties`
   - 移除了过时的`-XX:MaxPermSize=512m`选项
   - 具体更改：
     ```properties
     # Gradle JVM????IDE???
     org.gradle.jvmargs=-Xmx2048m
     ```

2. **提交并推送更改**
   ```bash
   git add gradle.properties
   git commit -m "Remove deprecated MaxPermSize JVM option"
   git push origin main
   ```

## 为什么需要移除这个选项？

- Java 8及更高版本不再使用永久代(Permanent Generation)
- 永久代被元空间(Metaspace)取代，元空间默认使用系统内存
- 在Java 17中，`MaxPermSize`选项已完全被移除，会导致JVM启动失败
- 移除这个选项可以确保Gradle能够在Java 17环境下正常启动

## 接下来的步骤

1. **等待构建完成**
   - GitHub Actions已经自动触发了新的构建流程
   - 在GitHub仓库的"Actions"选项卡中查看构建进度

2. **检查构建结果**
   - 如果构建成功，您应该能够看到绿色的勾
   - 检查是否有其他错误需要解决

3. **配置GitHub Secrets（如果尚未配置）**
   - 在GitHub仓库中配置`SERVER_HOST`、`SERVER_USERNAME`和`SERVER_PASSWORD`
   - 这是APK上传到服务器所必需的

4. **测试下载功能**
   - 构建完成后，访问`http://120.48.95.51/download.html`下载APK
