import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173, // Puerto donde corre Vite (ya lo usas por defecto)
    proxy: {
      "/api": {
        target: "http://localhost:8080", // URL de tu backend Spring Boot
        changeOrigin: true,              // cambia el origen de la request
        secure: false,                   // desactiva verificación SSL (útil si usas HTTPS local)
      },
    },
  },
});
