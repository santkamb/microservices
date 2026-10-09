# Quick Start Guide - Product Service

## ⚡ 5-Minute Setup

### Step 1: Install Prerequisites
1. Download Java 17: https://www.oracle.com/java/technologies/downloads/#java17
2. Download MySQL: https://dev.mysql.com/downloads/mysql/
3. Set `JAVA_HOME` environment variable to your Java installation

### Step 2: Create Database
```bash
# Open MySQL Command Line
mysql -u root -p

# Run these commands
CREATE DATABASE product_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE product_db;
```

### Step 3: Start the Service
```bash
cd product-service
mvnw spring-boot:run
```

### Step 4: Access Swagger UI
Open browser: http://localhost:8080/api/v1/swagger-ui.html

---

## 🎯 Common Operations

### Create a Product
```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop with 16GB RAM",
    "price": 999.99,
    "quantity": 50,
    "category": "Electronics",
    "sku": "LAPTOP-001"
  }'
```

### Get All Products
```bash
curl http://localhost:8080/api/v1/products
```

### Search Products
```bash
curl "http://localhost:8080/api/v1/products/search/by-name?name=Laptop"
```

### Filter by Category
```bash
curl "http://localhost:8080/api/v1/products/category/Electronics"
```

### Update a Product
```bash
curl -X PUT http://localhost:8080/api/v1/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Laptop",
    "description": "Updated description",
    "price": 1199.99,
    "quantity": 45,
    "category": "Electronics",
    "sku": "LAPTOP-001"
  }'
```

### Delete a Product
```bash
curl -X DELETE http://localhost:8080/api/v1/products/1
```

---

## 🔧 Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| `JAVA_HOME not set` | Set environment variable to your Java installation path |
| `Connection refused` | Ensure MySQL is running and `product_db` database exists |
| `Port 8080 in use` | Change port: `mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"` |
| `Tables not created` | Check application.properties: `spring.jpa.hibernate.ddl-auto=update` |

---

## 📱 API Features

✅ CRUD Operations  
✅ Search & Filter  
✅ Pagination & Sorting  
✅ Input Validation  
✅ Error Handling  
✅ Swagger Documentation  
✅ Health Checks  

---

## 📚 Next Steps

1. Read full documentation: `README.md`
2. View API documentation: Swagger UI
3. Check database schema
4. Run unit tests: `mvnw test`
5. Deploy to production

---

**Ready to go!** 🚀
