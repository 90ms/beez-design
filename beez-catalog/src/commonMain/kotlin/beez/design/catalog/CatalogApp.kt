package beez.design.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import beez.design.components.BeezActionButton
import beez.design.components.BeezActionButtonVariant
import beez.design.foundation.BeezTheme
import beez.design.tokens.BeezTokenScheme
import beez.design.tokens.BeezTokenSchemes
import beez.design.beez_catalog.generated.resources.Res
import beez.design.beez_catalog.generated.resources.noto_sans_kr_catalog
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

public enum class CatalogLocale {
    Korean,
    English,
}

private enum class CatalogAppearance {
    Light,
    Dark,
}

private enum class CatalogBrand {
    Beez,
    Test,
}

private enum class CatalogSection {
    Overview,
    Foundations,
    Themes,
    Components,
    Accessibility,
}

internal data class CatalogCopy(
    val locale: CatalogLocale,
    val designSystem: String,
    val explore: String,
    val overview: String,
    val foundations: String,
    val themes: String,
    val components: String,
    val accessibility: String,
    val guidelines: String,
    val light: String,
    val dark: String,
    val beez: String,
    val testBrand: String,
    val appearance: String,
    val brandMapping: String,
    val overviewTitle: String,
    val overviewBody: String,
    val principlesTitle: String,
    val principlesBody: String,
    val foundationsTitle: String,
    val themesTitle: String,
    val componentsTitle: String,
    val accessibilityTitle: String,
    val actionButton: String,
    val checkbox: String,
    val text: String,
    val textField: String,
    val surface: String,
    val componentOverviewBody: String,
    val componentDetails: String,
    val backToComponents: String,
    val anatomy: String,
    val properties: String,
    val guidelinesTitle: String,
    val accessibilityGuide: String,
    val email: String,
    val placeholder: String,
    val supporting: String,
    val error: String,
    val readOnly: String,
    val disabled: String,
    val reset: String,
    val ready: String,
    val experimental: String,
)

private fun copyFor(locale: CatalogLocale): CatalogCopy = when (locale) {
    CatalogLocale.Korean -> CatalogCopy(
        locale = CatalogLocale.Korean,
        designSystem = "디자인 시스템",
        explore = "탐색",
        overview = "Overview",
        foundations = "Foundations",
        themes = "Themes",
        components = "Components",
        accessibility = "Accessibility",
        guidelines = "가이드라인",
        light = "라이트",
        dark = "다크",
        beez = "BEEZ",
        testBrand = "테스트 브랜드",
        appearance = "화면 모드",
        brandMapping = "브랜드 매핑",
        overviewTitle = "Design with meaning.",
        overviewBody = "토큰을 중심으로 설계된 테마형 컴포넌트 언어입니다.",
        principlesTitle = "분명한 관점을 가진 시스템.",
        principlesBody = "값보다 의미를 우선하고, 하나의 공통 계약으로 브랜드를 확장합니다.",
        foundationsTitle = "작은 결정을 일관되게.",
        themesTitle = "나만의 것으로 만드세요.",
        componentsTitle = "하나의 계약, 다양한 맥락.",
        accessibilityTitle = "품질은 형태의 일부입니다.",
        actionButton = "Action Button",
        checkbox = "Checkbox",
        text = "Text",
        textField = "Text Field",
        surface = "Surface",
        componentOverviewBody = "컴포넌트 카드를 선택해 실제 동작, 속성, 사용 가이드와 접근성 계약을 확인하세요.",
        componentDetails = "컴포넌트 상세",
        backToComponents = "컴포넌트 목록",
        anatomy = "구조",
        properties = "속성",
        guidelinesTitle = "사용 가이드",
        accessibilityGuide = "접근성",
        email = "이메일 주소",
        placeholder = "name@example.com",
        supporting = "확인할 수 있는 주소를 입력하세요.",
        error = "오류 표시",
        readOnly = "읽기 전용",
        disabled = "비활성화",
        reset = "초기화",
        ready = "준비됨 · 값을 입력하세요",
        experimental = "실험적",
    )

    CatalogLocale.English -> CatalogCopy(
        locale = CatalogLocale.English,
        designSystem = "Design System",
        explore = "Explore",
        overview = "Overview",
        foundations = "Foundations",
        themes = "Themes",
        components = "Components",
        accessibility = "Accessibility",
        guidelines = "Guidelines",
        light = "Light",
        dark = "Dark",
        beez = "BEEZ",
        testBrand = "Test Brand",
        appearance = "Appearance",
        brandMapping = "Brand mapping",
        overviewTitle = "Design with meaning.",
        overviewBody = "A token-first, themeable component language for products that feel unmistakably their own.",
        principlesTitle = "A system with a point of view.",
        principlesBody = "Meaning over raw values, one shared contract, and room for each brand to make it theirs.",
        foundationsTitle = "Small decisions, repeated well.",
        themesTitle = "Make it yours.",
        componentsTitle = "One contract, many contexts.",
        accessibilityTitle = "Quality is part of the shape.",
        actionButton = "Action Button",
        checkbox = "Checkbox",
        text = "Text",
        textField = "Text Field",
        surface = "Surface",
        componentOverviewBody = "Choose a component card to inspect its behavior, properties, usage guidance, and accessibility contract.",
        componentDetails = "Component details",
        backToComponents = "All components",
        anatomy = "Anatomy",
        properties = "Properties",
        guidelinesTitle = "Guidelines",
        accessibilityGuide = "Accessibility",
        email = "Email address",
        placeholder = "name@example.com",
        supporting = "Use an address you can access.",
        error = "Show error",
        readOnly = "Read only",
        disabled = "Disable",
        reset = "Reset",
        ready = "Ready · enter a value",
        experimental = "Experimental",
    )
}

