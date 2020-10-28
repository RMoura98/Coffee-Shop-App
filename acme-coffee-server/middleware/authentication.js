const crypto = require('crypto');
const { getUser } = require('../services/userService.js');

const Authentication = async (req, res, next) => {
  const authHeader = req.get('Authorization').toString('utf-8');
  const [uuid, signature] = authHeader.split(',');
  const user = await getUser({ uuid });
  if (!user) return res.status(401).end();
  const publicKey = `-----BEGIN PUBLIC KEY-----\n${user.public_key}-----END PUBLIC KEY-----`;
  const verifier = crypto.createVerify('RSA-SHA256');

  if (req.method === 'GET' || req.method === 'DELETE') {
    verifier.update(req.originalUrl, 'utf8');
  } else {
    const rawBody = req.rawBody.toString();
    verifier.update(rawBody, 'utf8');
  }

  const signatureValid = verifier.verify(publicKey, signature, 'base64');
  console.log(signatureValid);
  if (signatureValid) return next();
  return res.status(401).end();
};

module.exports = Authentication;
