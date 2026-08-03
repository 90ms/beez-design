package beez.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import beez.design.foundation.BeezTheme
import beez.design.tokens.BeezElevationScheme

/**
 * The semantic depth of a BEEZ surface.
 */
public enum class BeezSurfaceElevation {
    Flat,
    Raised,
    Floating,
}

/**
 * A non-interactive neutral container with BEEZ shape and elevation.
 *
 * Surface does not merge content semantics or add click, selection, or focus
 * behavior. Interactive containers require an explicit component or pattern
 * that owns those accessibility and input contracts.
 */
@Composable
public fun BeezSurface(
    modifier: Modifier = Modifier,
    elevation: BeezSurfaceElevation = BeezSurfaceElevation.Flat,
    content: @Composable BoxScope.() -> Unit,
) {
    val shape = RoundedCornerShape(BeezTheme.shapes.containerRadius)
    val resolvedElevation = resolveSurfaceElevation(
        elevation = elevation,
        scheme = BeezTheme.elevation,
    )

    val elevationModifier = if (resolvedElevation == null) {
        Modifier
    } else {
        Modifier.shadow(
            elevation = resolvedElevation,
            shape = shape,
            clip = false,
        )
    }

    Box(
        modifier = modifier
            .then(elevationModifier)
            .clip(shape)
            .background(BeezTheme.colors.backgroundNeutral),
        content = content,
    )
}

internal fun resolveSurfaceElevation(
    elevation: BeezSurfaceElevation,
    scheme: BeezElevationScheme,
): Dp? = when (elevation) {
    BeezSurfaceElevation.Flat -> null
    BeezSurfaceElevation.Raised -> scheme.raised
    BeezSurfaceElevation.Floating -> scheme.floating
}
