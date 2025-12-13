# Nginx多进程管理解决方案

根据您提供的`lsof -i :80`输出，我发现Nginx正在以多进程模式运行，这是Nginx的正常工作方式。当您尝试直接kill单个进程时，Nginx的主进程会自动启动新的工作进程，这就是为什么总是有新的进程PID出现。

以下是正确的解决方案：

## 1. 使用systemctl停止Nginx服务

不要直接kill进程，而是使用systemctl停止整个Nginx服务：

```bash
systemctl stop nginx
```

## 2. 验证Nginx已停止

检查Nginx服务状态：

```bash
systemctl status nginx
```

确保输出显示Nginx已停止。

## 3. 再次检查80端口

确认80端口不再被Nginx占用：

```bash
lsof -i :80
```

## 4. 重新启动Nginx

```bash
systemctl start nginx
```

## 5. 验证Nginx正常运行

```bash
systemctl status nginx
```

输出应该显示Nginx正在运行。

## 6. 测试Web服务

在浏览器中访问：`http://120.48.95.51`

或者在服务器上执行：

```bash
curl http://localhost
```

## 7. 重新触发CI/CD流程

1. 确保服务器上的Web服务正常运行
2. 在GitHub仓库中配置好Secrets
3. 推送代码到GitHub：

```bash
git add .
git commit -m "Fix Nginx configuration"
git push
```

4. 查看GitHub Actions构建进度
5. 构建完成后访问`http://120.48.95.51/download.html`下载APK

## 重要说明

- Nginx默认以多进程模式运行，主进程负责管理工作进程
- 直接kill工作进程会导致主进程立即启动新的工作进程
- 始终使用systemctl命令管理Nginx服务的启动、停止和重启
- 如果需要彻底禁用Nginx服务，可以使用：`systemctl disable nginx`

如果您在配置过程中遇到任何其他问题，请随时咨询。