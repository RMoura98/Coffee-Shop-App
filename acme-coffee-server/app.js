require('dotenv').config();
const express = require('express');
const cors = require('cors');

const routes = require('./routes');

const app = express();

app.use(express.json({
  verify(req, res, buf, encoding) {
    req.rawBody = buf;
  },
}));
app.use(express.urlencoded({ extended: false }));
app.use(cors());
app.use('/', routes);

module.exports = app;
