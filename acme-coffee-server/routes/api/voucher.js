const router = require('express').Router();
const controller = require('../../controllers/voucherController');
const authentication = require('../../middleware/authentication');

router.get('/:user/unused', authentication, controller.getUnusedVouchers);

module.exports = router;
