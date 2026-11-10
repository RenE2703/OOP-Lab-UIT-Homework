// Main server entry point - Homework (Orders & Inventory API with MySQL)
const express = require('express');
const pool = require('./config/db');
const orderRoutes = require('./routes/order.routes');
const customerRoutes = require('./routes/customer.routes');
const { notFound, errorHandler } = require('./middlewares/errorHandler');

const app = express();
const PORT = Number(process.env.PORT) || 3001;

app.use(express.json());

app.get('/', (req, res) => {
    res.status(200).json({
        success: true,
        message: 'Lab 3 - Orders & Inventory Management API',
        endpoints: {
            orders: '/api/v1/orders',
            salesSummary: '/api/v1/orders/analytics/sales-summary',
            customerHistory: '/api/v1/customers/:id/order-history'
        }
    });
});

app.use('/api/v1/orders', orderRoutes);
app.use('/api/v1/customers', customerRoutes);

app.use(notFound);
app.use(errorHandler);

async function start() {
    // Fail fast when MySQL is unreachable or the schema was not imported.
    await pool.query('SELECT 1');
    app.listen(PORT, () => {
        console.log(`Orders API is running at: http://localhost:${PORT}`);
    });
}

start().catch((error) => {
    console.error('MySQL connection failed:', error.message);
    process.exit(1);
});
