const { v4: uuidv4 } = require('uuid');

module.exports = {
  up: async (queryInterface, Sequelize) => {
    await queryInterface.bulkInsert('Vouchers',
      [
        {
          voucherId: uuidv4(),
          voucherType: 'free_coffee',
          userId: '3e948d72-98ac-4a18-8158-fa65e31bbdb5',
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        {
          voucherId: uuidv4(),
          voucherType: 'free_coffee',
          userId: '3e948d72-98ac-4a18-8158-fa65e31bbdb5',
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        {
          voucherId: uuidv4(),
          voucherType: 'discount',
          userId: '3e948d72-98ac-4a18-8158-fa65e31bbdb5',
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        {
          voucherId: uuidv4(),
          voucherType: 'free_coffee',
          userId: '211e552e-9dfe-4936-aac1-96ec7bf9c846',
          createdAt: new Date(),
          updatedAt: new Date(),
        },
      ], { validate: true });
  },

  down: async (queryInterface, Sequelize) => queryInterface.bulkDelete('Voucher', null, {}),
};
