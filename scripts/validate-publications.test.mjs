import assert from "node:assert/strict";
import test from "node:test";
import {
  containsForbiddenMaterialDependency,
  expectedBeezDependency,
  expectedMainArtifactExtension,
  expectedPublicationNames,
} from "./validate-publications.mjs";

test("defines the three aligned root and target publication sets", () => {
  const publications = expectedPublicationNames();

  assert.equal(publications.length, 18);
  assert.equal(new Set(publications).size, 18);
  assert.ok(publications.includes("beez-components"));
  assert.ok(publications.includes("beez-foundation-iossimulatorarm64"));
  assert.ok(publications.includes("beez-tokens-wasm-js"));
});

test("maps each platform publication to its primary artifact", () => {
  assert.equal(expectedMainArtifactExtension("beez-components"), "jar");
  assert.equal(expectedMainArtifactExtension("beez-components-android"), "aar");
  assert.equal(expectedMainArtifactExtension("beez-components-desktop"), "jar");
  assert.equal(expectedMainArtifactExtension("beez-components-iosarm64"), "klib");
  assert.equal(expectedMainArtifactExtension("beez-components-wasm-js"), "klib");
});

test("maps the aligned internal dependency chain", () => {
  assert.equal(expectedBeezDependency("beez-components"), "beez-foundation");
  assert.equal(
    expectedBeezDependency("beez-components-iosarm64"),
    "beez-foundation-iosarm64",
  );
  assert.equal(
    expectedBeezDependency("beez-foundation-wasm-js"),
    "beez-tokens-wasm-js",
  );
  assert.equal(expectedBeezDependency("beez-tokens-android"), null);
});

test("detects forbidden Material dependency metadata", () => {
  assert.equal(containsForbiddenMaterialDependency("androidx.compose.material3"), true);
  assert.equal(containsForbiddenMaterialDependency("org.jetbrains.compose.material"), true);
  assert.equal(containsForbiddenMaterialDependency("org.jetbrains.compose.foundation"), false);
});
