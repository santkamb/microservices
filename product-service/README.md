# Product Service - E-Commerce Microservice

## 📋 Overview

A comprehensive, production-ready Product Service built with Spring Boot that manages all product-related operations in the e-commerce platform. This service implements real-world operations including CRUD operations, search, filtering, pagination, and integration with Eureka service discovery.

## ✨ Features Implemented

### Core Features
- ✅ **Product Management**: Create, Read, Update, Delete (CRUD) operations
- ✅ **Search & Filter**: 
  - Search by product name
  - Filter by category
  - Filter by price range
  - Complex search combining multiple criteria
- ✅ **Pagination & Sorting**: All list endpoints support pagination and sorting
- ✅ **Inventory Management**: Track stock quantities and identify low-stock products
- ✅ **Soft Delete**: Inactive instead of permanent deletion for data retention
- ✅ **Timestamps**: Automatic createdAt and updatedAt tracking

### Technical Features
- ✅ **Input Validation**: JSR-303/JSR-380 annotations with comprehensive validation
- ✅ **Global Exception Handling**: Centralized error handling with custom exceptions
- ✅ **API Documentation**: OpenAPI 3.0/Swagger UI for interactive API exploration
- ✅ **Comprehensive Logging**: SLF4J logging throughout the application
- ✅ **Service Discovery**: Eureka client integration for microservice registration
- ✅ **Health Checks & Metrics**: Spring Boot Actuator endpoints
- ✅ **Transaction Management**: @Transactional annotations for data consistency
- ✅ **Database Indexing**: Strategic indexes for query performance

## 🏗️ Project Structure

```
product-service/
├── src/main/java/com/ecommerce/product_service/
│   ├── entity/
│   │   └── Product.java                 # JPA Entity with business logic
│   ├── repository/
│   │   └── ProductRepository.java       # Data access layer with custom queries
│   ├── service/
│   │   └── ProductService.java          # Business logic and operations
│   ├── controller/
│   │   └── ProductController.java       # REST API endpoints
│   ├── dto/
│   │   ├── ProductRequest.java          # Request DTO with validation
│   │   ├── ProductResponse.java         # Response DTO
│   │   └── ProductListResponse.java     # Paginated response DTO
│   ├── exception/
│   │   ├── ResourceNotFoundException.java
│   │   ├── ErrorResponse.java
│   │   └── GlobalExceptionHandler.java  # Global exception handler
│   ├── config/
│   │   └── OpenApiConfig.java           # Swagger/OpenAPI configuration
│   └── ProductServiceApplication.java   # Main application class
├── src/test/java/com/ecommerce/product_service/
│   ├── service/
│   │   └── ProductServiceTest.java      # Unit tests
│   └── controller/
│       └── ProductControllerTest.java   # Integration tests
├── src/main/resources/
│   └── application.properties           # Application configuration
└── pom.xml                              # Maven dependencies
```

## 📊 Database Schema

### Products Table
```sql
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    category VARCHAR(100) NOT NULL,
    sku VARCHAR(100) UNIQUE,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    
    INDEX idx_category (category),
    INDEX idx_name (name),
    INDEX idx_price (price)
);
```

## 🚀 Prerequisites & Setup

### System Requirements
- **Java**: JDK 17 or higher
- **Maven**: 3.6.0 or higher
- **MySQL**: 5.7 or higher (or MariaDB 10.3+)
- **Memory**: 2GB minimum for compilation

### Installation Steps

#### 1. Install Java 17
```bash
# Windows - Download from https://www.oracle.com/java/technologies/downloads/
# Set JAVA_HOME environment variable to your Java installation directory
# Example: C:\Program Files\Java\jdk-17.0.0

# Verify Java installation
java -version
```

