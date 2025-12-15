# APK文件名修复

## 问题识别

在GitHub Actions构建过程中，我们遇到了以下错误：

```
tar all files into /tmp/PfkYJZwpqf.tar.gz
tar: empty archive
exit status 1
```

这个错误发生在`appleboy/scp-action@v0.1.4`步骤中，表明scp-action无法找到要上传的APK文件。

## 原因分析

通过在本地构建项目并检查构建输出，我们发现了问题：

```
app/build/intermediates/apk/debug/app-debug.apk
app/build/outputs/apk/debug/app-debug.apk
app/build/outputs/apk/release/app-release-unsigned.apk
```

构建生成的是`app-release-unsigned.apk`文件，而不是我们在scp-action中指定的`app-release.apk`文件。这是因为我们的构建过程没有对APK进行签名。

## 解决方案

1. **修改GitHub Actions工作流**
   - 更新scp-action的source参数，使用实际的APK文件名
   - 同时更新下载HTML页面中的链接
   - 文件路径：`.github/workflows/android.yml`
   - 具体更改：
     ```yaml
     - name: Upload APK to Server
       uses: appleboy/scp-action@v0.1.4
       with:
         host: ${{ secrets.SERVER_HOST }}
         username: ${{ secrets.SERVER_USERNAME }}
         password: ${{ secrets.SERVER_PASSWORD }}
         source: "app/build/outputs/apk/release/app-release-unsigned.apk"
         target: "/var/www/html/apk"
         overwrite: true
     ```

     ```yaml
     - name: Create Download HTML
       run: |
         echo "<html>" > download.html
         echo "<body>" >> download.html
         echo "<h1>Android App Download</h1>" >> download.html
         echo "<p>Click below to download the latest APK:</p>" >> download.html
         echo "<a href='app-release-unsigned.apk' download>Download APK</a>" >> download.html
         echo "</body>" >> download.html
         echo "</html>" >> download.html
     ```

2. **提交并推送更改**
   ```bash
   git add .github/workflows/android.yml
   git commit -m "Fix APK file name in CI/CD workflow"
   git push origin main
   ```

## 为什么文件名为app-release-unsigned.apk？

- 在Android构建过程中，如果没有配置签名信息，Gradle会生成未签名的APK文件
- 未签名的APK文件名后缀为`-unsigned.apk`
- 要生成已签名的APK，需要在项目中配置签名信息

## 接下来的步骤

1. **等待构建完成**
   - GitHub Actions已经自动触发了新的构建流程
   - 在GitHub仓库的"Actions"选项卡中查看构建进度

2. **检查构建结果**
   - 如果构建成功，您应该能够看到绿色的勾
   - 检查是否有其他错误需要解决

3. **测试下载功能**
   - 构建完成后，访问`http://120.48.95.51/download.html`下载APK

## 创建的文档

- `APK_FILENAME_FIX.md`: APK文件名修复的详细解决方案
