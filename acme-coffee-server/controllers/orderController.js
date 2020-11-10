const orderService = require('../services/orderService');
const menuService = require('../services/menuService');
const voucherService = require('../services/voucherService');
const ErrorMessage = require('../utils/ErrorMessage');

async function getOrders(req, res) {
  try {
    const { uuid } = res.locals;
    const orders = await orderService.getOrders(uuid);
    const ordersIDs = orders.map((item) => item.dataValues.order_id);
    const orderItems = await orderService.getOrderItems(ordersIDs);

    return res.status(200).json({
      orders,
      orderItems,
    });
  } catch (err) {
    console.log(err);
    return res.status(500).json(new ErrorMessage());
  }
}

async function placeOrder(req, res) {
  try {
    const {
      cartItems,
      vouchers,
    } = req.body;

    const { uuid } = res.locals;

    const cartItemsIDs = cartItems.map((e) => e.item.id);
    const cartItemsQuantities = cartItems.map((e) => e.quantity);
    const cartItemsObjects = (await menuService.getMenuItemsByIds(cartItemsIDs)).map((e) => e.dataValues);

    const vouchersIDs = vouchers.map((e) => e.voucherId);
    const voucherObjects = (await voucherService.getUnusedVouchersByIDs(uuid, vouchersIDs)).map((e) => e.dataValues);

    if (voucherObjects.length < vouchersIDs.length) return res.status(500).json(new ErrorMessage('At least one of the vouchers doesn\'t exist or has been used before.'));

    const subTotal = cartItemsObjects.map((item) => {
      let quantity = 0;
      for (let i = 0; i < cartItemsIDs.length; i++) {
        if (cartItemsIDs[i] == item.id) {
          quantity = cartItemsQuantities[i];
          break;
        }
      }
      return item.price * quantity;
    }).reduce((a, b) => a + b, 0);

    const numberOfCoffes = cartItemsObjects.map((item) => {
      let quantity = 0;
      if (item.name == 'Coffee') {
        for (let i = 0; i < cartItemsIDs.length; i++) {
          if (cartItemsIDs[i] == item.id) {
            quantity = cartItemsQuantities[i];
            break;
          }
        }
      }
      return quantity;
    }).reduce((a, b) => a + b, 0);

    const numberOfCoffeVouchers = voucherObjects.map((voucher) => {
      if (voucher.voucherType === 'free_coffee') return 1;
      return 0;
    }).reduce((a, b) => a + b, 0);

    const numberOfDiscountVouchers = voucherObjects.map((voucher) => {
      if (voucher.voucherType === 'discount') return 1;
      return 0;
    }).reduce((a, b) => a + b, 0);

    if (numberOfCoffeVouchers > numberOfCoffes) return res.status(500).json(new ErrorMessage('Invalid number of coffee vouchers.'));
    if (numberOfDiscountVouchers > 1) return res.status(500).json(new ErrorMessage('Invalid number of discount vouchers.'));

    console.log('subTotal', subTotal);
    console.log('numberOfCoffes', numberOfCoffes);
  } catch (err) {
    return res.status(500).json(new ErrorMessage(err.toString()));
  }
}

async function getOrderStatus(req, res) {
  const { uuid } = res.locals;
  const { order_id } = req.params;
  const order = await orderService.getOrder(order_id, uuid).dataValues;
  if (order != null) {
    const vouchers = voucherService.getVouchersReceivedFromOrder(order_id).map((e) => e.dataValues);
    return res.json({
      order_sequential_id: order.order_sequential_id,
      vouchers,
    }).end();
  }

  return res.status(404).end();
}

module.exports = {
  getOrders,
  placeOrder,
  getOrderStatus,
};
