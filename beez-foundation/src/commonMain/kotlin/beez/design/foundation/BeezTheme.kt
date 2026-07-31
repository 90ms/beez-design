package beez.design.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import beez.design.tokens.BeezColorScheme
import beez.design.tokens.BeezElevationScheme
import beez.design.tokens.BeezMotionScheme
import beez.design.tokens.BeezShapeScheme
import beez.design.tokens.BeezSpacingScheme
import beez.design.tokens.BeezTokenScheme
import beez.design.tokens.BeezTokenSchemes
import beez.design.tokens.BeezTypographyScheme

private val LocalBeezTokenScheme = staticCompositionLocalOf { BeezTokenSchemes.light }

/**
 * Provides and reads the current BEEZ semantic token scheme.
 *
 * BeezTheme can be invoked as a composable wrapper:
 *
 * BeezTheme {
 *     // BEEZ components and product content
 * }
 */
public object BeezTheme {
    public val scheme: BeezTokenScheme
        @Composable get() = LocalBeezTokenScheme.current

    public val colors: BeezColorScheme
        @Composable get() = scheme.colors

    public val typography: BeezTypographyScheme
        @Composable get() = scheme.typography

    public val spacing: BeezSpacingScheme
        @Composable get() = scheme.spacing

    public val shapes: BeezShapeScheme
        @Composable get() = scheme.shapes

    public val elevation: BeezElevationScheme
        @Composable get() = scheme.elevation

    public val motion: BeezMotionScheme
        @Composable get() = scheme.motion

    /**
     * Provides a BEEZ scheme to all descendants.
     */
    @Composable
    public operator fun invoke(
        scheme: BeezTokenScheme = BeezTokenSchemes.light,
        content: @Composable () -> Unit,
    ) {
        Provide(scheme = scheme, content = content)
    }

    /**
     * Provides an explicit scheme to all descendants.
     */
    @Composable
    public fun Provide(
        scheme: BeezTokenScheme,
        content: @Composable () -> Unit,
    ) {
        CompositionLocalProvider(
            LocalBeezTokenScheme provides scheme,
            content = content,
        )
    }

    /**
     * Applies the default BEEZ light scheme.
     */
    @Composable
    public fun Light(content: @Composable () -> Unit) {
        Provide(scheme = BeezTokenSchemes.light, content = content)
    }

    /**
     * Applies the default BEEZ dark scheme.
     */
    @Composable
    public fun Dark(content: @Composable () -> Unit) {
        Provide(scheme = BeezTokenSchemes.dark, content = content)
    }
}
