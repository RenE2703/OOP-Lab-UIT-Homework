// Routes for /api/v1/orders
const express = require('express');
const router = express.Router();
const orderController = require('../controllers/order.controller');

router.post('/', orderController.createOrder);
router.get('/', orderController.listOrders);

// Static path must be declared before "/:id", otherwise "analytics" is parsed as an ID.
router.get('/analytics/sales-summary', orderController.getSalesSummary);

router.get('/:id', orderController.getOrderById);
router.patch('/:id/status', orderController.updateOrderStatus);
router.delete('/:id', orderController.deleteOrder);

module.exports = router;
