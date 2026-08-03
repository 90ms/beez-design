import { join } from "node:path";
import {
  formatCodePoints,
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

const required = requiredHangulCodePoints(sourceRoot);
const supported = readFontCodePoints(fontPath);
const missing = missingFontCodePoints(required, supported);

if (missing.length > 0) {
  console.error(`Catalog font is missing ${missing.length} required Hangul glyphs:`);
  console.error(missing.map((codePoint) => String.fromCodePoint(codePoint)).join(""));
  console.error(formatCodePoints(missing));
  process.exitCode = 1;
} else {
  console.log(`Validated ${required.size} Hangul glyphs used by the Catalog.`);
}
