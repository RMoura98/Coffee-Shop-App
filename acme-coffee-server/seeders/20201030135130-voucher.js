const { v4: uuidv4 } = require('uuid');

module.exports = {
  up: async (queryInterface, Sequelize) => {
    await queryInterface.bulkInsert('Vouchers',
      [
        {
          voucherId: uuidv4(),
          voucherType: 'free_coffee',
          userId: '95850c47-bfa2-4254-84a8-36b587dfeb27',
          used: false,
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        {
          voucherId: uuidv4(),
          voucherType: 'free_coffee',
          userId: '95850c47-bfa2-4254-84a8-36b587dfeb27',
          used: true,
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        {
          voucherId: uuidv4(),
          voucherType: 'discount',
          userId: '95850c47-bfa2-4254-84a8-36b587dfeb27',
          used: false,
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        {
          voucherId: uuidv4(),
          voucherType: 'discount',
          userId: '95850c47-bfa2-4254-84a8-36b587dfeb27',
          used: false,
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        {
          voucherId: uuidv4(),
          voucherType: 'free_coffee',
          userId: '95850c47-bfa2-4254-84a8-36b587dfeb27',
          used: true,
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        {
          voucherId: uuidv4(),
          voucherType: 'free_coffee',
          userId: '95850c47-bfa2-4254-84a8-36b587dfeb27',
          used: false,
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        {
          voucherId: uuidv4(),
          voucherType: 'free_coffee',
          userId: '95850c47-bfa2-4254-84a8-36b587dfeb27',
          used: true,
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        {
          voucherId: uuidv4(),
          voucherType: 'free_coffee',
          userId: '95850c47-bfa2-4254-84a8-36b587dfeb27',
          used: false,
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        {
          voucherId: uuidv4(),
          voucherType: 'free_coffee',
          userId: '95850c47-bfa2-4254-84a8-36b587dfeb27',
          used: true,
          createdAt: new Date(),
          updatedAt: new Date(),
        },
      ], { validate: true });
  },

  down: async (queryInterface, Sequelize) => queryInterface.bulkDelete('Voucher', null, {}),
};
