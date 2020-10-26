const router = require('express').Router();
const controller = require('../../controllers/menuController');

router.get('/', controller.getMenu);

module.exports = router;
