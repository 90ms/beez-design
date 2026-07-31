package beez.design.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Semantic color roles shared by BEEZ components.
 *
 * The role names remain stable while a brand or appearance can replace their values.
 */
@Immutable
public data class BeezColorScheme(
    public val backgroundBrand: Color,
    public val backgroundNeutral: Color,
    public val backgroundCritical: Color,
    public val foregroundPrimary: Color,
    public val foregroundSecondary: Color,
    public val foregroundOnBrand: Color,
    public val strokeNeutral: Color,
    public val strokeFocus: Color,
    public val overlayScrim: Color,
)
