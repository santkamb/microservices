# Product Service - Developer Guide

## 🛠️ Development Environment Setup

### Prerequisites Installation

#### Windows

**1. Java 17 Installation**
```bash
# Download from https://www.oracle.com/java/technologies/downloads/#java17
# Install to C:\Program Files\Java\jdk-17.x.x

# Set JAVA_HOME (Environment Variables)
JAVA_HOME=C:\Program Files\Java\jdk-17.x.x

# Verify
java -version
javac -version
```

**2. MySQL Installation**
```bash
# Download from https://dev.mysql.com/downloads/mysql/
# Install and start MySQL service
# Default port: 3306
# Default user: root

# Verify
mysql --version
mysql -u root -p -e "SELECT VERSION();"
```

**3. Maven Installation** (Optional - mvnw included)
```bash
# If using system Maven:
# Download from https://maven.apache.org/download.cgi
# Set M2_HOME and add to PATH

mvn -version
```

#### Mac/Linux

```bash
# Using Homebrew
brew install openjdk@17
brew install mysql

# Or using package managers
sudo apt-get install openjdk-17-jdk    # Ubuntu/Debian
sudo yum install java-17-openjdk       # RedHat/CentOS
```

---

## 📦 Build & Run Commands

### Clean Build
```bash
cd product-service
.\mvnw clean install
```

### Run Application
```bash
# Using Spring Boot Maven plugin
.\mvnw spring-boot:run

# Using Java directly (after build)
java -jar target/product-service-0.0.1-SNAPSHOT.jar

# With custom port
.\mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Run Tests
```bash
# All tests
.\mvnw clean test

# Specific test class
.\mvnw test -Dtest=ProductServiceTest
.\mvnw test -Dtest=ProductControllerTest

# With coverage
.\mvnw clean test jacoco:report
# Report: target/site/jacoco/index.html
```

### Build JAR
```bash
.\mvnw clean package -DskipTests
# Output: target/product-service-0.0.1-SNAPSHOT.jar
```

### Compile Only
```bash
.\mvnw clean compile
```

### Skip Tests
```bash
.\mvnw clean install -DskipTests
```

---

## 🗄️ Database Management

### Initialize Database

**1. Create Database**
```bash
# Using MySQL CLI
mysql -u root -p -e "CREATE DATABASE product_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# Verify
mysql -u root -p -e "SHOW DATABASES LIKE 'product_db';"
```

**2. Run Initialization Script**
```bash
mysql -u root -p product_db < init-database.sql
```

**3. Verify Tables Created**
```bash
mysql -u root -p product_db -e "SHOW TABLES;"
mysql -u root -p product_db -e "DESCRIBE products;"
```

### Useful Database Queries

**View All Products**
```sql
SELECT * FROM product_db.products WHERE is_active = true;
```

**View Products by Category**
```sql
SELECT * FROM product_db.products WHERE category = 'Electronics' AND is_active = true;
```

**View Low Stock Products**
```sql
SELECT * FROM product_db.products WHERE quantity < 20 AND is_active = true;
```

**Database Statistics**
```sql
SELECT 
  COUNT(*) as total_products,
  COUNT(DISTINCT category) as categories,
  MIN(price) as min_price,
  MAX(price) as max_price,
  AVG(price) as avg_price,
  SUM(quantity) as total_stock
FROM product_db.products 
WHERE is_active = true;
```

### Reset Database
```bash
# Drop and recreate
mysql -u root -p -e "DROP DATABASE product_db;"
mysql -u root -p < init-database.sql
```

---

## 🌐 API Testing

### Using cURL

**Create Product**
```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "Test laptop",
    "price": 999.99,
    "quantity": 50,
    "category": "Electronics",
    "sku": "LAPTOP-001"
  }'
```

**Get All Products**
```bash
curl http://localhost:8080/api/v1/products?page=0&size=10
```

**Get Product by ID**
```bash
curl http://localhost:8080/api/v1/products/1
```

**Search by Name**
```bash
curl "http://localhost:8080/api/v1/products/search/by-name?name=Laptop"
```

**Update Product**
```bash
curl -X PUT http://localhost:8080/api/v1/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Laptop",
    "description": "Updated description",
    "price": 1099.99,
    "quantity": 45,
    "category": "Electronics",
    "sku": "LAPTOP-001"
  }'
