package beez.design.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasContentDescriptionExactly
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import beez.design.foundation.BeezTheme
import kotlin.test.Test
import kotlin.test.assertEquals

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

        onNodeWithContentDescription("Email address")
            .assertIsEnabled()
            .assertTextEquals("account@example.com")
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
}
