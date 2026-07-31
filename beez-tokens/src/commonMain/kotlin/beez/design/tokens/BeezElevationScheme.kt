package beez.design.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/**
 * Semantic elevation roles. Rendering a role is decided by the consuming component.
 */
@Immutable
public data class BeezElevationScheme(
    public val level0: Dp,
    public val level1: Dp,
    public val level2: Dp,
    public val raised: Dp,
    public val floating: Dp,
    public val overlay: Dp,
)
