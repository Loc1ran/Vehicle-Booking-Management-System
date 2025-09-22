import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    allowedHosts: [
      "localhost",
      "127.0.0.1",
      "loctran-api-env.eba-p5enpnf2.us-east-1.elasticbeanstalk.com",
    ],
    proxy: {
      "/api": {
        target: "http://loctran-api-env.eba-p5enpnf2.us-east-1.elasticbeanstalk.com",
        changeOrigin: true,
      },
    },
  },
});