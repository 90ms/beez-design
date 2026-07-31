package beez.design.tokens

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BeezTokenSchemeTest {
    @Test
    fun lightAndDarkSchemesKeepTheSameSemanticContract() {
        val light = BeezTokenSchemes.light
        val dark = BeezTokenSchemes.dark

        assertNotEquals(light.colors.backgroundBrand, dark.colors.backgroundBrand)
        assertNotEquals(light.colors.foregroundCritical, dark.colors.foregroundCritical)
        assertNotEquals(light.colors.strokeCritical, dark.colors.strokeCritical)
        assertEquals(light.typography.body.fontSize, dark.typography.body.fontSize)
        assertEquals(light.spacing.screenGutter, dark.spacing.screenGutter)
        assertEquals(light.shapes.controlRadius, dark.shapes.controlRadius)
        assertEquals(light.motion.fastMillis, dark.motion.fastMillis)
    }

    @Test
    fun productThemeCanReplaceSemanticColorsWithoutChangingOtherRoles() {
        val productBrand = BeezColorScheme(
            backgroundBrand = Color(0xFF0057B8),
            backgroundNeutral = BeezTokenSchemes.light.colors.backgroundNeutral,
            backgroundCritical = BeezTokenSchemes.light.colors.backgroundCritical,
            foregroundPrimary = BeezTokenSchemes.light.colors.foregroundPrimary,
            foregroundSecondary = BeezTokenSchemes.light.colors.foregroundSecondary,
            foregroundOnBrand = Color.White,
            foregroundCritical = BeezTokenSchemes.light.colors.foregroundCritical,
            strokeNeutral = BeezTokenSchemes.light.colors.strokeNeutral,
            strokeFocus = Color(0xFF0057B8),
            strokeCritical = BeezTokenSchemes.light.colors.strokeCritical,
            overlayScrim = BeezTokenSchemes.light.colors.overlayScrim,
        )

        val themed = BeezTokenSchemes.light.withColors(productBrand)

        assertEquals(productBrand, themed.colors)
        assertEquals(BeezTokenSchemes.light.typography, themed.typography)
        assertEquals(BeezTokenSchemes.light.spacing, themed.spacing)
    }
}
