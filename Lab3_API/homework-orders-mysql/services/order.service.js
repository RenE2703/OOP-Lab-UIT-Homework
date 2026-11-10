// Business logic for orders: transactions, stock handling and analytics.
// Controllers stay thin; every rule lives here.
const pool = require('../config/db');
const ApiError = require('../utils/ApiError');
const { toPositiveInt, roundMoney } = require('../utils/validate');

const ORDER_STATUSES = ['pending', 'completed', 'cancelled'];

// Validate the create-order payload and merge duplicated product lines.
function parseCreatePayload(payload) {
    if (!payload || typeof payload !== 'object') {
        throw new ApiError(400, 'Request body is required');
    }

    const customerId = Number(payload.customerId);
    if (!Number.isInteger(customerId) || customerId <= 0) {
        throw new ApiError(400, 'customerId must be a positive integer');
    }

    if (!Array.isArray(payload.items) || payload.items.length === 0) {
        throw new ApiError(400, 'items must be a non-empty array');
    }

    const items = payload.items.map((item, index) => {
        const productId = Number(item && item.productId);
        const quantity = Number(item && item.quantity);

        if (!Number.isInteger(productId) || productId <= 0) {
            throw new ApiError(400, `items[${index}].productId must be a positive integer`);
        }
        if (!Number.isInteger(quantity) || quantity <= 0) {
            throw new ApiError(400, `items[${index}].quantity must be a positive integer`);
        }
        return { productId, quantity };
    });

    // If the same product appears twice, buy it once with the summed quantity.
    const merged = new Map();
    for (const item of items) {
        merged.set(item.productId, (merged.get(item.productId) || 0) + item.quantity);
    }

    return {
        customerId,
        items: [...merged.entries()].map(([productId, quantity]) => ({ productId, quantity }))
    };
}

// POST /api/v1/orders
async function createOrder(payload) {
    const { customerId, items } = parseCreatePayload(payload);
    const conn = await pool.getConnection();

    try {
        await conn.beginTransaction();

        const [customers] = await conn.query(
            'SELECT id FROM customers WHERE id = ?',
            [customerId]
        );
        if (customers.length === 0) {
            throw new ApiError(404, `Customer ${customerId} not found`);
        }

        // Lock the product rows until commit so concurrent orders cannot oversell stock.
        const productIds = items.map((item) => item.productId);
        const [products] = await conn.query(
            'SELECT id, product_name, price, stock FROM products WHERE id IN (?) FOR UPDATE',
            [productIds]
        );
        const productMap = new Map(products.map((product) => [product.id, product]));

        for (const item of items) {
            const product = productMap.get(item.productId);
            if (!product) {
                throw new ApiError(404, `Product ${item.productId} not found`);
            }
            if (product.stock < item.quantity) {
                throw new ApiError(400, `Product "${product.product_name}" is out of stock`);
            }
        }

        const totalAmount = roundMoney(items.reduce((sum, item) => (
            sum + Number(productMap.get(item.productId).price) * item.quantity
        ), 0));

        const [orderResult] = await conn.query(
            'INSERT INTO orders (customer_id, status, total_amount) VALUES (?, ?, ?)',
            [customerId, 'pending', totalAmount]
        );
        const orderId = orderResult.insertId;

        const itemRows = items.map((item) => [
            orderId,
            item.productId,
            item.quantity,
            productMap.get(item.productId).price
        ]);
        await conn.query(
            'INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES ?',
            [itemRows]
        );

        for (const item of items) {
            await conn.query(
                'UPDATE products SET stock = stock - ? WHERE id = ?',
                [item.quantity, item.productId]
            );
        }

        await conn.commit();
        return await getOrderById(orderId);
    } catch (error) {
        await conn.rollback();
        throw error;
    } finally {
        conn.release();
    }
}

