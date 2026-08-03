package beez.design.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import beez.design.foundation.BeezTheme
import beez.design.tokens.BeezColorScheme

/**
 * A binary selection control with a required accessible label.
 *
 * The checked state is owned by the caller. The indicator and label form one
 * checkbox semantics node and one minimum-size interaction target.
 */
@Composable
public fun BeezCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    var focused by remember { mutableStateOf(false) }
    val colors = resolveCheckboxColors(
        checked = checked,
        enabled = enabled,
        focused = focused,
        scheme = BeezTheme.colors,
    )
    val minimumTouchTarget = BeezTheme.spacing.minimumTouchTarget
    val indicatorSize = minimumTouchTarget * 0.5f

    Row(
        modifier = modifier
            .heightIn(min = minimumTouchTarget)
            .defaultMinSize(minHeight = minimumTouchTarget)
            .onFocusChanged { focused = it.isFocused }
            .semantics(mergeDescendants = true) {}
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Checkbox,
                onValueChange = onCheckedChange,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.controlContentGap),
    ) {
        Box(
            modifier = Modifier
                .size(indicatorSize)
                .background(colors.container, RectangleShape)
                .border(colors.borderWidth, colors.border, RectangleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (checked) {
                BeezCheckboxCheckmark(
                    color = colors.indicator,
                    modifier = Modifier.size(indicatorSize),
                )
            }
        }

        BasicText(
            text = label,
            style = BeezTheme.typography.label.copy(color = colors.label),
        )
    }
}

@Immutable
internal data class BeezCheckboxColors(
    val container: Color,
    val indicator: Color,
    val border: Color,
    val borderWidth: Dp,
    val label: Color,
)

internal fun resolveCheckboxColors(
    checked: Boolean,
    enabled: Boolean,
    focused: Boolean,
    scheme: BeezColorScheme,
): BeezCheckboxColors {
    if (!enabled) {
        return BeezCheckboxColors(
            container = Color.Transparent,
            indicator = if (checked) scheme.foregroundSecondary else Color.Transparent,
            border = scheme.strokeNeutral,
            borderWidth = 1.dp,
            label = scheme.foregroundSecondary,
        )
    }

    if (checked) {
        return BeezCheckboxColors(
            container = scheme.backgroundBrand,
            indicator = scheme.foregroundOnBrand,
            border = if (focused) scheme.foregroundOnBrand else scheme.backgroundBrand,
            borderWidth = if (focused) 2.dp else 1.dp,
            label = scheme.foregroundPrimary,
        )
    }

    return BeezCheckboxColors(
        container = Color.Transparent,
        indicator = Color.Transparent,
        border = if (focused) scheme.strokeFocus else scheme.strokeNeutral,
        borderWidth = if (focused) 2.dp else 1.dp,
        label = scheme.foregroundPrimary,
    )
}

@Composable
private fun BeezCheckboxCheckmark(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * 0.1f
        val first = Offset(size.width * 0.22f, size.height * 0.52f)
        val middle = Offset(size.width * 0.43f, size.height * 0.72f)
        val last = Offset(size.width * 0.79f, size.height * 0.29f)

        drawLine(
            color = color,
            start = first,
            end = middle,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = color,
            start = middle,
            end = last,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
    }
}
