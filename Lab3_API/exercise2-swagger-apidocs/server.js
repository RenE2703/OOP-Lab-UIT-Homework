// Main server entry point - Exercise 2 (API + Swagger UI)
const express = require('express');
const connectDatabase = require('./config/db');
const setupSwagger = require('./config/swagger');
const Product = require('./models/product.model');
const Category = require('./models/category.model');
const productRoutes = require('./routes/product.routes');
const categoryRoutes = require('./routes/category.routes');

const app = express();
const PORT = Number(process.env.PORT) || 3000;

app.use(express.json());

// Interactive API documentation at http://localhost:3000/api-docs
setupSwagger(app);

app.get('/', (req, res) => {
    res.status(200).json({
        success: true,
        message: 'Lab 3 - Products & Categories RESTful API',
        documentation: '/api-docs',
        endpoints: {
            products: '/api/products',
            categories: '/api/categories'
        }
    });
});

// Register API routes
app.use('/api/products', productRoutes);
app.use('/api/categories', categoryRoutes);

// Unknown route handler
app.use((req, res) => {
    res.status(404).json({
        success: false,
        error: `Route ${req.method} ${req.originalUrl} not found`
    });
});

// Global error handler (must be registered AFTER the routes)
app.use((err, req, res, next) => {
    console.error('Server Error:', err.message);

    let status = err.status || 500;
    let message = err.message || 'Internal server error';

    if (err.name === 'ValidationError') {
        status = 400; // schema validation failed
        message = Object.values(err.errors).map((item) => item.message).join(', ');
    }
    if (err.name === 'CastError') {
        status = 400; // malformed ObjectId
        message = `Invalid value for field "${err.path}": ${err.value}`;
    }
    if (err.code === 11000) {
        status = 409; // duplicate unique key
        message = `Duplicate value for field: ${Object.keys(err.keyValue || {}).join(', ')}`;
    }

    res.status(status).json({ success: false, error: message });
});

connectDatabase()
    .then(async () => {
        // Build the unique indexes before accepting requests (name/slug uniqueness).
        // A low-disk or already-dirty collection should not stop the API from booting.
        try {
            await Promise.all([Product.init(), Category.init()]);
        } catch (indexError) {
            console.warn('Could not build indexes:', indexError.message);
        }
        app.listen(PORT, () => {
            console.log(`Server API is running at: http://localhost:${PORT}`);
            console.log(`Swagger UI: http://localhost:${PORT}/api-docs`);
        });
    })
    .catch((err) => {
        console.error('MongoDB connection failed:', err.message);
        process.exit(1);
    });
