# Quick Deploy Guide - Winmart Backend

## 🚀 Deploy trên VPS trong 5 phút

### Bước 1: Cài đặt Docker trên VPS (Ubuntu)

```bash
# Cài Docker nhanh
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Kiểm tra
docker --version
docker compose version
```

### Bước 2: Upload code lên VPS

```bash
# Clone từ git (khuyến nghị)
git clone <your-repo-url>
cd winmart

# Hoặc sử dụng scp
scp -r ./winmart user@your-vps-ip:/home/user/
```

### Bước 3: Deploy

```bash
# Trên VPS, chạy lệnh:
sudo docker compose up -d --build

# Xem logs
sudo docker compose logs -f
```

### Bước 4: Kiểm tra

```bash
# Health check
curl http://localhost:3333/actuator/health

# Hoặc mở trình duyệt
http://your-vps-ip:3333/actuator/health
```

## 🔥 Cấu hình Firewall (Quan trọng!)

```bash
# Cho phép SSH (QUAN TRỌNG - làm trước tiên!)
sudo ufw allow 22/tcp

# Cho phép port application
sudo ufw allow 3333/tcp

# Enable firewall
sudo ufw enable
```

## 🌐 Truy cập từ bên ngoài

Sau khi deploy, application có thể truy cập tại:
- `http://your-vps-ip:3333`
- Health check: `http://your-vps-ip:3333/actuator/health`

## 📝 Các lệnh thường dùng

```bash
# Xem logs realtime
sudo docker compose logs -f winmart-app

# Restart application
sudo docker compose restart

# Stop application
sudo docker compose stop

# Start lại
sudo docker compose start

# Rebuild và restart
sudo docker compose up -d --build

# Xóa toàn bộ (kể cả database)
sudo docker compose down -v
```

## 🔒 Bảo mật (Production)

### Đổi mật khẩu database và JWT secret

Chỉnh sửa file `docker-compose.yml`:

```yaml
# Đổi password database
POSTGRES_PASSWORD: your_strong_password_here

# Đổi JWT secret
JWT_SECRET: your_super_secret_key_here
```

### Sử dụng Nginx (Khuyến nghị)

```bash
# Cài Nginx
sudo apt install -y nginx

# Tạo file cấu hình
sudo nano /etc/nginx/sites-available/winmart
```

Nội dung file:

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    location / {
        proxy_pass http://localhost:3333;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

Enable và restart:

```bash
sudo ln -s /etc/nginx/sites-available/winmart /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

## 🩺 Troubleshooting

### Container không start

```bash
# Xem logs lỗi
sudo docker compose logs

# Xem logs chi tiết của app
sudo docker compose logs winmart-app
```

### Port bị chiếm

```bash
# Tìm process đang dùng port
sudo lsof -i :3333

# Kill process
sudo kill -9 <PID>
```

### Database không connect được

```bash
# Kiểm tra database logs
sudo docker compose logs postgres-db

# Restart database
sudo docker compose restart postgres-db
```

## 📊 Monitoring

```bash
# Xem resource usage
sudo docker stats

# Xem disk usage
sudo docker system df
```

## 🔄 Update code mới

```bash
# Pull code mới
git pull

# Rebuild và restart
sudo docker compose up -d --build
```

## 💾 Backup Database

```bash
# Backup
sudo docker exec winmart-postgres pg_dump -U winmart winmart_db > backup_$(date +%Y%m%d).sql

# Restore
sudo docker exec -i winmart-postgres psql -U winmart winmart_db < backup.sql
```

## 📞 Support

Nếu gặp lỗi:
1. Kiểm tra logs: `sudo docker compose logs`
2. Kiểm tra firewall: `sudo ufw status`
3. Kiểm tra port: `sudo netstat -tulpn | grep 3333`
4. Kiểm tra resources: `sudo docker stats`

---

## ⚡ Deploy script (Tự động)

### Linux/Mac:
```bash
chmod +x deploy.sh
./deploy.sh
```

### Windows:
```cmd
deploy.bat
```

---

**Ports:**
- Application: `3333`
- PostgreSQL: `5432` (chỉ trong container network)

**Default credentials:**
- DB User: `winmart`
- DB Password: `winmart@2024` (NÊN ĐỔI trong production!)

