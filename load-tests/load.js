import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8088';
const TEST_USER = __ENV.TEST_USER || 'loadtest-user';
const TEST_PASSWORD = __ENV.TEST_PASSWORD || 'password';

export const options = {
  stages: [
    { duration: '30s', target: Number(__ENV.RAMP_1 || 50) },
    { duration: '1m', target: Number(__ENV.RAMP_2 || 200) },
    { duration: '1m', target: Number(__ENV.RAMP_3 || 500) },
    { duration: '30s', target: 0 },
  ],
  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<500'],
  },
};

export function setup() {
  let res = http.post(
    `${BASE_URL}/api/v1/auth/login`,
    JSON.stringify({ username: TEST_USER, password: TEST_PASSWORD }),
    { headers: { 'Content-Type': 'application/json' } }
  );

  if (res.status !== 200) {
    res = http.post(
      `${BASE_URL}/api/v1/users`,
      JSON.stringify({ name: TEST_USER, password: TEST_PASSWORD }),
      { headers: { 'Content-Type': 'application/json' } }
    );
  }

  check(res, { 'auth ok': (r) => r.status === 200 });

  return { token: res.headers['Authorization'] || res.json('jwtToken') };
}

export default function (data) {
  const res = http.get(`${BASE_URL}/api/v1/cars`, {
    headers: { Authorization: `Bearer ${data.token}` },
    tags: { endpoint: 'GET /api/v1/cars' },
  });

  check(res, { 'status 200': (r) => r.status === 200 });

  sleep(0.1);
}
