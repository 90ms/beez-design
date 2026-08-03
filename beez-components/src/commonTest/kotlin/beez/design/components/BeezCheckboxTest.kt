package beez.design.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import beez.design.tokens.BeezTokenSchemes
import kotlin.test.Test
import kotlin.test.assertEquals

class BeezCheckboxTest {
    @Test
    fun checkedStateUsesBrandAndOnBrandRoles() {
        val scheme = BeezTokenSchemes.light.colors
        val colors = resolveCheckboxColors(
            checked = true,
            enabled = true,
            focused = false,
            scheme = scheme,
        )

        assertEquals(scheme.backgroundBrand, colors.container)
        assertEquals(scheme.foregroundOnBrand, colors.indicator)
        assertEquals(scheme.backgroundBrand, colors.border)
        assertEquals(scheme.foregroundPrimary, colors.label)
        assertEquals(1.dp, colors.borderWidth)
    }

    @Test
    fun focusStrokeDependsOnTheIndicatorContainer() {
        val scheme = BeezTokenSchemes.dark.colors
        val checked = resolveCheckboxColors(
            checked = true,
            enabled = true,
            focused = true,
            scheme = scheme,
        )
        val unchecked = resolveCheckboxColors(
            checked = false,
            enabled = true,
            focused = true,
            scheme = scheme,
        )

        assertEquals(scheme.foregroundOnBrand, checked.border)
        assertEquals(scheme.strokeFocus, unchecked.border)
        assertEquals(2.dp, checked.borderWidth)
        assertEquals(2.dp, unchecked.borderWidth)
    }

    @Test
    fun disabledCheckedStateKeepsAVisibleCheckmark() {
        val scheme = BeezTokenSchemes.light.colors
        val colors = resolveCheckboxColors(
            checked = true,
            enabled = false,
            focused = true,
            scheme = scheme,
        )

        assertEquals(Color.Transparent, colors.container)
        assertEquals(scheme.foregroundSecondary, colors.indicator)
        assertEquals(scheme.strokeNeutral, colors.border)
        assertEquals(scheme.foregroundSecondary, colors.label)
        assertEquals(1.dp, colors.borderWidth)
    }
}
