const router = require('express').Router();
const controller = require('../../controllers/orderController');
const authentication = require('../../middleware/authentication');

router.use('*', authentication);
router.get('/', controller.getOrders);
router.post('/', controller.placeOrder);
router.get('/status/:order_id', controller.getOrderStatus);

module.exports = router;
