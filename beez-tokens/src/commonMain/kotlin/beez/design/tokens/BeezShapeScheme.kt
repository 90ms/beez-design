package beez.design.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/**
 * Semantic shape radii. Foundation maps these roles to platform-independent shapes.
 */
@Immutable
public data class BeezShapeScheme(
    public val controlRadius: Dp,
    public val containerRadius: Dp,
    public val overlayRadius: Dp,
    public val roundRadius: Dp,
)
