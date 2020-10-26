const menuService = require('../services/menuService');

async function getMenu(req, res) {
  const menuItems = await menuService.getMenuItems();
  return res.json(menuItems);
}

module.exports = {
  getMenu,
};
