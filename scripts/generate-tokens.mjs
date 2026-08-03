import { existsSync, mkdirSync, readFileSync, writeFileSync } from "node:fs";
import { dirname, join, relative } from "node:path";
import {
  renderBeezTokenSchemes,
  renderCatalogTestBrandTokens,
} from "./token-kotlin.mjs";
import { validateTokenRepository } from "./token-tools.mjs";

const repositoryRoot = process.cwd();
const tokenRoot = join(repositoryRoot, "specification", "tokens");
const coreOutput = join(
  repositoryRoot,
  "beez-tokens",
  "src",
  "commonMain",
  "kotlin",
  "beez",
  "design",
  "tokens",
  "BeezTokenSchemes.generated.kt",
);
const catalogOutput = join(
  repositoryRoot,
  "beez-catalog",
  "src",
  "commonMain",
  "kotlin",
  "beez",
  "design",
  "catalog",
  "CatalogTestBrandTokens.generated.kt",
);

const { contexts, overrides } = validateTokenRepository(tokenRoot);
const outputs = [
  {
    path: coreOutput,
    content: renderBeezTokenSchemes(contexts),
  },
  {
    path: catalogOutput,
    content: renderCatalogTestBrandTokens(contexts.test, overrides.testBrand),
  },
];
const check = process.argv.includes("--check");

if (check) {
  const drifted = outputs.filter(({ path, content }) => (
    !existsSync(path) || readFileSync(path, "utf8") !== content
  ));
  if (drifted.length) {
    drifted.forEach(({ path }) => console.error(`Generated token output is stale: ${relative(repositoryRoot, path)}`));
    console.error("Run: node scripts/generate-tokens.mjs");
    process.exitCode = 1;
  } else {
    console.log(`Verified ${outputs.length} generated token output.`);
  }
} else {
  outputs.forEach(({ path, content }) => {
    mkdirSync(dirname(path), { recursive: true });
    writeFileSync(path, content, "utf8");
    console.log(`Generated ${relative(repositoryRoot, path)}`);
  });
}
