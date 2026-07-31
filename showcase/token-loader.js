(() => {
  const tokenFiles = [
    "../specification/tokens/scale/color.tokens.json",
    "../specification/tokens/scale/dimension.tokens.json",
    "../specification/tokens/scale/typography.tokens.json",
    "../specification/tokens/scale/radius.tokens.json",
    "../specification/tokens/scale/elevation.tokens.json",
    "../specification/tokens/scale/motion.tokens.json",
    "../specification/tokens/semantic/color-light.tokens.json",
    "../specification/tokens/semantic/color-dark.tokens.json",
    "../specification/tokens/semantic/typography.tokens.json",
    "../specification/tokens/semantic/spacing.tokens.json",
    "../specification/tokens/semantic/shape.tokens.json",
    "../specification/tokens/semantic/elevation.tokens.json",
    "../specification/tokens/semantic/motion.tokens.json",
    "../specification/tokens/themes/test-brand.theme.json",
  ];

  const flatten = (node, path, result) => {
    if (!node || typeof node !== "object" || Array.isArray(node)) return;
    if (Object.prototype.hasOwnProperty.call(node, "$value")) {
      result[path.join(".")] = node.$value;
      return;
    }
    Object.entries(node).forEach(([key, value]) => {
      if (!key.startsWith("$")) flatten(value, path.concat(key), result);
    });
  };

  const mergeTables = (files) => {
    const table = {};
    files.forEach((file) => Object.assign(table, file));
    return table;
  };

  const resolve = (value, table, stack = []) => {
    if (typeof value === "string") {
      const alias = value.match(/^\{([^}]+)\}$/);
      if (!alias) return value;
      const path = alias[1];
      if (stack.includes(path)) throw new Error("Token alias cycle: " + stack.join(" → ") + " → " + path);
      if (!Object.prototype.hasOwnProperty.call(table, path)) throw new Error("Missing token alias: " + path);
      return resolve(table[path], table, stack.concat(path));
    }
    if (Array.isArray(value)) return value.map((item) => resolve(item, table, stack));
    if (value && typeof value === "object") {
      return Object.fromEntries(Object.entries(value).map(([key, item]) => [
        key,
        resolve(item, table, stack),
      ]));
    }
    return value;
  };

  const colorToCss = (value) => {
    if (typeof value === "string") return value;
    const channels = (value.components || [0, 0, 0]).map((channel) => Math.round(channel * 255));
    const alpha = value.alpha === undefined ? 1 : value.alpha;
    if (alpha < 1) return "rgba(" + channels.join(", ") + ", " + alpha + ")";
    return "#" + channels.map((channel) => channel.toString(16).padStart(2, "0")).join("");
  };

  const dimensionToCss = (value) => {
    if (!value || typeof value !== "object") return String(value);
    const unit = value.unit === "sp" || value.unit === "dp" ? "px" : value.unit;
    return value.value + unit;
  };

  const buildTheme = (appearance, brand, tables) => {
    const common = tables.common;
    const appearanceTable = appearance === "dark" ? tables.dark : tables.light;
    const overrides = brand === "test" ? tables.testBrand : {};
    const table = Object.assign({}, common, appearanceTable, overrides);
    const get = (path) => resolve(table[path], table);
    const brandColor = colorToCss(get("color.semantic.background.brand"));
    const neutralColor = colorToCss(get("color.semantic.background.neutral"));
    const primaryColor = colorToCss(get("color.semantic.foreground.primary"));
    const secondaryColor = colorToCss(get("color.semantic.foreground.secondary"));
    const onBrandColor = colorToCss(get("color.semantic.foreground.onBrand"));
    const strokeColor = colorToCss(get("color.semantic.stroke.neutral"));
    const focusColor = colorToCss(get("color.semantic.stroke.focus"));
    const criticalColor = colorToCss(get("color.semantic.background.critical"));
    const typographyDisplay = get("typography.semantic.display");
    const typographyBody = get("typography.semantic.body");

    return {
      css: {
        "--brand": brandColor,
        "--brand-soft": "color-mix(in srgb, " + brandColor + " 20%, " + neutralColor + ")",
        "--on-brand": onBrandColor,
        "--surface": neutralColor,
        "--surface-raised": neutralColor,
        "--ink": primaryColor,
        "--ink-soft": secondaryColor,
        "--line": strokeColor,
        "--focus": focusColor,
        "--critical": criticalColor,
        "--radius-control": dimensionToCss(get("shape.semantic.control")),
        "--radius-card": dimensionToCss(get("shape.semantic.container")),
        "--radius-pill": dimensionToCss(get("shape.semantic.round")),
        "--space-inline": dimensionToCss(get("spacing.semantic.content.inlineGap")),
        "--space-stack": dimensionToCss(get("spacing.semantic.content.stackGap")),
        "--space-gutter": dimensionToCss(get("spacing.semantic.screen.gutter")),
        "--minimum-touch-target": dimensionToCss(get("spacing.semantic.interaction.minimumTouchTarget")),
        "--control-compact-height": dimensionToCss(get("spacing.semantic.control.compactHeight")),
        "--control-default-height": dimensionToCss(get("spacing.semantic.control.defaultHeight")),
        "--control-comfortable-height": dimensionToCss(get("spacing.semantic.control.comfortableHeight")),
        "--control-compact-inset": dimensionToCss(get("spacing.semantic.control.compactHorizontalInset")),
        "--control-default-inset": dimensionToCss(get("spacing.semantic.control.defaultHorizontalInset")),
        "--control-comfortable-inset": dimensionToCss(get("spacing.semantic.control.comfortableHorizontalInset")),
        "--display-token-size": dimensionToCss(typographyDisplay.fontSize),
        "--body-token-size": dimensionToCss(typographyBody.fontSize),
      },
      labels: {
        brand: brand === "test" ? "Test Brand" : "BEEZ",
        appearance: appearance === "dark" ? "Dark" : "Light",
        colors: {
          brand: brandColor,
          primary: primaryColor,
          focus: focusColor,
        },
        dimensions: {
          inline: dimensionToCss(get("spacing.semantic.content.inlineGap")),
          stack: dimensionToCss(get("spacing.semantic.content.stackGap")),
          gutter: dimensionToCss(get("spacing.semantic.screen.gutter")),
          control: dimensionToCss(get("shape.semantic.control")),
          fast: dimensionToCss(get("duration.semantic.colorTransition")),
          moderate: dimensionToCss(get("duration.scale.moderate")),
        },
      },
    };
  };

  const ready = Promise.all(tokenFiles.map((file) => fetch(file).then((response) => {
    if (!response.ok) throw new Error("Unable to load " + file);
    return response.json();
  }))).then((documents) => {
    const tables = {
      common: mergeTables(documents.slice(0, 6).map((document) => {
        const table = {};
        flatten(document, [], table);
        return table;
      }).concat(documents.slice(8, 13).map((document) => {
        const table = {};
        flatten(document, [], table);
        return table;
      }))),
      light: {},
      dark: {},
      testBrand: {},
    };
    flatten(documents[6], [], tables.light);
    flatten(documents[7], [], tables.dark);
    flatten(documents[13], [], tables.testBrand);

    return {
      applyTheme(appearance, brand) {
        const theme = buildTheme(appearance, brand, tables);
        const root = document.documentElement;
        root.dataset.theme = appearance;
        root.dataset.brand = brand;
        Object.entries(theme.css).forEach(([name, value]) => root.style.setProperty(name, value));
        return theme;
      },
    };
  });

  window.BeezTokenLoader = { ready };
})();
