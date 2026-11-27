const mysql = require('mysql2/promise');
const { Sequelize } = require('sequelize');
const { db } = require('./env');

// Shared Sequelize connection. Modules will define their models on it later.
const sequelize = new Sequelize(db.name, db.user, db.password, {
  host: db.host,
  port: db.port,
  dialect: 'mysql',
  logging: false,
});

// Creates the database if it does not exist yet (same idea as
// createDatabaseIfNotExist=true on the Spring Boot services).
async function ensureDatabase() {
  const connection = await mysql.createConnection({
    host: db.host,
    port: db.port,
    user: db.user,
    password: db.password,
  });
  try {
    await connection.query(`CREATE DATABASE IF NOT EXISTS \`${db.name}\``);
  } finally {
    await connection.end();
  }
}

// True when a query can reach the database right now. Used by GET /health.
async function isDatabaseUp() {
  try {
    await sequelize.authenticate();
    return true;
  } catch (error) {
    return false;
  }
}

module.exports = { sequelize, ensureDatabase, isDatabaseUp };
