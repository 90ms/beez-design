package beez.design.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
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
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import beez.design.foundation.BeezTheme
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class BeezSurfaceUiTest {
    @Test
    fun surfaceRendersContentWithoutAddingInteractionSemantics() = runComposeUiTest {
        setContent {
            BeezTheme {
                BeezSurface(
                    modifier = Modifier.testTag("surface"),
                    elevation = BeezSurfaceElevation.Raised,
                ) {
                    BasicText("Grouped content")
                }
            }
        }

        onNodeWithText("Grouped content").assertExists()
        onNodeWithTag("surface")
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.Role))
            .assert(SemanticsMatcher.keyNotDefined(SemanticsActions.OnClick))
    }

    @Test
    fun surfaceRespectsParentConstraint() = runComposeUiTest {
        setContent {
            BeezTheme {
                Box(modifier = Modifier.width(240.dp)) {
                    BeezSurface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("constrained-surface"),
                    ) {
                        BasicText("Constrained content")
                    }
                }
            }
        }

        onNodeWithTag("constrained-surface").assertWidthIsEqualTo(240.dp)
    }

    @Test
    fun surfacePreservesRtlContentAtLargeFontScale() = runComposeUiTest {
        setContent {
            val density = LocalDensity.current
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Rtl,
                LocalDensity provides Density(density.density, fontScale = 2f),
            ) {
                BeezTheme {
                    BeezSurface(
                        modifier = Modifier
                            .width(240.dp)
                            .testTag("rtl-surface"),
                        elevation = BeezSurfaceElevation.Floating,
                    ) {
                        BasicText(
                            text = "محتوى طويل داخل سطح مشترك",
                            modifier = Modifier.padding(BeezTheme.spacing.contentStackGap),
                        )
                    }
                }
            }
        }

        onNodeWithText("محتوى طويل داخل سطح مشترك").assertExists()
        onNodeWithTag("rtl-surface")
            .assert(SemanticsMatcher.keyNotDefined(SemanticsActions.OnClick))
    }
}
