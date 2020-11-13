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
  try {
    const user = await User.findByPk(uuid);

    if (user == null) throw new HostNotFoundError('No User found');

    user.name = newData.name ? newData.name : user.name;
    user.NIF = newData.NIF ? newData.NIF : user.NIF;
    user.card_number = newData.card_number ? newData.card_number : user.card_number;
    user.card_cvc = newData.card_cvc ? newData.card_cvc : user.card_cvc;
    user.card_expiration = newData.card_expiration ? newData.card_expiration : user.card_expiration;
    user.phone_number = newData.phone_number ? newData.phone_number : user.phone_number;

    await user.save();

    return user.dataValues;
  } catch (err) {
    if (err instanceof Sequelize.UniqueConstraintError) {
      for (const field in err.fields) {
        if (Object.prototype.hasOwnProperty.call(err.fields, field)) throw new UniqueFieldError(`${field} is already taken.`);
      }
    }
    throw err;
  }
}

/**
 * Increments a given user's total_coffees and total_spent fields
 * @param {String} userId user ID
 * @param {Number} numberOfCoffees number of Coffees to add
 * @param {Number} orderTotal value to add to total_spent
 */
async function updateUserTotals(userId, numberOfCoffees, orderTotal) {
  const user = await User.findByPk(userId);

  user.total_coffees += numberOfCoffees;
  user.total_spent += orderTotal;

  await user.save();
}

module.exports = {
  createUser,
  getUser,
  updateUser,
  updateUserTotals,
};
