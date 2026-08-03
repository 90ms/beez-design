import { existsSync, mkdtempSync, rmSync, writeFileSync } from "node:fs";
import { tmpdir } from "node:os";
import { basename, join, resolve } from "node:path";
import { spawnSync } from "node:child_process";
import {
  collectCatalogCodePoints,
  missingFontCodePoints,
  readFontCodePoints,
  requiredHangulCodePoints,
} from "./catalog-font.mjs";

function run(command, args) {
  const result = spawnSync(command, args, { encoding: "utf8", stdio: "inherit" });
  if (result.error) throw result.error;
  if (result.status !== 0) throw new Error(`${command} exited with status ${result.status}.`);
}

const sourceArgument = process.argv[2];
if (!sourceArgument) {
  console.error("Usage: node scripts/subset-catalog-font.mjs <NotoSansKR-variable-font.ttf>");
  process.exit(2);
}

const repositoryRoot = process.cwd();
const sourceFont = resolve(sourceArgument);
if (!existsSync(sourceFont)) throw new Error(`Source font does not exist: ${sourceFont}`);

const catalogSource = join(repositoryRoot, "beez-catalog", "src", "commonMain", "kotlin");
const outputFont = join(
  repositoryRoot,
  "beez-catalog",
  "src",
  "commonMain",
  "composeResources",
  "font",
  "noto_sans_kr_catalog.ttf",
);
const temporaryDirectory = mkdtempSync(join(tmpdir(), "beez-catalog-font-"));
const staticFont = join(temporaryDirectory, "NotoSansKR-Regular.ttf");
const characterFile = join(temporaryDirectory, "catalog-characters.txt");

try {
  const characters = [...collectCatalogCodePoints(catalogSource)]
    .sort((left, right) => left - right)
    .map((codePoint) => String.fromCodePoint(codePoint))
    .join("");
  writeFileSync(characterFile, characters, "utf8");

  run("fonttools", [
    "varLib.instancer",
    sourceFont,
    "wght=400",
    "--update-name-table",
    "--output",
    staticFont,
  ]);
  run("pyftsubset", [
    staticFont,
    `--output-file=${outputFont}`,
    `--text-file=${characterFile}`,
    "--layout-features=*",
    "--glyph-names",
    "--symbol-cmap",
    "--legacy-cmap",
    "--notdef-glyph",
    "--notdef-outline",
    "--recommended-glyphs",
    "--name-IDs=*",
    "--name-legacy",
    "--name-languages=*",
  ]);

  const missing = missingFontCodePoints(
    requiredHangulCodePoints(catalogSource),
    readFontCodePoints(outputFont),
  );
  if (missing.length > 0) throw new Error(`Generated font is missing ${missing.length} Hangul glyphs.`);
  console.log(`Generated ${basename(outputFont)} from ${basename(sourceFont)}.`);
} finally {
  rmSync(temporaryDirectory, { recursive: true, force: true });
}
