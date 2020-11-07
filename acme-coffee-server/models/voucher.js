const Sequelize = require('sequelize');
const db = require('../db');

const Voucher = db.define('Voucher', {
  voucherId: {
    primaryKey: true,
    type: Sequelize.UUID,
  },
  voucherType: {
    type: Sequelize.ENUM('free_coffee', 'discount'),
    allowNull: false,
  },
}, { timestamps: true });

module.exports = Voucher;
