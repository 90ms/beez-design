package beez.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import beez.design.foundation.BeezTheme
import beez.design.tokens.BeezColorScheme

/**
 * A single-line text input with BEEZ theme, state, and accessibility semantics.
 *
 * The value is state-hoisted to the caller. Multiline, password, and transformed
 * input are intentionally outside this first component contract.
 */
@Composable
public fun BeezTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    supportingText: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    var focused by remember { mutableStateOf(false) }
    val resolvedColors = resolveTextFieldColors(
        enabled = enabled,
        isError = isError,
        focused = focused,
        scheme = BeezTheme.colors,
    )
    val shape = RoundedCornerShape(BeezTheme.shapes.controlRadius)
    val borderWidth = if (focused && enabled && !isError) 2.dp else 1.dp
    val labelStyle = BeezTheme.typography.label.copy(color = resolvedColors.label)
    val inputStyle = BeezTheme.typography.body.copy(color = resolvedColors.content)
    val supportingStyle = BeezTheme.typography.caption.copy(color = resolvedColors.supporting)

    Column(modifier = modifier) {
        BasicText(
            text = label,
            style = labelStyle,
            modifier = Modifier.padding(bottom = BeezTheme.spacing.controlContentGap),
        )

        Row(
            modifier = Modifier
                .heightIn(min = BeezTheme.spacing.controlComfortableHeight)
                .defaultMinSize(minHeight = BeezTheme.spacing.minimumTouchTarget)
                .clip(shape)
                .background(Color.Transparent)
                .border(
                    width = borderWidth,
                    color = resolvedColors.stroke,
                    shape = shape,
                )
                .onFocusChanged { focused = it.isFocused }
                .padding(
                    horizontal = BeezTheme.spacing.controlDefaultHorizontalInset,
                    vertical = BeezTheme.spacing.controlDefaultVerticalInset,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.controlContentGap),
        ) {
            leadingContent?.invoke()

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                readOnly = readOnly,
                singleLine = true,
                textStyle = inputStyle,
                modifier = Modifier
                    .weight(1f)
                    .semantics {
                        contentDescription = label
                        if (!enabled) {
                            disabled()
                        }
                        if (isError && supportingText != null) {
                            error(supportingText)
                        }
                    },
                decorationBox = { innerTextField ->
                    if (value.isEmpty() && placeholder != null) {
                        BasicText(
                            text = placeholder,
                            style = BeezTheme.typography.body.copy(
                                color = resolvedColors.placeholder,
                            ),
                        )
                    }
                    innerTextField()
                },
            )

            trailingContent?.invoke()
        }

        if (supportingText != null) {
            BasicText(
                text = supportingText,
                style = supportingStyle,
                modifier = Modifier.padding(top = BeezTheme.spacing.contentInlineGap),
            )
        }
    }
}

@Immutable
internal data class BeezTextFieldColors(
    val label: Color,
    val content: Color,
    val placeholder: Color,
    val supporting: Color,
    val stroke: Color,
)

internal fun resolveTextFieldColors(
    enabled: Boolean,
    isError: Boolean,
    focused: Boolean,
    scheme: BeezColorScheme,
): BeezTextFieldColors {
    if (!enabled) {
        return BeezTextFieldColors(
            label = scheme.foregroundSecondary,
            content = scheme.foregroundSecondary,
            placeholder = scheme.foregroundSecondary,
            supporting = scheme.foregroundSecondary,
            stroke = scheme.strokeNeutral,
        )
    }

    if (isError) {
        return BeezTextFieldColors(
            label = scheme.foregroundCritical,
            content = scheme.foregroundPrimary,
            placeholder = scheme.foregroundSecondary,
            supporting = scheme.foregroundCritical,
            stroke = scheme.strokeCritical,
        )
    }

    return BeezTextFieldColors(
        label = scheme.foregroundSecondary,
        content = scheme.foregroundPrimary,
        placeholder = scheme.foregroundSecondary,
        supporting = scheme.foregroundSecondary,
        stroke = if (focused) scheme.strokeFocus else scheme.strokeNeutral,
    )
}
