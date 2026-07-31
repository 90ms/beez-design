package beez.design.catalog

import androidx.compose.ui.test.ExperimentalTestApi
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
}
