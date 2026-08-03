import assert from "node:assert/strict";
import { join } from "node:path";
import test from "node:test";
import {
  applyTokenOverrides,
  buildTokenTable,
  resolveToken,
  validateIdentifierCollisions,
  validateSemanticContracts,
  validateTokenRepository,
  validateTokenTable,
} from "./token-tools.mjs";

const document = (value, path = "test.tokens.json") => ({ value, path });
const token = (type, value) => ({ $type: type, $value: value });
const dimension = (value, unit = "dp") => ({ value, unit });

test("validates the current repository contexts", () => {
  const result = validateTokenRepository(join(process.cwd(), "specification", "tokens"));

  assert.equal(result.contexts.light.size, 90);
  assert.equal(result.contexts.dark.size, 90);
  assert.equal(result.contexts.test.size, 90);
  assert.equal(result.documents.size, 15);
});

test("rejects a missing alias", () => {
  const table = buildTokenTable([document({
    spacing: { semantic: { gap: token("dimension", "{dimension.scale.missing}") } },
  })]);

  assert.throws(() => resolveToken("spacing.semantic.gap", table), /Missing token alias/);
});

test("rejects an alias cycle", () => {
  const table = buildTokenTable([document({
    dimension: {
      scale: {
        first: token("dimension", "{dimension.scale.second}"),
        second: token("dimension", "{dimension.scale.first}"),
      },
    },
  })]);

  assert.throws(() => resolveToken("dimension.scale.first", table), /Token alias cycle/);
});

test("rejects an unsupported dimension unit", () => {
  const table = buildTokenTable([document({
    dimension: { scale: { invalid: token("dimension", dimension(1, "px")) } },
  })]);

  assert.throws(() => validateTokenTable(table), /Unsupported dimension unit/);
});

test("rejects a Light and Dark semantic contract mismatch", () => {
  const light = buildTokenTable([document({
    color: { semantic: { foreground: { primary: token("color", {
      colorSpace: "srgb",
      components: [0, 0, 0],
      alpha: 1,
    }) } } },
  }, "light.json")]);
  const dark = buildTokenTable([document({}, "dark.json")]);

  assert.throws(() => validateSemanticContracts(light, dark), /semantic contract mismatch/);
});

test("rejects an override for an unknown semantic token", () => {
  const base = buildTokenTable([document({
    dimension: { semantic: { known: token("dimension", dimension(8)) } },
  }, "base.json")]);
  const overrides = [document({
    dimension: { semantic: { unknown: token("dimension", dimension(12)) } },
  }, "brand.json")];

  assert.throws(() => applyTokenOverrides(base, overrides), /Override targets unknown token/);
});

test("rejects Kotlin identifier collisions", () => {
  const table = buildTokenTable([document({
    color: {
      semantic: {
        foregroundPrimary: token("fontWeight", 400),
        foreground: { primary: token("fontWeight", 500) },
      },
    },
  })]);

  assert.throws(() => validateIdentifierCollisions(table), /Kotlin identifier collision/);
});
