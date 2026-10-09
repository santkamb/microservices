#!/bin/bash
# Product Service API - Curl Examples
# Make sure the service is running on http://localhost:8080

BASE_URL="http://localhost:8080/api/v1"

echo "=========================================="
echo "Product Service API - Testing Examples"
echo "=========================================="
echo ""

# 1. Create a Product
echo "1. Creating Product..."
PRODUCT_RESPONSE=$(curl -s -X POST "$BASE_URL/products" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MacBook Pro",
    "description": "Premium laptop with M2 chip",
    "price": 1999.99,
    "quantity": 30,
    "category": "Electronics",
    "sku": "MACBOOK-M2-001"
  }')
echo "Response: $PRODUCT_RESPONSE"
PRODUCT_ID=$(echo $PRODUCT_RESPONSE | grep -o '"id":[0-9]*' | grep -o '[0-9]*')
echo "Created Product ID: $PRODUCT_ID"
echo ""

# 2. Create Another Product
echo "2. Creating Another Product..."
curl -s -X POST "$BASE_URL/products" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Dell XPS",
    "description": "High performance Windows laptop",
    "price": 1299.99,
    "quantity": 25,
    "category": "Electronics",
    "sku": "DELL-XPS-001"
  }' | jq '.'
echo ""

# 3. Get Product by ID
echo "3. Getting Product by ID..."
curl -s "$BASE_URL/products/$PRODUCT_ID" | jq '.'
echo ""

# 4. Get All Products with Pagination
echo "4. Getting All Products (Paginated)..."
curl -s "$BASE_URL/products?page=0&size=10" | jq '.'
echo ""

# 5. Search by Name
echo "5. Searching Products by Name 'MacBook'..."
curl -s "$BASE_URL/products/search/by-name?name=MacBook&page=0&size=10" | jq '.'
echo ""

# 6. Filter by Category
echo "6. Filtering by Category 'Electronics'..."
curl -s "$BASE_URL/products/category/Electronics?page=0&size=10" | jq '.'
echo ""

# 7. Filter by Price Range
echo "7. Filtering by Price Range (1000-2000)..."
curl -s "$BASE_URL/products/price-range?minPrice=1000&maxPrice=2000&page=0&size=10" | jq '.'
echo ""

# 8. Complex Search
echo "8. Complex Search (searchTerm + category)..."
curl -s "$BASE_URL/products/search?searchTerm=laptop&category=Electronics&page=0&size=10" | jq '.'
echo ""

# 9. Update Product
echo "9. Updating Product..."
curl -s -X PUT "$BASE_URL/products/$PRODUCT_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MacBook Pro 16\"",
    "description": "Premium laptop with M3 Max chip",
    "price": 2499.99,
    "quantity": 25,
    "category": "Electronics",
    "sku": "MACBOOK-M3-001"
  }' | jq '.'
echo ""

# 10. Update Product Quantity
echo "10. Updating Product Quantity to 15..."
curl -s -X PATCH "$BASE_URL/products/$PRODUCT_ID/quantity?quantity=15" | jq '.'
echo ""

# 11. Get Low Stock Products (threshold = 20)
echo "11. Getting Low Stock Products (threshold=20)..."
curl -s "$BASE_URL/products/low-stock?threshold=20&page=0&size=10" | jq '.'
echo ""

# 12. Test Validation - Invalid Product
echo "12. Testing Validation - Creating Product with Invalid Data..."
curl -s -X POST "$BASE_URL/products" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "description": "Short",
    "price": -100,
    "quantity": -5,
    "category": "X",
    "sku": "SKU"
  }' | jq '.'
echo ""

# 13. Delete Product
echo "13. Deleting Product (Soft Delete)..."
curl -s -X DELETE "$BASE_URL/products/$PRODUCT_ID" -w "\nStatus: %{http_code}\n"
echo ""

# 14. Try to Get Deleted Product (should fail)
echo "14. Trying to Get Deleted Product (should fail)..."
curl -s "$BASE_URL/products/$PRODUCT_ID" | jq '.'
echo ""

# 15. Health Check
echo "15. Health Check..."
curl -s "http://localhost:8080/api/v1/actuator/health" | jq '.'
echo ""

# 16. Metrics
echo "16. Available Metrics..."
curl -s "http://localhost:8080/api/v1/actuator/metrics" | jq '.names[]' | head -10
echo ""

# 17. API Documentation
echo "17. API Documentation available at:"
echo "    http://localhost:8080/api/v1/swagger-ui.html"
echo "    http://localhost:8080/api/v1/v3/api-docs"
echo ""

echo "=========================================="
echo "Testing Complete!"
echo "=========================================="
