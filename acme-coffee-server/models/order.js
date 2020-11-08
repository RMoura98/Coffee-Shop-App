const Sequelize = require('sequelize');
const db = require('../db');

const Order = db.define('Order', {
  order_id: {
    primaryKey: true,
    type: Sequelize.UUID,
  },
  order_sequential_id: {
    type: Sequelize.INTEGER,
    autoIncrement: true,
  },
  createdAt: Sequelize.DATE,
  updatedAt: Sequelize.DATE,
  completed: {
    type: Sequelize.BOOLEAN,
    defaultValue: false,
  },
  total: Sequelize.FLOAT,
}, {
  timestamps: true,
});

module.exports = Order;
