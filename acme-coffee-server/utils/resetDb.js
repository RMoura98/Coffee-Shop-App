// Standalone script to rebuild the database

require('dotenv').config();
const createDb = require('./createDb');

createDb(true);
