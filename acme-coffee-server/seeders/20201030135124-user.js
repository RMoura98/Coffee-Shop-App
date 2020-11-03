module.exports = {
  up: async (queryInterface, Sequelize) => {
    await queryInterface.bulkInsert('Users', [{
      uuid: '3e948d72-98ac-4a18-8158-fa65e31bbdb5',
      name: 'John Doe',
      NIF: '999888777',
      card_number: '4716807481940513',
      card_cvc: '123',
      card_expiration: '01/21',
      phone_number: '912345678',
      public_key: 'pubkey1',
    },
    {
      uuid: '211e552e-9dfe-4936-aac1-96ec7bf9c846',
      name: 'Mike Doe',
      NIF: '111222333',
      card_number: '4024007157683292',
      card_cvc: '666',
      card_expiration: '02/21',
      phone_number: '919218937',
      public_key: 'pubkey2',
    },
    {
      uuid: '95850c47-bfa2-4254-84a8-36b587dfeb27',
      name: 'Fabio Oliveira',
      NIF: '249720955',
      card_number: '450077003964930',
      card_cvc: '748',
      card_expiration: '12/24',
      phone_number: '931798250',
      public_key: 'doesnt_matter',
    }]);
  },

  down: async (queryInterface, Sequelize) => queryInterface.bulkDelete('User', null, {}),
};
