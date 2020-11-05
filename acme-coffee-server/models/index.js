/* eslint-disable global-require */
const models = {
  User: require('./user'),
  MenuItem: require('./menu_item'),
  Voucher: require('./voucher'),
  Order: require('./order'),
  OrderItem: require('./order_item'),
};

// Vouchers Relations
models.Voucher.belongsTo(models.User, { foreignKey: 'userId' });
models.User.hasMany(models.Voucher, { foreignKey: 'userId' });

// Orders Relations
models.Order.belongsTo(models.User, { foreignKey: 'userId' });
models.User.hasMany(models.Order, { foreignKey: 'userId' });

// Orders Items Relations
models.OrderItem.belongsTo(models.Order, { foreignKey: 'order_id' });
models.Order.hasMany(models.OrderItem, { foreignKey: 'order_id' });
models.OrderItem.belongsTo(models.MenuItem, { foreignKey: 'item_id' });

module.exports = models;
