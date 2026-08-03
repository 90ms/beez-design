package beez.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import beez.design.foundation.BeezTheme
import beez.design.tokens.BeezTokenScheme
import beez.design.tokens.BeezTokenSchemes
import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.max
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class BeezComponentVisualTest {
    @Test
    fun actionButtonLight() = verifyVisual(
        name = "action-button-light",
        scheme = BeezTokenSchemes.light,
        expected = ACTION_BUTTON_LIGHT,
        height = 232.dp,
    ) {
        ActionButtonVisualScenario()
    }

    @Test
    fun actionButtonDark() = verifyVisual(
        name = "action-button-dark",
        scheme = BeezTokenSchemes.dark,
        expected = ACTION_BUTTON_DARK,
        height = 232.dp,
    ) {
        ActionButtonVisualScenario()
    }

    @Test
    fun actionButtonAlternateBrand() = verifyVisual(
        name = "action-button-alternate-brand",
        scheme = alternateBrandScheme(BeezTokenSchemes.light),
        expected = ACTION_BUTTON_ALTERNATE_BRAND,
        height = 232.dp,
    ) {
        ActionButtonVisualScenario()
    }

    @Test
    fun textFieldLight() = verifyVisual(
        name = "text-field-light",
        scheme = BeezTokenSchemes.light,
        expected = TEXT_FIELD_LIGHT,
        height = 212.dp,
    ) {
        TextFieldVisualScenario()
    }

    @Test
    fun textFieldDarkError() = verifyVisual(
        name = "text-field-dark-error",
        scheme = BeezTokenSchemes.dark,
        expected = TEXT_FIELD_DARK_ERROR,
        height = 212.dp,
    ) {
        TextFieldVisualScenario(error = true)
    }

    @Test
    fun textFieldAlternateBrandRtl() = verifyVisual(
        name = "text-field-alternate-brand-rtl",
        scheme = alternateBrandScheme(BeezTokenSchemes.light),
        expected = TEXT_FIELD_ALTERNATE_BRAND_RTL,
        height = 212.dp,
        layoutDirection = LayoutDirection.Rtl,
    ) {
        TextFieldVisualScenario(readOnly = true)
    }
}

@OptIn(ExperimentalTestApi::class)
private fun verifyVisual(
    name: String,
    scheme: BeezTokenScheme,
    expected: String,
    height: Dp,
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
    content: @Composable () -> Unit,
) = runComposeUiTest {
    setContent {
        CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
            BeezTheme(scheme = scheme) {
                Box(
                    modifier = Modifier
                        .width(360.dp)
                        .height(height)
                        .background(BeezTheme.colors.backgroundNeutral)
                        .padding(BeezTheme.spacing.screenGutter)
                        .testTag(name),
                ) {
                    content()
                }
            }
        }
    }

    val image = onNodeWithTag(name).captureToImage()
    val actual = image.normalizedSignature()
    image.writePpmCandidate(name)

    if (expected == PENDING_BASELINE) {
        error("Missing visual baseline for $name. Signature: $actual")
    }

    val comparison = compareSignatures(expected, actual)
    assertTrue(
        comparison.meanChannelDelta <= 4.0 && comparison.changedCellRatio <= 0.05,
        "Visual baseline mismatch for $name: $comparison. Candidate written to build/reports/visual-candidates/$name.ppm",
    )
}

