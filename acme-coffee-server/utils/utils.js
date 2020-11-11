const menuService = require('../services/menuService');

function getItemIds(orderItems) {
  const itemIds = [];

  for (const key in orderItems) {
    if (Object.prototype.hasOwnProperty.call(orderItems, key)) {
      itemIds.push(key);
    }
  }

  return itemIds;
}

function getItemQuantities(orderItems) {
  const itemQuantities = [];

  for (const key in orderItems) {
    if (Object.prototype.hasOwnProperty.call(orderItems, key)) {
      itemQuantities.push(orderItems[key]);
    }
  }

  return itemQuantities;
}

function calculateSubTotal(cartItemsObjects, cartItemsIDs, cartItemsQuantities) {
  const subTotal = cartItemsObjects.map((item) => {
    let quantity = 0;
    for (let i = 0; i < cartItemsIDs.length; i += 1) {
      if (Number(cartItemsIDs[i]) === item.id) {
        quantity = cartItemsQuantities[i];
        break;
      }
    }
    return item.price * quantity;
  }).reduce((a, b) => a + b, 0);

  return subTotal;
}

function countCoffees(cartItemsObjects, cartItemsIDs, cartItemsQuantities) {
  const numberOfCoffes = cartItemsObjects.map((item) => {
    let quantity = 0;
    if (item.name === 'Coffee') {
      for (let i = 0; i < cartItemsIDs.length; i += 1) {
        if (Number(cartItemsIDs[i]) === item.id) {
          quantity = cartItemsQuantities[i];
          break;
        }
      }
    }
    return quantity;
  }).reduce((a, b) => a + b, 0);

  return numberOfCoffes;
}

function countVouchersOfType(voucherObjects, voucherType) {
  const numberOfCoffeeVouchers = voucherObjects.map((voucher) => {
    if (voucher.voucherType === voucherType) return 1;
    return 0;
  }).reduce((a, b) => a + b, 0);

  return numberOfCoffeeVouchers;
}

async function calculateTotal(cartItems, cartItemsIDs, cartItemsQuantities, vouchers) {
  const subTotal = calculateSubTotal(cartItems, cartItemsIDs, cartItemsQuantities);
  const numberOfCoffees = countCoffees(cartItems, cartItemsIDs, cartItemsQuantities);
  const numberOfCoffeeVouchers = countVouchersOfType(vouchers, 'free_coffee');
  const numberOfDiscountVouchers = countVouchersOfType(vouchers, 'discount');

  if (numberOfCoffeeVouchers > numberOfCoffees) throw new Error('Invalid number of coffee vouchers');
  if (numberOfDiscountVouchers > 1) throw new Error('Invalid number of coffee vouchers');

  const coffeePrice = await menuService.getCoffeePrice();

  let total = subTotal - numberOfCoffeeVouchers * coffeePrice;
  if (numberOfDiscountVouchers > 0) total -= total * 0.05;

  return total;
}

/**
 * Counts the number of coffees that the customer payed for in an order
 * @param {OrderItem} orderItems the order items
 * @param {array} orderItemsQuantities the quantities of the order items
 * @param {array} vouchers applied vouchers
 */
function countPayedCoffees(orderItems, orderItemsQuantities, vouchers) {
  let coffees = 0;
  const coffeeVouchers = countVouchersOfType(vouchers, 'free_coffee');

  for (let i = 0; i < orderItems.length; i += 1) {
    const item = orderItems[i];
    const quantity = orderItemsQuantities[i];

    if (item.name === 'Coffee') coffees += quantity;
  }

  return coffees - coffeeVouchers;
}

module.exports = {
  getItemIds,
  getItemQuantities,
  calculateTotal,
  countPayedCoffees,
};
