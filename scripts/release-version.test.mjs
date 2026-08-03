import assert from "node:assert/strict";
import test from "node:test";
import {
  isBeezVersion,
  validateBeezVersion,
} from "./release-version.mjs";

test("accepts the aligned BEEZ version forms", () => {
  [
    "0.1.0-SNAPSHOT",
    "0.1.0-alpha.1",
    "0.1.0-beta.2",
    "0.1.0-rc.3",
    "1.0.0",
  ].forEach((version) => assert.equal(validateBeezVersion(version), version));
});

test("rejects unsupported or ambiguous version forms", () => {
  [
    "",
    "0.1",
    "v0.1.0",
    "0.1.0-alpha",
    "0.1.0-alpha.01",
    "0.1.0-preview.1",
    "01.0.0",
    "0.1.0+build.1",
  ].forEach((version) => assert.equal(isBeezVersion(version), false));
});
