# API Bất Động Sản

## Tổng Quan
Dự án này là một API backend được phát triển bằng **Java Spring Boot**, sử dụng **PostgreSQL** làm cơ sở dữ liệu và **Redis** để caching. Ứng dụng được thiết kế để xử lý các thao tác liên quan đến đăng tin bất động sản, bao gồm tạo mới, cập nhật và lấy thông tin các tin đăng.

Dự án cũng sử dụng **Docker** để chạy PostgreSQL và Redis trong các container, giúp việc thiết lập môi trường dễ dàng và nhất quán.

---

## Tính Năng
- Xác thực và phân quyền người dùng.
- API quản lý tin đăng bất động sản:
  - Tạo tin đăng mới.
  - Cập nhật tin đăng.
  - Lấy danh sách tin đăng với các tùy chọn lọc (ví dụ: vị trí, giá, diện tích).
- Tích hợp Redis để caching dữ liệu truy cập thường xuyên.
- Xử lý lỗi và xác thực dữ liệu toàn diện.

---

## Công Nghệ Sử Dụng

### Backend
- **Java Spring Boot**
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Validation

### Cơ Sở Dữ Liệu
- **PostgreSQL** (Cơ sở dữ liệu quan hệ để lưu trữ dữ liệu có cấu trúc)

### Caching
- **Redis** (Caching trong bộ nhớ để cải thiện hiệu suất)

### Containerization
- **Docker** (Dùng để container hóa PostgreSQL và Redis, giúp dễ dàng triển khai)

---

## Yêu Cầu

### Công Cụ Cần Thiết
- **Java 17**
- **Maven**
- **Docker** và **Docker Compose**

### Biến Môi Trường
Tạo file `.env` trong thư mục gốc với các biến sau:

```env
# PostgreSQL
DATA_BASE_URL=jdbc:postgresql://db:5432/real_estate_db
DATA_BASE_USER=postgres
DATA_BASE_PASSWORD=yourpassword

# Redis
REDIS_HOST=redis
REDIS_PORT=6379

# Server
PORT_SERVER=8080
```

---

## Bắt Đầu

### 1. Clone Repository
```bash
git clone https://github.com/Long23112002/bds-be
cd bds-be
```

### 2. Build Dự Án
```bash
mvn clean install
```

### 3. Chạy Docker Containers
Đảm bảo Docker đã được cài đặt và chạy, sau đó thực hiện:
```bash
docker-compose up -d
```
Lệnh này sẽ khởi động các container PostgreSQL và Redis.

### 4. Khởi Động Ứng Dụng
```bash
mvn spring-boot:run
```
Ứng dụng sẽ chạy tại `http://localhost:8888`.

## Cấu Hình Docker

### Docker Compose
File `docker-compose.yml` được cấu hình để chạy PostgreSQL và Redis:

```yaml
version: '3.8'
services:
  db:
    image: postgres:15
    container_name: postgres_db
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: yourpassword
      POSTGRES_DB: real_estate_db
    ports:
      - "5432:5432"

  redis:
    image: redis:7
    container_name: redis_cache
    ports:
      - "6379:6379"
```

---

## Kiểm Thử
Chạy bộ kiểm thử với lệnh:
```bash
mvn test
```

---


## Giấy Phép
Dự án này được cấp phép theo [MIT License](LICENSE).

---


