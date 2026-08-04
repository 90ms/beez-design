package beez.design.components

import beez.design.tokens.BeezTokenSchemes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BeezTextTest {
    @Test
    fun rolesUseSemanticTypography() {
        val typography = BeezTokenSchemes.light.typography

        val expected = mapOf(
            BeezTextRole.Display to typography.display,
            BeezTextRole.ScreenTitle to typography.screenTitle,
            BeezTextRole.SectionTitle to typography.sectionTitle,
            BeezTextRole.Body to typography.body,
            BeezTextRole.Label to typography.label,
            BeezTextRole.Caption to typography.caption,
        )

        expected.forEach { (role, style) ->
            assertEquals(style, resolveTextStyle(role, typography))
        }
    }

    @Test
    fun tonesUseSemanticForegroundColors() {
        val colors = BeezTokenSchemes.light.colors

        val expected = mapOf(
            BeezTextTone.Primary to colors.foregroundPrimary,
            BeezTextTone.Secondary to colors.foregroundSecondary,
            BeezTextTone.Critical to colors.foregroundCritical,
            BeezTextTone.OnBrand to colors.foregroundOnBrand,
        )

        expected.forEach { (tone, color) ->
            assertEquals(color, resolveTextColor(tone, colors))
        }
    }

    @Test
    fun maxLinesMustBePositive() {
        requireValidTextMaxLines(1)
        requireValidTextMaxLines(Int.MAX_VALUE)

        assertFailsWith<IllegalArgumentException> { requireValidTextMaxLines(0) }
        assertFailsWith<IllegalArgumentException> { requireValidTextMaxLines(-1) }
    }
}
