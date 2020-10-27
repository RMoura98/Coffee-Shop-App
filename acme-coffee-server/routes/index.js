const path = require('path');
const router = require('express').Router();
const express = require('express');

const api = require('./api');

router.use('/api', api);
router.use('/assets', express.static('assets'));

module.exports = router;