/** The Compose Multiplatform Showcase application. */
@OptIn(ExperimentalResourceApi::class)
@Composable
public fun CatalogApp(initialLocale: CatalogLocale = defaultCatalogLocale()) {
    var locale by remember { mutableStateOf(initialLocale) }
    var appearance by remember { mutableStateOf(CatalogAppearance.Light) }
    var brand by remember { mutableStateOf(CatalogBrand.Beez) }
    var section by remember { mutableStateOf(CatalogSection.Overview) }
    var component by remember { mutableStateOf<CatalogComponent?>(null) }
    val copy = copyFor(locale)
    val scheme = catalogScheme(appearance, brand)

    BeezTheme(scheme = scheme) {
        Row(modifier = Modifier.fillMaxSize().background(BeezTheme.colors.backgroundNeutral)) {
            CatalogSidebar(
                copy = copy,
                selected = section,
                onSelect = {
                    section = it
                    component = null
                },
            )

            Column(modifier = Modifier.fillMaxSize()) {
                CatalogTopBar(
                    copy = copy,
                    locale = locale,
                    appearance = appearance,
                    onLocaleChange = { locale = it },
                    onToggleAppearance = {
                        appearance = if (appearance == CatalogAppearance.Light) {
                            CatalogAppearance.Dark
                        } else {
                            CatalogAppearance.Light
                        }
                    },
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(BeezTheme.spacing.screenGutter),
                    verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.screenSectionGap),
                ) {
                    when (section) {
                        CatalogSection.Overview -> OverviewSection(copy)
                        CatalogSection.Foundations -> FoundationsSection(copy)
                        CatalogSection.Themes -> ThemesSection(
                            copy = copy,
                            appearance = appearance,
                            brand = brand,
                            onAppearanceChange = { appearance = it },
                            onBrandChange = { brand = it },
                        )
                        CatalogSection.Components -> ComponentsSection(
                            copy = copy,
                            selected = component,
                            onSelect = { component = it },
                            onBack = { component = null },
                        )
                        CatalogSection.Accessibility -> AccessibilitySection(copy)
                    }
                }
            }
        }
    }
}

@Composable
private fun CatalogSidebar(
    copy: CatalogCopy,
    selected: CatalogSection,
    onSelect: (CatalogSection) -> Unit,
) {
    Column(
        modifier = Modifier
            .width(240.dp)
            .fillMaxHeight()
            .background(BeezTheme.colors.backgroundBrand)
            .padding(BeezTheme.spacing.screenGutter),
        verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentStackGap),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(BeezTheme.shapes.controlRadius))
                    .background(BeezTheme.colors.foregroundOnBrand),
                contentAlignment = Alignment.Center,
            ) {
                BasicText(
                    text = "B",
                    style = BeezTheme.typography.sectionTitle.copy(color = BeezTheme.colors.backgroundBrand),
                )
            }
            Spacer(modifier = Modifier.width(BeezTheme.spacing.contentInlineGap))
            Column {
                BasicText(
                    text = "BEEZ",
                    style = BeezTheme.typography.sectionTitle.copy(color = BeezTheme.colors.foregroundOnBrand),
                )
                BasicText(
                    text = copy.designSystem,
                    style = BeezTheme.typography.caption.copy(color = BeezTheme.colors.foregroundOnBrand),
                )
            }
        }

        Spacer(modifier = Modifier.height(BeezTheme.spacing.contentStackGap))
        BasicText(
            text = copy.explore,
            style = BeezTheme.typography.caption.copy(color = BeezTheme.colors.foregroundOnBrand),
        )

        CatalogSection.entries.forEach { item ->
            CatalogChoice(
                label = sectionLabel(item, copy),
                selected = item == selected,
                onClick = { onSelect(item) },
                selectedColor = BeezTheme.colors.foregroundOnBrand,
                contentColor = BeezTheme.colors.foregroundOnBrand,
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        BasicText(
            text = "Experimental · 0.1.0",
            style = BeezTheme.typography.caption.copy(color = BeezTheme.colors.foregroundOnBrand),
        )
    }
}

