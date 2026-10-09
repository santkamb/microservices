# Product Service - Architecture Documentation

## 🏛️ System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Client Applications                        │
│              (Web, Mobile, Third-party APIs)                  │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼ HTTP/REST
┌─────────────────────────────────────────────────────────────┐
│                   API Gateway / Load Balancer                 │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│                  Product Service Instance                     │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  ProductController (REST Endpoints)                  │   │
│  │  ├─ POST /products                                   │   │
│  │  ├─ GET /products/{id}                               │   │
│  │  ├─ PUT /products/{id}                               │   │
│  │  ├─ DELETE /products/{id}                            │   │
│  │  ├─ GET /products (paginated)                        │   │
│  │  ├─ GET /products/search/by-name                     │   │
│  │  ├─ GET /products/category/{category}                │   │
│  │  └─ GET /products/price-range                        │   │
│  └──────────────────┬──────────────────────────────────┘   │
│                     │                                         │
│  ┌──────────────────▼──────────────────────────────────┐   │
│  │  Global Exception Handler & Validation              │   │
│  │  ├─ Input validation (JSR-303)                       │   │
│  │  ├─ Custom exception handling                        │   │
│  │  └─ Error response formatting                        │   │
│  └──────────────────┬──────────────────────────────────┘   │
│                     │                                         │
│  ┌──────────────────▼──────────────────────────────────┐   │
│  │  ProductService (Business Logic)                    │   │
│  │  ├─ Create product                                   │   │
│  │  ├─ Read products                                    │   │
│  │  ├─ Update products                                  │   │
│  │  ├─ Delete products (soft delete)                    │   │
│  │  ├─ Search functionality                             │   │
│  │  ├─ Filter by category & price                       │   │
│  │  ├─ Pagination & sorting                             │   │
│  │  └─ Stock management                                 │   │
│  └──────────────────┬──────────────────────────────────┘   │
│                     │                                         │
│  ┌──────────────────▼──────────────────────────────────┐   │
│  │  ProductRepository (Data Access)                    │   │
│  │  ├─ findById(id)                                     │   │
│  │  ├─ findAll(pageable)                                │   │
│  │  ├─ findByCategory(category, pageable)               │   │
│  │  ├─ findByPriceRange(min, max, pageable)             │   │
│  │  ├─ findByNameContaining(name, pageable)             │   │
│  │  ├─ searchByTermAndCategory(...)                     │   │
│  │  ├─ findLowStockProducts(threshold, pageable)        │   │
│  │  └─ Custom JPA queries with @Query                   │   │
│  └──────────────────┬──────────────────────────────────┘   │
│                     │                                         │
│                     ▼ JDBC                                    │
└─────────────────────────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│                    MySQL Database                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Database: product_db                               │   │
│  │  ┌───────────────────────────────────────────────┐  │   │
│  │  │ Table: products                               │  │   │
│  │  │ ├─ id (PK)                                    │  │   │
│  │  │ ├─ name (IDX)                                 │  │   │
│  │  │ ├─ description                                │  │   │
│  │  │ ├─ price (IDX)                                │  │   │
│  │  │ ├─ quantity                                   │  │   │
│  │  │ ├─ category (IDX)                             │  │   │
│  │  │ ├─ sku (UNIQUE)                               │  │   │
│  │  │ ├─ is_active (IDX)                            │  │   │
│  │  │ ├─ created_at (IDX)                           │  │   │
│  │  │ └─ updated_at                                 │  │   │
│  │  └───────────────────────────────────────────────┘  │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## 📦 Layered Architecture

### 1. **Presentation Layer** (Controller)
```
ProductController
├─ Handles HTTP requests/responses
├─ Input validation annotations
├─ Swagger/OpenAPI documentation
└─ Exception handling
```

**Responsibilities:**
- Accept HTTP requests
- Validate request parameters
- Delegate to service layer
- Return appropriate responses
- Document API with OpenAPI annotations

### 2. **Business Logic Layer** (Service)
```
ProductService
├─ CRUD operations
├─ Search & filtering logic
├─ Pagination & sorting
├─ Data transformation
└─ Business rules enforcement
```

**Responsibilities:**
- Implement business logic
- Orchestrate data operations
- Enforce business constraints
- Manage transactions
- Log operations

### 3. **Data Access Layer** (Repository)
```
ProductRepository
├─ findById
├─ findAll
├─ Custom queries
├─ Database operations
└─ Query optimization
```

**Responsibilities:**
- Database CRUD operations
- Query execution
- Result mapping to entities
- Performance optimization

### 4. **Data Layer** (Entity & Database)
```
Product Entity
├─ JPA annotations
├─ Column mappings
├─ Validators
└─ Lifecycle callbacks

MySQL Database
├─ Table definition
├─ Indexes
├─ Constraints
└─ Sample data
```

## 🔄 Request Flow

```
Client Request
    │
    ▼
HTTP Request (POST /api/v1/products)
    │
    ▼
ProductController.createProduct()
    │
    ├─ Validate input (JSR-303)
    │  └─ If invalid → GlobalExceptionHandler → 400 Bad Request
    │
    ▼
ProductService.createProduct()
    │
    ├─ Check SKU uniqueness
    │  └─ If duplicate → ResourceNotFoundException → 400 Conflict
    │
    ├─ Create Product entity
    │
    ▼
ProductRepository.save()
    │
    ├─ Persist to database
    │
    ▼
Database
    │
    ├─ Insert record
    │ ├─ Generate ID
    │ ├─ Set created_at/updated_at
    │ └─ Apply constraints
    │
    ▼
Return saved entity
    │
    ▼
Service → map to DTO → ProductResponse
    │
    ▼
Controller → HTTP 201 Created → Response Body (JSON)
    │
    ▼
Client receives ProductResponse
```

