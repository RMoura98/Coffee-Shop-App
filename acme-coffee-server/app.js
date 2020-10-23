require('dotenv').config();
const express = require('express');
const cors = require('cors');

const routes = require('./routes');
const createDb = require('./utils/createDb');

createDb(false);

const app = express();

app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cors());
app.use('/', routes);

module.exports = app;
