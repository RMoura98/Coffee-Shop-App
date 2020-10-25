const { v4: uuidv4 } = require('uuid');
const UniqueFieldError = require('../errors/UniqueFieldError');
const userService = require('../services/userService');
const ErrorMessage = require('../utils/ErrorMessage');

async function createUser(req, res) {
  const {
    name, NIF, card_number, card_cvc, card_expiration, phone_number
  } = req.body;

  try {
    uuid = uuidv4()
    await userService.createUser({uuid, name, NIF, card_number, card_cvc, card_expiration, phone_number});
    return res.status(201).json({uuid});
  } catch (err) {

    if (err instanceof UniqueFieldError) return res.status(400).json(new ErrorMessage(err.message));

    return res.status(500).json(new ErrorMessage());
  }
}

module.exports = {
  createUser,
};
