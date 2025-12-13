# Android CI/CD 服务器配置指南

## 1. 登录服务器

使用SSH登录到您的服务器：

```bash
ssh root@120.48.95.51
```

然后输入您的服务器密码。

## 2. 安装Nginx Web服务器

如果服务器尚未安装Nginx，执行以下命令安装：

```bash
# 更新软件包列表
apt update

# 安装Nginx
apt install -y nginx

# 启动Nginx服务
systemctl start nginx

# 设置Nginx开机自启
systemctl enable nginx
```

## 3. 创建APK存储目录

创建用于存储APK文件的目录：

```bash
mkdir -p /var/www/html/apk
```

## 4. 设置目录权限

确保Web服务器可以访问这些文件：

```bash
chown -R www-data:www-data /var/www/html
chmod -R 755 /var/www/html
```

## 5. 配置Nginx

编辑Nginx默认配置文件：

```bash
nano /etc/nginx/sites-available/default
```

确保配置文件包含以下内容（通常默认配置已经足够）：

```nginx
server {
    listen 80 default_server;
    listen [::]:80 default_server;

    root /var/www/html;
    index index.html index.htm index.nginx-debian.html;

    server_name _;

    location / {
        try_files $uri $uri/ =404;
    }
}
```

保存并退出编辑器（按Ctrl+O，然后Ctrl+X）。

## 6. 测试Nginx配置

```bash
nginx -t
```

如果配置正确，将显示：

```
nginx: the configuration file /etc/nginx/nginx.conf syntax is ok
nginx: configuration file /etc/nginx/nginx.conf test is successful
```

## 7. 重启Nginx

```bash
systemctl restart nginx
```

## 8. 测试Web服务器

在浏览器中访问 `http://120.48.95.51`，您应该看到Nginx的欢迎页面。

## 9. 配置GitHub Secrets

在GitHub仓库中添加以下Secrets：

1. 登录到GitHub，进入您的仓库
2. 点击 "Settings" -> "Secrets and variables" -> "Actions"
3. 点击 "New repository secret"
4. 添加以下Secrets：
   - `SERVER_HOST`: 120.48.95.51
   - `SERVER_USERNAME`: root
   - `SERVER_PASSWORD`: 您的服务器密码

## 10. 推送到GitHub仓库

将项目推送到GitHub仓库：

```bash
# 添加远程仓库
git remote add origin https://github.com/您的用户名/您的仓库名.git

# 推送代码
git push -u origin master
```

## 11. 测试CI/CD流程

1. 推送代码到GitHub后，GitHub Actions将自动触发
2. 进入GitHub仓库的 "Actions" 标签查看构建进度
3. 构建完成后，访问 `http://120.48.95.51/download.html` 下载APK

## 12. 手机安装APK

1. 在手机浏览器中访问 `http://120.48.95.51/download.html`
2. 点击 "Download APK" 下载APK文件
3. 打开下载的APK文件进行安装
4. 如果遇到 "安装被阻止" 的提示，请到 "设置" -> "安全" -> "未知来源" 启用安装来自未知来源的应用

## 故障排除

- **无法访问Web服务器**：检查服务器防火墙是否允许80端口访问
- **构建失败**：检查GitHub Actions日志，确保所有Secrets都已正确配置
- **APK下载失败**：检查文件权限和Nginx配置