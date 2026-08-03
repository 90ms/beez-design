import { resolveToken } from "./token-tools.mjs";

const colorProperties = [
  ["backgroundBrand", "color.semantic.background.brand"],
  ["backgroundNeutral", "color.semantic.background.neutral"],
  ["backgroundCritical", "color.semantic.background.critical"],
  ["foregroundPrimary", "color.semantic.foreground.primary"],
  ["foregroundSecondary", "color.semantic.foreground.secondary"],
  ["foregroundOnBrand", "color.semantic.foreground.onBrand"],
  ["foregroundCritical", "color.semantic.foreground.critical"],
  ["strokeNeutral", "color.semantic.stroke.neutral"],
  ["strokeFocus", "color.semantic.stroke.focus"],
  ["strokeCritical", "color.semantic.stroke.critical"],
  ["overlayScrim", "color.semantic.overlay.scrim"],
];

const typographyProperties = [
  ["display", "typography.semantic.display"],
  ["screenTitle", "typography.semantic.screenTitle"],
  ["sectionTitle", "typography.semantic.sectionTitle"],
  ["body", "typography.semantic.body"],
  ["label", "typography.semantic.label"],
  ["caption", "typography.semantic.caption"],
];

const spacingProperties = [
  ["minimumTouchTarget", "spacing.semantic.interaction.minimumTouchTarget"],
  ["screenGutter", "spacing.semantic.screen.gutter"],
  ["screenSectionGap", "spacing.semantic.screen.sectionGap"],
  ["contentInlineGap", "spacing.semantic.content.inlineGap"],
  ["contentStackGap", "spacing.semantic.content.stackGap"],
  ["controlContentGap", "spacing.semantic.control.contentGap"],
  ["controlCompactHeight", "spacing.semantic.control.compactHeight"],
  ["controlDefaultHeight", "spacing.semantic.control.defaultHeight"],
  ["controlComfortableHeight", "spacing.semantic.control.comfortableHeight"],
  ["controlCompactHorizontalInset", "spacing.semantic.control.compactHorizontalInset"],
  ["controlDefaultHorizontalInset", "spacing.semantic.control.defaultHorizontalInset"],
  ["controlComfortableHorizontalInset", "spacing.semantic.control.comfortableHorizontalInset"],
  ["controlCompactVerticalInset", "spacing.semantic.control.compactVerticalInset"],
  ["controlDefaultVerticalInset", "spacing.semantic.control.defaultVerticalInset"],
  ["controlComfortableVerticalInset", "spacing.semantic.control.comfortableVerticalInset"],
];

const shapeProperties = [
  ["controlRadius", "shape.semantic.control"],
  ["containerRadius", "shape.semantic.container"],
  ["overlayRadius", "shape.semantic.overlay"],
  ["roundRadius", "shape.semantic.round"],
];

const elevationProperties = [
  ["level0", "elevation.scale.level0"],
  ["level1", "elevation.scale.level1"],
  ["level2", "elevation.scale.level2"],
  ["raised", "elevation.semantic.raised"],
  ["floating", "elevation.semantic.floating"],
  ["overlay", "elevation.semantic.overlay"],
];

const motionProperties = [
  ["instantMillis", "duration.scale.instant"],
  ["fastMillis", "duration.scale.fast"],
  ["moderateMillis", "duration.scale.moderate"],
  ["slowMillis", "duration.scale.slow"],
];

const requireToken = (table, tokenPath, expectedType) => {
  const token = resolveToken(tokenPath, table);
  if (token.type !== expectedType) {
    throw new Error(`Expected ${expectedType} for ${tokenPath}, received ${token.type}`);
  }
  return token.value;
};

const formatNumber = (value) => Number.isInteger(value) ? `${value}` : `${value}`;

const renderConstructor = (name, properties, indent = 0) => {
  const padding = " ".repeat(indent);
  const propertyPadding = " ".repeat(indent + 4);
  return `${name}(\n${properties.map(([property, value]) => `${propertyPadding}${property} = ${value},`).join("\n")}\n${padding})`;
};

const byteHex = (component) => Math.round(component * 255)
  .toString(16)
  .toUpperCase()
  .padStart(2, "0");

