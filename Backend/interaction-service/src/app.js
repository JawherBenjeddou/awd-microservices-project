const express = require('express');
const cors = require('cors');
const { appName, version, description, port } = require('./config/env');
const { isDatabaseUp } = require('./config/db');

const app = express();

app.use(cors());
app.use(express.json());

// Shows which instance answered (useful with 2 instances behind the gateway).
app.get('/api/interactions/hello', (req, res) => {
  res.json({ message: `Hello from ${appName} on port ${port}` });
});

// Equivalent of /actuator/health: DOWN (503) when the database is unreachable.
app.get('/health', async (req, res) => {
  if (await isDatabaseUp()) {
    res.status(200).json({ status: 'UP' });
  } else {
    res.status(503).json({ status: 'DOWN' });
  }
});

// Equivalent of /actuator/info.
app.get('/info', (req, res) => {
  res.json({ name: appName, version, description });
});

// Module routes (messagerie, paiements, avis, notifications, moderation)
// will be mounted here under /api/interactions/...

module.exports = app;
