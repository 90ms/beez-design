package beez.design.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/**
 * Semantic spacing roles for screen, content, and control relationships.
 */
@Immutable
public data class BeezSpacingScheme(
    public val screenGutter: Dp,
    public val screenSectionGap: Dp,
    public val contentInlineGap: Dp,
    public val contentStackGap: Dp,
    public val controlContentGap: Dp,
)
