const router = require('express').Router();

const user = require('./user');
const menu = require('./menu');
const debug = require('./debug');

router.use('/user', user);
router.use('/menu', menu);
router.use('/debug', debug);

module.exports = router;
