import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    outDir: "build", // para que coincida con el CMD del Dockerfile
    emptyOutDir: true,
  },
  base: "/", // ruta base relativa (muy importante en Docker)
});
