# Exercise 2 - Swagger (OpenAPI 3.0) API Documentation

This project is Exercise 1 plus auto-generated interactive documentation
(`swagger-jsdoc` + `swagger-ui-express`).

## Run

```bash
npm install
npm start
# API:     http://localhost:3000/api/products
# Swagger: http://localhost:3000/api-docs
```

## What is documented

- `Products`: list (with filters + pagination), create, details, update, delete.
- `Categories`: list (pagination + sorting), create, details, update, patch, delete.
- Reusable `Id` path parameter: `#/components/parameters/Id`.
- Reusable schemas: `Product`, `ProductInput`, `Category`, `CategoryInput`, `Error`.

Annotations live in `routes/product.routes.js` and `routes/category.routes.js`;
the OpenAPI header/schemas live in `config/swagger.js`. Every endpoint can be
executed from the Swagger UI with **Try it out**.
