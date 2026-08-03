package beez.design.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import beez.design.foundation.BeezTheme
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class BeezActionButtonUiTest {
    @Test
    fun buttonExposesRoleAndInvokesCallback() = runComposeUiTest {
        var clicks = 0

        setContent {
            BeezTheme {
                BeezActionButton(
                    label = "Continue",
                    onClick = { clicks += 1 },
                )
            }
        }

        onNodeWithText("Continue")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button))
            .assertHasClickAction()
            .assertIsEnabled()
            .performClick()

        assertEquals(1, clicks)
    }

    @Test
    fun disabledButtonExposesDisabledSemantics() = runComposeUiTest {
        setContent {
            BeezTheme {
                BeezActionButton(
                    label = "Unavailable",
                    onClick = {},
                    enabled = false,
                )
            }
        }

        onNodeWithText("Unavailable").assertIsNotEnabled()
    }

    @Test
    fun loadingButtonExposesProgressAndBlocksInteraction() = runComposeUiTest {
        setContent {
            BeezTheme {
                BeezActionButton(
                    label = "Saving",
                    onClick = {},
                    loading = true,
                )
            }
        }

        onNodeWithText("Saving")
            .assertIsNotEnabled()
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.ProgressBarRangeInfo,
                    ProgressBarRangeInfo.Indeterminate,
                ),
            )
    }

    @Test
    fun smallButtonKeepsTheMinimumTouchTarget() = runComposeUiTest {
        setContent {
            BeezTheme {
                BeezActionButton(
                    label = "Compact",
                    onClick = {},
                    size = BeezActionButtonSize.Small,
                    modifier = Modifier.testTag("small-button"),
                )
            }
        }

        onNodeWithTag("small-button").assertHeightIsAtLeast(48.dp)
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
                        BeezActionButton(
                            label = "متابعة إلى الخطوة التالية",
                            onClick = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("rtl-long-label"),
                        )
                    }
                }
            }
        }

        onNodeWithTag("rtl-long-label").assertHasClickAction()
    }
}