```

**Delete Product**
```bash
curl -X DELETE http://localhost:8080/api/v1/products/1
```

### Using Postman

1. Open Postman
2. Import `Postman_Collection.json`
3. Set base URL: `http://localhost:8080/api/v1`
4. Run requests from collection

### Using Swagger UI

1. Start application
2. Open http://localhost:8080/api/v1/swagger-ui.html
3. Interactive API documentation
4. Test endpoints directly from UI

---

## 🔍 Debugging

### Enable Debug Mode
```bash
# Run with debug flag
.\mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"

# Connect debugger on port 5005
```

### Logging Levels

**application.properties**
```properties
# Enable DEBUG for application
logging.level.com.ecommerce.product_service=DEBUG

# Reduce noise from frameworks
logging.level.org.springframework.web=INFO
logging.level.org.hibernate=INFO
logging.level.org.springframework.data=INFO
```

### View Application Logs

**In Console** (when running from IDE or Maven)
- Look for log output with timestamps
- Filter by class name

**Application Log File** (if configured)
```properties
logging.file.name=logs/application.log
logging.file.max-size=10MB
logging.file.max-history=30
```

---

## 📋 Code Organization Best Practices

### Adding New Endpoint

**1. Update Controller**
```java
@PostMapping("/new-endpoint")
@Operation(summary = "Description")
public ResponseEntity<ResponseType> newEndpoint(@Valid @RequestBody RequestType request) {
    // Log request
    log.info("POST /new-endpoint");
    // Call service
    ResponseType response = service.newMethod(request);
    // Return response
    return new ResponseEntity<>(response, HttpStatus.OK);
}
```

**2. Add Service Method**
```java
public ResponseType newMethod(RequestType request) {
    log.info("Processing new method");
    // Business logic
    // Return result
}
```

**3. Add Repository Query** (if needed)
```java
@Query("SELECT e FROM Entity e WHERE e.field = :value")
Page<Entity> findByField(@Param("value") String value, Pageable pageable);
```

**4. Write Tests**
```java
@Test
void testNewMethod_Success() {
    // Arrange
    when(repository.method()).thenReturn(expected);
    
    // Act
    ResponseType result = service.newMethod(input);
    
    // Assert
    assertNotNull(result);
    assertEquals(expected, result);
}
```

---

## 🔄 Common Development Tasks

### Add New Validation Rule

**In DTO**
```java
@NotEmpty(message = "Field is required")
@Size(min = 5, max = 50, message = "Field must be 5-50 characters")
private String fieldName;
```

**Validation Annotations Available**
- `@NotNull` - Not null
- `@NotBlank` - Not empty/blank
- `@Size(min, max)` - String length
- `@Min/@Max` - Numeric range
- `@DecimalMin/@DecimalMax` - Decimal range
- `@Email` - Email format
- `@Pattern(regexp)` - Regex pattern

### Add New Search Filter

**1. Add Repository Query**
```java
@Query("SELECT p FROM Product p WHERE p.newField = :value")
Page<Product> findByNewField(@Param("value") String value, Pageable pageable);
```

**2. Add Service Method**
```java
public ProductListResponse filterByNewField(String value, Pageable pageable) {
    log.info("Filtering by newField: {}", value);
    Page<Product> products = productRepository.findByNewField(value, pageable);
    return mapToPageResponse(products);
}
```

**3. Add Controller Endpoint**
```java
@GetMapping("/filter/new-field/{value}")
public ResponseEntity<ProductListResponse> filterByNewField(
        @PathVariable String value,
        Pageable pageable) {
    ProductListResponse response = productService.filterByNewField(value, pageable);
    return ResponseEntity.ok(response);
}
```

### Add New Database Field

**1. Update Entity**
```java
@Column(name = "new_field")
private String newField;
```

**2. Run Application**
- With `spring.jpa.hibernate.ddl-auto=update`, changes auto-apply

**3. Verify in Database**
```sql
DESCRIBE product_db.products;
```

### Add Custom Exception

