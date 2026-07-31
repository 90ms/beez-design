package beez.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import beez.design.foundation.BeezTheme
import beez.design.tokens.BeezColorScheme
import beez.design.tokens.BeezSpacingScheme

/**
 * The semantic emphasis of an action button.
 */
public enum class BeezActionButtonVariant {
    BrandSolid,
    Neutral,
    Outline,
}

/**
 * The semantic size of an action button.
 */
public enum class BeezActionButtonSize {
    Small,
    Medium,
    Large,
}

/**
 * A shared action control with BEEZ theme, state, and accessibility semantics.
 */
@Composable
public fun BeezActionButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: BeezActionButtonVariant = BeezActionButtonVariant.BrandSolid,
    size: BeezActionButtonSize = BeezActionButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    val colors = resolveColors(
        variant = variant,
        enabled = enabled,
        scheme = BeezTheme.colors,
    )
    val sizeTokens = resolveSize(size, BeezTheme.spacing)
    val shape = RoundedCornerShape(BeezTheme.shapes.controlRadius)
    val interactive = isActionButtonInteractive(enabled = enabled, loading = loading)
    var focused by remember { mutableStateOf(false) }
    val borderWidth = when {
        focused && interactive -> 2.dp
        variant == BeezActionButtonVariant.Outline -> 1.dp
        else -> 0.dp
    }
    val borderColor = when {
        focused && interactive -> BeezTheme.colors.strokeFocus
        variant == BeezActionButtonVariant.Outline -> BeezTheme.colors.strokeNeutral
        else -> Color.Transparent
    }
    val minHeight = if (sizeTokens.height < BeezTheme.spacing.minimumTouchTarget) {
        BeezTheme.spacing.minimumTouchTarget
    } else {
        sizeTokens.height
    }

    Row(
        modifier = modifier
            .heightIn(min = minHeight)
            .defaultMinSize(minHeight = minHeight)
            .clip(shape)
            .background(colors.container)
            .border(width = borderWidth, color = borderColor, shape = shape)
            .onFocusChanged { focused = it.isFocused }
            .semantics(mergeDescendants = true) {
                role = Role.Button
                if (!enabled) {
                    disabled()
                }
                if (loading) {
                    progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
                }
            }
            .clickable(
                enabled = interactive,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(
                horizontal = sizeTokens.horizontalInset,
                vertical = sizeTokens.verticalInset,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.controlContentGap),
    ) {
        if (loading) {
            BeezActionButtonProgressIndicator(
                color = colors.content,
                modifier = Modifier.size(18.dp),
            )
        }

        leadingContent?.invoke()

        BasicText(
            text = label,
            style = BeezTheme.typography.label.copy(color = colors.content),
            modifier = Modifier.weight(1f),
        )

        trailingContent?.invoke()
    }
}

internal fun isActionButtonInteractive(
    enabled: Boolean,
    loading: Boolean,
): Boolean = enabled && !loading

@Immutable
internal data class BeezActionButtonColors(
    val container: Color,
    val content: Color,
)

@Immutable
internal data class BeezActionButtonSizeTokens(
    val height: Dp,
    val horizontalInset: Dp,
    val verticalInset: Dp,
)

internal fun resolveColors(
    variant: BeezActionButtonVariant,
    enabled: Boolean,
    scheme: BeezColorScheme,
): BeezActionButtonColors {
    if (!enabled) {
        return BeezActionButtonColors(
            container = if (variant == BeezActionButtonVariant.Outline) {
                Color.Transparent
            } else {
                scheme.backgroundNeutral
            },
            content = scheme.foregroundSecondary,
        )
    }

    return when (variant) {
        BeezActionButtonVariant.BrandSolid -> BeezActionButtonColors(
            container = scheme.backgroundBrand,
            content = scheme.foregroundOnBrand,
        )

        BeezActionButtonVariant.Neutral -> BeezActionButtonColors(
            container = scheme.backgroundNeutral,
            content = scheme.foregroundPrimary,
        )

        BeezActionButtonVariant.Outline -> BeezActionButtonColors(
            container = Color.Transparent,
            content = scheme.foregroundPrimary,
        )
    }
}

internal fun resolveSize(
    size: BeezActionButtonSize,
    spacing: BeezSpacingScheme,
): BeezActionButtonSizeTokens = when (size) {
    BeezActionButtonSize.Small -> BeezActionButtonSizeTokens(
        height = spacing.controlCompactHeight,
        horizontalInset = spacing.controlCompactHorizontalInset,
        verticalInset = spacing.controlCompactVerticalInset,
    )

    BeezActionButtonSize.Medium -> BeezActionButtonSizeTokens(
        height = spacing.controlDefaultHeight,
        horizontalInset = spacing.controlDefaultHorizontalInset,
        verticalInset = spacing.controlDefaultVerticalInset,
    )

    BeezActionButtonSize.Large -> BeezActionButtonSizeTokens(
        height = spacing.controlComfortableHeight,
        horizontalInset = spacing.controlComfortableHorizontalInset,
        verticalInset = spacing.controlComfortableVerticalInset,
    )
}

@Composable
private fun BeezActionButtonProgressIndicator(
    color: Color,
    modifier: Modifier = Modifier,
) {
    androidx.compose.foundation.Canvas(modifier = modifier) {
        drawArc(
            color = color,
            startAngle = -45f,
            sweepAngle = 270f,
            useCenter = false,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round),
        )
    }
}
