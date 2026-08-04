package beez.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import beez.design.foundation.BeezTheme
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class BeezTextUiTest {
    @Test
    fun textKeepsItsSemanticsWithoutAddingInteraction() = runComposeUiTest {
        setContent {
            BeezTheme {
                BeezText(
                    text = "Account overview",
                    role = BeezTextRole.ScreenTitle,
                    modifier = Modifier.testTag("text"),
                )
            }
        }

        onNodeWithText("Account overview").assertExists()
        onNodeWithTag("text")
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.Role))
            .assert(SemanticsMatcher.keyNotDefined(SemanticsActions.OnClick))
    }

    @Test
    fun constrainedEllipsisKeepsTheOriginalSemanticText() = runComposeUiTest {
        val text = "A complete description that does not fit on one line"

        setContent {
            BeezTheme {
                BeezText(
                    text = text,
                    modifier = Modifier
                        .width(120.dp)
                        .testTag("ellipsis-text"),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        onNodeWithText(text).assertExists()
        onNodeWithTag("ellipsis-text").assertWidthIsEqualTo(120.dp)
    }

    @Test
    fun onBrandToneRendersOnItsSupportedBackground() = runComposeUiTest {
        setContent {
            BeezTheme {
                Box(modifier = Modifier.background(BeezTheme.colors.backgroundBrand)) {
                    BeezText(
                        text = "BEEZ",
                        tone = BeezTextTone.OnBrand,
                    )
                }
            }
        }

        onNodeWithText("BEEZ").assertExists()
    }

    @Test
    fun longRtlTextRendersAtLargeFontScale() = runComposeUiTest {
        setContent {
            val currentDensity = LocalDensity.current
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Rtl,
                LocalDensity provides Density(currentDensity.density, fontScale = 2f),
            ) {
                BeezTheme {
                    Box(modifier = Modifier.width(240.dp)) {
                        BeezText(
                            text = "وصف طويل يلتف داخل مساحة ضيقة دون فقدان المعنى",
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("rtl-text"),
                            textAlign = TextAlign.Start,
                        )
                    }
                }
            }
        }

        onNodeWithTag("rtl-text")
            .assertWidthIsEqualTo(240.dp)
            .assert(SemanticsMatcher.keyNotDefined(SemanticsActions.OnClick))
    }
}
