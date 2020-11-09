const { HostNotFoundError } = require('sequelize');
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

async function getUser({ uuid }) {
  const user = await User.findOne({ where: { uuid } });
  return user;
}

async function updateUser(uuid, newData) {
  const user = await User.findByPk(uuid);

  if (user == null) throw new HostNotFoundError('No User found');

  user.name = newData.name;
  user.NIF = newData.NIF;
  user.card_number = newData.card_number;
  user.card_cvc = newData.card_cvc;
  user.card_expiration = newData.card_expiration;
  user.phone_number = newData.phone_number;

  await user.save();

  return user;
}

module.exports = {
  createUser,
  getUser,
  updateUser,
};
