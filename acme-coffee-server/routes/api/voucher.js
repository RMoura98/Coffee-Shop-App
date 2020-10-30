const router = require('express').Router();
const controller = require('../../controllers/voucherController');

router.get('/:user/unused', controller.getUnusedVouchers);

module.exports = router;
