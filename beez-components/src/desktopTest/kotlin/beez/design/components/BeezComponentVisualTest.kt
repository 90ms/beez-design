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
import java.io.ByteArrayInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.Base64
import java.util.zip.GZIPInputStream
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

    val comparison = compareSignatures(expected.decodeSignature(), actual)
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

private fun String.decodeSignature(): String = GZIPInputStream(
    ByteArrayInputStream(Base64.getDecoder().decode(this)),
).bufferedReader().use { it.readText() }

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
private const val ACTION_BUTTON_LIGHT = "H4sIAAAAAAAAA+1WSY7EMAj8kpd4e47B8P8nTJlOpiPNpRfnMkpxKDmRcBkwWJV6SUlUW4+bD0s4+ag6GoWeVXUzU2EhGWCC8QJWEcjWRrH4vFS/MeKSE/xrMluP3e+xX28pRKx7RfgQrz4SbQ6ct4Hfk3nyy/pbCanv8Z/6nZlKhxHy06fpjOIjkNnsdex+f/VjsWFNWiRXMBdINm4Z/znWXE718a5+KTD45QprYGZi1AGjsEAqedrn+pfXz9RPp/ivxu73qJvV+rk2X077XKVfA/KG/SRwIlrCmWZBoBDNP5s9+s+KvrOz3ZZyQVz+GYRHG7jv7KrPPNNSXe7fM+ZL7O/c9w9BUkYuF/QJjeLltNFRT3u94qDT/vK7+vc+2h229U+2uXnuG8cclcieXz/HgUHIs9hcMeaOPq3Wp9vA3BkYY6N+oX/2uWwcjfc5Oa/5rAdOFZPG5kwpqDeqxaX2un6bxtnykCwPQb3GU16O75/qr8nFqWvLLbln/nvYUpjniVsJzfLjwpz7lErsb+i/CDQK5wveV3b+cb1+oVHnA8r6Dy3sPxXv2yvm4o0bN27cuHE5fgAzASGmABIAAA=="
private const val ACTION_BUTTON_DARK = "H4sIAAAAAAAAA+2VS3LsIAxFtwT6AcsRv/0vIZLbTvxeJu0UTibugU9RtKUrfJHmrJwjw7RfRakL2CuIzpkiI9oaGBCysUH1NNChYljAAhm6BdQapCzUv/M4FyDTn+aMLdY41/GIe+QbXUniRt7IZbDVN4aK+MEFTXLhO9WSifHr/DHAQHsfC2Zsc1IjpWFrQkCx/2VIUC/o3+N+6ne9FrdDCX5uHY3o6zx93SQL2f4cL39c1t9Q0fRKlSJ2LsICYrp5cGV7y3a3ui7rf1PPZf/kjGf9t/jH4vZWtvpX6y8ijdL9/scEbpHJjQRpAavZhrb4ASxRHLE7vf8s6TsHEQBk/bn8OmvUOG6It9O+hoDdS82peL/RknQFU3jNl+X6/6tDW6p0w/2aoCon/4BYp7A5BiGat4zR/EvfebWOo49avixn9hr/mZttn6OqU/L7dRx5ONBgv38mkq0uSUK+b8/o8biw+Dz7qf4RrM/picPm5GnONM5Mlq9pztS9vCKc3tdv0098/n3yuOfH+uJ8/KYfS+eXvu0eTNYqr8GlQvt3cSZt7ouhCu/09dv9P1K7xf9o9adf6D/iE+aG/hOZ8Ad+ePjw4cOHD/+eH075N1QAEgAA"
private const val ACTION_BUTTON_ALTERNATE_BRAND = "H4sIAAAAAAAAA+1VW46DMAy8EiEQkuPEr/sfYR1jUNv9KW2ilVbMfIxAwpnYxhahKcUURaAGZReFACK81lKLiCxGYWRgUgUldlBhZg0PEDhwV/+mmpclqW9Zjf3hcY/zKoTypKsruvKu7/pHSVOaPP/N/2QUrkqtD9VGaVncE5mM78Pjnv7db0mu2/NzbvfQc6Goluv+eVNmfZ+V+j0iAmofoDaWinBq/Nx/b/2V/97wuLhEjDig/0uOOY73L4kjx/ZfAwF1UR0QYgWwfkbjPn96zB1Xi74NyMt/w0wL6RylZZs2/R8o7mX5Vs/9Mhg0J0wD/i/QwPPjvDr6yftVwPhbL+KYQ1V8TovvkfziZ3NN1/bMmSegQmx7xRSrzmmxOV2o1X2hmfIX/oP7D8978dwvvjez36/Qe/c40bZxsjqsVodZgsSHuhzvP/Uf9/Nyq0Pbg7P7mPw+R12iP5eL/gdB2xQSDOn/dR6xV14RKFIcMH+WmusH/Xzjxo0bN278PX4ARnvZbwASAAA="
private const val TEXT_FIELD_LIGHT = "H4sIAAAAAAAAA+1WW47DIAy8UqBA4DiJH/c/wo69kESV9qPSRm12M6PWMuB2UMhgVZmlSFPVCCZVWUFGPoMVOSEXH198PICPff7doJUqT6ocSLkgJ1o4IF+5MXRTo5ksLlTJ1gsRR+wnSZTybvV/ABmnJvdzQ9eJQzc+2ePVMHSnvo+rYegecXXuePW5PNefjSf9HI3wF9D8aC3g7H4Ew0FMoK0rnN2nCmjzFWzmX+ChnqsRcQJDz9uJ+kGZDnE2Qk8GoReqXTc79nlOoNU343e9XRP4lh799yQbz9P/I8T5eUgH/7niPVS6busH4vv9/GX/77qvDnn0Pibo5M9hAemQRwn23smE9xB9mzSpYj75AK3fq+gAF93Oo/eDy2F+9H+jzu7NXzyvQz/+NVsfuuVDd8JIcf3i+sc+um64DEnY9wu/YYm69bVDN9axrRv7uXHjxo0bN/4vvgDnP1S4ABIAAA=="
private const val TEXT_FIELD_DARK_ERROR = "H4sIAAAAAAAAA+2WUY6kMAxEr0RslxOOEwi+/xG23N0srZXmozXTw7bE11NwCGUHlxxh0KotQkVmWSNM1NQjuFIhdVM+4fNJVXHb13PfLV4jylqWEuexNsygjqoAqLMOrOgRPlkz5tUKNgw+X7BgJjt6EqLM6Xz9n05ZxYX11FX5h3wOd92yCKSdX8eX6/7QLeOex9l6Xtb/0P2XUwm25BGfpcrywnn/vP/b+tHoP8nA8JK+VL2mD7mr05+8e3XmA0UBfdRnRjIuCKfudC1sZIWB94rJNgjZeXL6cthAeZ9+c7P0S6sGm4/v+Uql40nHat22J307BVP6L+MLkee49eM89t2ib6z/l/u3MuQH6/bj+j/cf/Z54Gw/f5kP3WfX8btEMzXLPuSkxrVW/llJ4w2xT20w4+zfco8bpzilH1nTptmfzvkv+31hf67pQ+xbv8XnjGu5z4Xv0m+cPvP7mOk/uM2bJe/HunZNvzSbrBxr2x756H1+2/PBoO567IebGAuxz7dn39PFixcvXrz4//AP9YDxJQASAAA="
private const val TEXT_FIELD_ALTERNATE_BRAND_RTL = "H4sIAAAAAAAAA+1WW5KEIAy8Eio+OA4k4f5H2E4mrO7X+sFaaxXd5aQAwY5IT2p9HDMYEXfwQJzAxdobriokRRhR8Bt8fEa7SBaqlYkLV+sP2v80ZJUo0E2ViKGbF54YuompMPRwZZb50h9ppmy6s+rm2fuJEofn9XfHit1cbX+K7s9bYtONa7X4NjTd0fN4G5ruv9bPxvsoxt/RST8nUP1hBw+P8AfelJCjUN8L4ATfWED4J1xkUr+hHTys/WMc/lTVX8wvMZ8KSL7u3lG/v14WJWIGi+V1cPL8kj0/k/YH19XmLXBE+KhMoOaTlOe4zJ9+rGbryQZ21H8bYuyHePGfreO6T2Fz3ef/87/w9dv+77oHHP4dWn2j5yuIviP96MWi12V2rrU+O2QX+JbVc3p+d5xLbcfveiDr/a1Os/eu8xJmlst4L4TPc+Aaon4BNavWaaYbqUjgKstZn8kCX1GdGYrIfCupf8HESI853Iyl5R/Mn4LEc50WBwYGBgYG3ocvPLEBgAASAAA="