@Composable
private fun ActionButtonVisualScenario() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentStackGap),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap),
        ) {
            BeezActionButton(
                label = "Continue",
                onClick = {},
                modifier = Modifier.weight(1f),
            )
            BeezActionButton(
                label = "Outline",
                onClick = {},
                variant = BeezActionButtonVariant.Outline,
                modifier = Modifier.weight(1f),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap),
        ) {
            BeezActionButton(
                label = "Disabled",
                onClick = {},
                enabled = false,
                modifier = Modifier.weight(1f),
            )
            BeezActionButton(
                label = "Loading",
                onClick = {},
                loading = true,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun TextFieldVisualScenario(
    error: Boolean = false,
    readOnly: Boolean = false,
) {
    BeezTextField(
        value = if (readOnly) "account@example.com" else "draft@example.com",
        onValueChange = {},
        label = if (readOnly) "Account identifier" else "Email address",
        supportingText = if (error) {
            "Enter a valid email address."
        } else {
            "Use an address you can access."
        },
        readOnly = readOnly,
        isError = error,
        modifier = Modifier.fillMaxWidth(),
    )
}

private fun alternateBrandScheme(base: BeezTokenScheme): BeezTokenScheme = base.copy(
    colors = base.colors.copy(
        backgroundBrand = base.colors.backgroundCritical,
        foregroundOnBrand = base.colors.foregroundPrimary,
        strokeFocus = base.colors.strokeCritical,
    ),
)

private fun ImageBitmap.normalizedSignature(gridSize: Int = SIGNATURE_GRID_SIZE): String {
    val pixels = toPixelMap()
    val cells = ArrayList<Int>(gridSize * gridSize)

    for (gridY in 0 until gridSize) {
        val startY = gridY * height / gridSize
        val endY = max(startY + 1, (gridY + 1) * height / gridSize)
        for (gridX in 0 until gridSize) {
            val startX = gridX * width / gridSize
            val endX = max(startX + 1, (gridX + 1) * width / gridSize)
            var alpha = 0L
            var red = 0L
            var green = 0L
            var blue = 0L
            var count = 0L

            for (y in startY until endY) {
                for (x in startX until endX) {
                    val argb = pixels[x, y].toArgb()
                    alpha += (argb ushr 24) and 0xFF
                    red += (argb ushr 16) and 0xFF
                    green += (argb ushr 8) and 0xFF
                    blue += argb and 0xFF
                    count += 1
                }
            }

            cells += ((alpha / count).toInt() shl 24) or
                ((red / count).toInt() shl 16) or
                ((green / count).toInt() shl 8) or
                (blue / count).toInt()
        }
    }

    return cells.joinToString(separator = "") { cell ->
        cell.toUInt().toString(16).padStart(8, '0')
    }
}

private data class SignatureComparison(
    val meanChannelDelta: Double,
    val changedCellRatio: Double,
)

private fun compareSignatures(expected: String, actual: String): SignatureComparison {
    require(expected.length == actual.length) {
        "Visual signatures have different lengths: expected=${expected.length}, actual=${actual.length}"
    }
    val cells = expected.length / 8
    var channelDelta = 0L
    var changedCells = 0

    for (index in 0 until cells) {
        val start = index * 8
        val expectedColor = expected.substring(start, start + 8).toUInt(16).toInt()
        val actualColor = actual.substring(start, start + 8).toUInt(16).toInt()
        var cellDelta = 0
        for (shift in intArrayOf(24, 16, 8, 0)) {
            cellDelta += kotlin.math.abs(
                ((expectedColor ushr shift) and 0xFF) - ((actualColor ushr shift) and 0xFF),
            )
        }
        channelDelta += cellDelta
        if (cellDelta / 4.0 > 18.0) {
            changedCells += 1
        }
    }

    return SignatureComparison(
        meanChannelDelta = channelDelta.toDouble() / (cells * 4),
        changedCellRatio = changedCells.toDouble() / cells,
    )
}

private fun ImageBitmap.writePpmCandidate(name: String) {
    val pixels = toPixelMap()
    val header = "P6\n$width $height\n255\n".encodeToByteArray()
    val output = ByteArray(header.size + width * height * 3)
    header.copyInto(output)
    var offset = header.size

    for (y in 0 until height) {
        for (x in 0 until width) {
            val argb = pixels[x, y].toArgb()
            output[offset++] = ((argb ushr 16) and 0xFF).toByte()
            output[offset++] = ((argb ushr 8) and 0xFF).toByte()
            output[offset++] = (argb and 0xFF).toByte()
        }
    }

    val reportDirectory = Path.of("build", "reports", "visual-candidates")
    Files.createDirectories(reportDirectory)
    Files.write(reportDirectory.resolve("$name.ppm"), output)
}

private const val SIGNATURE_GRID_SIZE = 24
private const val PENDING_BASELINE = "pending"
private const val ACTION_BUTTON_LIGHT = PENDING_BASELINE
private const val ACTION_BUTTON_DARK = PENDING_BASELINE
private const val ACTION_BUTTON_ALTERNATE_BRAND = PENDING_BASELINE
private const val TEXT_FIELD_LIGHT = PENDING_BASELINE
private const val TEXT_FIELD_DARK_ERROR = PENDING_BASELINE
private const val TEXT_FIELD_ALTERNATE_BRAND_RTL = PENDING_BASELINE
