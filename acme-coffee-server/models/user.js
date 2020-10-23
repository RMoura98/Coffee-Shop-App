const Sequelize = require('sequelize');
const db = require('../db');

const User = db.define('User', {
  fullName: {
    type: Sequelize.STRING,
    field: 'fullName',
  },
  nif: {
    type: Sequelize.STRING,
    field: 'nif',
  },
}, {
  indexes: [
    {
      unique: true,
      fields: ['nif'],
    },
  ],
});

module.exports = User;
