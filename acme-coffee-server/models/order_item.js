const Sequelize = require('sequelize');
const db = require('../db');

const Order = db.define('OrderItem', {
  order_item_id: {
    primaryKey: true,
    type: Sequelize.UUID,
  },
  quantity: {
    type: Sequelize.INTEGER,
  },
  price: {
    type: Sequelize.FLOAT,
  },
});

module.exports = Order;
