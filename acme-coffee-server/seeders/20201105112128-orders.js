module.exports = {
  up: async (queryInterface, Sequelize) => {
    await queryInterface.bulkInsert('Orders',
      [
        {
          userId: '95850c47-bfa2-4254-84a8-36b587dfeb27',
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        {
          userId: '95850c47-bfa2-4254-84a8-36b587dfeb27',
          createdAt: new Date(),
          updatedAt: new Date(),
        },
      ]);
    await queryInterface.bulkInsert('OrderItems',
      [
        {
          order_id: 1,
          item_id: 1,
          quantity: 1,
          priceBeforeDiscounts: 6.30,
          priceAfterDiscounts: 4.53,
        },
        {
          order_id: 1,
          item_id: 2,
          quantity: 5,
          priceBeforeDiscounts: 5.50,
          priceAfterDiscounts: 2.40,
        },
      ]);
  },

  down: async (queryInterface, Sequelize) => {
    queryInterface.bulkDelete('Order', null, {});
    queryInterface.bulkDelete('OrderItem', null, {});
  },
};
