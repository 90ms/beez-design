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
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
    fun checkboxLight() = verifyVisual(
        name = "checkbox-light",
        scheme = BeezTokenSchemes.light,
        expected = CHECKBOX_LIGHT,
        height = 264.dp,
    ) {
        CheckboxVisualScenario()
    }

    @Test
    fun checkboxDark() = verifyVisual(
        name = "checkbox-dark",
        scheme = BeezTokenSchemes.dark,
        expected = CHECKBOX_DARK,
        height = 264.dp,
    ) {
        CheckboxVisualScenario()
    }

    @Test
    fun checkboxAlternateBrand() = verifyVisual(
        name = "checkbox-alternate-brand",
        scheme = alternateBrandScheme(BeezTokenSchemes.light),
        expected = CHECKBOX_ALTERNATE_BRAND,
        height = 264.dp,
    ) {
        CheckboxVisualScenario()
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

    @Test
    fun surfaceLight() = verifyVisual(
        name = "surface-light",
        scheme = BeezTokenSchemes.light,
        expected = SURFACE_LIGHT,
        height = 264.dp,
    ) {
        SurfaceVisualScenario()
    }

    @Test
    fun surfaceDark() = verifyVisual(
        name = "surface-dark",
        scheme = BeezTokenSchemes.dark,
        expected = SURFACE_DARK,
        height = 264.dp,
    ) {
        SurfaceVisualScenario()
    }

    @Test
    fun surfaceAlternateBrand() = verifyVisual(
        name = "surface-alternate-brand",
        scheme = alternateBrandScheme(BeezTokenSchemes.light),
        expected = SURFACE_ALTERNATE_BRAND,
        height = 264.dp,
    ) {
        SurfaceVisualScenario()
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
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
    }

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
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
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
private fun CheckboxVisualScenario() {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap),
    ) {
        BeezCheckbox(
            checked = true,
            onCheckedChange = {},
            label = "Checked focused",
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
        )
        BeezCheckbox(
            checked = false,
            onCheckedChange = {},
            label = "Unchecked",
            modifier = Modifier.fillMaxWidth(),
        )
        BeezCheckbox(
            checked = false,
            onCheckedChange = {},
            label = "Disabled unchecked",
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
        )
        BeezCheckbox(
            checked = true,
            onCheckedChange = {},
            label = "Disabled checked",
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
        )
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

@Composable
private fun SurfaceVisualScenario() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentStackGap),
    ) {
        BeezSurface(
            modifier = Modifier.fillMaxWidth(),
            elevation = BeezSurfaceElevation.Flat,
        ) {
            BasicText(
                text = "Flat surface",
                style = BeezTheme.typography.body.copy(color = BeezTheme.colors.foregroundPrimary),
                modifier = Modifier.padding(BeezTheme.spacing.contentStackGap),
            )
        }
        BeezSurface(
            modifier = Modifier.fillMaxWidth(),
            elevation = BeezSurfaceElevation.Raised,
        ) {
            BasicText(
                text = "Raised surface",
                style = BeezTheme.typography.body.copy(color = BeezTheme.colors.foregroundPrimary),
                modifier = Modifier.padding(BeezTheme.spacing.contentStackGap),
            )
        }
        BeezSurface(
            modifier = Modifier.fillMaxWidth(),
            elevation = BeezSurfaceElevation.Floating,
        ) {
            BasicText(
                text = "Floating surface",
                style = BeezTheme.typography.body.copy(color = BeezTheme.colors.foregroundPrimary),
                modifier = Modifier.padding(BeezTheme.spacing.contentStackGap),
            )
        }
    }
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
private const val ACTION_BUTTON_LIGHT = "H4sIAAAAAAAAA+1WR3IDMQz7knp5jsTy/ycEVNaxJ7m4aC+ZBQ6Y9YGCSIq0KslofaqOWXpOW3SYSuNMCqRFFZIpDJ0gbVAVEYTvkkPsqm0E8W6fUuihBRyQF/fjiNspSUR+ek3djoO2gIv1mb3dC0lNYdz1af+uSZUj/+bfLaoMEPXmYVTL4nciy+LzOOL++OecI1v9q8umvXCG/9EK5QaV2ko5/PnX/eOjCuJQAxGXiCahDwiNZX0mxfi+/+398zv/u3HEnbHWstP/jM6jEbngIc/z/SvpAPdBlEFcAJFpxV+65s+OuXPoei11o+9/CiHuHNd78AV5m9pcGZ8r9xnHK+/9TUypXFDnPmKyd7FNNYqXh4Nu/XT0Ky5q/Kuv+u81ZORrOBzr74r0VZ8f/Nh3sX0aydPz97iBJ+osa68spYE5rWtOd8Zc4cSB2wf+O/Z8WRqXNuzHZGmpav2AJUPV9kJstaLfZsMe6s/7X9u4rDrkVYegXuNDXW6/v+u/ZRfNV8L/JHev/wgpB7tPTDX0VR8XnO3RXON4wf9JmFzJ9vYp/c/n+5fJjcMxf+bG+dNmGGfsxQsXLly4cOF0fAHO8f6SABIAAA=="
private const val ACTION_BUTTON_DARK = "H4sIAAAAAAAAA+2WS5bjIAxFtwT6AcsRv/0voR9JXJXqDDrJIdUTT3yPjI94EkLynClp5DhnlVREtjAvSmEhnZOUmDLYqNIEO1UOG1goU5+zpZyx3RzqwcI+5qKTK/YR6E9zxhYr0rSNh9/muSpedC2s6Ztt5K4M24oo8tdT0cWn9Xcj7t/550CDcQBcOHPD+TRxGbCFiQ3fZUpUX9B/83vob/2qt/YcxMGR44XLLiCltOpiwNAnzutBf2Nn6LVqxfDe1MigW4fW5Q+rl7je0O9L//b6aT/1f6R+4NclBaGN+rkMXXnOkml+vv6JY49t2dGjbaDBPxjXTogjjtgpXvvPlr5zkInI9ufl11mRt/EBfzeKshHO1zPmA2wvyXcwBeXVn7fr/ysOb6mu+YIOxlY3ktztrn7I0CnQJylE1BYYKZA88tU4asmy+jL2y3bPjmtS7vS0Gi62O+7+83Ec+2iQodCnEKmIy5LJWsczLn9a1LS/r3+EUtXvOBwTAGFwCWu9aVapl36epa/wiq05+qx+TD9b8++Lxz0/7Bfn44N+Ll2v+i73YKpXuw4uN7mdy2LytupiuK/fp3/r/3T9j9Q+Uv+M+NMv9B/jtP4Tt/efqMJv1MPJkydPnjz5//kHAuGHZgASAAA="
private const val ACTION_BUTTON_ALTERNATE_BRAND = "H4sIAAAAAAAAA+1WQY7DIAz8UoDEwHOMjf//hDXgRN1qD2lLVGmVmcOERMKDMSYiuWwCi0gUl1yep+QRMIrI2imVaqmsWpQ0QaXWqtNn79AVEYxOOVEZPHgNsHXOh82bncZLquigxT10NS2mNPS0fzLbLfvtYemUikrNF2OjtCyORELnedi8h3/zm8A0/h5HNv9tDK/7r1GpcSgpW30RFdI6IC0sFanQ+L7/6fXznP/Z2P1DSCFP9L0NLSEtyV3vv67VVY2jx5q1LD/X9tTmBzu31Dn6z4y+Y9pPS7wgL/8NnlfW/s9rXKL2Hw6jzXyqdcOM+Xr77IFA97ug3jM4UXVi/9iv9nqyem2FLOUPfRGkt3u731FGXNXc46cnP9EU7PvJdRx5Kpy59nulK6H2ael9OnPb95U9pw/8O/Pvhj+kodn8pmJq68t8bh0HWrcAGX2j7YMXJ+FhX/b37/oPI16S8b9SvPlYbD37vgQb5xf9XwQt0wLlkvrf/BX3yjMcBw4X9J8VE75Rzzdu3Lhx48b38QM5tTZhABIAAA=="
private const val CHECKBOX_LIGHT = "H4sIAAAAAAAAA+1XW46DMAy8Ek0CCcdJbOf+R9gZ0weq9qe7pRSJGamDQwQOxYPTu41kP6xKqMOcoHOVGvfP51WtYRpS7b2V0rJgHUYiNrBDK4iDNoOcN4IT5kUJEleaSJxXks+F3D7/lkrMyTVk3E+FhFYSGkloIVexE5pI6AhiXTqR0Jnc///5du2T87h6cKBYm+FFNmLvZP6CBI4rDU7WcbvXcVvVM2GP2O1Kvij/i7NbsWzFtbgmkHUTSehEYn7EauN+6d/fm0sfmPeJE6/g8O/Pb/Ubr75qvj7aTcev3JUHGURdu1vFa71Xv07kdW7z/bnAzzA2bvK9ecqf+eKO9EkxakZf0xY/9Vi8PetYXaVvogtSW/q0RJ+CqxZV96lAf/KYC6FPpfenf/jv14kT/4D3AXmpV9Ylaji5T8wg/aQ+qTgXVR+vPn7DLX4e3whIQ6/7KXMfoa8gLx3EuK+SJkWHVaxSlf1RsAv7Iey+gu/LEvZlUJHlPEYH/UB/7v3Z/PChE5/ED8eqg3sAEgAA"
private const val CHECKBOX_DARK = "H4sIAAAAAAAAA+2XW5LjIAxFt2T0slkOL+1/CXNFcE+q8jU1k3G7Oj85AUxKBHQs3FNOR6p+W1qXQt1dh2yUr4/nT1llz6Luh2lmduedjbEO6VLFwSEtqLuqHosxLsJi4CG7FNDQs4NNiozZb/If/o9MOpgQ/1DE7k5KTMGdLPaDibdYF3WqhLg4rfaWnCja5DGfBnXennjgF8r1+/PtWVNJ/cZs+Ob3JTUqFCw4sTdcz8zX44mdWuTfl0eqZOlPPL20xlmZI++/S/y80fQRd67QjHPhgxviJdlEpjdpenPjIbT2b1wY/zo3+DSq15+HD+/Fu5+fF/8M+AcDqIKU8f5ngV+O2Zaoi06e652+iucrZx6/fTyfK9MHPuuNN/n5xT+MCsceflR4VFk33WcdV5WWR08vnXUa/MQwVvhIN+n68FSSx7wW86J+Ci//8/hv/v768MO/4bx/yPJH5EFGHiAfkc007zFEiXS1kdfJ04h7y8k5bk/5tNrnvHfHf/qBGxeBH2AVY3hRXZthXaZGBl/apn22EZSFnxT3R/Rr1QNhumbcLJH/JpYs7plNs73BNy/+gfGnn5eHrj4PP4u/AJqb5yQAEgAA"
private const val CHECKBOX_ALTERNATE_BRAND = "H4sIAAAAAAAAA+1XW46DMBC7EiSBhOMkM5P7H2HtoQ9U7U9321IkbKkmAVGnZcykd5vIflhdwtQmgWoe87i/n2c1d9jOUI1zhIqRvTcDO7SCOGgLWKFYbptxXZQgcaOJxHklMQ7k+/3PCf5L7yWt/lVIaCWhkYQWcjN2QhMJnUCsS2cSupD7/z/frn12HlcPDhRrMzzIRuxt5i9I4LTR4GQdt1sdt009E3Yfe1zJF/kfnd2KZSuuxTWBrJtIQmcS10esNu5n//bcjH2g7xMnnsHhn5/f6jdectV8fYybjk+5KQ8yiLr2tIqXeq9+n8j7XK/33wV5hrnpLe+bB//0i29kTopRM/qatuapj8Xbs47VVeYmuiC1tU9LzCmkalH1nArMJx9zIcyp9Hr7h39/nTjxD3gfkNd6ZV2ihpPnxAIyT+qDinNV9fnq81dcx4/zbwJs6GU/ZZ4jzBX40kGM+yppUnTYjFWqsj8KNrIfwu4r+L4sYV8GFVnPY3bQD/Tn3p8t9xw68Un8AIKr6H0AEgAA"
private const val TEXT_FIELD_LIGHT = "H4sIAAAAAAAAA+1WW47DIAy8UqBA4DiJH/c/wo69kESV9qPSRm12M6PWMuB2UMhgVZmlSFPVCCZVWUFGPoMVOSEXH198PICPff7doJUqT6ocSLkgJ1o4IF+5MXRTo5ksLlTJ1gsRR+wnSZTybvV/ABmnJvdzQ9eJQzc+2ePVMHSnvo+rYegecXXuePW5PNefjSf9HI3wF9D8aC3g7H4Ew0FMoK0rnN2nCmjzFWzmX+ChnqsRcQJDz9uJ+kGZDnE2Qk8GoReqXTc79nlOoNU343e9XRP4lh799yQbz9P/I8T5eUgH/7niPVS6busH4vv9/GX/77qvDnn0Pibo5M9hAemQRwn23smE9xB9mzSpYj75AK3fq+gAF93Oo/eDy2F+9H+jzu7NXzyvQz/+NVsfuuVDd8JIcf3i+sc+um64DEnY9wu/YYm69bVDN9axrRv7uXHjxo0bN/4vvgDnP1S4ABIAAA=="
private const val TEXT_FIELD_DARK_ERROR = "H4sIAAAAAAAAA+2WUY6kMAxEr0RslxOOEwi+/xG23N0srZXmozXTw7bE11NwCGUHlxxh0KotQkVmWSNM1NQjuFIhdVM+4fNJVXHb13PfLV4jylqWEuexNsygjqoAqLMOrOgRPlkz5tUKNgw+X7BgJjt6EqLM6Xz9n05ZxYX11FX5h3wOd92yCKSdX8eX6/7QLeOex9l6Xtb/0P2XUwm25BGfpcrywnn/vP/b+tHoP8nA8JK+VL2mD7mr05+8e3XmA0UBfdRnRjIuCKfudC1sZIWB94rJNgjZeXL6cthAeZ9+c7P0S6sGm4/v+Uql40nHat22J307BVP6L+MLkee49eM89t2ib6z/l/u3MuQH6/bj+j/cf/Z54Gw/f5kP3WfX8btEMzXLPuSkxrVW/llJ4w2xT20w4+zfco8bpzilH1nTptmfzvkv+31hf67pQ+xbv8XnjGu5z4Xv0m+cPvP7mOk/uM2bJe/HunZNvzSbrBxr2x756H1+2/PBoO567IebGAuxz7dn39PFixcvXrz4//AP9YDxJQASAAA="
private const val TEXT_FIELD_ALTERNATE_BRAND_RTL = "H4sIAAAAAAAAA+1WW5KEIAy8Eio+OA4k4f5H2E4mrO7X+sFaaxXd5aQAwY5IT2p9HDMYEXfwQJzAxdobriokRRhR8Bt8fEa7SBaqlYkLV+sP2v80ZJUo0E2ViKGbF54YuompMPRwZZb50h9ppmy6s+rm2fuJEofn9XfHit1cbX+K7s9bYtONa7X4NjTd0fN4G5ruv9bPxvsoxt/RST8nUP1hBw+P8AfelJCjUN8L4ATfWED4J1xkUr+hHTys/WMc/lTVX8wvMZ8KSL7u3lG/v14WJWIGi+V1cPL8kj0/k/YH19XmLXBE+KhMoOaTlOe4zJ9+rGbryQZ21H8bYuyHePGfreO6T2Fz3ef/87/w9dv+77oHHP4dWn2j5yuIviP96MWi12V2rrU+O2QX+JbVc3p+d5xLbcfveiDr/a1Os/eu8xJmlst4L4TPc+Aaon4BNavWaaYbqUjgKstZn8kCX1GdGYrIfCupf8HESI853Iyl5R/Mn4LEc50WBwYGBgYG3ocvPLEBgAASAAA="
private const val SURFACE_LIGHT = PENDING_BASELINE
private const val SURFACE_DARK = PENDING_BASELINE
private const val SURFACE_ALTERNATE_BRAND = PENDING_BASELINE
