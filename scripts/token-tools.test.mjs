import assert from "node:assert/strict";
import { join } from "node:path";
import test from "node:test";
import {
  applyTokenOverrides,
  buildTokenTable,
  contrastRatio,
  resolveToken,
  validateColorContrastPairs,
  validateIdentifierCollisions,
  validateSemanticContracts,
  validateTokenRepository,
  validateTokenTable,
} from "./token-tools.mjs";

const document = (value, path = "test.tokens.json") => ({ value, path });
const token = (type, value) => ({ $type: type, $value: value });
const dimension = (value, unit = "dp") => ({ value, unit });
const color = (components, alpha = 1) => ({ colorSpace: "srgb", components, alpha });
const contrastTable = (foreground, background) => buildTokenTable([document({
  color: {
    semantic: {
      foreground: { test: token("color", foreground) },
      background: { test: token("color", background) },
    },
  },
})]);
const contrastPair = (minimum = 4.5, kind = "text") => [{
  foreground: "color.semantic.foreground.test",
  background: "color.semantic.background.test",
  minimum,
  kind,
}];

test("validates the current repository contexts", () => {
  const result = validateTokenRepository(join(process.cwd(), "specification", "tokens"));

  assert.equal(result.contexts.light.size, 90);
  assert.equal(result.contexts.dark.size, 90);
  assert.equal(result.contexts.test.size, 90);
  assert.equal(result.contrast.light.length, 8);
  assert.equal(result.contrast.dark.length, 8);
  assert.equal(result.contrast.test.length, 8);
  assert.equal(result.documents.size, 15);
});

test("calculates WCAG relative luminance contrast without rounding", () => {
  assert.equal(contrastRatio(color([0, 0, 0]), color([1, 1, 1])), 21);

  const ratio = contrastRatio(color([0.5, 0.5, 0.5]), color([1, 1, 1]));
  const table = contrastTable(color([0.5, 0.5, 0.5]), color([1, 1, 1]));
  assert.throws(
    () => validateColorContrastPairs(table, "test", contrastPair(ratio + 1e-12)),
    /Color contrast failure in test/,
  );
});

test("rejects a semantic text pair below its minimum contrast", () => {
  const table = contrastTable(color([0.5, 0.5, 0.5]), color([1, 1, 1]));

  assert.throws(
    () => validateColorContrastPairs(table, "test", contrastPair()),
    /text requires at least 4.5:1/,
  );
});

test("rejects a semantic non-text pair below its minimum contrast", () => {
  const table = contrastTable(color([0.6, 0.6, 0.6]), color([1, 1, 1]));

  assert.throws(
    () => validateColorContrastPairs(table, "test", contrastPair(3, "non-text")),
    /non-text requires at least 3:1/,
  );
});

test("rejects alpha colors without an explicit compositing contract", () => {
  const table = contrastTable(color([0, 0, 0], 0.8), color([1, 1, 1]));

  assert.throws(
    () => validateColorContrastPairs(table, "test", contrastPair()),
    /Contrast pair color must be opaque/,
  );
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
