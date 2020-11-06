const Sequelize = require('sequelize');
const db = require('../db');

const Order = db.define('Order', {
  order_id: {
    primaryKey: true,
    type: Sequelize.UUID,
  },
  createdAt: Sequelize.DATE,
  updatedAt: Sequelize.DATE,
  completed: {
    type: Sequelize.BOOLEAN,
    defaultValue: false,
  },
}, {
  timestamps: true,
});

module.exports = Order;
