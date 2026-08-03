import assert from "node:assert/strict";
import { join } from "node:path";
import test from "node:test";
import {
  missingFontCodePoints,
  readFontCodePoints,
  requiredHangulCodePoints,
} from "./catalog-font.mjs";

const repositoryRoot = process.cwd();
const sourceRoot = join(repositoryRoot, "beez-catalog", "src", "commonMain", "kotlin");
const fontPath = join(
  repositoryRoot,
  "beez-catalog",
  "src",
  "commonMain",
  "composeResources",
  "font",
  "noto_sans_kr_catalog.ttf",
);

test("Catalog font covers every Hangul glyph used by commonMain", () => {
  const required = requiredHangulCodePoints(sourceRoot);
  const supported = readFontCodePoints(fontPath);

  assert.ok(required.size > 0);
  assert.deepEqual(missingFontCodePoints(required, supported), []);
});
