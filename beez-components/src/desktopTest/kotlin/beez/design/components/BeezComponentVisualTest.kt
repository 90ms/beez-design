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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Density
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

    @Test
    fun textLight() = verifyVisual(
        name = "text-light",
        scheme = BeezTokenSchemes.light,
        expected = TEXT_LIGHT,
        height = 440.dp,
    ) {
        TextVisualScenario()
    }

    @Test
    fun textDark() = verifyVisual(
        name = "text-dark",
        scheme = BeezTokenSchemes.dark,
        expected = TEXT_DARK,
        height = 440.dp,
    ) {
        TextVisualScenario()
    }

    @Test
    fun textAlternateBrandRtlAtLargeFontScale() = verifyVisual(
        name = "text-alternate-brand-rtl-large-font",
        scheme = alternateTextBrandScheme(BeezTokenSchemes.light),
        expected = TEXT_ALTERNATE_BRAND_RTL_LARGE_FONT,
        height = 620.dp,
        layoutDirection = LayoutDirection.Rtl,
        fontScale = 1.5f,
    ) {
        TextVisualScenario(rtl = true)
    }
}

@OptIn(ExperimentalTestApi::class)
private fun verifyVisual(
    name: String,
    scheme: BeezTokenScheme,
    expected: String,
    height: Dp,
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
    fontScale: Float? = null,
    content: @Composable () -> Unit,
) = runComposeUiTest {
    setContent {
        val currentDensity = LocalDensity.current
        CompositionLocalProvider(
            LocalLayoutDirection provides layoutDirection,
            LocalDensity provides Density(
                density = currentDensity.density,
                fontScale = fontScale ?: currentDensity.fontScale,
            ),
        ) {
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

@Composable
private fun TextVisualScenario(rtl: Boolean = false) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap),
    ) {
        BeezText(
            text = if (rtl) "واجهة BEEZ" else "BEEZ interface",
            role = BeezTextRole.Display,
            modifier = Modifier.fillMaxWidth(),
        )
        BeezText(
            text = if (rtl) "نظرة عامة على الحساب" else "Account overview",
            role = BeezTextRole.ScreenTitle,
            modifier = Modifier.fillMaxWidth(),
        )
        BeezText(
            text = if (rtl) "تفاصيل الدفع" else "Payment details",
            role = BeezTextRole.SectionTitle,
            modifier = Modifier.fillMaxWidth(),
        )
        BeezText(
            text = if (rtl) {
                "نص طويل يلتف داخل المساحة المتاحة ويحافظ على ترتيب القراءة."
            } else {
                "Supporting body text wraps within the available width."
            },
            tone = BeezTextTone.Secondary,
            modifier = Modifier.fillMaxWidth(),
        )
        BeezText(
            text = if (rtl) "تحقق من المعلومات مرة أخرى." else "Check the information again.",
            tone = BeezTextTone.Critical,
            modifier = Modifier.fillMaxWidth(),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BeezTheme.colors.backgroundBrand)
                .padding(BeezTheme.spacing.contentInlineGap),
        ) {
            BeezText(
                text = if (rtl) "نص فوق خلفية العلامة" else "Text on a brand background",
                role = BeezTextRole.Label,
                tone = BeezTextTone.OnBrand,
            )
        }
        BeezText(
            text = if (rtl) "تم التحديث اليوم" else "Updated today",
            role = BeezTextRole.Caption,
            tone = BeezTextTone.Secondary,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private fun alternateBrandScheme(base: BeezTokenScheme): BeezTokenScheme = base.copy(
    colors = base.colors.copy(
        backgroundBrand = base.colors.backgroundCritical,
        foregroundOnBrand = base.colors.foregroundPrimary,
        strokeFocus = base.colors.strokeCritical,
    ),
)

private fun alternateTextBrandScheme(base: BeezTokenScheme): BeezTokenScheme = base.copy(
    colors = base.colors.copy(
        backgroundBrand = base.colors.backgroundCritical,
        foregroundOnBrand = base.colors.foregroundOnBrand,
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
private const val SURFACE_LIGHT = "H4sIAAAAAAAAA+2XURKDIAxEr9SCWj2OkOT+R+hmFe342xnbzLDvI4D5iIpLNOv6D0kC2awKUDPNmhRzWUFBnMGC6wVUzD1RkLc4v64eGshRrz2I6dNBHMGE60B9fXCQN5KurpgSgg1N4sZoutRNn0kYZGI2kcOXjnV3G6zrDJaP9Rbb+7xZzdeLFXX7rC8HcQTumy+ZxOdDzRV+KRkMe958RqVuLDz6/qkknq7fb9u3UWLUuq/1FxJP1/sILv+I5XH6ZZ03P2RfneiX7K/RdWbvO1v/TN8t7LtrlTP/dl/Yz6vWF+vk7P39iroWB3lPkM5+GuPEedr+F5C9nWt3vdcfnZdfqz2f1icsJE5cSVfXF3oDrs34JwASAAA="
private const val SURFACE_DARK = "H4sIAAAAAAAAA+2WTbKDQAiErzQDDepx5o/7H+G1MUaXb5XEJBs/B7GqKbEhItdc8ojIjXdxIe66r67/fjaYmEd4su4MaOiARKChgHmWMIxnG9Y8MV4wozHPFTq/vh4pskg/9GqSUOXZAVCfdm1YdSsyjBQkgO81qfJG3+Ey/LD+vyzPdVyB9cGP6h+kzS/pQrO0GyepJ1Y6FPPFxWShH010znKK3/0rj9wlP78e61Zpi+Hdq69sXnwcRKAbE312d+pm9mLU68t23uOcBqrT7z/+t+5P0f/ja/nqfvjW/t/9cza3cvJD9ex2xHmFzcfeiQUTOBc8G3NvHL7u15OZcT5IliR4Xj2PuXTf+7VppazHnNr9X0KGrs/Htk9Ll6brXp23uvjWousc67lJev53uAzP/d/fwD++xXd+fCP+AbVfpe8AEgAA"
private const val SURFACE_ALTERNATE_BRAND = "H4sIAAAAAAAAA+2XURKDIAxEr9SCWj2OkOT+R+hmFe342xnbzLDvI4D5iIpLNOv6D0kC2awKUDPNmhRzWUFBnMGC6wVUzD1RkLc4v64eGshRrz2I6dNBHMGE60B9fXCQN5KurpgSgg1N4sZoutRNn0kYZGI2kcOXjnV3G6zrDJaP9Rbb+7xZzdeLFXX7rC8HcQTumy+ZxOdDzRV+KRkMe958RqVuLDz6/qkknq7fb9u3UWLUuq/1FxJP1/sILv+I5XH6ZZ03P2RfneiX7K/RdWbvO1v/TN8t7LtrlTP/dl/Yz6vWF+vk7P39iroWB3lPkM5+GuPEedr+F5C9nWt3vdcfnZdfqz2f1icsJE5cSVfXF3oDrs34JwASAAA="
private const val TEXT_LIGHT = "H4sIAAAAAAAAA+1WW44cIQy8UtM8+zi8fP8jpMo0DZmNlM1Km+RjXNJ4MMa4bWwQKUeWYkRKKL5EcA8EkRqBJJJ7bllE2gFQLwGXyjvlk2oGCvQUIv0CoFBOwGKsBF4IcN9d95hvQF+crFUYyAq11o9tfSPWvtHGMzpwE4+Ifeh2hB62b3BTgoQesR5fUTPksQBYXw/ALDtXvMKF700eQBxSA2g/APgTe2wJdhIpq55LiFM2wLm+M7hgA+TJpjPBr2IAzMcMVLVbU98C5xVwQKEOAyJOseb5S7kBzm1+UlJ85NPeHc/H3pyf8qoQsYo1PhUf/bsJyWyF56MCiGsLzTfMN9dsg39N6Z6HvRYJcCSEC6EfqF+Aeuh++n1zfT8Bu9ntMIf4VSU9H3oc7mPF/YYfESuwTz8A5LkHIMoHmueC6clpnftiAafnspam9RFYH5cFEL9qAPhZPUD/S8mlqt+u+ZX3aR+zV2E98ACmtQ67Bu4745hJ+INddd+LNOP0C/9nPUlX/J6m3sx/VKx8T/mr/W8iRKdkfGf2AOOBQGX4kS9gixePi8Zxxi8A8DsnIC87xSFvXuUxX2ov057G/zu+g9XBemHURn2MOr3u8axXj2oNqhc1vhx7GfUUt/lZ5xG/1GM1Ohn1l2XkK23rJi+Kxw6S3MVs9qaegz2//EOXTtqPI+rj0r5sWSe9Iu9d+26TQ89BZqGpXr77sGzjgJVp2z/Dcl32ta7tZvdEXbql98zPdVNusDvjd3T+H9817oOxv8H9MPqE4X2CftA6+0dCvYz7qHM892X/YD945vvQ1zzu/fRvUbn7a7vzN6k9fXnU56R+1+nUn+PX+m93v/5dX5jrv0oJ+WXeT3Y+cNfGgyHgfh9j7f9iR9/HxTwa9jn6vvjxXnjm3bgnxKD/W50fiY6PvdEoD14oasc3v9n9QyoeDx+rPHu+b1zgtY3+H4JrG1/y6PpP8ssfG3+Vv9r57PwneT6dO3lPHcE49sPioy06Phz7oHf5rCoPln1ShrycwTl9nwXvyraOY+oLHmiMS0Bh+80+C9wt/StbZ86v825q4jsM/FJ+3OPjRU5OfYdr37Pv4NjG/4DPfsX+0rY+2SDhuX3tezd/05ve9KY3venf0Q8ZsmTjABIAAA=="
private const val TEXT_DARK = "H4sIAAAAAAAAA+1WSZLkIAz8EqAFeA5m+f8TJoXBdlRPx3RPzHKpQ4VKAgkhZQqPoaqsaQwl9SqQXvB/DHHcJUBPKprH4MyRD+hOupo9nn6++sMP7Pc8BH5UqVCH9OSIZ1ynkKGFSg5yhE7wJyUm+MshWRpklSLm16my7fPBBfjRQZna9D/gBu8zzj63+NwK/hQpoegYOWbOiBtLjBH5FiquWB4FGUPPOWsuOC+JSrnjJEouYV8OaWTcI5Wkqdzxt192qWU/9/WMhHAKR6tfxQnI/6CCH9Y5+yzTnhX555pTroh7pJTqfW7gQCFChvO+0ELAPYJgJT3Wt4xBQ5nrU7/ibP9b+iAPv1XPy3+vO48zEWf4HnCfkLHjeOgFlvbYrzg5P/pez/7RoMYwACXChpfEyuW2X+uBHfNDrv27HzgthXrb0f1EpgsTxxsfG5/UyMo9gJ4k9XEO4QS98QO0ET3qteXuD8oyouFehQT7YFU1nOCGivM2TzZOpAO5485779t56nHG3bhA2SavEIcU6yLnOTFEF3nmHwV1j05b9DcPE5qXaN7Hs3zMH3xq5D/afyWvfi68+e5b8Ld979s8/m78r0rct0fjUQNjUacoqIjecvMWLNt60vqo3+v+VffN/9c4fzr/a64Zz4yv6eTPxbuO/oQb1xevTW8PPm7d+FenXab9AAP7R/5ht85z9nkePObJ1xmHfBjWt5/EyzMeI2Kc/IqTH+CR8Wryh+Z8FjL+EhCga16Xtc9PPgXj05z3Y/HycU+wVo23M35dc9v8BBFxf2bwMz3W21pPpz79j4XvsN4NeuSzJKYM25zZ8bYOdDjDx9YRdd4H75iyvTcoluh616zu9Xxf/hbOP8VPR2fKnMOTf5fdr/kb0acn78fi6Vrf+oe4hHlvc739Io9P/L8q0X9n/RHCPLf512BBPdmhQ2LvADpp/fBAiO0XVLpPfZjOis7Y/Hfn+vbH8zZmvAyEHBOXZHjmiolu/LcXxr4fGHPRcBOA/Pb9/KtPWZBnpVQMD1VSNdxUTU3SQ3I6pv113ez6WH/d/xrnq+tflINLse+2HnK196PH4uw7qnNu9h4Ojw8ZFLbjI87e6W2/9vt82PvVc/H2nnWXy9RXnHZktTw7LT/JfcpcDJbDvnhsrv6ulMJx4gO8nDgwPT/0V7tgsPLCk/v/8pqfa17teTbnZX7MrT0P1xz613PmLd/yLd/yLd/ylj8AwcwAsQASAAA="
private const val TEXT_ALTERNATE_BRAND_RTL_LARGE_FONT = "H4sIAAAAAAAAA9VXCXLdIAy9ks3OcVjE/Y/Q9wTYbqa/naZNmurNhHwWoQ1JHuPDqSnu8TepBCDiH6/4dEoFgNzxBCzUsIDD7w5QPgdArmoArMcaS+zYl1tqeYzQQwvY1xuA+TSS5AP7fXX1M/SxiiGdgFy1lVYhR0g+QYFSAegnQbxAUFHcJA3o030dcueaS8b+drajnR8vfgEq7kkVoP0iALtCi9wK7F0AyFMt7O8e+/3UrxmAfksttoR5l2yC3QvpHfH42/IbwK24wH0I5xxov2PaL4TgQ7rj/Npvo4mMKxysWM+SO9TmvOV8Q1i1+PHyf1VCVFZp+s40DkZUDHEE/GzSmRj3kYAdEfgV8aMBwv0rH1WpvZKhUVx8RlYMHk/HbW9Ruvm+lzrc2SFHhQTt0HHoiAdWMd89EG79mpt5ZwTFJR+fReV78EDQ96xxrXpBziKF11z8wcWQD83R/iD++exK/tHCsu+SD7nHMP/sxFI8QDnTi/NvSApx+68nAguH4jWt+9dx2s81f+fvkiEBFmCdXmTlAwRCTkC57VXsfI/1ABAfBYmn9Dvfb0J+0XzfSFjPPrsMPWsAovLP5K/vG+c6SV5K/37a8dEVF+14o7oz4CdJIsZLe2rdYLxXYtzvZPv3D0n9CT6wls3Mdydg9Hnyeev74LU73+86JAdw3nkU0a7xvt8py1rFQZbdJvpOIt8J/Gnpz34CWw8GyAntjdojasAYzNjvRo1jmKPx3h3Pmg0OtVNTO3GcfvCaR6qC+9Td4B6EdtNC9eBTEah93eMe+xzgH/60c33Le/VVW879+xDuuUZcIvqb0UG50IjQf1v+a57ZL6l80y6Lr54/prjKZ9/n8Nc/9F/22/vBS/nt+7ee1/orYrcXVp7nfo+8zvybccPDnts+ex5RHnt7zDuxPAfjidC/Zx+sD/Bjpx+xatj37PVL3nVu+wG7TvGP/a3X/hPx/xlt+WEf9deyy9u4gDYH7fDXqa/8//b328ayaQRy/rs8BelnHC7/X/owDuSOs2v9VyRLb7vqhmfmwBhgAcZxWXkvzr5hOCYMjKYH1l88d6d113TXnfKZjZhhhVY95nfJ5r/X+9RPBH0n9TnW54/rs+HY93x1qivfbz8WRA/ttvSAfXzzX3BsrAzMG6i7fL+BhU9HYb2Xwo6CdeIE3j+20xrLehUciH2Vt97qb+s4FqfpulmLDw/2ae5w7MMaP1D4Wz/PUL9MMOz38IHiOJ/ZsCh/EL9jwJb9S3TesU6KwQcC89Isw/9q7AMNKu1Kcxi1d2FfJZZfxo/9lh/SrNtsRO75T6ddz3ad2nWXbnCPOrXr9c47a7z6hH3uv6Zv/HVWCwASAAA="
