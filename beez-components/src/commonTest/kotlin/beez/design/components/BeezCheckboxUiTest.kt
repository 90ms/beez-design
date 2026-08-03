package beez.design.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.click
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.pressKey
import androidx.compose.ui.test.requestFocus
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import beez.design.foundation.BeezTheme
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class BeezCheckboxUiTest {
    @Test
    fun checkboxExposesRoleAndHoistsItsChangedState() = runComposeUiTest {
        var checked by mutableStateOf(false)

        setContent {
            BeezTheme {
                BeezCheckbox(
                    checked = checked,
                    onCheckedChange = { checked = it },
                    label = "Receive updates",
                )
            }
        }

        onNodeWithText("Receive updates")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Checkbox))
            .assertHasClickAction()
            .assertIsEnabled()
            .assertIsOff()
            .performClick()
            .assertIsOn()

        assertTrue(checked)
    }

    @Test
    fun disabledCheckboxKeepsItsCheckedAndDisabledSemantics() = runComposeUiTest {
        var changes = 0

        setContent {
            BeezTheme {
                BeezCheckbox(
                    checked = true,
                    onCheckedChange = { changes += 1 },
                    label = "Required option",
                    enabled = false,
                )
            }
        }

        onNodeWithText("Required option")
            .assertIsOn()
            .assertIsNotEnabled()
            .performTouchInput { click() }

        assertEquals(0, changes)
    }

    @Test
    fun checkboxKeepsTheMinimumTouchTarget() = runComposeUiTest {
        setContent {
            BeezTheme {
                BeezCheckbox(
                    checked = false,
                    onCheckedChange = {},
                    label = "Compact label",
                    modifier = Modifier.testTag("minimum-checkbox"),
                )
            }
        }

        onNodeWithTag("minimum-checkbox").assertHeightIsAtLeast(48.dp)
    }

    @Test
    fun focusedCheckboxTogglesWithSpace() = runComposeUiTest {
        var checked by mutableStateOf(false)

        setContent {
            BeezTheme {
                BeezCheckbox(
                    checked = checked,
                    onCheckedChange = { checked = it },
                    label = "Keyboard option",
                    modifier = Modifier.testTag("keyboard-checkbox"),
                )
            }
        }

        val checkbox = onNodeWithTag("keyboard-checkbox")
            .requestFocus()
            .assertIsFocused()
            .assertIsOff()
        checkbox.performKeyInput { pressKey(Key.Spacebar) }
        checkbox.assertIsOn()

        assertTrue(checked)
    }

    @Test
    fun longRtlLabelRendersAtLargeFontScale() = runComposeUiTest {
        setContent {
            val currentDensity = LocalDensity.current
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Rtl,
                LocalDensity provides Density(currentDensity.density, fontScale = 2f),
            ) {
                BeezTheme {
                    Box(modifier = Modifier.width(240.dp)) {
                        BeezCheckbox(
                            checked = true,
                            onCheckedChange = {},
                            label = "أوافق على تلقي تحديثات مفصلة حول هذا الخيار",
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("rtl-long-checkbox"),
                        )
                    }
                }
            }
        }

        onNodeWithTag("rtl-long-checkbox")
            .assertHasClickAction()
            .assertIsOn()
    }
}
