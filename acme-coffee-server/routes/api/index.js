const router = require('express').Router();

const user = require('./user');
const menu = require('./menu');

router.use('/user', user);
router.use('/menu', menu);

module.exports = router;
