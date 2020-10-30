/* eslint-disable global-require */
const models = {
  User: require('./user'),
  MenuItem: require('./menu_item'),
  Voucher: require('./voucher'),
};

models.Voucher.belongsTo(models.User, { foreignKey: 'userId' });
models.User.hasMany(models.Voucher, { foreignKey: 'userId' });

module.exports = models;
