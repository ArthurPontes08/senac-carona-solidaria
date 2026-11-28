import assert from "node:assert/strict";
import test from "node:test";
import { createRuntimeConfig } from "../scripts/generate-config.mjs";

test("generates a normalized HTTPS API base", () => {
  assert.equal(
    createRuntimeConfig("https://api.example.onrender.com/api/"),
    'window.VAIJUNTO_CONFIG = Object.freeze({ apiBase: "https://api.example.onrender.com/api" });\n',
  );
});

test("allows HTTP only for local development", () => {
  assert.equal(
    createRuntimeConfig("http://localhost:8080/api"),
    'window.VAIJUNTO_CONFIG = Object.freeze({ apiBase: "http://localhost:8080/api" });\n',
  );
  assert.throws(() => createRuntimeConfig("http://api.example.com/api"), /must use HTTPS/);
});

test("rejects missing, credentialed, or non-API URLs", () => {
  assert.throws(() => createRuntimeConfig(" "), /must be set/);
  assert.throws(() => createRuntimeConfig("not-a-url"), /absolute HTTP\(S\) URL/);
  assert.throws(() => createRuntimeConfig("https://user:pass@api.example.com/api"), /cannot include credentials/);
  assert.throws(() => createRuntimeConfig("https://api.example.com/api?key=value"), /cannot include credentials/);
  assert.throws(() => createRuntimeConfig("https://api.example.com"), /must end with \/api/);
});