@Composable
private fun CatalogTopBar(
    copy: CatalogCopy,
    locale: CatalogLocale,
    appearance: CatalogAppearance,
    onLocaleChange: (CatalogLocale) -> Unit,
    onToggleAppearance: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = BeezTheme.spacing.screenGutter,
                vertical = BeezTheme.spacing.contentInlineGap,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap),
    ) {
        BasicText(
            text = "BEEZ / Showcase",
            style = BeezTheme.typography.label.copy(color = BeezTheme.colors.foregroundSecondary),
            modifier = Modifier.weight(1f),
        )
        CatalogChoice(
            label = "한국어",
            selected = locale == CatalogLocale.Korean,
            onClick = { onLocaleChange(CatalogLocale.Korean) },
            selectedColor = BeezTheme.colors.backgroundBrand,
            contentColor = BeezTheme.colors.foregroundPrimary,
        )
        CatalogChoice(
            label = "English",
            selected = locale == CatalogLocale.English,
            onClick = { onLocaleChange(CatalogLocale.English) },
            selectedColor = BeezTheme.colors.backgroundBrand,
            contentColor = BeezTheme.colors.foregroundPrimary,
        )
        BeezActionButton(
            label = if (appearance == CatalogAppearance.Light) copy.light else copy.dark,
            onClick = onToggleAppearance,
            variant = BeezActionButtonVariant.Outline,
            size = beez.design.components.BeezActionButtonSize.Small,
        )
    }
}

@Composable
internal fun CatalogChoice(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    selectedColor: Color,
    contentColor: Color,
) {
    BasicText(
        text = label,
        style = BeezTheme.typography.label.copy(
            color = if (selected) selectedColor else contentColor,
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(BeezTheme.shapes.controlRadius))
            .background(if (selected) selectedColor.copy(alpha = 0.14f) else Color.Transparent)
            .border(
                width = if (selected) 1.dp else 0.dp,
                color = if (selected) selectedColor else Color.Transparent,
                shape = RoundedCornerShape(BeezTheme.shapes.controlRadius),
            )
            .clickable(onClick = onClick)
            .semantics { role = Role.Button }
            .padding(
                horizontal = BeezTheme.spacing.controlCompactHorizontalInset,
                vertical = BeezTheme.spacing.controlCompactVerticalInset,
            ),
    )
}

@Composable
private fun OverviewSection(copy: CatalogCopy) {
    CatalogSectionHeader(
        eyebrow = "01 / OVERVIEW",
        title = copy.overviewTitle,
        body = copy.overviewBody,
    )
    CatalogCard(
        title = copy.principlesTitle,
        body = copy.principlesBody,
    )
}

