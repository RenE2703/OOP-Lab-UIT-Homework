// HTTP layer for orders: read the request, call the service, send JSON.
const asyncHandler = require('../middlewares/asyncHandler');
const orderService = require('../services/order.service');

// POST /api/v1/orders
exports.createOrder = asyncHandler(async (req, res) => {
    const order = await orderService.createOrder(req.body);
    res.status(201).json({
        success: true,
        message: 'Order created successfully',
        data: order
    });
});

// GET /api/v1/orders
exports.listOrders = asyncHandler(async (req, res) => {
    const result = await orderService.listOrders(req.query);
    res.status(200).json({
        success: true,
        count: result.data.length,
        pagination: result.pagination,
        data: result.data
    });
});

// GET /api/v1/orders/:id
exports.getOrderById = asyncHandler(async (req, res) => {
    const order = await orderService.getOrderById(req.params.id);
    res.status(200).json({ success: true, data: order });
});

// PATCH /api/v1/orders/:id/status
exports.updateOrderStatus = asyncHandler(async (req, res) => {
    const status = req.body ? req.body.status : undefined;
    const order = await orderService.updateOrderStatus(req.params.id, status);
    res.status(200).json({
        success: true,
        message: `Order status changed to "${status}"`,
        data: order
    });
});

// DELETE /api/v1/orders/:id
exports.deleteOrder = asyncHandler(async (req, res) => {
    const result = await orderService.deleteOrder(req.params.id);
    res.status(200).json({
        success: true,
        message: 'Order deleted successfully',
        data: result
    });
});

// GET /api/v1/orders/analytics/sales-summary
exports.getSalesSummary = asyncHandler(async (req, res) => {
    const summary = await orderService.getSalesSummary();
    res.status(200).json({ success: true, data: summary });
});
