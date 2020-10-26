const MenuItem = require('../models/menu_item');

async function getMenuItems() {
  return MenuItem.findAll();
}

module.exports = {
  getMenuItems,
};
