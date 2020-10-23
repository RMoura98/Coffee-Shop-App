const db = require('../db');
require('../models');

/**
 * Resyncs the database according to the current model
 * @param {boolean} reset wether or not to force the sync operation to reset the database.
 * If enabled, every table is dropped before being recreated
 */
async function run(reset) {
  await db.sync({ alter: true, force: reset });

  if (reset) await db.close();
}

module.exports = run;
