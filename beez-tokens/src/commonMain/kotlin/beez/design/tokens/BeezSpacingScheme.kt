package beez.design.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/**
 * Semantic spacing roles for screen, content, and control relationships.
 */
@Immutable
public data class BeezSpacingScheme(
    public val minimumTouchTarget: Dp,
    public val screenGutter: Dp,
    public val screenSectionGap: Dp,
    public val contentInlineGap: Dp,
    public val contentStackGap: Dp,
    public val controlContentGap: Dp,
    public val controlCompactHeight: Dp,
    public val controlDefaultHeight: Dp,
    public val controlComfortableHeight: Dp,
    public val controlCompactHorizontalInset: Dp,
    public val controlDefaultHorizontalInset: Dp,
    public val controlComfortableHorizontalInset: Dp,
    public val controlCompactVerticalInset: Dp,
    public val controlDefaultVerticalInset: Dp,
    public val controlComfortableVerticalInset: Dp,
)
