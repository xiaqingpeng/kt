# SSH认证失败问题解决方案

## 问题识别

在GitHub Actions构建过程中，我们遇到了以下错误：

```
drone-scp error:  error copy file to dest: ***, error message: ssh: handshake failed: ssh: unable to authenticate, attempted methods [none password], no supported methods remain
```

这个错误发生在`appleboy/scp-action@v0.1.4`步骤中，表明SSH连接认证失败。

## 原因分析

可能的原因：
1. GitHub Secrets中的用户名或密码不正确
2. 服务器不允许密码认证方式
3. 服务器的SSH配置有问题
4. 网络连接问题

## 解决方案

### 1. 检查GitHub Secrets配置

首先，确保您在GitHub仓库中正确配置了以下Secrets：

- `SERVER_HOST`: 服务器的IP地址或域名
- `SERVER_USERNAME`: SSH登录用户名
- `SERVER_PASSWORD`: SSH登录密码

**配置步骤**：
1. 进入GitHub仓库页面
2. 点击"Settings" -> "Secrets and variables" -> "Actions"
3. 检查并更新上述三个Secrets的值
4. 确保值正确无误，没有多余的空格或特殊字符

### 2. 验证服务器SSH设置

在服务器上检查SSH配置：

```bash
# 检查SSH配置文件
cat /etc/ssh/sshd_config

# 确保PasswordAuthentication设置为yes
grep PasswordAuthentication /etc/ssh/sshd_config

# 检查SSH服务状态
systemctl status sshd
```

如果`PasswordAuthentication`设置为`no`，您需要将其改为`yes`并重启SSH服务：

```bash
sudo sed -i 's/PasswordAuthentication no/PasswordAuthentication yes/' /etc/ssh/sshd_config
sudo systemctl restart sshd
```

### 3. 测试服务器连接

从本地机器测试SSH连接，确保用户名和密码正确：

```bash
ssh username@server_ip
```

如果连接成功，说明用户名和密码正确。

### 4. 考虑使用SSH密钥认证

密码认证可能会遇到各种问题，推荐使用SSH密钥认证，这更加安全和可靠：

**生成SSH密钥对**：
```bash
ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
```

**将公钥上传到服务器**：
```bash
ssh-copy-id username@server_ip
```

**在GitHub Secrets中配置SSH密钥**：
1. 创建一个新的Secret，例如`SSH_PRIVATE_KEY`，值为私钥内容
2. 修改GitHub Actions工作流，使用SSH密钥认证

**修改GitHub Actions工作流**：
```yaml
- name: Upload APK to Server
  uses: appleboy/scp-action@v0.1.4
  with:
    host: ${{ secrets.SERVER_HOST }}
    username: ${{ secrets.SERVER_USERNAME }}
    key: ${{ secrets.SSH_PRIVATE_KEY }}
    source: "app/build/outputs/apk/release/app-release-unsigned.apk"
    target: "/var/www/html/apk"
    overwrite: true
```

### 5. 检查网络连接

确保服务器可以通过SSH访问：

```bash
# 检查服务器是否在线
ping server_ip

# 检查SSH端口是否开放
nc -zv server_ip 22
```

## 接下来的步骤

1. **检查GitHub Secrets配置**
   - 验证`SERVER_HOST`、`SERVER_USERNAME`和`SERVER_PASSWORD`的正确性

2. **验证服务器SSH设置**
   - 确保服务器允许密码认证
   - 检查SSH服务是否正常运行

3. **测试服务器连接**
   - 从本地机器测试SSH连接

4. **考虑使用SSH密钥认证**
   - 生成SSH密钥对
   - 将公钥上传到服务器
   - 在GitHub Secrets中配置SSH私钥
   - 修改工作流使用SSH密钥认证

## 创建的文档

- `SSH_AUTHENTICATION_FIX.md`: SSH认证失败问题的详细解决方案
