const router = require('express').Router();
const authentication = require('../../middleware/authentication');

router.use('*', authentication);
router.post('/', async (req, res) => res.json({}));
router.get('/', async (req, res) => res.json({}));

module.exports = router;
