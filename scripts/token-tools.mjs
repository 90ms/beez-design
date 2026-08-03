import { readdirSync, readFileSync } from "node:fs";
import { join, relative } from "node:path";

const SUPPORTED_TYPES = new Set([
  "color",
  "dimension",
  "duration",
  "fontFamily",
  "fontWeight",
  "typography",
]);

const TOKEN_SEGMENT = /^[a-z][A-Za-z0-9]*$/;

export const SEMANTIC_COLOR_CONTRAST_PAIRS = Object.freeze([
  {
    foreground: "color.semantic.foreground.primary",
    background: "color.semantic.background.neutral",
    minimum: 4.5,
    kind: "text",
  },
  {
    foreground: "color.semantic.foreground.secondary",
    background: "color.semantic.background.neutral",
    minimum: 4.5,
    kind: "text",
  },
  {
    foreground: "color.semantic.foreground.critical",
    background: "color.semantic.background.neutral",
    minimum: 4.5,
    kind: "text",
  },
  {
    foreground: "color.semantic.foreground.onBrand",
    background: "color.semantic.background.brand",
    minimum: 4.5,
    kind: "text",
  },
  {
    foreground: "color.semantic.background.brand",
    background: "color.semantic.background.neutral",
    minimum: 3,
    kind: "non-text",
  },
  {
    foreground: "color.semantic.stroke.neutral",
    background: "color.semantic.background.neutral",
    minimum: 3,
    kind: "non-text",
  },
  {
    foreground: "color.semantic.stroke.focus",
    background: "color.semantic.background.neutral",
    minimum: 3,
    kind: "non-text",
  },
  {
    foreground: "color.semantic.stroke.critical",
    background: "color.semantic.background.neutral",
    minimum: 3,
    kind: "non-text",
  },
]);

export const findJsonFiles = (directory) => readdirSync(directory, { withFileTypes: true })
  .flatMap((entry) => {
    const path = join(directory, entry.name);
    return entry.isDirectory()
      ? findJsonFiles(path)
      : entry.name.endsWith(".json")
        ? [path]
        : [];
  })
  .sort();

export const loadTokenDocuments = (tokenRoot) => {
  const files = findJsonFiles(tokenRoot);
  if (!files.length) throw new Error(`No token JSON files found under ${tokenRoot}`);

  return new Map(files.map((file) => [relative(tokenRoot, file), {
    value: JSON.parse(readFileSync(file, "utf8")),
    path: file,
  }]));
};

const flattenNode = ({
  node,
  path,
  table,
  source,
  inheritedType,
}) => {
  if (!node || typeof node !== "object" || Array.isArray(node)) return;

  const type = node.$type ?? inheritedType;
  if (Object.prototype.hasOwnProperty.call(node, "$value")) {
    const tokenPath = path.join(".");
    if (!tokenPath) throw new Error(`Token without path in ${source}`);
    if (!type) throw new Error(`Token without $type: ${tokenPath} (${source})`);
    if (table.has(tokenPath)) {
      const existing = table.get(tokenPath);
      throw new Error(`Duplicate token path: ${tokenPath} (${existing.source} and ${source})`);
    }

    table.set(tokenPath, {
      description: node.$description,
      path: tokenPath,
      source,
      type,
      value: node.$value,
    });
    return;
  }

  Object.entries(node).forEach(([key, value]) => {
    if (!key.startsWith("$")) {
      flattenNode({
        node: value,
        path: path.concat(key),
        table,
        source,
        inheritedType: type,
      });
    }
  });
};

export const buildTokenTable = (documents) => {
  const table = new Map();
  documents.forEach(({ value, path }) => flattenNode({
    node: value,
    path: [],
    table,
    source: path,
    inheritedType: undefined,
  }));
  return table;
};

