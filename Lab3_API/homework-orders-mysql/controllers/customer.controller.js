// HTTP layer for customers
const asyncHandler = require('../middlewares/asyncHandler');
const orderService = require('../services/order.service');

// GET /api/v1/customers/:id/order-history
exports.getOrderHistory = asyncHandler(async (req, res) => {
    const history = await orderService.getCustomerOrderHistory(req.params.id);
    res.status(200).json({ success: true, data: history });
});
