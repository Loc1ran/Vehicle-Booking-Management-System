import http from 'k6/http';
import { check, sleep, fail } from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8088';
const TEST_USER = __ENV.TEST_USER || 'loadtest-user';
const TEST_PASSWORD = __ENV.TEST_PASSWORD || 'password';
const EXPECTED_CARS = Number(__ENV.EXPECTED_CARS || 100);

if (!/^http:\/\/(localhost|127\.0\.0\.1):\d+$/.test(BASE_URL)) {
  throw new Error('This benchmark only targets an isolated loopback server.');
}

export const options = {
  ...(__ENV.VUS ? { vus: Number(__ENV.VUS), duration: __ENV.DURATION || '30s' } : { stages: [
    { duration: '30s', target: Number(__ENV.RAMP_1 || 50) },
    { duration: '1m', target: Number(__ENV.RAMP_2 || 200) },
    { duration: '1m', target: Number(__ENV.RAMP_3 || 500) },
    { duration: '30s', target: 0 },
  ] }),
  thresholds: {
    'http_reqs{phase:measure}': ['count>0'],
    'http_req_failed{phase:measure}': ['rate==0'],
    'http_req_duration{phase:measure}': ['p(95)<500'],
    'checks{phase:measure}': ['rate==1'],
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

  if (res.status !== 200) fail(`Authentication failed: HTTP ${res.status}`);
  const token = res.headers['Authorization'] || res.json('jwtToken');
  if (!token) fail('Authentication returned no token');
  const probe = http.get(`${BASE_URL}/api/v1/cars`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (probe.status !== 200 || probe.json().length !== EXPECTED_CARS) {
    fail(`Expected HTTP 200 and exactly ${EXPECTED_CARS} seeded cars`);
  }

  return { token };
}

export default function (data) {
  const res = http.get(`${BASE_URL}/api/v1/cars`, {
    headers: { Authorization: `Bearer ${data.token}` },
    tags: { endpoint: 'GET /api/v1/cars', phase: 'measure' },
  });

  check(res, {
    'status 200': (r) => r.status === 200,
    'complete seeded catalog': (r) => r.status === 200 && r.json().length === EXPECTED_CARS,
  }, { phase: 'measure' });

  sleep(0.1);
}

export function handleSummary(data) {
  data.benchmark = { vus: __ENV.VUS || 'staged', duration: __ENV.DURATION || 'staged', expected_cars: EXPECTED_CARS, base_url: BASE_URL };
  return { [__ENV.SUMMARY_PATH || 'summary.json']: JSON.stringify(data, null, 2) };
}