export const applyTokenOverrides = (baseTable, overrideDocuments) => {
  const overrides = buildTokenTable(overrideDocuments);
  const result = new Map(baseTable);

  overrides.forEach((override, tokenPath) => {
    const base = baseTable.get(tokenPath);
    if (!base) throw new Error(`Override targets unknown token: ${tokenPath} (${override.source})`);
    if (base.type !== override.type) {
      throw new Error(`Override type mismatch: ${tokenPath} (${base.type} → ${override.type})`);
    }
    result.set(tokenPath, override);
  });

  return result;
};

export const resolveTokenValue = (value, tokenPath, table, stack = [tokenPath]) => {
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
    return resolveTokenValue(
      table.get(target).value,
      target,
      table,
      stack.concat(target),
    );
  }

  if (Array.isArray(value)) {
    return value.map((item) => resolveTokenValue(item, tokenPath, table, stack));
  }

  if (value && typeof value === "object") {
    return Object.fromEntries(Object.entries(value).map(([key, item]) => [
      key,
      resolveTokenValue(item, tokenPath, table, stack),
    ]));
  }

  return value;
};

export const resolveToken = (tokenPath, table) => {
  const token = table.get(tokenPath);
  if (!token) throw new Error(`Unknown token: ${tokenPath}`);
  return {
    ...token,
    value: resolveTokenValue(token.value, tokenPath, table),
  };
};

const assertFiniteNumber = (value, label) => {
  if (typeof value !== "number" || !Number.isFinite(value)) {
    throw new Error(`${label} must be a finite number`);
  }
};

const validateColor = (value, tokenPath) => {
  if (!value || typeof value !== "object" || Array.isArray(value)) {
    throw new Error(`Color token must be an object: ${tokenPath}`);
  }
  if (value.colorSpace !== "srgb") {
    throw new Error(`Unsupported color space for ${tokenPath}: ${value.colorSpace}`);
  }
  if (!Array.isArray(value.components) || value.components.length !== 3) {
    throw new Error(`sRGB color must have three components: ${tokenPath}`);
  }
  value.components.forEach((component, index) => {
    assertFiniteNumber(component, `${tokenPath}.components[${index}]`);
    if (component < 0 || component > 1) {
      throw new Error(`Color component out of range for ${tokenPath}: ${component}`);
    }
  });
  assertFiniteNumber(value.alpha, `${tokenPath}.alpha`);
  if (value.alpha < 0 || value.alpha > 1) {
    throw new Error(`Color alpha out of range for ${tokenPath}: ${value.alpha}`);
  }
};

const requireOpaqueColor = (value, label) => {
  validateColor(value, label);
  if (value.alpha !== 1) {
    throw new Error(`Contrast pair color must be opaque: ${label} (alpha ${value.alpha})`);
  }
};

const linearizeSrgbComponent = (component) => component <= 0.04045
  ? component / 12.92
  : ((component + 0.055) / 1.055) ** 2.4;

export const relativeLuminance = (color, label = "color") => {
  requireOpaqueColor(color, label);
  const [red, green, blue] = color.components.map(linearizeSrgbComponent);
  return 0.2126 * red + 0.7152 * green + 0.0722 * blue;
};

export const contrastRatio = (foreground, background) => {
  const foregroundLuminance = relativeLuminance(foreground, "foreground");
  const backgroundLuminance = relativeLuminance(background, "background");
  const lighter = Math.max(foregroundLuminance, backgroundLuminance);
  const darker = Math.min(foregroundLuminance, backgroundLuminance);
  return (lighter + 0.05) / (darker + 0.05);
};

export const validateColorContrastPairs = (
  table,
  context,
  pairs = SEMANTIC_COLOR_CONTRAST_PAIRS,
) => pairs.map((pair) => {
  const foreground = resolveToken(pair.foreground, table);
  const background = resolveToken(pair.background, table);
  if (foreground.type !== "color" || background.type !== "color") {
    throw new Error(`Contrast pair must reference color tokens: ${pair.foreground} on ${pair.background}`);
  }

  requireOpaqueColor(foreground.value, pair.foreground);
  requireOpaqueColor(background.value, pair.background);
  const ratio = contrastRatio(foreground.value, background.value);
  if (ratio < pair.minimum) {
    throw new Error(
      `Color contrast failure in ${context}: ${pair.foreground} on ${pair.background} ` +
      `is ${ratio.toFixed(2)}:1; ${pair.kind} requires at least ${pair.minimum}:1`,
    );
  }

  return { ...pair, ratio };
});

