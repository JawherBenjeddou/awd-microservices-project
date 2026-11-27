const os = require('os');
const { Eureka } = require('eureka-js-client');
const { appName, port, eurekaHosts, instanceHost } = require('../config/env');

// First non-internal IPv4 address of this machine, like prefer-ip-address=true
// on the Spring Boot services. Falls back to 127.0.0.1 when offline.
function localIpAddress() {
  for (const addresses of Object.values(os.networkInterfaces())) {
    for (const address of addresses || []) {
      if (address.family === 'IPv4' && !address.internal) {
        return address.address;
      }
    }
  }
  return '127.0.0.1';
}

function createEurekaClient() {
  const host = instanceHost || localIpAddress();
  const baseUrl = `http://${host}:${port}`;

  return new Eureka({
    instance: {
      app: appName.toUpperCase(),
      instanceId: `${appName}:${port}`,
      hostName: host,
      ipAddr: host,
      port: { $: port, '@enabled': true },
      vipAddress: appName,
      secureVipAddress: appName,
      homePageUrl: `${baseUrl}/`,
      statusPageUrl: `${baseUrl}/info`,
      healthCheckUrl: `${baseUrl}/health`,
      dataCenterInfo: {
        '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
        name: 'MyOwn',
      },
    },
    eureka: {
      // One URL per Eureka server. The client registers with the first one that
      // answers and switches to the next if it stops responding; the servers
      // replicate the registration to each other.
      serviceUrls: {
        default: eurekaHosts.map((eurekaHost) => `http://${eurekaHost}/eureka/apps/`),
      },
      preferIpAddress: true,
    },
  });
}

module.exports = { createEurekaClient };
