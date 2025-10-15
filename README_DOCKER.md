# Hướng dẫn Deploy Winmart Backend lên VPS với Docker

## Yêu cầu hệ thống

- VPS với hệ điều hành Linux (Ubuntu 20.04+ khuyến nghị)
- Docker Engine 20.10+
- Docker Compose 2.0+
- Tối thiểu 2GB RAM
- Tối thiểu 10GB ổ cứng trống

## Cài đặt Docker trên VPS

### Ubuntu/Debian

```bash
# Cập nhật package index
sudo apt-get update

# Cài đặt các package cần thiết
sudo apt-get install -y \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

# Thêm Docker's official GPG key
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

# Thêm Docker repository
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Cài đặt Docker Engine
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# Kiểm tra cài đặt
sudo docker --version
sudo docker compose version
```

## Deploy Application

### 1. Upload code lên VPS

```bash
# Sử dụng git (khuyến nghị)
git clone <repository-url>
cd winmart

# Hoặc sử dụng scp để copy từ máy local
scp -r /path/to/winmart user@your-vps-ip:/home/user/
```

### 2. Cấu hình môi trường

Tạo file `.env` (tùy chọn) để override các biến môi trường:

```bash
# Database Configuration
POSTGRES_DB=winmart_db
POSTGRES_USER=winmart
POSTGRES_PASSWORD=your_secure_password_here

# Application Configuration
SERVER_PORT=3333

# JWT Configuration
JWT_SECRET=your-super-secret-key-change-this-in-production
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000
```

### 3. Chạy application

```bash
# Build và start containers
sudo docker compose up -d

# Xem logs
sudo docker compose logs -f

# Chỉ xem logs của app
sudo docker compose logs -f winmart-app

# Chỉ xem logs của database
sudo docker compose logs -f postgres-db
```

### 4. Kiểm tra trạng thái

```bash
# Kiểm tra containers đang chạy
sudo docker compose ps

# Kiểm tra health của application
curl http://localhost:3333/actuator/health
```

## Cấu hình Firewall

### UFW (Ubuntu)

```bash
# Cho phép SSH (quan trọng!)
sudo ufw allow 22/tcp

# Cho phép HTTP/HTTPS
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp

# Cho phép port application
sudo ufw allow 3333/tcp

# Enable firewall
sudo ufw enable

# Kiểm tra status
sudo ufw status
```

### Firewalld (CentOS/RHEL)

```bash
# Cho phép port application
sudo firewall-cmd --permanent --add-port=3333/tcp
sudo firewall-cmd --reload

# Kiểm tra
sudo firewall-cmd --list-ports
```

## Cấu hình Nginx Reverse Proxy (Khuyến nghị cho Production)

### 1. Cài đặt Nginx

```bash
sudo apt-get update
sudo apt-get install -y nginx
```

### 2. Cấu hình Nginx

Tạo file `/etc/nginx/sites-available/winmart`:

```nginx
server {
    listen 80;
    server_name your-domain.com;  # Thay bằng domain của bạn
    
    client_max_body_size 20M;
    
    location / {
        proxy_pass http://localhost:3333;
        proxy_http_version 1.1;
        
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }
    
    # Health check endpoint
    location /actuator/health {
        proxy_pass http://localhost:3333;
        access_log off;
    }
}
```

### 3. Enable site và restart Nginx

```bash
# Tạo symbolic link
sudo ln -s /etc/nginx/sites-available/winmart /etc/nginx/sites-enabled/

# Kiểm tra cấu hình
sudo nginx -t

# Restart Nginx
sudo systemctl restart nginx

# Enable Nginx khởi động cùng hệ thống
sudo systemctl enable nginx
```

### 4. Cài đặt SSL với Let's Encrypt (Tùy chọn)

```bash
# Cài đặt Certbot
sudo apt-get install -y certbot python3-certbot-nginx

# Lấy SSL certificate
sudo certbot --nginx -d your-domain.com

# Certificate sẽ tự động renew
```

## Truy cập Application