const validateDimension = (value, tokenPath, allowedUnits = new Set(["dp", "sp"])) => {
  if (!value || typeof value !== "object" || Array.isArray(value)) {
    throw new Error(`Dimension token must be an object: ${tokenPath}`);
  }
  assertFiniteNumber(value.value, `${tokenPath}.value`);
  if (!allowedUnits.has(value.unit)) {
    throw new Error(`Unsupported dimension unit for ${tokenPath}: ${value.unit}`);
  }
};

const validateDuration = (value, tokenPath) => {
  if (!value || typeof value !== "object" || Array.isArray(value)) {
    throw new Error(`Duration token must be an object: ${tokenPath}`);
  }
  assertFiniteNumber(value.value, `${tokenPath}.value`);
  if (!Number.isInteger(value.value) || value.value < 0) {
    throw new Error(`Duration must be a non-negative integer for ${tokenPath}`);
  }
  if (value.unit !== "ms") {
    throw new Error(`Unsupported duration unit for ${tokenPath}: ${value.unit}`);
  }
};

const validateFontFamily = (value, tokenPath) => {
  if (!Array.isArray(value) || !value.length || value.some((family) => typeof family !== "string")) {
    throw new Error(`Font family must be a non-empty string array: ${tokenPath}`);
  }
  if (value.length !== 1 || value[0] !== "sans-serif") {
    throw new Error(`Unsupported font family for ${tokenPath}: ${JSON.stringify(value)}`);
  }
};

const validateFontWeight = (value, tokenPath) => {
  assertFiniteNumber(value, tokenPath);
  if (!Number.isInteger(value) || value < 1 || value > 1000) {
    throw new Error(`Font weight out of range for ${tokenPath}: ${value}`);
  }
};

const validateTypography = (value, tokenPath) => {
  if (!value || typeof value !== "object" || Array.isArray(value)) {
    throw new Error(`Typography token must be an object: ${tokenPath}`);
  }

  const required = ["fontFamily", "fontSize", "lineHeight", "fontWeight"];
  required.forEach((property) => {
    if (!Object.prototype.hasOwnProperty.call(value, property)) {
      throw new Error(`Typography token is missing ${property}: ${tokenPath}`);
    }
  });

  validateFontFamily(value.fontFamily, `${tokenPath}.fontFamily`);
  validateDimension(value.fontSize, `${tokenPath}.fontSize`, new Set(["sp"]));
  validateDimension(value.lineHeight, `${tokenPath}.lineHeight`, new Set(["sp"]));
  validateFontWeight(value.fontWeight, `${tokenPath}.fontWeight`);
  if (value.letterSpacing !== undefined) {
    validateDimension(value.letterSpacing, `${tokenPath}.letterSpacing`, new Set(["sp"]));
  }
};

const validateResolvedValue = (type, value, tokenPath) => {
  switch (type) {
    case "color":
      validateColor(value, tokenPath);
      break;
    case "dimension":
      validateDimension(value, tokenPath);
      break;
    case "duration":
      validateDuration(value, tokenPath);
      break;
    case "fontFamily":
      validateFontFamily(value, tokenPath);
      break;
    case "fontWeight":
      validateFontWeight(value, tokenPath);
      break;
    case "typography":
      validateTypography(value, tokenPath);
      break;
    default:
      throw new Error(`Unsupported token type for ${tokenPath}: ${type}`);
  }
};

