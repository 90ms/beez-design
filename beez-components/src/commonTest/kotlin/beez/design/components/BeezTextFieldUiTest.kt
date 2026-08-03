package beez.design.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasContentDescriptionExactly
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextInputSelection
import androidx.compose.ui.test.pressKey
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import beez.design.foundation.BeezTheme
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class BeezTextFieldUiTest {
    @Test
    fun fieldExposesLabelAndAcceptsTextInput() = runComposeUiTest {
        var value by mutableStateOf("")

        setContent {
            BeezTheme {
                BeezTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = "Email address",
                    placeholder = "name@example.com",
                )
            }
        }

        val field = onNode(hasContentDescriptionExactly("Email address"))
        field.performTextInput("user@example.com")

        field.assertTextEquals("user@example.com")
        assertEquals("user@example.com", value)
        onNodeWithText("name@example.com").assertDoesNotExist()
    }

    @Test
    fun emptyFieldExposesPlaceholderAndSupportingText() = runComposeUiTest {
        setContent {
            BeezTheme {
                BeezTextField(
                    value = "",
                    onValueChange = {},
                    label = "Email address",
                    placeholder = "name@example.com",
                    supportingText = "Use an address you can access.",
                )
            }
        }

        onNodeWithText("name@example.com").assertExists()
        onNodeWithText("Use an address you can access.").assertExists()
    }

    @Test
    fun disabledFieldIsNotEnabledInSemantics() = runComposeUiTest {
        setContent {
            BeezTheme {
                BeezTextField(
                    value = "locked@example.com",
                    onValueChange = {},
                    label = "Email address",
                    enabled = false,
                )
            }
        }

        onNodeWithContentDescription("Email address").assertIsNotEnabled()
    }

    @Test
    fun readOnlyFieldRemainsEnabledForFocusAndSelection() = runComposeUiTest {
        setContent {
            BeezTheme {
                BeezTextField(
                    value = "account@example.com",
                    onValueChange = {},
                    label = "Email address",
                    readOnly = true,
                )
            }
        }

        val field = onNodeWithContentDescription("Email address")
        field
            .assertIsEnabled()
            .assertTextEquals("account@example.com")
        assertFalse(field.fetchSemanticsNode().config.contains(SemanticsActions.SetText))
    }

    @Test
    fun errorStateExposesLocalizedErrorSemantics() = runComposeUiTest {
        val message = "Enter a valid email address."

        setContent {
            BeezTheme {
                BeezTextField(
                    value = "invalid",
                    onValueChange = {},
                    label = "Email address",
                    supportingText = message,
                    isError = true,
                )
            }
        }

        onNodeWithContentDescription("Email address")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Error, message))
    }

    @Test
    fun selectionAndTextInputReplaceTheSelectedRange() = runComposeUiTest {
        var value by mutableStateOf("account@example.com")

        setContent {
            BeezTheme {
                BeezTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = "Email address",
                )
            }
        }

        val field = onNodeWithContentDescription("Email address")
        field.performTextInputSelection(TextRange(0, 7))
        field.performTextInput("user")

        field.assertTextEquals("user@example.com")
        assertEquals("user@example.com", value)
    }

    @Test
    fun focusedFieldHandlesHardwareBackspace() = runComposeUiTest {
        var value by mutableStateOf("draft")

        setContent {
            BeezTheme {
                BeezTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = "Message",
                )
            }
        }

        val field = onNodeWithContentDescription("Message")
        field.performClick().assertIsFocused()
        field.performKeyInput { pressKey(Key.Backspace) }

        field.assertTextEquals("draf")
        assertEquals("draf", value)
    }

    @Test
    fun rtlLayoutPlacesLeadingSlotAtTheLogicalStart() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                BeezTheme {
                    BeezTextField(
                        value = "example",
                        onValueChange = {},
                        label = "حقل",
                        leadingContent = {
                            BasicText("L", modifier = Modifier.testTag("leading-slot"))
                        },
                        trailingContent = {
                            BasicText("T", modifier = Modifier.testTag("trailing-slot"))
                        },
                        modifier = Modifier.width(320.dp),
                    )
                }
            }
        }

        val leading = onNodeWithTag("leading-slot", useUnmergedTree = true)
            .fetchSemanticsNode().boundsInRoot.left
        val trailing = onNodeWithTag("trailing-slot", useUnmergedTree = true)
            .fetchSemanticsNode().boundsInRoot.left

        assertTrue(leading > trailing)
    }

    @Test
    fun longContentUsesTheNarrowConstraintAtLargeFontScale() = runComposeUiTest {
        setContent {
            val currentDensity = LocalDensity.current
            CompositionLocalProvider(
                LocalDensity provides Density(currentDensity.density, fontScale = 2f),
            ) {
                BeezTheme {
                    Box(modifier = Modifier.width(240.dp)) {
                        BeezTextField(
                            value = "a-very-long-account-name@example.com",
                            onValueChange = {},
                            label = "Account recovery email address",
                            supportingText = "We use this address only when account recovery is requested.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("narrow-large-text-field"),
                        )
                    }
                }
            }
        }

        onNodeWithTag("narrow-large-text-field").assertWidthIsEqualTo(240.dp)
    }
}
