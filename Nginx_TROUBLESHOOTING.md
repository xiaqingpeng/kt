# Nginx安装问题解决方案

根据您提供的Nginx安装日志，遇到了以下问题：
1. 端口80已被占用：`Not attempting to start NGINX, port 80 is already in use.`
2. Nginx启动失败：`Job for nginx.service failed because the control process exited with error code.`

以下是解决方案：

## 1. 检查占用80端口的进程

执行以下命令查看哪个进程占用了80端口：

```bash
lsof -i :80
```

或者使用：

```bash
netstat -tulpn | grep :80
```

## 2. 停止占用80端口的进程

根据上一步的输出，找到占用80端口的进程ID（PID），然后停止该进程：

```bash
kill -9 <进程ID>
```

例如，如果Apache占用了80端口：

```bash
systemctl stop apache2
systemctl disable apache2
```

## 3. 启动Nginx

```bash
systemctl start nginx
```

## 4. 检查Nginx状态

```bash
systemctl status nginx
```

## 5. 测试Nginx是否正常运行

在浏览器中访问：`http://120.48.95.51`

或者在服务器上执行：

```bash
curl http://localhost
```

如果看到Nginx的欢迎页面，说明Nginx已经正常运行。

## 6. 备用方案：更改Nginx端口

如果您不想停止占用80端口的进程，可以修改Nginx配置使用其他端口（例如8080）：

1. 编辑Nginx配置文件：

```bash
nano /etc/nginx/sites-available/default
```

2. 将listen指令修改为其他端口：

```nginx
listen 8080 default_server;
listen [::]:8080 default_server;
```

3. 保存并退出编辑器

4. 重启Nginx：

```bash
systemctl restart nginx
```

5. 访问：`http://120.48.95.51:8080`

## 7. 配置防火墙

确保防火墙允许Nginx使用的端口：

```bash
# 允许80端口
ufw allow 80/tcp

# 如果使用8080端口
ufw allow 8080/tcp

# 重新加载防火墙
ufw reload
```

## 8. 重新测试CI/CD流程

解决Nginx问题后，重新推送代码到GitHub测试CI/CD流程：

```bash
git add .
git commit -m "Update CI/CD configuration"
git push
```

然后查看GitHub Actions构建进度和结果。