**1. Create Exception Class**
```java
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
```

**2. Add Handler**
```java
@ExceptionHandler(CustomException.class)
public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, WebRequest request) {
    ErrorResponse error = new ErrorResponse(400, ex.getMessage(), ...);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
}
```

---

## 📊 Performance Optimization Tips

### Database Optimization
```sql
-- Check query performance
EXPLAIN SELECT * FROM products WHERE category = 'Electronics';

-- Analyze table
ANALYZE TABLE products;

-- Optimize table
OPTIMIZE TABLE products;
```

### Application Optimization
```properties
# Connection pool tuning
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5

# JPA optimization
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.jdbc.fetch_size=50
```

### Caching Setup (Future)
```java
@Cacheable("products")
public ProductResponse getProductById(Long id) {
    // Will be cached after first call
}

@CacheEvict(value = "products", key = "#id")
public void updateProduct(Long id, ProductRequest request) {
    // Will invalidate cache
}
```

---

## 🔐 Security Checklist

- ✅ All inputs validated with JSR-303
- ✅ Prepared statements used (no SQL injection)
- ✅ Error messages don't expose internals
- ✅ Sensitive data not logged
- ✅ CORS configured appropriately
- ✅ Ready for JWT authentication
- ✅ Proper HTTP status codes used

### Add JWT Authentication (Future)
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // JWT filter configuration
}
```

---

## 📚 IDE Setup

### IntelliJ IDEA

**1. Import Project**
- File → Open → Select product-service folder
- Select "Maven" when prompted

**2. Configure Java SDK**
- File → Project Structure → SDK → Select JDK 17

**3. Run Configuration**
- Run → Edit Configurations
- Add "Maven" configuration
- Command line: `spring-boot:run`

**4. Debug**
- Set breakpoints (click line number)
- Right-click → Debug

### VS Code

**1. Install Extensions**
- Extension Pack for Java
- REST Client (for API testing)

**2. Configure Java Home**
```json
{
  "java.home": "C:\\Program Files\\Java\\jdk-17",
  "maven.executable.path": "./mvnw"
}
```

**3. Run & Debug**
- Press F5 to debug
- Use REST Client extension for API testing

---

## 📝 Code Style Guide

### Naming Conventions
- **Classes**: PascalCase (ProductService)
- **Methods**: camelCase (getProductById)
- **Constants**: UPPER_SNAKE_CASE (MAX_PRODUCTS)
- **Variables**: camelCase (productName)

### Code Organization
```java
// 1. Class declaration with annotations
@Service
@Slf4j
public class ProductService {
    
    // 2. Dependencies
    @Autowired
    private ProductRepository repository;
    
    // 3. Public methods
    public ProductResponse getProduct(Long id) {
        // Implementation
    }
    
    // 4. Private helper methods
    private ProductResponse mapToResponse(Product product) {
        // Implementation
    }
}
```

### Documentation
```java
/**
 * Retrieves a product by its ID.
 * 
 * @param id the product identifier
 * @return ProductResponse containing product details
 * @throws ResourceNotFoundException if product not found
 */
public ProductResponse getProductById(Long id) {
    // Implementation
}
```

---

## 🚀 Deployment Checklist

- ✅ All tests passing
- ✅ Code review completed
- ✅ Database migrations run
- ✅ Environment variables configured
- ✅ Logging configured
- ✅ Actuator endpoints verified
- ✅ Performance tested
- ✅ Security audit passed
- ✅ Documentation updated
- ✅ Backup strategy in place

---

## 📞 Troubleshooting

### Issue: Application won't start
```
Solution: Check application.properties and database connection
```

### Issue: Tests fail
```
Solution: Ensure H2 in-memory database is available or configure test database
```

### Issue: API endpoint returns 404
```
Solution: Verify endpoint path matches controller mapping and context path
```

### Issue: Database connection timeout
```
Solution: Verify MySQL is running and credentials are correct
```

---

## 📖 Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [OpenAPI Specification](https://spec.openapis.org/oas/v3.0.0)
- [REST API Best Practices](https://restfulapi.net/)

---

**Last Updated**: September 17, 2024  
**Version**: 1.0.0
