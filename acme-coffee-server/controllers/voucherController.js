const voucherService = require('../services/voucherService');
const ErrorMessage = require('../utils/ErrorMessage');

async function getUnusedVouchers(req, res) {
  const userId = req.params.user;

  try {
    const vouchers = await voucherService.getUnusedVouchers(userId);

    return res.status(200).json(vouchers);
  } catch (err) {
    return res.status(500).json(new ErrorMessage());
  }
}

module.exports = {
  getUnusedVouchers,
};
