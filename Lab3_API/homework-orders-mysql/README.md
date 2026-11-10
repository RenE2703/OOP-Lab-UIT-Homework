# Homework - Orders & Inventory Management API (MySQL)

Production-style e-commerce orders API built with Node.js, Express and
`mysql2/promise`, split into `routes / controllers / services`.

## 1. Create the database

```bash
mysql -u root -p < sql/schema.sql
mysql -u root -p < sql/seed.sql
```

`schema.sql` creates `ecommerce_order_db` with the four related tables
(`customers`, `products`, `orders`, `order_items`) and their foreign keys.
`seed.sql` inserts 4 customers, 5 products, 5 orders and 7 order items.

## 2. Run the API

```bash
npm install
npm start
```

Environment variables (all optional):

| Variable      | Default                 |
| ------------- | ----------------------- |
| `PORT`        | `3001`                  |
| `DB_HOST`     | `127.0.0.1`             |
| `DB_PORT`     | `3306`                  |
| `DB_USER`     | `root`                  |
| `DB_PASSWORD` | *(empty)*               |
| `DB_NAME`     | `ecommerce_order_db`    |

## 3. Endpoints

| Method | Path | Description |
| ------ | ---- | ----------- |
| POST   | `/api/v1/orders` | Create an order inside a transaction (checks customer, products and stock, calculates the total, inserts order + items, deducts stock) |
| GET    | `/api/v1/orders` | List orders with `status`, `customerId`, `page`, `limit`; JOINs customers and sums item quantities |
| GET    | `/api/v1/orders/:id` | Order details with customer info and nested items (`product_name`, `quantity`, `unit_price`, `subtotal`) |
| PATCH  | `/api/v1/orders/:id/status` | Change status; cancelling restores stock; an already cancelled order cannot change again |
| DELETE | `/api/v1/orders/:id` | Restore stock if needed, then delete (items cascade) |
| GET    | `/api/v1/orders/analytics/sales-summary` | Revenue aggregation + top 3 best-selling products |
| GET    | `/api/v1/customers/:id/order-history` | All orders of a customer + `totalLifetimeSpent` |

## 4. Sample requests

```bash
# Create an order (prices are read from the database, not from the payload)
curl -X POST http://localhost:3001/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId":1,"items":[{"productId":1,"quantity":1},{"productId":3,"quantity":2}]}'

# List pending orders, page 1
curl "http://localhost:3001/api/v1/orders?status=pending&page=1&limit=5"

# Order details
curl http://localhost:3001/api/v1/orders/1

# Cancel an order (restores stock)
curl -X PATCH http://localhost:3001/api/v1/orders/2/status \
  -H "Content-Type: application/json" \
  -d '{"status":"cancelled"}'

# Revenue analytics
curl http://localhost:3001/api/v1/orders/analytics/sales-summary

# Customer history
curl http://localhost:3001/api/v1/customers/1/order-history
```

## 5. Error cases covered

| Case | Status |
| ---- | ------ |
| Unknown customer / product / order | `404` |
| Invalid payload, bad quantity, invalid status, out of stock | `400` |
| Already cancelled order changes status again | `400` |
| Malformed JSON body | `400` |
| Duplicate unique value | `409` |
