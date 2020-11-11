const { Op } = require('sequelize');
const MenuItem = require('../models/menu_item');

async function getMenuItems() {
  return MenuItem.findAll();
}

async function getMenuItemsByIds(itemIDs) {
  const items = await MenuItem.findAll({
    where: {
      id: {
        [Op.or]: itemIDs,
      },
    },
  });

  return items;
}

async function getCoffeePrice() {
  const coffee = await MenuItem.findOne({
    where: { name: 'Coffee' },
  });

  return coffee.price;
}

module.exports = {
  getMenuItems,
  getMenuItemsByIds,
  getCoffeePrice,
};
