const { appName, port } = require('./src/config/env');
const app = require('./src/app');
const { sequelize, ensureDatabase } = require('./src/config/db');
const { createEurekaClient } = require('./src/eureka/eurekaClient');

async function start() {
  try {
    await ensureDatabase();
    console.log('[db] database ready');
  } catch (error) {
    // The service still starts so that /health can report DOWN.
    console.warn(`[db] database unreachable: ${error.message || error.code}`);
  }

  const server = app.listen(port, () => {
    console.log(`[http] ${appName} listening on port ${port}`);
  });

  const eureka = createEurekaClient();
  eureka.start((error) => {
    if (error) {
      console.warn(`[eureka] registration failed: ${error.message}`);
    }
  });

  let stopping = false;
  function shutdown(signal) {
    if (stopping) return;
    stopping = true;
    console.log(`[app] ${signal} received, deregistering from Eureka`);
    eureka.stop(() => {
      server.close(() => {
        sequelize.close().finally(() => process.exit(0));
      });
    });
    // Never hang on shutdown if Eureka or a client connection does not answer.
    setTimeout(() => process.exit(0), 5000).unref();
  }

  process.on('SIGINT', () => shutdown('SIGINT'));
  process.on('SIGTERM', () => shutdown('SIGTERM'));
}

start();