#### 2. Install MySQL Database
```bash
# Windows - Download from https://dev.mysql.com/downloads/mysql/
# Start MySQL service
# Create database
mysql -u root -p -e "CREATE DATABASE product_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

#### 3. Clone and Build the Project
```bash
cd product-service
.\mvnw clean install
```

## ⚙️ Configuration

### Database Configuration (application.properties)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/product_db?serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

### Eureka Service Discovery
```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.instance.prefer-ip-address=true
```

### API Context Path
```properties
server.servlet.context-path=/api/v1
server.port=8080
```

## 🏃 Running the Application

### Start the Service
```bash
cd product-service
.\mvnw spring-boot:run
```

### Access the API
- **Swagger UI**: http://localhost:8080/api/v1/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/v1/v3/api-docs
- **Actuator Health**: http://localhost:8080/api/v1/actuator/health
- **Actuator Metrics**: http://localhost:8080/api/v1/actuator/metrics

## 📡 API Endpoints

### Product Management

#### Create Product
```
POST /api/v1/products
Content-Type: application/json

{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "quantity": 50,
  "category": "Electronics",
  "sku": "LAPTOP-001"
}
```

#### Get Product by ID
```
GET /api/v1/products/{id}
```

#### Get All Products (Paginated)
```
GET /api/v1/products?page=0&size=10&sortBy=id&direction=ASC
```

#### Search by Name
```
GET /api/v1/products/search/by-name?name=Laptop&page=0&size=10
```

#### Filter by Category
```
GET /api/v1/products/category/Electronics?page=0&size=10
```

#### Filter by Price Range
```
GET /api/v1/products/price-range?minPrice=500&maxPrice=1500&page=0&size=10
```

#### Complex Search
```
GET /api/v1/products/search?searchTerm=laptop&category=Electronics&page=0&size=10
```

#### Get Low Stock Products
```
GET /api/v1/products/low-stock?threshold=10&page=0&size=10
```

#### Update Product
```
PUT /api/v1/products/{id}
Content-Type: application/json

{
  "name": "Updated Laptop",
  "description": "Updated description",
  "price": 1099.99,
  "quantity": 45,
  "category": "Electronics",
  "sku": "LAPTOP-001"
}
```

#### Update Product Quantity
```
PATCH /api/v1/products/{id}/quantity?quantity=30
```

#### Delete Product (Soft Delete)
```
DELETE /api/v1/products/{id}
```

## 🧪 Testing

### Run All Tests
```bash
.\mvnw clean test
```

### Run Specific Test Class
```bash
.\mvnw test -Dtest=ProductServiceTest
.\mvnw test -Dtest=ProductControllerTest
```

### Test Coverage Report
```bash
.\mvnw clean test jacoco:report
# Report available at: target/site/jacoco/index.html
```

## 📝 Input Validation

### ProductRequest Validation Rules
- **name**: Required, 3-255 characters
- **description**: Required, 10-2000 characters
- **price**: Required, must be > 0
- **quantity**: Required, non-negative integer
- **category**: Required, 2-100 characters
- **sku**: Required, 3-100 characters, unique

### Example Error Response
```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2024-09-17T23:03:12",
  "path": "/api/v1/products",
  "validationErrors": {
    "name": "Product name is required",
    "price": "Price must be greater than 0"
  }
}
```

## 🔍 Search & Filter Examples

### Find Products by Multiple Criteria
```bash
# Electronics between $500-1500
GET /api/v1/products/price-range?minPrice=500&maxPrice=1500

# Search for gaming laptops in Electronics
GET /api/v1/products/search?searchTerm=gaming&category=Electronics

# Find products with low stock
GET /api/v1/products/low-stock?threshold=20
```

## 📊 Query Examples

### Database Queries

```sql
-- Find all products by category
SELECT * FROM products WHERE category = 'Electronics' AND is_active = true;

-- Find products by price range
SELECT * FROM products WHERE price BETWEEN 500 AND 1500 AND is_active = true;

-- Find low stock products
SELECT * FROM products WHERE quantity < 10 AND is_active = true;

