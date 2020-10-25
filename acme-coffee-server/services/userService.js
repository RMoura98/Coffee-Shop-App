const Sequelize = require('sequelize');
const UniqueFieldError = require('../errors/UniqueFieldError');
const User = require('../models/user');

async function createUser(userData) {
  try {
    const user = await User.create(userData);
    return user;
  } catch (err) {
    if (err instanceof Sequelize.UniqueConstraintError) {
      for (const field in err.fields) {
        if (Object.prototype.hasOwnProperty.call(err.fields, field)) throw new UniqueFieldError(`${field} is already taken.`);
      }
    }
    throw err;
  }
}

module.exports = {
  createUser,
};
