module.exports = {
  up: async (queryInterface, Sequelize) => queryInterface.bulkInsert('MenuItems', [{
    name: 'Caffe',
    category: 'Drinks',
    price: 1.20,
    createdAt: new Date(),
    updatedAt: new Date(),
  },
  {
    name: 'Beer',
    category: 'Drinks',
    price: 2.60,
    createdAt: new Date(),
    updatedAt: new Date(),
  },
  {
    name: 'Coca-Cola',
    category: 'Drinks',
    price: 2.00,
    createdAt: new Date(),
    updatedAt: new Date(),
  },
  {
    name: 'KitKat',
    category: 'Food',
    price: 2.00,
    createdAt: new Date(),
    updatedAt: new Date(),
  },
  {
    name: 'Pastle de Nata',
    category: 'Food',
    price: 1.50,
    createdAt: new Date(),
    updatedAt: new Date(),
  }]),

  down: async (queryInterface, Sequelize) => queryInterface.bulkDelete('MenuItems', null, {}),
};
