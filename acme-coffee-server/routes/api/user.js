const router = require('express').Router();
const controller = require('../../controllers/userController');

router.get('/:user', controller.getUser);
router.post('/new', controller.createUser);
router.put('/:user', controller.updateUser);

module.exports = router;
