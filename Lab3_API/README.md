# Lab 3 - RESTful APIs, Routing, Postman & Swagger

Implementation of all three parts of Lab 3:

| Folder | Content |
| ------ | ------- |
| `exercise1-products-api/` | Exercise 1: Products CRUD with Express Router + Mongoose, plus the extended Category module (slug hook, validation middleware, pagination, 400/404/409) |
| `exercise2-swagger-apidocs/` | Exercise 2: the same API with interactive Swagger UI (OpenAPI 3.0) |
| `homework-orders-mysql/` | Homework: Orders & Inventory API with MySQL, transactions, stock handling and analytics |

Each folder is a standalone Node.js project with its own `package.json`.

## Requirements

- Node.js 18+
- MongoDB running on `mongodb://127.0.0.1:27017` (Exercise 1 & 2)
- MySQL running on `127.0.0.1:3306` (Homework)

## Quick start

```bash
# Exercise 1 - port 3000
cd exercise1-products-api && npm install && npm start

# Exercise 2 - port 3000, Swagger UI at /api-docs
cd exercise2-swagger-apidocs && npm install && npm start

# Homework - port 3001
cd homework-orders-mysql
mysql -u root -p < sql/schema.sql
mysql -u root -p < sql/seed.sql
npm install && npm start
```

See each folder's `README.md` for the endpoint tables and sample requests.

## Notes

- Controllers stay thin: they read `req`, call the model/service and send JSON.
- All errors go through one global handler, which maps `ValidationError` /
  `CastError` to `400`, duplicate keys (`11000`) to `409`, and MySQL foreign-key
  errors to `400`.
- The homework API uses a transaction + `SELECT ... FOR UPDATE` so two orders
  cannot oversell the same stock, and restores stock on cancel/delete.
