package beez.design.components

import androidx.compose.ui.unit.dp
import beez.design.tokens.BeezTokenSchemes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class BeezActionButtonTest {
    @Test
    fun variantsUseDifferentSemanticRoles() {
        val colors = BeezTokenSchemes.light.colors

        val brand = resolveColors(
            variant = BeezActionButtonVariant.BrandSolid,
            enabled = true,
            scheme = colors,
        )
        val outline = resolveColors(
            variant = BeezActionButtonVariant.Outline,
            enabled = true,
            scheme = colors,
        )

        assertEquals(colors.backgroundBrand, brand.container)
        assertEquals(colors.foregroundOnBrand, brand.content)
        assertNotEquals(brand.container, outline.container)
        assertEquals(colors.foregroundPrimary, outline.content)
    }

    @Test
    fun disabledStateDoesNotUseBrandActionColor() {
        val colors = resolveColors(
            variant = BeezActionButtonVariant.BrandSolid,
            enabled = false,
            scheme = BeezTokenSchemes.light.colors,
        )

        assertEquals(BeezTokenSchemes.light.colors.backgroundNeutral, colors.container)
        assertEquals(BeezTokenSchemes.light.colors.foregroundSecondary, colors.content)
    }

    @Test
    fun smallControlStillUsesMinimumTouchTarget() {
        val spacing = BeezTokenSchemes.light.spacing
        val size = resolveSize(BeezActionButtonSize.Small, spacing)

        assertEquals(spacing.controlCompactHeight, size.height)
        assertTrue(size.height < spacing.minimumTouchTarget)
        assertEquals(spacing.minimumTouchTarget, 48.dp)
    }
}
