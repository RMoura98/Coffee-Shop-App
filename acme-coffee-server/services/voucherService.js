const { Op } = require('sequelize');
const { Voucher } = require('../models');

/**
 * Returns all vouchers of a given user.
 * @param {uuid} userId the user's UUID
 */
async function getVouchers(userId) {
  const vouchers = await Voucher.findAll({
    where: {
      [Op.and]: [
        { userId },
      ],
    },
  });

  return vouchers;
}

/**
 * Returns all unused vouchers of a given user filtered by ID.
 * @param {uuid} userId the user's UUID
 * @param {uuid} vouchersIds the voucher IDs
 */
async function getUnusedVouchersByIDs(userId, vouchersIds) {
  const vouchers = await Voucher.findAll({
    where: {
      [Op.and]: [
        { userId },
        { used_on_order_id: { [Op.is]: null } },
        { voucherId: { [Op.in]: vouchersIds } },
      ],
    },
  });

  return vouchers;
}

async function getVouchersReceivedFromOrder(order_id) {
  const vouchers = await Voucher.findAll({
    where: {
      [Op.and]: [
        { received_from_order_id: order_id },
      ],
    },
  });

  return vouchers;
}

module.exports = {
  getVouchers,
  getUnusedVouchersByIDs,
  getVouchersReceivedFromOrder,
};
