const { Op } = require('sequelize');
const { Order, OrderItem } = require('../models');

/**
 * Returns all orders of a given user.
 * @param {uuid} userId the user's UUID
 */
async function getOrders(userId) {
  const orders = await Order.findAll({
    where: {
      [Op.and]: [
        { userId },
      ],
    },
  });

  return orders;
}

/**
 * Returns the order matching a given order_id from a given user.
 * @param {uuid} order_id the order's id
 * @param {uuid} user_id the user's id
 */
async function getOrder(order_id, user_id) {
  const order = await Order.findOne({
    where: {
      [Op.and]: [
        { userId: user_id },
        { order_id },
      ],
    },
  });

  return order;
}

/**
 * Returns all orders items associated with the orders IDs.
 * @param {orderIDs} orderIDs the IDs of the orders as array
 */
async function getOrderItems(orderIDs) {
  const orders = await OrderItem.findAll({
    where: {
      order_id: {
        [Op.or]: orderIDs,
      },
    },
  });

  return orders;
}

module.exports = {
  getOrders,
  getOrderItems,
  getOrder,
};
