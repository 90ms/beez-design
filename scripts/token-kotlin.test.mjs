import assert from "node:assert/strict";
import { join } from "node:path";
import test from "node:test";
import {
  renderBeezTokenSchemes,
  renderColor,
  renderDimension,
} from "./token-kotlin.mjs";
import { validateTokenRepository } from "./token-tools.mjs";

test("renders sRGB and alpha as an ARGB Compose color", () => {
  assert.equal(renderColor({
    colorSpace: "srgb",
    components: [0.603922, 0.203922, 0.070588],
    alpha: 1,
  }), "Color(0xFF9A3412)");
  assert.equal(renderColor({
    colorSpace: "srgb",
    components: [0, 0, 0],
    alpha: 0.6,
  }), "Color(0x99000000)");
});

test("renders Compose dimensions with their source unit", () => {
  assert.equal(renderDimension({ value: 12, unit: "dp" }), "12.dp");
  assert.equal(renderDimension({ value: 16, unit: "sp" }), "16.sp");
});

test("renders deterministic default schemes from the repository source", () => {
  const { contexts } = validateTokenRepository(join(process.cwd(), "specification", "tokens"));
  const first = renderBeezTokenSchemes(contexts);
  const second = renderBeezTokenSchemes(contexts);

  assert.equal(first, second);
  assert.match(first, /public object BeezTokenSchemes/);
  assert.match(first, /containerRadius = 12\.dp/);
  assert.match(first, /fontWeight = FontWeight\.Normal/);
  assert.doesNotMatch(first, /containerRadius = 16\.dp/);
  assert.equal(first.endsWith("\n"), true);
});
