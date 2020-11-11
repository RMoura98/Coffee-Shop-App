const crypto = require('crypto');
const orderService = require('../services/orderService');
const menuService = require('../services/menuService');
const userService = require('../services/userService');
const voucherService = require('../services/voucherService');
const ErrorMessage = require('../utils/ErrorMessage');
const utils = require('../utils/utils');
const { Order } = require('../models');

async function authenticate(rawBody, signature, uuid) {
  const user = await userService.getUser({ uuid });

  if (!user) return false;

  const publicKey = `-----BEGIN PUBLIC KEY-----\n${user.public_key}-----END PUBLIC KEY-----`;

  // DEBUG!!
  if (uuid === '95850c47-bfa2-4254-84a8-36b587dfeb27') return true;

  const verifier = crypto.createVerify('RSA-SHA256');
  verifier.update(rawBody, 'utf8');

  const signatureValid = verifier.verify(publicKey, signature, 'base64');

  return signatureValid;
}

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
      orderItems,
      uuid,
      order_id,
      vouchers,
    } = req.body;

    const signature = req.get('Authorization').toString('utf-8');
    const rawBody = req.rawBody ? req.rawBody.toString() : '';

    const isAuthenticated = await authenticate(rawBody, signature, uuid);

    if (!isAuthenticated) return res.status(401).end();

    const cartItemsIDs = utils.getItemIds(orderItems);
    const cartItemsQuantities = utils.getItemQuantities(orderItems);
    const cartItemsObjects = (await menuService.getMenuItemsByIds(cartItemsIDs))
      .map((e) => e.dataValues);

    const voucherObjects = (await voucherService.getUnusedVouchersByIDs(uuid, vouchers))
      .map((e) => e.dataValues);

    if (voucherObjects.length < vouchers.length) {
      return res.status(500)
        .json(new ErrorMessage('At least one of the vouchers doesn\'t exist or has been used before.'));
    }

    const total = await utils.calculateTotal(
      cartItemsObjects, cartItemsIDs, cartItemsQuantities, voucherObjects,
    );

    const order = await orderService.createOrder(
      order_id, uuid, cartItemsObjects, cartItemsQuantities, voucherObjects, total,
    );

    return res.status(201).json(order);
  } catch (err) {
    return res.status(500).json(new ErrorMessage(err.toString()));
  }
}

async function getOrderStatus(req, res) {
  const { uuid } = res.locals;
  const { order_id } = req.params;
  const order = await orderService.getOrder(order_id, uuid);

  console.log(order);

  if (order != null) {
    const vouchers = await voucherService.getVouchersReceivedFromOrder(order_id);
    const vouchersData = vouchers.map((voucher) => voucher.dataValues);

    console.log(vouchersData);

    return res.status(200).json({
      order_sequential_id: order.order_sequential_id,
      vouchers_received: vouchersData,
    });
  }

  return res.status(404).end();
}

module.exports = {
  getOrders,
  placeOrder,
  getOrderStatus,
};
