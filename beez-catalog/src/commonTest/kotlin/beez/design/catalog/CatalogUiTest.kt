package beez.design.catalog

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class CatalogUiTest {
    @Test
    fun overviewUsesFixedEnglishBrandMessageWhenLocaleIsKorean() = runComposeUiTest {
        setContent {
            CatalogApp(initialLocale = CatalogLocale.Korean)
        }

        onNodeWithText("Design with meaning.").assertTextEquals("Design with meaning.")
    }

    @Test
    fun themeControlsChangeAppearanceAndBrandMapping() = runComposeUiTest {
        setContent {
            CatalogApp(initialLocale = CatalogLocale.English)
        }

        onNodeWithText("Themes").performClick()
        onNodeWithText("Dark").performClick()
        onNodeWithText("Test Brand").performClick()

        onNodeWithText("Test Brand · Dark").assertTextEquals("Test Brand · Dark")
    }

    @Test
    fun componentsSectionUsesRealTextFieldApi() = runComposeUiTest {
        setContent {
            CatalogApp(initialLocale = CatalogLocale.English)
        }

        onNodeWithText("Components").performClick()
        val field = onNodeWithContentDescription("Email address")
        field.performTextInput("user@example.com")

        field.assertTextEquals("user@example.com")
    }

    @Test
    fun actionButtonCatalogProvidesPlaygroundAndMatrices() = runComposeUiTest {
        setContent {
            CatalogApp(initialLocale = CatalogLocale.English)
        }

        onNodeWithText("Themes").performClick()
        onNodeWithText("Dark").performClick()
        onNodeWithText("Test Brand").performClick()
        onNodeWithText("Components").performClick()
        onNodeWithText("Continue").performClick()

        onNodeWithText("Playground · Clicks: 1").assertExists()
        onNodeWithText("Brand solid").assertExists()
        onNodeWithText("Neutral").assertExists()
        onNodeWithText("Outline").assertExists()
        onNodeWithText("Small").assertExists()
        onNodeWithText("Medium").assertExists()
        onNodeWithText("Large").assertExists()
        onNodeWithText("Enabled").assertExists()
        onNodeWithText("Disabled").assertExists()
        onNodeWithText("Loading").assertExists()
        onNodeWithText("متابعة إلى الخطوة التالية").assertExists()
    }

    @Test
    fun textFieldCatalogProvidesStateSlotAndRtlScenarios() = runComposeUiTest {
        setContent {
            CatalogApp(initialLocale = CatalogLocale.English)
        }

        onNodeWithText("Themes").performClick()
        onNodeWithText("Dark").performClick()
        onNodeWithText("Test Brand").performClick()
        onNodeWithText("Components").performClick()

        onNodeWithContentDescription("Empty field").assertExists()
        onNodeWithContentDescription("Filled field").assertTextEquals("Filled value")
        onNodeWithContentDescription("Read-only field").assertTextEquals("Read-only value")
        onNodeWithContentDescription("Disabled field").assertTextEquals("Disabled value")
        onNodeWithContentDescription("Error field").assertTextEquals("Invalid value")
        onNodeWithContentDescription("Slotted field").assertTextEquals("account")
        onNodeWithContentDescription("عنوان حقل طويل للتحقق من التخطيط").assertExists()
    }

    @Test
    fun checkboxCatalogProvidesPlaygroundStateAndRtlScenarios() = runComposeUiTest {
        setContent {
            CatalogApp(initialLocale = CatalogLocale.English)
        }

        onNodeWithText("Components").performClick()
        onNodeWithText("Receive product updates")
            .assertIsOff()
            .performClick()

        onNodeWithText("Playground · Checked: true").assertExists()
        onNodeWithText("Receive product updates").assertIsOn()
        onNodeWithText("Unchecked option").assertIsOff()
        onNodeWithText("Checked option").assertIsOn()
        onNodeWithText("Disabled unchecked option").assertIsNotEnabled().assertIsOff()
        onNodeWithText("Disabled checked option").assertIsNotEnabled().assertIsOn()
        onNodeWithText("أوافق على تلقي تحديثات مفصلة حول هذا الخيار").assertIsOn()
    }
}
