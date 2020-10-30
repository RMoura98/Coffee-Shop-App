const { Op } = require('sequelize');
const { Voucher } = require('../models');

/**
 * Returns all unused vouchers of a given user
 * @param {uuid} userId the user's UUID
 */
async function getUnusedVouchers(userId) {
  const vouchers = await Voucher.findAll({
    where: {
      [Op.and]: [
        { userId },
        { used: false },
      ],
    },
  });

  return vouchers;
}

module.exports = {
  getUnusedVouchers,
};
