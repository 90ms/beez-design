package beez.design.tokens

import androidx.compose.runtime.Immutable

/**
 * Semantic motion durations in milliseconds.
 *
 * A platform or accessibility adapter may reduce these values for motion reduction.
 */
@Immutable
public data class BeezMotionScheme(
    public val instantMillis: Int,
    public val fastMillis: Int,
    public val moderateMillis: Int,
    public val slowMillis: Int,
)