@Composable
private fun FoundationsSection(copy: CatalogCopy) {
    CatalogSectionHeader(
        eyebrow = "02 / FOUNDATIONS",
        title = copy.foundationsTitle,
        body = "Semantic tokens turn design intent into a vocabulary components can share.",
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentStackGap),
    ) {
        CatalogTokenCard(
            title = "Color",
            value = "background.brand",
            preview = BeezTheme.colors.backgroundBrand,
            modifier = Modifier.weight(1f),
        )
        CatalogTokenCard(
            title = "Typography",
            value = "screenTitle · ${BeezTheme.typography.screenTitle.fontSize}",
            preview = BeezTheme.colors.foregroundPrimary,
            modifier = Modifier.weight(1f),
        )
        CatalogTokenCard(
            title = "Spacing",
            value = "content.stackGap · ${BeezTheme.spacing.contentStackGap}",
            preview = BeezTheme.colors.strokeFocus,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun ThemesSection(
    copy: CatalogCopy,
    appearance: CatalogAppearance,
    brand: CatalogBrand,
    onAppearanceChange: (CatalogAppearance) -> Unit,
    onBrandChange: (CatalogBrand) -> Unit,
) {
    CatalogSectionHeader(
        eyebrow = "03 / THEME LAB",
        title = copy.themesTitle,
        body = "Change the semantic mapping. The component contract stays the same.",
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.screenSectionGap),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentStackGap),
        ) {
            BasicText(text = copy.appearance, style = BeezTheme.typography.label)
            Row(horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap)) {
                CatalogChoice("${copy.light}", appearance == CatalogAppearance.Light, { onAppearanceChange(CatalogAppearance.Light) }, BeezTheme.colors.backgroundBrand, BeezTheme.colors.foregroundPrimary)
                CatalogChoice("${copy.dark}", appearance == CatalogAppearance.Dark, { onAppearanceChange(CatalogAppearance.Dark) }, BeezTheme.colors.backgroundBrand, BeezTheme.colors.foregroundPrimary)
            }
            BasicText(text = copy.brandMapping, style = BeezTheme.typography.label)
            Row(horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap)) {
                CatalogChoice(copy.beez, brand == CatalogBrand.Beez, { onBrandChange(CatalogBrand.Beez) }, BeezTheme.colors.backgroundBrand, BeezTheme.colors.foregroundPrimary)
                CatalogChoice(copy.testBrand, brand == CatalogBrand.Test, { onBrandChange(CatalogBrand.Test) }, BeezTheme.colors.backgroundBrand, BeezTheme.colors.foregroundPrimary)
            }
        }
        CatalogCard(
            title = "${if (brand == CatalogBrand.Beez) copy.beez else copy.testBrand} · ${if (appearance == CatalogAppearance.Light) copy.light else copy.dark}",
            body = "background.brand",
            accent = BeezTheme.colors.backgroundBrand,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun AccessibilitySection(copy: CatalogCopy) {
    CatalogSectionHeader(
        eyebrow = "05 / ACCESSIBILITY",
        title = copy.accessibilityTitle,
        body = "Accessibility is part of every component contract, not a later audit.",
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentStackGap),
    ) {
        CatalogCard("48dp target", "Visual size and interaction area stay distinct.", Modifier.weight(1f))
        CatalogCard("Focus visible", "Keyboard and assistive focus has a clear ring.", Modifier.weight(1f))
        CatalogCard("State semantics", "Disabled, loading and error states are exposed to assistive technology.", Modifier.weight(1f))
    }
}

@Composable
internal fun CatalogSectionHeader(eyebrow: String, title: String, body: String) {
    Column(verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap)) {
        BasicText(text = eyebrow, style = BeezTheme.typography.caption.copy(color = BeezTheme.colors.foregroundSecondary))
        BasicText(text = title, style = BeezTheme.typography.display.copy(color = BeezTheme.colors.foregroundPrimary))
        BasicText(text = body, style = BeezTheme.typography.body.copy(color = BeezTheme.colors.foregroundSecondary))
    }
}

@Composable
internal fun CatalogCard(
    title: String,
    body: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    accent: Color = BeezTheme.colors.backgroundBrand,
    content: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(BeezTheme.shapes.containerRadius))
            .background(BeezTheme.colors.backgroundNeutral)
            .border(1.dp, BeezTheme.colors.strokeNeutral, RoundedCornerShape(BeezTheme.shapes.containerRadius))
            .padding(BeezTheme.spacing.screenGutter),
        verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentStackGap),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(BeezTheme.shapes.roundRadius))
                .background(accent),
        )
        BasicText(text = title, style = BeezTheme.typography.sectionTitle)
        BasicText(text = body, style = BeezTheme.typography.body)
        content?.invoke()
    }
}

@Composable
private fun CatalogTokenCard(title: String, value: String, preview: Color, modifier: Modifier) {
    CatalogCard(title = title, body = value, accent = preview, modifier = modifier)
}

private fun sectionLabel(section: CatalogSection, copy: CatalogCopy): String = when (section) {
    CatalogSection.Overview -> copy.overview
    CatalogSection.Foundations -> copy.foundations
    CatalogSection.Themes -> copy.themes
    CatalogSection.Components -> copy.components
    CatalogSection.Accessibility -> copy.accessibility
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun catalogScheme(appearance: CatalogAppearance, brand: CatalogBrand): BeezTokenScheme {
    val base = if (appearance == CatalogAppearance.Light) BeezTokenSchemes.light else BeezTokenSchemes.dark
    val fontFamily = FontFamily(Font(Res.font.noto_sans_kr_catalog))
    val typography = base.typography.copy(
        display = base.typography.display.copy(fontFamily = fontFamily),
        screenTitle = base.typography.screenTitle.copy(fontFamily = fontFamily),
        sectionTitle = base.typography.sectionTitle.copy(fontFamily = fontFamily),
        body = base.typography.body.copy(fontFamily = fontFamily),
        label = base.typography.label.copy(fontFamily = fontFamily),
        caption = base.typography.caption.copy(fontFamily = fontFamily),
    )
    val themedBase = base.copy(typography = typography)
    if (brand == CatalogBrand.Beez) return themedBase

    return themedBase.withCatalogTestBrand()
}
