const router = require('express').Router();
const controller = require('../../controllers/voucherController');
const authentication = require('../../middleware/authentication');

router.use('*', authentication);
router.get('/', controller.getVouchers);

module.exports = router;
