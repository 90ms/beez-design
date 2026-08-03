import { join } from "node:path";
import { validateTokenRepository } from "./token-tools.mjs";

const tokenRoot = join(process.cwd(), "specification", "tokens");
const { contexts, documents } = validateTokenRepository(tokenRoot);

console.log(`Validated light: ${contexts.light.size} token values, types, and aliases.`);
console.log(`Validated dark: ${contexts.dark.size} token values, types, and aliases.`);
console.log(`Validated test-brand: ${contexts.test.size} token values, overrides, types, and aliases.`);
console.log("Validated Light/Dark semantic contracts and Kotlin identifiers.");
console.log(`Parsed ${documents.size} token files.`);
