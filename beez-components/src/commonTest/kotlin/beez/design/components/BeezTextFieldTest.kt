package beez.design.components

import beez.design.tokens.BeezTokenSchemes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BeezTextFieldTest {
    @Test
    fun enabledFieldUsesPrimaryContentAndNeutralStroke() {
        val scheme = BeezTokenSchemes.light.colors

        val colors = resolveTextFieldColors(
            enabled = true,
            isError = false,
            focused = false,
            scheme = scheme,
        )

        assertEquals(scheme.foregroundPrimary, colors.content)
        assertEquals(scheme.strokeNeutral, colors.stroke)
    }

    @Test
    fun focusedFieldUsesFocusStroke() {
        val scheme = BeezTokenSchemes.light.colors

        val colors = resolveTextFieldColors(
            enabled = true,
            isError = false,
            focused = true,
            scheme = scheme,
        )

        assertEquals(scheme.strokeFocus, colors.stroke)
    }

    @Test
    fun errorStateTakesPrecedenceOverFocus() {
        val scheme = BeezTokenSchemes.light.colors

        val colors = resolveTextFieldColors(
            enabled = true,
            isError = true,
            focused = true,
            scheme = scheme,
        )

        assertEquals(scheme.strokeCritical, colors.stroke)
        assertEquals(scheme.foregroundCritical, colors.label)
        assertNotEquals(scheme.strokeFocus, colors.stroke)
    }

    @Test
    fun disabledStateTakesPrecedenceOverError() {
        val scheme = BeezTokenSchemes.light.colors

        val colors = resolveTextFieldColors(
            enabled = false,
            isError = true,
            focused = true,
            scheme = scheme,
        )

        assertEquals(scheme.strokeNeutral, colors.stroke)
        assertEquals(scheme.foregroundSecondary, colors.label)
        assertEquals(scheme.foregroundSecondary, colors.supporting)
    }
}
