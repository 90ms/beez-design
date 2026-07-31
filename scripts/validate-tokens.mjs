import { readdirSync, readFileSync } from "node:fs";
import { join, relative } from "node:path";

const tokenRoot = join(process.cwd(), "specification", "tokens");

const findJsonFiles = (directory) => readdirSync(directory, { withFileTypes: true })
  .flatMap((entry) => {
    const path = join(directory, entry.name);
    return entry.isDirectory() ? findJsonFiles(path) : entry.name.endsWith(".json") ? [path] : [];
  })
  .sort();

const flatten = (node, path, table, source, allowOverrides = false) => {
  if (!node || typeof node !== "object" || Array.isArray(node)) return;
  if (Object.prototype.hasOwnProperty.call(node, "$value")) {
    const tokenPath = path.join(".");
    if (table.has(tokenPath) && !allowOverrides) {
      throw new Error(`Duplicate token path: ${tokenPath} (${relative(process.cwd(), table.get(tokenPath).source)} and ${relative(process.cwd(), source)})`);
    }
    table.set(tokenPath, { value: node.$value, source });
    return;
  }
  Object.entries(node).forEach(([key, value]) => {
    if (!key.startsWith("$")) flatten(value, path.concat(key), table, source, allowOverrides);
  });
};

const resolve = (value, tokenPath, table, stack = []) => {
  if (typeof value === "string") {
    const alias = value.match(/^\{([^}]+)\}$/);
    if (!alias) return value;
    const target = alias[1];
    if (stack.includes(target)) {
      throw new Error(`Token alias cycle: ${stack.join(" → ")} → ${target}`);
    }
    if (!table.has(target)) {
      throw new Error(`Missing token alias: ${tokenPath} → ${target}`);
    }
    return resolve(table.get(target).value, target, table, stack.concat(target));
  }
  if (Array.isArray(value)) return value.map((item) => resolve(item, tokenPath, table, stack));
  if (value && typeof value === "object") {
    return Object.fromEntries(Object.entries(value).map(([key, item]) => [
      key,
      resolve(item, tokenPath, table, stack),
    ]));
  }
  return value;
};

const files = findJsonFiles(tokenRoot);
if (!files.length) throw new Error(`No token JSON files found under ${tokenRoot}`);

const documents = new Map(files.map((file) => [relative(tokenRoot, file), {
  value: JSON.parse(readFileSync(file, "utf8")),
  path: file,
}]));
const filePaths = (predicate) => [...documents.entries()]
  .filter(([file]) => predicate(file))
  .map(([, document]) => document);
const scaleFiles = filePaths((file) => file.startsWith("scale/"));
const semanticFiles = filePaths((file) => file.startsWith("semantic/") && !file.startsWith("semantic/color-"));
const lightColor = filePaths((file) => file === "semantic/color-light.tokens.json");
const darkColor = filePaths((file) => file === "semantic/color-dark.tokens.json");
const testBrand = filePaths((file) => file === "themes/test-brand.theme.json");

const buildTable = (contextFiles, allowOverrides = false) => {
  const table = new Map();
  contextFiles.forEach(({ value, path }) => flatten(value, [], table, path, allowOverrides));
  return table;
};

const validateContext = (name, contextFiles, allowOverrides = false) => {
  const table = buildTable(contextFiles, allowOverrides);
  table.forEach(({ value }, tokenPath) => resolve(value, tokenPath, table, [tokenPath]));
  console.log(`Validated ${name}: ${table.size} token values and all aliases.`);
};

if (!scaleFiles.length || !semanticFiles.length || !lightColor.length || !darkColor.length) {
  throw new Error("Token source layout is incomplete: expected scale, semantic, light, and dark files.");
}

const commonFiles = [...scaleFiles, ...semanticFiles];
validateContext("light", [...commonFiles, ...lightColor]);
validateContext("dark", [...commonFiles, ...darkColor]);
validateContext("test-brand", [...commonFiles, ...lightColor, ...testBrand], true);
console.log(`Parsed ${files.length} token files.`);