export const toKotlinIdentifier = (tokenPath) => {
  const segments = tokenPath.split(".");
  return segments.map((segment, index) => {
    const normalized = segment.replace(/[^A-Za-z0-9]+(.)?/g, (_, next = "") => next.toUpperCase());
    if (!normalized) throw new Error(`Token path cannot become a Kotlin identifier: ${tokenPath}`);
    return index === 0
      ? normalized[0].toLowerCase() + normalized.slice(1)
      : normalized[0].toUpperCase() + normalized.slice(1);
  }).join("");
};

export const validateIdentifierCollisions = (table) => {
  const identifiers = new Map();
  table.forEach((token, tokenPath) => {
    const identifier = toKotlinIdentifier(tokenPath);
    const existing = identifiers.get(identifier);
    if (existing && existing !== tokenPath) {
      throw new Error(`Kotlin identifier collision: ${identifier} (${existing} and ${tokenPath})`);
    }
    identifiers.set(identifier, tokenPath);
  });
};

export const validateTokenTable = (table) => {
  table.forEach((token, tokenPath) => {
    tokenPath.split(".").forEach((segment) => {
      if (!TOKEN_SEGMENT.test(segment)) {
        throw new Error(`Invalid token path segment in ${tokenPath}: ${segment}`);
      }
    });
    if (!SUPPORTED_TYPES.has(token.type)) {
      throw new Error(`Unsupported token type for ${tokenPath}: ${token.type}`);
    }
    const resolved = resolveToken(tokenPath, table);
    validateResolvedValue(token.type, resolved.value, tokenPath);
  });
  validateIdentifierCollisions(table);
};

const semanticContract = (table) => [...table.values()]
  .filter((token) => token.path.split(".")[1] === "semantic")
  .map((token) => `${token.path}:${token.type}`)
  .sort();

export const validateSemanticContracts = (lightTable, darkTable) => {
  const light = semanticContract(lightTable);
  const dark = semanticContract(darkTable);
  if (light.length !== dark.length || light.some((entry, index) => entry !== dark[index])) {
    const lightOnly = light.filter((entry) => !dark.includes(entry));
    const darkOnly = dark.filter((entry) => !light.includes(entry));
    throw new Error(`Light/Dark semantic contract mismatch (light only: ${lightOnly.join(", ") || "none"}; dark only: ${darkOnly.join(", ") || "none"})`);
  }
};

export const validateTokenRepository = (tokenRoot) => {
  const documents = loadTokenDocuments(tokenRoot);
  const select = (predicate) => [...documents.entries()]
    .filter(([file]) => predicate(file))
    .map(([, document]) => document);
  const scaleFiles = select((file) => file.startsWith("scale/"));
  const semanticFiles = select((file) => file.startsWith("semantic/") && !file.startsWith("semantic/color-"));
  const lightColor = select((file) => file === "semantic/color-light.tokens.json");
  const darkColor = select((file) => file === "semantic/color-dark.tokens.json");
  const testBrand = select((file) => file === "themes/test-brand.theme.json");

  if (!scaleFiles.length || !semanticFiles.length || lightColor.length !== 1 || darkColor.length !== 1 || testBrand.length !== 1) {
    throw new Error("Token source layout is incomplete: expected scale, semantic, light, dark, and test-brand files.");
  }

  const commonFiles = [...scaleFiles, ...semanticFiles];
  const light = buildTokenTable([...commonFiles, ...lightColor]);
  const dark = buildTokenTable([...commonFiles, ...darkColor]);
  const testBrandOverrides = buildTokenTable(testBrand);
  const test = applyTokenOverrides(light, testBrand);

  validateTokenTable(light);
  validateTokenTable(dark);
  validateTokenTable(test);
  validateSemanticContracts(light, dark);
  const contrast = {
    light: validateColorContrastPairs(light, "light"),
    dark: validateColorContrastPairs(dark, "dark"),
    test: validateColorContrastPairs(test, "test-brand"),
  };

  return {
    contrast,
    documents,
    contexts: { light, dark, test },
    overrides: { testBrand: testBrandOverrides },
  };
};
