const Sequelize = require('sequelize');
const db = require('../db');

const Order = db.define('Order', {
  id: {
    type: Sequelize.INTEGER,
    autoIncrement: true,
    primaryKey: true,
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
