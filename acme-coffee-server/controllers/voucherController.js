const voucherService = require('../services/voucherService');
const ErrorMessage = require('../utils/ErrorMessage');

async function getVouchers(req, res) {
  try {
    const { uuid } = res.locals;
    const vouchers = await voucherService.getVouchers(uuid);

    return res.status(200).json(vouchers);
  } catch (err) {
    return res.status(500).json(new ErrorMessage());
  }
}

module.exports = {
  getVouchers,
};