// GET /api/v1/orders
async function listOrders({ status, customerId, page, limit }) {
    const currentPage = Math.max(Number(page) || 1, 1);
    const pageSize = Math.min(Math.max(Number(limit) || 5, 1), 100);
    const offset = (currentPage - 1) * pageSize;

    const conditions = [];
    const params = [];

    if (status !== undefined && status !== '') {
        if (!ORDER_STATUSES.includes(status)) {
            throw new ApiError(400, `status must be one of: ${ORDER_STATUSES.join(', ')}`);
        }
        conditions.push('o.status = ?');
        params.push(status);
    }

    if (customerId !== undefined && customerId !== '') {
        conditions.push('o.customer_id = ?');
        params.push(toPositiveInt(customerId, 'customerId'));
    }

    const whereClause = conditions.length > 0 ? `WHERE ${conditions.join(' AND ')}` : '';

    const [[countRow]] = await pool.query(
        `SELECT COUNT(*) AS totalOrders FROM orders o ${whereClause}`,
        params
    );
    const totalOrders = Number(countRow.totalOrders);

    const [orders] = await pool.query(
        `SELECT o.id AS order_id,
                c.full_name AS customer_name,
                c.email,
                o.status,
                o.total_amount,
                COALESCE(SUM(oi.quantity), 0) AS total_items_count,
                o.created_at
         FROM orders o
         JOIN customers c ON c.id = o.customer_id
         LEFT JOIN order_items oi ON oi.order_id = o.id
         ${whereClause}
         GROUP BY o.id, c.full_name, c.email, o.status, o.total_amount, o.created_at
         ORDER BY o.created_at DESC, o.id DESC
         LIMIT ? OFFSET ?`,
        [...params, pageSize, offset]
    );

    return {
        data: orders,
        pagination: {
            currentPage,
            totalPages: Math.ceil(totalOrders / pageSize),
            totalOrders,
            limit: pageSize
        }
    };
}

// GET /api/v1/orders/:id
async function getOrderById(id) {
    const orderId = toPositiveInt(id);

    const [orders] = await pool.query(
        `SELECT o.id AS order_id, o.customer_id, o.status, o.total_amount, o.created_at,
                c.full_name, c.email, c.phone
         FROM orders o
         JOIN customers c ON c.id = o.customer_id
         WHERE o.id = ?`,
        [orderId]
    );

    if (orders.length === 0) {
        throw new ApiError(404, `Order ${orderId} not found`);
    }

    const [items] = await pool.query(
        `SELECT oi.product_id, p.product_name, oi.quantity, oi.unit_price,
                ROUND(oi.quantity * oi.unit_price, 2) AS subtotal
         FROM order_items oi
         JOIN products p ON p.id = oi.product_id
         WHERE oi.order_id = ?
         ORDER BY oi.id`,
        [orderId]
    );

    const order = orders[0];

    return {
        order_id: order.order_id,
        customer: {
            id: order.customer_id,
            full_name: order.full_name,
            email: order.email,
            phone: order.phone
        },
        status: order.status,
        total_amount: order.total_amount,
        created_at: order.created_at,
        items
    };
}

// PATCH /api/v1/orders/:id/status
async function updateOrderStatus(id, status) {
    const orderId = toPositiveInt(id);

    if (!ORDER_STATUSES.includes(status)) {
        throw new ApiError(400, `status must be one of: ${ORDER_STATUSES.join(', ')}`);
    }

    const conn = await pool.getConnection();

    try {
        await conn.beginTransaction();

        const [orders] = await conn.query(
            'SELECT id, status FROM orders WHERE id = ? FOR UPDATE',
            [orderId]
        );
        if (orders.length === 0) {
            throw new ApiError(404, `Order ${orderId} not found`);
        }

        const currentStatus = orders[0].status;
        if (currentStatus === 'cancelled') {
            throw new ApiError(400, 'Order is already cancelled and cannot change status again');
        }

        if (currentStatus === status) {
            await conn.commit();
            return await getOrderById(orderId);
        }

        // Cancelling gives the reserved quantities back to the inventory.
        if (status === 'cancelled') {
            await conn.query(
                `UPDATE products p
                 JOIN order_items oi ON oi.product_id = p.id
                 SET p.stock = p.stock + oi.quantity
                 WHERE oi.order_id = ?`,
                [orderId]
            );
        }

        await conn.query('UPDATE orders SET status = ? WHERE id = ?', [status, orderId]);
        await conn.commit();

        return await getOrderById(orderId);
    } catch (error) {
        await conn.rollback();
        throw error;
    } finally {
        conn.release();
    }
}

