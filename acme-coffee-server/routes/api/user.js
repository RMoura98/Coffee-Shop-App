const router = require('express').Router();
const controller = require('../../controllers/userController');

router.post('/new', controller.createUser);

module.exports = router;