Sau khi deploy thành công:

- **Direct access**: `http://your-vps-ip:3333`
- **Via Nginx**: `http://your-domain.com` hoặc `http://your-vps-ip`
- **API Health check**: `http://your-vps-ip:3333/actuator/health`

## Quản lý Container

### Các lệnh cơ bản

```bash
# Stop containers
sudo docker compose stop

# Start containers
sudo docker compose start

# Restart containers
sudo docker compose restart

# Stop và remove containers
sudo docker compose down

# Stop, remove containers và volumes
sudo docker compose down -v

# Rebuild và restart
sudo docker compose up -d --build

# Xem logs realtime
sudo docker compose logs -f

# Vào bên trong container
sudo docker exec -it winmart-service sh

# Xem resource usage
sudo docker stats
```

### Database Management

```bash
# Backup database
sudo docker exec winmart-postgres pg_dump -U winmart winmart_db > backup_$(date +%Y%m%d_%H%M%S).sql

# Restore database
sudo docker exec -i winmart-postgres psql -U winmart winmart_db < backup.sql

# Connect to database
sudo docker exec -it winmart-postgres psql -U winmart -d winmart_db
```

## Troubleshooting

### Container không start

```bash
# Xem logs chi tiết
sudo docker compose logs

# Kiểm tra network
sudo docker network ls

# Kiểm tra volumes
sudo docker volume ls
```

### Application không kết nối được database

```bash
# Kiểm tra database đã ready chưa
sudo docker compose logs postgres-db

# Test connection từ app container
sudo docker exec -it winmart-service sh
wget --spider http://postgres-db:5432
```

### Port đã được sử dụng

```bash
# Kiểm tra port đang được sử dụng
sudo netstat -tulpn | grep 3333

# Hoặc
sudo lsof -i :3333

# Kill process nếu cần
sudo kill -9 <PID>
```

### Xóa toàn bộ và rebuild

```bash
# Stop và remove mọi thứ
sudo docker compose down -v

# Remove images
sudo docker rmi $(sudo docker images | grep winmart | awk '{print $3}')

# Rebuild từ đầu
sudo docker compose up -d --build
```

## Monitoring và Logs

### Xem logs

```bash
# Tất cả logs
sudo docker compose logs -f

# Logs của service cụ thể
sudo docker compose logs -f winmart-app

# Số dòng logs cuối cùng
sudo docker compose logs --tail=100 winmart-app
```

### Monitoring resources

```bash
# Real-time monitoring
sudo docker stats

# Disk usage
sudo docker system df
```

## Update Application

### Cập nhật code mới

```bash
# Pull code mới
git pull

# Rebuild và restart
sudo docker compose up -d --build

# Hoặc rebuild specific service
sudo docker compose up -d --build winmart-app
```

## Cleanup

### Dọn dẹp Docker resources

```bash
# Remove unused images
sudo docker image prune -a

# Remove unused volumes
sudo docker volume prune

# Remove unused networks
sudo docker network prune

# Remove everything unused
sudo docker system prune -a --volumes
```

## Production Best Practices

1. **Luôn sử dụng Nginx reverse proxy** thay vì expose trực tiếp port application
2. **Sử dụng SSL/TLS** cho bảo mật
3. **Thay đổi JWT_SECRET** và database password mặc định
4. **Backup database** thường xuyên
5. **Monitor logs** và resources
6. **Sử dụng Docker secrets** cho sensitive data trong production
7. **Setup log rotation** để tránh disk full
8. **Cấu hình restart policy** phù hợp
9. **Test health check endpoint** thường xuyên

## Support

Nếu gặp vấn đề, kiểm tra:
1. Logs của container: `sudo docker compose logs`
2. Resource usage: `sudo docker stats`
3. Network connectivity
4. Firewall rules
5. Database connection

## Thông tin ports

- **Application**: Port 3333
- **PostgreSQL**: Port 5432 (chỉ accessible từ container network)
- **Nginx** (nếu dùng): Port 80 (HTTP), 443 (HTTPS)

