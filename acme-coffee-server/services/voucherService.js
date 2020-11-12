const { Op } = require('sequelize');
const { v4: uuidv4 } = require('uuid');
const { Voucher } = require('../models');
const NotFoundError = require('../errors/NotFoundError');
const userService = require('./userService');

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

/**
 * Marks a voucher as used on the given order
 * @param {String} voucherId the voucher's ID
 * @param {String} orderId the order's ID
 * @param {Sequelize.Transaction} transaction the transaction in which this operation takes place
 */
async function useVoucher(voucherId, orderId, transaction) {
  const voucher = await Voucher.findByPk(voucherId);

  if (voucher == null) throw new NotFoundError("Voucher doesn't exist");

  voucher.used_on_order_id = orderId;

  await voucher.save({ transaction });
}

/**
 * Emits vouchers after an order
 * @param {String} userId the customer who made the order
 * @param {String} orderId the order in which the vouchers should be emitted
 * @param {Number} payedCoffees the number of coffees the customer payed for in the order
 * @param {Number} orderTotal the order total (discounts included)
 * @param {Sequelize.Transacton} transaction the transaction in which this operation takes place
 */
async function emitVouchers(userId, orderId, payedCoffees, orderTotal, transaction) {
  const user = await userService.getUser({ uuid: userId });

  const newTotalCoffees = user.total_coffees + payedCoffees;

  const emittedVouchers = [];

  for (let coffees = user.total_coffees + 1; coffees <= newTotalCoffees; coffees += 1) {
    if (coffees % 3 === 0) {
      const voucher = await Voucher.create({
        voucherId: uuidv4(),
        voucherType: 'free_coffee',
        userId,
        received_from_order_id: orderId,
      }, { transaction });

      emittedVouchers.push(voucher.dataValues);
    }
  }

  // const numberOfDiscountVouchers =
  // Math.floor(newTotalSpent / 100) - Math.floor(user.total_spent / 100);
  const numberOfDiscountVouchers = Math.floor((orderTotal + (user.total_spent % 100)) / 100);

  for (let i = 0; i < numberOfDiscountVouchers; i += 1) {
    const voucher = await Voucher.create({
      voucherId: uuidv4(),
      voucherType: 'discount',
      userId,
      received_from_order_id: orderId,
    }, { transaction });

    emittedVouchers.push(voucher.dataValues);
  }

  return emittedVouchers;
}

module.exports = {
  getVouchers,
  getUnusedVouchersByIDs,
  getVouchersReceivedFromOrder,
  useVoucher,
  emitVouchers,
};
