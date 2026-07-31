package beez.design.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
 * BEEZ's provisional default schemes.
 *
 * Values are intentionally centralized here until the token generator is introduced.
 * Consumers should copy a scheme and replace semantic roles instead of using these
 * values directly.
 */
public object BeezTokenSchemes {
    public val light: BeezTokenScheme = BeezTokenScheme(
        colors = BeezColorScheme(
            backgroundBrand = Color(0xFF9A3412),
            backgroundNeutral = Color.White,
            backgroundCritical = Color(0xFFBA1A1A),
            foregroundPrimary = Color(0xFF1C1B1F),
            foregroundSecondary = Color(0xFF625B71),
            foregroundOnBrand = Color(0xFFFFFFFF),
            strokeNeutral = Color(0xFF79747E),
            strokeFocus = Color(0xFF9A3412),
            overlayScrim = Color(0x99000000),
        ),
        typography = defaultTypography(),
        spacing = BeezSpacingScheme(
            screenGutter = 24.dp,
            screenSectionGap = 32.dp,
            contentInlineGap = 8.dp,
            contentStackGap = 16.dp,
            controlContentGap = 8.dp,
        ),
        shapes = BeezShapeScheme(
            controlRadius = 12.dp,
            containerRadius = 16.dp,
            overlayRadius = 24.dp,
            roundRadius = 999.dp,
        ),
        elevation = defaultElevation(),
        motion = defaultMotion(),
    )

    public val dark: BeezTokenScheme = light.copy(
        colors = BeezColorScheme(
            backgroundBrand = Color(0xFFFFB36B),
            backgroundNeutral = Color(0xFF1C1B1F),
            backgroundCritical = Color(0xFFFFB4AB),
            foregroundPrimary = Color(0xFFE6E1E5),
            foregroundSecondary = Color(0xFFCAC4D0),
            foregroundOnBrand = Color(0xFF3B1800),
            strokeNeutral = Color(0xFF938F99),
            strokeFocus = Color(0xFFFFB36B),
            overlayScrim = Color(0x99000000),
        ),
    )

    private fun defaultTypography(): BeezTypographyScheme {
        val family = FontFamily.SansSerif
        return BeezTypographyScheme(
            display = TextStyle(
                fontFamily = family,
                fontSize = 36.sp,
                lineHeight = 44.sp,
                fontWeight = FontWeight.Bold,
            ),
            screenTitle = TextStyle(
                fontFamily = family,
                fontSize = 28.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold,
            ),
            sectionTitle = TextStyle(
                fontFamily = family,
                fontSize = 20.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            body = TextStyle(
                fontFamily = family,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            ),
            label = TextStyle(
                fontFamily = family,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium,
            ),
            caption = TextStyle(
                fontFamily = family,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            ),
        )
    }

    private fun defaultElevation(): BeezElevationScheme = BeezElevationScheme(
        level0 = 0.dp,
        level1 = 1.dp,
        level2 = 3.dp,
        raised = 1.dp,
        floating = 3.dp,
        overlay = 8.dp,
    )

    private fun defaultMotion(): BeezMotionScheme = BeezMotionScheme(
        instantMillis = 0,
        fastMillis = 120,
        moderateMillis = 200,
        slowMillis = 300,
    )
}

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
