const router = require('express').Router();
const controller = require('../../controllers/orderController');
const authentication = require('../../middleware/authentication');

router.get('/', authentication, controller.getOrders);
router.post('/', controller.placeOrder);
router.get('/status/:order_id', authentication, controller.getOrderStatus);
router.get('/all', controller.allOrders);

module.exports = router;
