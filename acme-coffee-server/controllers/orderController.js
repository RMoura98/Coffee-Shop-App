const orderService = require('../services/orderService');
const ErrorMessage = require('../utils/ErrorMessage');

async function getOrders(req, res) {
  try {
    const { uuid } = res.locals;
    const orders = await orderService.getOrders(uuid);
    const ordersIDs = orders.map((item) => item.dataValues.id);
    const orderItems = await orderService.getOrderItems(ordersIDs);

    return res.status(200).json({
      orders,
      orderItems,
    });
  } catch (err) {
    return res.status(500).json(new ErrorMessage());
  }
}

module.exports = {
  getOrders,
};
