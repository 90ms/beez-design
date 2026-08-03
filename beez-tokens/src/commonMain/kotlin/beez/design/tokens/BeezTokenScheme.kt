package beez.design.tokens

import androidx.compose.runtime.Immutable

/**
 * Complete semantic token contract consumed by BEEZ foundation and components.
 */
@Immutable
public data class BeezTokenScheme(
    public val colors: BeezColorScheme,
    public val typography: BeezTypographyScheme,
    public val spacing: BeezSpacingScheme,
    public val shapes: BeezShapeScheme,
    public val elevation: BeezElevationScheme,
    public val motion: BeezMotionScheme,
)

/**
 * Convenience helper for creating a product theme while preserving untouched roles.
 */
public fun BeezTokenScheme.withColors(colors: BeezColorScheme): BeezTokenScheme =
    copy(colors = colors)

/**
 * Convenience helper for creating a product theme while preserving untouched roles.
 */
public fun BeezTokenScheme.withShapes(shapes: BeezShapeScheme): BeezTokenScheme =
    copy(shapes = shapes)
