# Exercise 1 - Products & Categories RESTful API

RESTful CRUD API built with Express Router + Mongoose, following the
`routes / controllers / models` layering from the lab.

## Run

```bash
# MongoDB must be running on mongodb://127.0.0.1:27017
npm install
npm start
```

Optional environment variables:

| Variable      | Default                                   |
| ------------- | ----------------------------------------- |
| `PORT`        | `3000`                                    |
| `MONGODB_URI` | `mongodb://127.0.0.1:27017/shop_lab3_db`  |

## Endpoints

### Products

| Method | Path                | Description                              |
| ------ | ------------------- | ---------------------------------------- |
| GET    | `/api/products`     | List + filter (`category`, `minPrice`, `maxPrice`) + pagination (`page`, `limit`) |
| GET    | `/api/products/:id` | Product details                          |
| POST   | `/api/products`     | Create a product (201)                   |
| PUT    | `/api/products/:id` | Update a product                         |
| DELETE | `/api/products/:id` | Delete a product                         |

### Categories (extended requirement)

| Method | Path                  | Description                                       |
| ------ | --------------------- | ------------------------------------------------- |
| GET    | `/api/categories`     | List + pagination (`page`, `limit`) + sorting (`sort`, `order`) |
| GET    | `/api/categories/:id` | Category details                                  |
| POST   | `/api/categories`     | Create (name >= 3 chars, slug auto-generated)     |
| PUT    | `/api/categories/:id` | Full update                                       |
| PATCH  | `/api/categories/:id` | Partial update (e.g. only `description`)          |
| DELETE | `/api/categories/:id` | Delete                                            |

All category endpoints return `404` for a valid but missing ID, `400` for a
malformed ID or invalid input, and `409` for a duplicate `name`/`slug`.

## Quick test

```bash
curl -X POST http://localhost:3000/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Laptop Dell XPS 15","category":"Electronics","price":35000000,"stock":10}'

curl "http://localhost:3000/api/products?category=Electronics&minPrice=1000000&page=1&limit=5"

curl -X POST http://localhost:3000/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name":"Home & Living","description":"Furniture and decor"}'
# -> slug: "home-living"
```
