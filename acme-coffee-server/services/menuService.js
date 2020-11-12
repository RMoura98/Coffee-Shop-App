const { Op } = require('sequelize');
const { Order } = require('../models');
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

async function getMenuItemById(itemID) {
    const item = await MenuItem.findByPk(itemID)
  
    return item;
}

module.exports = {
  getMenuItems,
  getMenuItemsByIds,
  getCoffeePrice,
  getMenuItemById,
};
