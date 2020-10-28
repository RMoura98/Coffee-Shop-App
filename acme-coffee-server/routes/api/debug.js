const router = require('express').Router();
const authentication = require('../../middleware/authentication');

router.use('*', authentication);
router.post('/', async (req, res) => res.send('is success'));
router.get('/', async (req, res) => res.send('is success'));

module.exports = router;
