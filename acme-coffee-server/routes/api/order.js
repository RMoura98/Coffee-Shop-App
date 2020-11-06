const router = require('express').Router();
const controller = require('../../controllers/orderController');
const authentication = require('../../middleware/authentication');

router.use('*', authentication);
router.get('/', controller.getOrders);
router.post('/', controller.placeOrder);

module.exports = router;
