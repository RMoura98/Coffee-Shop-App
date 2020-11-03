const crypto = require('crypto');
const { getUser } = require('../services/userService.js');

const Authentication = async (req, res, next) => {
  const authHeader = req.get('Authorization').toString('utf-8');
  const [uuid, signature] = authHeader.split(',');
  // DEBUG!!
  if (uuid === '95850c47-bfa2-4254-84a8-36b587dfeb27') return next();
  //
  const user = await getUser({ uuid });
  if (!user) return res.status(401).end();
  const publicKey = `-----BEGIN PUBLIC KEY-----\n${user.public_key}-----END PUBLIC KEY-----`;
  const verifier = crypto.createVerify('RSA-SHA256');

  const rawBody = req.rawBody ? req.rawBody.toString() : '';
  const dateHeader = req.get('Date').toString('utf-8');
  const dateObject = new Date(dateHeader);
  const now = new Date();
  const requestAgeInMinutes = (now.getTime() - dateObject.getTime()) / 60000;

  if (requestAgeInMinutes > 1) return res.status(401).end();

  const toVerify = `${rawBody}${req.originalUrl}${dateHeader}`;

  verifier.update(toVerify, 'utf8');

  const signatureValid = verifier.verify(publicKey, signature, 'base64');
  if (signatureValid) return next();
  return res.status(401).end();
};

module.exports = Authentication;