// DELETE /api/v1/orders/:id
async function deleteOrder(id) {
    const orderId = toPositiveInt(id);
    const conn = await pool.getConnection();

    try {
        await conn.beginTransaction();

        const [orders] = await conn.query(
            'SELECT id, status FROM orders WHERE id = ? FOR UPDATE',
            [orderId]
        );
        if (orders.length === 0) {
            throw new ApiError(404, `Order ${orderId} not found`);
        }

        // If the order still holds reserved stock, put it back first.
        if (orders[0].status !== 'cancelled') {
            await conn.query(
                `UPDATE products p
                 JOIN order_items oi ON oi.product_id = p.id
                 SET p.stock = p.stock + oi.quantity
                 WHERE oi.order_id = ?`,
                [orderId]
            );
        }

        // order_items disappear automatically thanks to ON DELETE CASCADE.
        await conn.query('DELETE FROM orders WHERE id = ?', [orderId]);
        await conn.commit();

        return { order_id: orderId };
    } catch (error) {
        await conn.rollback();
        throw error;
    } finally {
        conn.release();
    }
}

// GET /api/v1/orders/analytics/sales-summary
async function getSalesSummary() {
    const [[summary]] = await pool.query(
        `SELECT COALESCE(SUM(total_amount), 0) AS totalRevenue,
                COUNT(*) AS completedOrdersCount,
                COALESCE(AVG(total_amount), 0) AS averageOrderValue
         FROM orders
         WHERE status = 'completed'`
    );

    const [topSellingProducts] = await pool.query(
        `SELECT p.id AS product_id,
                p.product_name,
                SUM(oi.quantity) AS total_quantity_sold,
                SUM(oi.quantity * oi.unit_price) AS total_revenue
         FROM order_items oi
         JOIN orders o ON o.id = oi.order_id AND o.status = 'completed'
         JOIN products p ON p.id = oi.product_id
         GROUP BY p.id, p.product_name
         ORDER BY total_quantity_sold DESC, total_revenue DESC
         LIMIT 3`
    );

    return {
        totalRevenue: roundMoney(summary.totalRevenue),
        completedOrdersCount: Number(summary.completedOrdersCount),
        averageOrderValue: roundMoney(summary.averageOrderValue),
        topSellingProducts
    };
}

// GET /api/v1/customers/:id/order-history
async function getCustomerOrderHistory(id) {
    const customerId = toPositiveInt(id);

    const [customers] = await pool.query(
        'SELECT id, full_name, email, phone, created_at FROM customers WHERE id = ?',
        [customerId]
    );
    if (customers.length === 0) {
        throw new ApiError(404, `Customer ${customerId} not found`);
    }

    const [orders] = await pool.query(
        `SELECT o.id AS order_id,
                o.status,
                o.total_amount,
                o.created_at,
                COALESCE(SUM(oi.quantity), 0) AS total_items
         FROM orders o
         LEFT JOIN order_items oi ON oi.order_id = o.id
         WHERE o.customer_id = ?
         GROUP BY o.id, o.status, o.total_amount, o.created_at
         ORDER BY o.created_at DESC, o.id DESC`,
        [customerId]
    );

    const [[spent]] = await pool.query(
        `SELECT COALESCE(SUM(total_amount), 0) AS totalLifetimeSpent
         FROM orders
         WHERE customer_id = ? AND status = 'completed'`,
        [customerId]
    );

    return {
        customer: customers[0],
        orders,
        totalLifetimeSpent: roundMoney(spent.totalLifetimeSpent)
    };
}

module.exports = {
    createOrder,
    listOrders,
    getOrderById,
    updateOrderStatus,
    deleteOrder,
    getSalesSummary,
    getCustomerOrderHistory
};
