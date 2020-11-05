module.exports = {
  up: async (queryInterface, Sequelize) => {
    await queryInterface.bulkInsert('Orders',
      [
        {
          userId: '95850c47-bfa2-4254-84a8-36b587dfeb27',
          createdAt: new Date(),
          updatedAt: new Date(),
          completed: false,
        },
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
        {
          userId: '95850c47-bfa2-4254-84a8-36b587dfeb27',
          createdAt: new Date(),
          updatedAt: new Date(),
          completed: false,
        },
      ]);
    await queryInterface.bulkInsert('OrderItems',
      [
        {
          order_id: 1,
          item_id: 1,
          quantity: 1,
          priceBeforeDiscounts: 6.30,
          priceAfterDiscounts: 4.53
        },
        {
          order_id: 1,
          item_id: 2,
          quantity: 5,
          priceBeforeDiscounts: 5.50,
          priceAfterDiscounts: 2.40,
        },
        {
          order_id: 2,
          item_id: 2,
          quantity: 3,
          priceBeforeDiscounts: 16.30,
          priceAfterDiscounts: 14.53,
        },
        {
          order_id: 2,
          item_id: 3,
          quantity: 2,
          priceBeforeDiscounts: 15.50,
          priceAfterDiscounts: 12.40,
        },
        {
          order_id: 3,
          item_id: 2,
          quantity: 1,
          priceBeforeDiscounts: 160.30,
          priceAfterDiscounts: 140.53,
        },
        {
          order_id: 4,
          item_id: 3,
          quantity: 10,
          priceBeforeDiscounts: 25.50,
          priceAfterDiscounts: 20,
        },
      ]);
  },

  down: async (queryInterface, Sequelize) => {
    queryInterface.bulkDelete('Order', null, {});
    queryInterface.bulkDelete('OrderItem', null, {});
  },
};
