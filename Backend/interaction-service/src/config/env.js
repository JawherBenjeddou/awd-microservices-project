// Loads .env once and exposes the settings used by the rest of the service.
// Variables already set in the shell win over .env, so `PORT=8096 npm start`
// starts a second instance without editing any file.
require('dotenv').config({ quiet: true });

const pkg = require('../../package.json');

module.exports = {
  appName: pkg.name,
  version: pkg.version,
  description: pkg.description,
  port: Number(process.env.PORT) || 8086,
  db: {
    host: process.env.DB_HOST || 'localhost',
    port: Number(process.env.DB_PORT) || 3306,
    name: process.env.DB_NAME || 'interaction_db',
    user: process.env.DB_USER || 'root',
    password: process.env.DB_PASSWORD || '',
  },
  eurekaHosts: (process.env.EUREKA_HOSTS || 'localhost:8761,localhost:8762')
    .split(',')
    .map((host) => host.trim())
    .filter(Boolean),
  // Optional: address other services use to reach this instance.
  instanceHost: process.env.INSTANCE_HOST || null,
};
