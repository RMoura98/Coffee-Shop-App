const { v4: uuidv4 } = require('uuid');
const UniqueFieldError = require('../errors/UniqueFieldError');
const NotFoundError = require('../errors/NotFoundError');
const userService = require('../services/userService');
const ErrorMessage = require('../utils/ErrorMessage');

async function createUser(req, res) {
  const {
    // eslint-disable-next-line camelcase
    name, NIF, card_number, card_cvc, card_expiration, phone_number, public_key,
  } = req.body;

  try {
    const uuid = uuidv4();
    await userService.createUser({
      uuid, name, NIF, card_number, card_cvc, card_expiration, phone_number, public_key,
    });
    return res.status(201).json({ uuid });
  } catch (err) {
    if (err instanceof UniqueFieldError) return res.status(400).json(new ErrorMessage(err.message));

    return res.status(500).json(new ErrorMessage(err.toString()));
  }
}

async function getUser(req, res) {
  try {
    const uuid = req.params.user;
    const user = await userService.getUser({ uuid });
    return res.status(200).json(user);
  } catch (err) {
    return res.status(500).json(new ErrorMessage(err.toString()));
  }
}

async function updateUser(req, res) {
  const uuid = req.params.user;

  try {
    const updatedUser = await userService.updateUser(uuid, req.body);
    return res.status(200).json(updatedUser);
  } catch (err) {
    if (err instanceof NotFoundError) return res.status(404).json(new ErrorMessage(err.toString()));

    return res.status(500).json(new ErrorMessage(err.toString()));
  }
}

module.exports = {
  createUser,
  getUser,
  updateUser,
};
