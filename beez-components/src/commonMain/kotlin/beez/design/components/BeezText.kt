package beez.design.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import beez.design.foundation.BeezTheme
import beez.design.tokens.BeezColorScheme
import beez.design.tokens.BeezTypographyScheme

/**
 * The semantic typography hierarchy of BEEZ text.
 */
public enum class BeezTextRole {
    Display,
    ScreenTitle,
    SectionTitle,
    Body,
    Label,
    Caption,
}

/**
 * The semantic foreground intent of BEEZ text.
 */
public enum class BeezTextTone {
    Primary,
    Secondary,
    Critical,
    OnBrand,
}

/**
 * Non-interactive text styled by BEEZ semantic typography and foreground roles.
 *
 * Text does not add a role, action, focus behavior, or selection. Use an
 * interactive component or a dedicated content pattern when those behaviors
 * are required.
 */
@Composable
public fun BeezText(
    text: String,
    modifier: Modifier = Modifier,
    role: BeezTextRole = BeezTextRole.Body,
    tone: BeezTextTone = BeezTextTone.Primary,
    textAlign: TextAlign = TextAlign.Start,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    requireValidTextMaxLines(maxLines)

    BasicText(
        text = text,
        modifier = modifier,
        style = resolveTextStyle(role, BeezTheme.typography).copy(
            color = resolveTextColor(tone, BeezTheme.colors),
            textAlign = textAlign,
        ),
        overflow = overflow,
        maxLines = maxLines,
    )
}

internal fun resolveTextStyle(
    role: BeezTextRole,
    scheme: BeezTypographyScheme,
): TextStyle = when (role) {
    BeezTextRole.Display -> scheme.display
    BeezTextRole.ScreenTitle -> scheme.screenTitle
    BeezTextRole.SectionTitle -> scheme.sectionTitle
    BeezTextRole.Body -> scheme.body
    BeezTextRole.Label -> scheme.label
    BeezTextRole.Caption -> scheme.caption
}

internal fun resolveTextColor(
    tone: BeezTextTone,
    scheme: BeezColorScheme,
): Color = when (tone) {
    BeezTextTone.Primary -> scheme.foregroundPrimary
    BeezTextTone.Secondary -> scheme.foregroundSecondary
    BeezTextTone.Critical -> scheme.foregroundCritical
    BeezTextTone.OnBrand -> scheme.foregroundOnBrand
}

internal fun requireValidTextMaxLines(maxLines: Int) {
    require(maxLines > 0) { "maxLines must be greater than zero" }
}
