const UniqueFieldError = require('../errors/UniqueFieldError');
const userService = require('../services/userService');
const ErrorMessage = require('../utils/ErrorMessage');

async function createUser(req, res) {
  const user = req.body;

  try {
    const savedUser = await userService.createUser(user);
    return res.status(201).json(savedUser);
  } catch (err) {
    if (err instanceof UniqueFieldError) return res.status(400).json(new ErrorMessage(err.message));

    return res.status(500).json(new ErrorMessage());
  }
}

module.exports = {
  createUser,
};