export const renderColor = (value) => {
  const alpha = byteHex(value.alpha);
  const components = value.components.map(byteHex).join("");
  return `Color(0x${alpha}${components})`;
};

export const renderDimension = (value) => `${formatNumber(value.value)}.${value.unit}`;

const renderDuration = (value) => {
  if (value.unit !== "ms") throw new Error(`Unsupported duration unit: ${value.unit}`);
  return formatNumber(value.value);
};

const renderFontFamily = (value) => {
  if (value.length === 1 && value[0] === "sans-serif") return "FontFamily.SansSerif";
  throw new Error(`Unsupported font family: ${JSON.stringify(value)}`);
};

const renderFontWeight = (value) => {
  const named = new Map([
    [400, "Normal"],
    [500, "Medium"],
    [600, "SemiBold"],
    [700, "Bold"],
  ]);
  return named.has(value) ? `FontWeight.${named.get(value)}` : `FontWeight(${value})`;
};

const renderTextStyle = (value, indent) => renderConstructor("TextStyle", [
  ["fontFamily", renderFontFamily(value.fontFamily)],
  ["fontSize", renderDimension(value.fontSize)],
  ["lineHeight", renderDimension(value.lineHeight)],
  ["fontWeight", renderFontWeight(value.fontWeight)],
  ...(value.letterSpacing === undefined
    ? []
    : [["letterSpacing", renderDimension(value.letterSpacing)]]),
], indent);

const renderColorScheme = (table, indent = 0) => renderConstructor(
  "BeezColorScheme",
  colorProperties.map(([property, path]) => [property, renderColor(requireToken(table, path, "color"))]),
  indent,
);

const renderTypographyScheme = (table, indent = 0) => renderConstructor(
  "BeezTypographyScheme",
  typographyProperties.map(([property, path]) => [
    property,
    renderTextStyle(requireToken(table, path, "typography"), indent + 4),
  ]),
  indent,
);

const renderSpacingScheme = (table, indent = 0) => renderConstructor(
  "BeezSpacingScheme",
  spacingProperties.map(([property, path]) => [property, renderDimension(requireToken(table, path, "dimension"))]),
  indent,
);

const renderShapeScheme = (table, indent = 0) => renderConstructor(
  "BeezShapeScheme",
  shapeProperties.map(([property, path]) => [property, renderDimension(requireToken(table, path, "dimension"))]),
  indent,
);

const renderElevationScheme = (table, indent = 0) => renderConstructor(
  "BeezElevationScheme",
  elevationProperties.map(([property, path]) => [property, renderDimension(requireToken(table, path, "dimension"))]),
  indent,
);

const renderMotionScheme = (table, indent = 0) => renderConstructor(
  "BeezMotionScheme",
  motionProperties.map(([property, path]) => [property, renderDuration(requireToken(table, path, "duration"))]),
  indent,
);

export const renderBeezTokenSchemes = ({ light, dark }) => `// Generated by node scripts/generate-tokens.mjs from specification/tokens.
// Do not edit this file directly. Update the DTCG source and regenerate it.

package beez.design.tokens

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * BEEZ default schemes generated from the DTCG token source.
 */
public object BeezTokenSchemes {
    public val light: BeezTokenScheme = BeezTokenScheme(
        colors = lightColors(),
        typography = defaultTypography(),
        spacing = defaultSpacing(),
        shapes = defaultShapes(),
        elevation = defaultElevation(),
        motion = defaultMotion(),
    )

    public val dark: BeezTokenScheme = light.copy(
        colors = darkColors(),
    )
}

private fun lightColors(): BeezColorScheme = ${renderColorScheme(light)}

private fun darkColors(): BeezColorScheme = ${renderColorScheme(dark)}

private fun defaultTypography(): BeezTypographyScheme = ${renderTypographyScheme(light)}

private fun defaultSpacing(): BeezSpacingScheme = ${renderSpacingScheme(light)}

private fun defaultShapes(): BeezShapeScheme = ${renderShapeScheme(light)}

private fun defaultElevation(): BeezElevationScheme = ${renderElevationScheme(light)}

private fun defaultMotion(): BeezMotionScheme = ${renderMotionScheme(light)}
`;
