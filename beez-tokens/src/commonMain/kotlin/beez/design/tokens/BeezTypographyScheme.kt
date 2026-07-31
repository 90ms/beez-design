package beez.design.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

/**
 * Semantic typography roles. TextStyle keeps the contract usable from commonMain.
 */
@Immutable
public data class BeezTypographyScheme(
    public val display: TextStyle,
    public val screenTitle: TextStyle,
    public val sectionTitle: TextStyle,
    public val body: TextStyle,
    public val label: TextStyle,
    public val caption: TextStyle,
)
