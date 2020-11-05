const Sequelize = require('sequelize');
const db = require('../db');

const Order = db.define('OrderItem', {
  id: {
    type: Sequelize.INTEGER,
    autoIncrement: true,
    primaryKey: true,
  },
  quantity: {
    type: Sequelize.INTEGER,
  },
  priceBeforeDiscounts: {
    type: Sequelize.FLOAT,
  },
  priceAfterDiscounts: {
    type: Sequelize.FLOAT,
  },
});

module.exports = Order;
