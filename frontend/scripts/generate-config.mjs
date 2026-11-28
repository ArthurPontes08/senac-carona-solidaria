import { writeFile } from "node:fs/promises";
import { dirname, resolve } from "node:path";
import { fileURLToPath } from "node:url";

export function createRuntimeConfig(apiBaseUrl) {
  const normalizedApiBaseUrl = typeof apiBaseUrl === "string" ? apiBaseUrl.trim() : "";
  if (!normalizedApiBaseUrl) {
    throw new Error("API_BASE_URL must be set to the deployed backend API URL.");
  }

  let apiUrl;
  try {
    apiUrl = new URL(normalizedApiBaseUrl);
  } catch (error) {
    throw new Error("API_BASE_URL must be an absolute HTTP(S) URL.", { cause: error });
  }

  const isLocal = ["localhost", "127.0.0.1"].includes(apiUrl.hostname);
  if (!["https:", ...(isLocal ? ["http:"] : [])].includes(apiUrl.protocol)
      || apiUrl.username || apiUrl.password || apiUrl.search || apiUrl.hash) {
    throw new Error("API_BASE_URL must use HTTPS and cannot include credentials, a query, or a fragment.");
  }
  if (!apiUrl.pathname.replace(/\/+$/, "").endsWith("/api")) {
    throw new Error("API_BASE_URL must end with /api.");
  }

  return `window.VAIJUNTO_CONFIG = Object.freeze({ apiBase: ${JSON.stringify(apiUrl.toString().replace(/\/+$/, ""))} });\n`;
}

const scriptPath = fileURLToPath(import.meta.url);
if (process.argv[1] && resolve(process.argv[1]) === scriptPath) {
  const configPath = resolve(dirname(scriptPath), "../js/config.js");
  await writeFile(configPath, createRuntimeConfig(process.env.API_BASE_URL), "utf8");
}
