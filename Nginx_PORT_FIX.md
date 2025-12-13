# Nginx端口占用彻底解决方案

根据您提供的最新日志，Nginx仍然无法启动，错误信息显示：`nginx: [emerg] bind() to 0.0.0.0:80 failed (98: Address already in use)`。这表明即使执行了`systemctl stop nginx`，端口80仍然被占用。

以下是彻底解决这个问题的步骤：

## 1. 检查所有占用端口80的进程

执行以下命令查看所有占用80端口的进程，包括TCP和UDP：

```bash
netstat -tulpn | grep :80
```

或者使用更详细的命令：

```bash
ss -tulpn | grep :80
```

## 2. 强制终止所有占用80端口的进程

使用`killall`命令终止所有可能占用80端口的进程：

```bash
# 终止所有Nginx进程
killall -9 nginx

# 终止Apache进程（如果存在）
killall -9 apache2

# 终止其他可能占用80端口的Web服务器进程
killall -9 httpd
killall -9 lighttpd
```

## 3. 再次检查端口占用情况

确保没有进程占用80端口：

```bash
netstat -tulpn | grep :80
```

如果输出为空，说明80端口已释放。

## 4. 清理Nginx配置和状态

```bash
# 清理Nginx运行时文件
rm -rf /var/run/nginx/*

# 重新加载systemd配置
systemctl daemon-reload
```

## 5. 启动Nginx

```bash
systemctl start nginx
```

## 6. 检查Nginx状态

```bash
systemctl status nginx
```

## 7. 验证Web服务

在服务器上执行：

```bash
curl http://localhost
```

如果看到Nginx的欢迎页面，说明Nginx已成功启动。

## 8. 备用方案：修改Nginx端口

如果仍然无法释放80端口，可以修改Nginx配置使用其他端口（如8080）：

1. 编辑Nginx配置文件：

```bash
nano /etc/nginx/sites-available/default
```

2. 将listen指令修改为：

```nginx
listen 8080 default_server;
listen [::]:8080 default_server;
```

3. 保存并退出编辑器

4. 启动Nginx：

```bash
systemctl start nginx
```

5. 测试访问：

```bash
curl http://localhost:8080
```

6. 更新CI/CD配置：

如果更改了端口，需要在GitHub Actions配置文件中更新服务器地址：

编辑`.github/workflows/android.yml`文件，将下载HTML中的链接更新为：

```html
<a href='http://120.48.95.51:8080/app-release.apk' download>Download APK</a>
```

## 9. 防火墙配置

确保防火墙允许新端口的访问：

```bash
# 允许8080端口
ufw allow 8080/tcp

# 重新加载防火墙
ufw reload
```

## 10. 重新触发CI/CD流程

1. 确保服务器上的Web服务正常运行
2. 在GitHub仓库中配置好Secrets
3. 推送代码到GitHub：

```bash
git add .
git commit -m "Fix Nginx port issue"
git push
```

4. 查看GitHub Actions构建进度
5. 构建完成后访问下载页面

## 重要提示

- 如果您使用了云服务提供商（如阿里云、腾讯云等），请确保安全组规则允许相应端口的访问
- 如果修改了端口，请更新所有相关的配置和链接
- 定期检查服务器状态，确保Nginx正常运行

如果您在配置过程中遇到任何其他问题，请随时咨询。