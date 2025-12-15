# APK下载失败问题修复报告

## 问题识别
- 用户无法从 `http://120.48.95.51` 下载 `app-release-unsigned.apk` 文件
- 浏览器提示："失败 - 未发现文件"

## 原因分析
1. GitHub Actions 工作流显示所有步骤（包括APK构建和上传）都成功完成
2. HTML文件 `download.html` 正确生成并上传到服务器根目录
3. 但APK文件被上传到了 `/var/www/html/apk` 目录，而HTML文件中的链接指向了根目录下的 `app-release-unsigned.apk`，导致路径不匹配

## 解决方案
修改 `.github/workflows/android.yml` 文件中的 `Upload APK to Server` 步骤，将APK文件的上传目标目录从 `/var/www/html/apk` 改为 `/var/www/html`，与HTML文件在同一目录下：

```yaml
- name: Upload APK to Server
  uses: appleboy/scp-action@v0.1.4
  with:
    host: ${{ secrets.SERVER_HOST }}
    username: ${{ secrets.SERVER_USERNAME }}
    password: ${{ secrets.SERVER_PASSWORD }}
    source: "app/build/outputs/apk/release/app-release-unsigned.apk"
    target: "/var/www/html"
    overwrite: true
```

## 后续步骤
1. 等待GitHub Actions工作流重新运行完成
2. 验证APK文件是否成功上传到服务器根目录
3. 测试从 `http://120.48.95.51/download.html` 下载APK文件

## 修复结果
- ✅ 修改了APK上传目录，确保与HTML文件在同一目录
- ✅ 提交并推送了更改到GitHub
- ✅ 触发了新的GitHub Actions工作流运行