-- Search by name
SELECT * FROM products WHERE name LIKE '%laptop%' AND is_active = true;

-- Complex search
SELECT * FROM products 
WHERE (name LIKE '%gaming%' OR description LIKE '%gaming%')
  AND category = 'Electronics'
  AND is_active = true;

-- Get product count by category
SELECT category, COUNT(*) as count FROM products WHERE is_active = true GROUP BY category;
```

## 🔐 Security & Best Practices

- **Input Validation**: All inputs validated using JSR-303 annotations
- **SQL Injection Prevention**: Using JPA with parameterized queries
- **Error Handling**: Custom exceptions with no stack traces exposed
- **Logging**: Sensitive data not logged
- **Soft Deletes**: Data retention for audit trails
- **Transaction Management**: Proper isolation levels maintained
- **API Documentation**: Comprehensive OpenAPI documentation

## 🚦 Logging & Monitoring

### Log Levels
```properties
logging.level.com.ecommerce.product_service=DEBUG  # Application logs
logging.level.org.springframework.web=INFO          # Web framework
logging.level.org.hibernate=INFO                    # Database operations
```

### Actuator Endpoints
- `/actuator/health` - Service health status
- `/actuator/metrics` - Application metrics
- `/actuator/info` - Application information

## 🔗 Integration with Eureka

The service automatically registers with Eureka upon startup:
```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.instance.instance-id=${spring.application.name}:${server.port}
```

This enables:
- Service discovery
- Load balancing
- Health monitoring
- Dynamic routing

## 📦 Dependencies

### Core Dependencies
- Spring Boot 4.1.1
- Spring Data JPA
- Spring Web MVC
- Spring Cloud (Eureka)
- MySQL Connector

### Testing
- JUnit 5
- Mockito
- Spring Test
- TestContainers

### Utilities
- Lombok (boilerplate reduction)
- SLF4J (logging)
- Springdoc OpenAPI (Swagger UI)

## 🐛 Troubleshooting

### Issue: Build fails with JAVA_HOME error
**Solution**: Set JAVA_HOME environment variable
```bash
# Windows
set JAVA_HOME=C:\Program Files\Java\jdk-17
# Verify
echo %JAVA_HOME%
```

### Issue: Database connection refused
**Solution**: Ensure MySQL is running
```bash
# Windows
net start MySQL80  # or your MySQL service name
```

### Issue: Port 8080 already in use
**Solution**: Change the port in application.properties
```properties
server.port=8081
```

### Issue: Tests fail due to database
**Solution**: Tests use H2 in-memory database by default. Ensure test profile is active.

## 📈 Performance Optimization

### Implemented Optimizations
1. **Database Indexes**: On category, name, and price columns
2. **Read-Only Transactions**: @Transactional(readOnly = true) for queries
3. **Pagination**: Prevent loading large datasets
4. **Connection Pooling**: HikariCP (default in Spring Boot)
5. **Lazy Loading**: Used appropriately in entities

### Query Optimization Tips
```java
// Use pagination for large results
Pageable pageable = PageRequest.of(0, 10);
Page<Product> products = productRepository.findByIsActiveTrue(pageable);

// Use specific queries instead of lazy loading all
List<Product> products = productRepository.findByCategory("Electronics");

// Use database indexes for filtering
// Avoid SELECT * - specify required fields
```

## 🔄 Future Enhancements

- Product images/media storage
- Product ratings and reviews
- Inventory notifications
- Batch operations (bulk create/update)
- Product variants and options
- Advanced analytics and reporting
- Caching layer (Redis)
- Event-driven architecture (Kafka)
- GraphQL API
- API rate limiting

## 📄 License

This project is licensed under the Apache 2.0 License.

## 👥 Support

For issues, questions, or contributions, contact the E-Commerce Team.

---

**Version**: 1.0.0  
**Last Updated**: 2024-09-17  
**Status**: Production Ready ✅
