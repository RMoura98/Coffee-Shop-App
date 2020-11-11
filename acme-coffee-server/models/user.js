const Sequelize = require('sequelize');
const db = require('../db');

const User = db.define('User', {
  uuid: {
    primaryKey: true,
    type: Sequelize.UUID,
  },
  name: {
    type: Sequelize.STRING,
    allowNull: false,
  },
  NIF: {
    type: Sequelize.STRING,
    allowNull: false,
  },
  card_number: {
    type: Sequelize.STRING,
    allowNull: false,
  },
  card_cvc: {
    type: Sequelize.STRING,
    allowNull: false,
  },
  card_expiration: {
    type: Sequelize.STRING,
    allowNull: false,
  },
  phone_number: {
    type: Sequelize.STRING,
    allowNull: false,
  },
  total_coffees: {
    type: Sequelize.INTEGER,
    allowNull: false,
    defaultValue: 0,
  },
  total_spent: {
    type: Sequelize.FLOAT,
    allowNull: false,
    defaultValue: 0,
  },
  public_key: {
    type: Sequelize.TEXT,
    allowNull: false,
  },
}, {
  indexes: [
    {
      unique: true,
      fields: ['NIF'],
    },
  ],
});

module.exports = User;