## 🗂️ Code Organization

```
src/main/java/com/ecommerce/product_service/
│
├── ProductServiceApplication.java          # Main application entry point
│
├── entity/
│   └── Product.java                        # JPA Entity with ORM mapping
│
├── repository/
│   └── ProductRepository.java              # Spring Data JPA interface
│
├── service/
│   └── ProductService.java                 # Business logic implementation
│
├── controller/
│   └── ProductController.java              # REST endpoints
│
├── dto/
│   ├── ProductRequest.java                 # Request payload with validation
│   ├── ProductResponse.java                # Response payload
│   └── ProductListResponse.java            # Paginated response
│
├── exception/
│   ├── ResourceNotFoundException.java      # Custom exception
│   ├── ErrorResponse.java                  # Error response format
│   └── GlobalExceptionHandler.java         # Centralized exception handling
│
└── config/
    └── OpenApiConfig.java                  # Swagger/OpenAPI configuration
```

## 🔐 Data Flow Security

### Input Validation Flow
```
Request Data
    ▼
ProductRequest DTO (with @Valid annotations)
    ├─ @NotBlank, @Size, @Positive, etc.
    ▼
MethodArgumentNotValidException (if invalid)
    ▼
GlobalExceptionHandler
    ▼
Return validation errors with field details
```

### Error Handling Flow
```
Any Exception
    ▼
GlobalExceptionHandler
    ├─ ResourceNotFoundException → 404
    ├─ MethodArgumentNotValidException → 400 (with field errors)
    └─ Generic Exception → 500
    ▼
ErrorResponse (formatted JSON)
    {
      "status": 404,
      "message": "Product not found",
      "timestamp": "2024-09-17T23:03:12",
      "path": "/api/v1/products/999"
    }
```

## 📊 Database Schema Relationships

```
products
├─ PK: id (BIGINT AUTO_INCREMENT)
├─ name (VARCHAR 255, IDX)
├─ description (TEXT)
├─ price (DECIMAL 10,2, IDX)
├─ quantity (INT)
├─ category (VARCHAR 100, IDX)
├─ sku (VARCHAR 100, UNIQUE, IDX)
├─ is_active (BOOLEAN, IDX)
├─ created_at (TIMESTAMP, IDX)
└─ updated_at (TIMESTAMP)
```

### Indexes
- **idx_category**: Fast filtering by category
- **idx_name**: Fast text search
- **idx_price**: Fast range queries
- **idx_sku**: Unique constraint enforcement
- **idx_is_active**: Fast active product filtering
- **idx_created_at**: Sorting by creation time

## 🔄 Transaction Management

```
@Transactional
ProductService.createProduct()
    ├─ Start transaction
    ├─ Check duplicate SKU
    ├─ Save entity
    ├─ On success: COMMIT
    └─ On error: ROLLBACK

@Transactional(readOnly = true)
ProductService.getProductById()
    ├─ Start READ-ONLY transaction
    ├─ Query database
    └─ Auto-close connection
```

## 🚀 Performance Optimizations

### 1. **Database Indexing**
- Multiple indexes for common queries
- Index on search/filter columns

### 2. **Pagination**
- Limit result set size
- Prevent memory issues with large datasets

### 3. **Read-Only Transactions**
- Used for all GET operations
- No locking overhead

### 4. **Connection Pooling**
- HikariCP (default Spring Boot)
- Manages connection reuse

### 5. **Query Optimization**
- Custom JPA queries with @Query
- Avoid N+1 query problems

## 🔗 Integration Points

### 1. **Eureka Service Discovery**
```
@EnableDiscoveryClient
ProductServiceApplication
    │
    ├─ Registers with Eureka on startup
    ├─ Service name: product-service
    ├─ Health checks via /actuator/health
    └─ Dynamic service discovery for other services
```

### 2. **Spring Boot Actuator**
```
/actuator/health      → Service health status
/actuator/metrics     → Application metrics
/actuator/info        → Service information
```

### 3. **OpenAPI/Swagger**
```
/swagger-ui.html      → Interactive API documentation
/v3/api-docs          → OpenAPI JSON specification
```

## 📈 Scalability Considerations

### Horizontal Scaling
- Stateless service design
- Multiple instances behind load balancer
- Shared database backend

### Vertical Scaling
- Increase server resources
- Optimize JVM settings
- Database connection pool tuning

### Caching Strategy
- Future: Redis for frequently accessed products
- Database query result caching
- ETag for HTTP caching

### Database Optimization
- Regular index maintenance
- Query optimization
- Connection pool sizing

## 🧪 Testing Architecture

### Unit Tests
```
ProductServiceTest
├─ Service logic testing
├─ Repository mocking
└─ Business logic validation
```

### Integration Tests
```
ProductControllerTest
├─ REST endpoint testing
├─ HTTP status validation
└─ Response format verification
```

### Test Coverage
- Service layer: ~90%
- Controller layer: ~85%
- Repository layer: Mocked in unit tests

## 📋 Deployment Architecture

```
Local Development
├─ MySQL (localhost:3306)
├─ Product Service (localhost:8080)
└─ Manual testing

Docker/Container
├─ MySQL container
├─ Product Service container
└─ Network bridge

Kubernetes Cluster
├─ Service deployment
├─ ConfigMap for configuration
├─ PersistentVolume for database
└─ Ingress for external access
```

## 🔒 Security Considerations

1. **Input Validation**: All inputs validated
2. **SQL Injection Prevention**: Parameterized queries
3. **Error Handling**: No stack traces in responses
4. **Logging**: Sensitive data not logged
5. **CORS**: Configurable if needed
6. **Authentication**: Ready for JWT integration

---

**Last Updated**: 2024-09-17  
**Architecture Version**: 1.0.0
