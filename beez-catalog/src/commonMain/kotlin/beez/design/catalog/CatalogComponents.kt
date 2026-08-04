package beez.design.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import beez.design.components.BeezActionButton
import beez.design.components.BeezActionButtonSize
import beez.design.components.BeezActionButtonVariant
import beez.design.components.BeezCheckbox
import beez.design.components.BeezSurface
import beez.design.components.BeezSurfaceElevation
import beez.design.components.BeezTextField
import beez.design.foundation.BeezTheme

internal enum class CatalogComponent {
    ActionButton,
    Checkbox,
    TextField,
    Surface,
}

@Composable
internal fun ComponentsSection(
    copy: CatalogCopy,
    selected: CatalogComponent?,
    onSelect: (CatalogComponent) -> Unit,
    onBack: () -> Unit,
) {
    if (selected == null) {
        ComponentOverview(copy = copy, onSelect = onSelect)
    } else {
        ComponentDetail(copy = copy, component = selected, onBack = onBack)
    }
}

@Composable
private fun ComponentOverview(
    copy: CatalogCopy,
    onSelect: (CatalogComponent) -> Unit,
) {
    CatalogSectionHeader(
        eyebrow = "04 / COMPONENTS",
        title = copy.componentsTitle,
        body = copy.componentOverviewBody,
    )

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        if (maxWidth < 720.dp) {
            Column(verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentStackGap)) {
                CatalogComponent.entries.forEach { component ->
                    CatalogComponentCard(
                        component = component,
                        copy = copy,
                        onClick = { onSelect(component) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentStackGap)) {
                CatalogComponent.entries.chunked(2).forEach { components ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentStackGap),
                    ) {
                        components.forEach { component ->
                            CatalogComponentCard(
                                component = component,
                                copy = copy,
                                onClick = { onSelect(component) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        if (components.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CatalogComponentCard(
    component: CatalogComponent,
    copy: CatalogCopy,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(BeezTheme.shapes.containerRadius)
    Column(
        modifier = modifier
            .clip(shape)
            .background(BeezTheme.colors.backgroundNeutral)
            .border(1.dp, BeezTheme.colors.strokeNeutral, shape)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics(mergeDescendants = true) { role = Role.Button },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(176.dp)
                .background(BeezTheme.colors.strokeNeutral.copy(alpha = 0.16f))
                .clearAndSetSemantics { }
                .padding(BeezTheme.spacing.screenGutter),
            contentAlignment = Alignment.Center,
        ) {
            ComponentCardPreview(component = component, copy = copy)
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(onClick = onClick)
                    .clearAndSetSemantics { },
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 144.dp)
                .padding(BeezTheme.spacing.screenGutter),
            verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap),
            ) {
                BasicText(
                    text = componentTitle(component, copy),
                    style = BeezTheme.typography.sectionTitle.copy(color = BeezTheme.colors.foregroundPrimary),
                    modifier = Modifier.weight(1f),
                )
                CatalogMaturityBadge(label = copy.experimental)
            }
            BasicText(
                text = componentSummary(component, copy.locale),
                style = BeezTheme.typography.body.copy(color = BeezTheme.colors.foregroundSecondary),
            )
            Spacer(modifier = Modifier.weight(1f))
            BasicText(
                text = localized(copy.locale, "가이드 열기 →", "Open guide →"),
                style = BeezTheme.typography.label.copy(color = BeezTheme.colors.foregroundPrimary),
            )
        }
    }
}

@Composable
private fun ComponentCardPreview(component: CatalogComponent, copy: CatalogCopy) {
    when (component) {
        CatalogComponent.ActionButton -> BeezActionButton(
            label = if (copy.locale == CatalogLocale.Korean) "계속하기" else "Continue",
            onClick = {},
        )

        CatalogComponent.Checkbox -> BeezCheckbox(
            checked = true,
            onCheckedChange = {},
            label = if (copy.locale == CatalogLocale.Korean) "업데이트 받기" else "Receive updates",
        )

        CatalogComponent.TextField -> BeezTextField(
            value = "beez.design",
            onValueChange = {},
            label = copy.email,
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
        )

        CatalogComponent.Surface -> BeezSurface(
            elevation = BeezSurfaceElevation.Floating,
            modifier = Modifier.fillMaxWidth().widthIn(max = 280.dp),
        ) {
            BasicText(
                text = if (copy.locale == CatalogLocale.Korean) "관련 콘텐츠 영역" else "Related content area",
                style = BeezTheme.typography.body.copy(color = BeezTheme.colors.foregroundPrimary),
                modifier = Modifier.padding(BeezTheme.spacing.contentStackGap),
            )
        }
    }
}

@Composable
private fun ComponentDetail(
    copy: CatalogCopy,
    component: CatalogComponent,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.screenSectionGap),
    ) {
        BeezActionButton(
            label = "← ${copy.backToComponents}",
            onClick = onBack,
            variant = BeezActionButtonVariant.Outline,
            size = BeezActionButtonSize.Small,
        )
        ComponentDetailHeader(copy = copy, component = component)

        when (component) {
            CatalogComponent.ActionButton -> ActionButtonDetail(copy)
            CatalogComponent.Checkbox -> CheckboxDetail(copy)
            CatalogComponent.TextField -> TextFieldDetail(copy)
            CatalogComponent.Surface -> SurfaceDetail(copy)
        }
    }
}

@Composable
private fun ComponentDetailHeader(copy: CatalogCopy, component: CatalogComponent) {
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(BeezTheme.shapes.containerRadius)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(BeezTheme.colors.backgroundNeutral)
            .border(1.dp, BeezTheme.colors.strokeNeutral, shape)
            .padding(BeezTheme.spacing.screenGutter),
        verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap),
    ) {
        BasicText(
            text = "${copy.components} / ${copy.componentDetails}",
            style = BeezTheme.typography.caption.copy(color = BeezTheme.colors.foregroundSecondary),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentStackGap),
        ) {
            BasicText(
                text = componentTitle(component, copy),
                style = BeezTheme.typography.display.copy(color = BeezTheme.colors.foregroundPrimary),
                modifier = Modifier.weight(1f),
            )
            CatalogMaturityBadge(label = copy.experimental)
        }
        BasicText(
            text = componentSummary(component, copy.locale),
            style = BeezTheme.typography.body.copy(color = BeezTheme.colors.foregroundSecondary),
        )
        BasicText(
            text = localized(
                copy.locale,
                "Playground · 구조 · 속성 · 사용 가이드 · 접근성 · API",
                "Playground · Anatomy · Properties · Guidelines · Accessibility · API",
            ),
            style = BeezTheme.typography.caption.copy(color = BeezTheme.colors.foregroundSecondary),
        )
    }
}

@Composable
private fun CatalogMaturityBadge(label: String) {
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(BeezTheme.shapes.roundRadius)
    BasicText(
        text = label,
        style = BeezTheme.typography.caption.copy(color = BeezTheme.colors.foregroundPrimary),
        modifier = Modifier
            .clip(shape)
            .background(BeezTheme.colors.strokeNeutral.copy(alpha = 0.16f))
            .border(1.dp, BeezTheme.colors.strokeNeutral, shape)
            .padding(
                horizontal = BeezTheme.spacing.controlContentGap,
                vertical = BeezTheme.spacing.controlCompactVerticalInset,
            ),
    )
}

@Composable
private fun ActionButtonDetail(copy: CatalogCopy) {
    var clicks by remember { mutableStateOf(0) }
    var disabled by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    CatalogCard(title = "Playground", body = playgroundDescription(copy.locale)) {
        BasicText(text = "Playground · Clicks: $clicks", style = BeezTheme.typography.label)
        BeezActionButton(
            label = "Continue",
            onClick = { clicks += 1 },
            enabled = !disabled,
            loading = loading,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap)) {
            CatalogChoice("Button disabled", disabled, { disabled = !disabled }, BeezTheme.colors.backgroundBrand, BeezTheme.colors.foregroundPrimary)
            CatalogChoice("Button loading", loading, { loading = !loading }, BeezTheme.colors.backgroundBrand, BeezTheme.colors.foregroundPrimary)
            CatalogChoice("Button reset", false, {
                clicks = 0
                disabled = false
                loading = false
            }, BeezTheme.colors.backgroundBrand, BeezTheme.colors.foregroundPrimary)
        }
    }

    CatalogCard(
        title = copy.anatomy,
        body = localized(copy.locale, "Container와 필수 label, 선택적인 leading/trailing content로 구성됩니다.", "A container owns the action while the required label and optional leading or trailing content communicate its purpose."),
    )
    CatalogCard(title = copy.properties, body = localized(copy.locale, "Variant, size와 state 조합을 비교합니다.", "Compare semantic variants, sizes, and states.")) {
        BasicText(text = "Variants", style = BeezTheme.typography.label)
        Row(horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap)) {
            BeezActionButton(label = "Brand solid", onClick = {}, modifier = Modifier.weight(1f))
            BeezActionButton(label = "Neutral", onClick = {}, variant = BeezActionButtonVariant.Neutral, modifier = Modifier.weight(1f))
            BeezActionButton(label = "Outline", onClick = {}, variant = BeezActionButtonVariant.Outline, modifier = Modifier.weight(1f))
        }
        BasicText(text = "Sizes", style = BeezTheme.typography.label)
        Row(horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap)) {
            BeezActionButton(label = "Small", onClick = {}, size = BeezActionButtonSize.Small, modifier = Modifier.weight(1f))
            BeezActionButton(label = "Medium", onClick = {}, size = BeezActionButtonSize.Medium, modifier = Modifier.weight(1f))
            BeezActionButton(label = "Large", onClick = {}, size = BeezActionButtonSize.Large, modifier = Modifier.weight(1f))
        }
        BasicText(text = "States", style = BeezTheme.typography.label)
        Row(horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap)) {
            BeezActionButton(label = "Enabled", onClick = {}, modifier = Modifier.weight(1f))
            BeezActionButton(label = "Disabled", onClick = {}, enabled = false, modifier = Modifier.weight(1f))
            BeezActionButton(label = "Loading", onClick = {}, loading = true, modifier = Modifier.weight(1f))
        }
    }
    CatalogCard(title = copy.guidelinesTitle, body = localized(copy.locale, "가장 중요한 action에는 Brand Solid를 제한적으로 사용하고 label은 결과를 예측할 수 있는 동사형으로 작성합니다.", "Reserve Brand Solid for the most important action and use a concise verb-led label with a predictable result.")) {
        BasicText(text = "Long label · RTL", style = BeezTheme.typography.label)
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            BeezActionButton(label = "متابعة إلى الخطوة التالية", onClick = {}, modifier = Modifier.fillMaxWidth())
        }
    }
    CatalogCard(title = copy.accessibilityGuide, body = localized(copy.locale, "Button role, 접근성 이름, disabled/loading 상태와 최소 48dp interaction target을 제공합니다.", "Provides a button role, accessible name, disabled/loading state, and a minimum 48dp interaction target."))
}

@Composable
private fun CheckboxDetail(copy: CatalogCopy) {
    var checked by remember { mutableStateOf(false) }
    var disabled by remember { mutableStateOf(false) }

    CatalogCard(title = "Playground", body = playgroundDescription(copy.locale)) {
        BasicText(text = "Playground · Checked: $checked", style = BeezTheme.typography.label)
        BeezCheckbox(
            checked = checked,
            onCheckedChange = { checked = it },
            label = "Receive product updates",
            enabled = !disabled,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap)) {
            CatalogChoice("Checkbox disabled", disabled, { disabled = !disabled }, BeezTheme.colors.backgroundBrand, BeezTheme.colors.foregroundPrimary)
            CatalogChoice("Checkbox reset", false, {
                checked = false
                disabled = false
            }, BeezTheme.colors.backgroundBrand, BeezTheme.colors.foregroundPrimary)
        }
    }
    CatalogCard(title = copy.anatomy, body = localized(copy.locale, "하나의 toggle root가 indicator와 필수 label을 묶습니다.", "One toggle root groups the indicator and required label."))
    CatalogCard(title = copy.properties, body = localized(copy.locale, "Binary checked 상태와 enabled 상태를 호출자가 소유합니다.", "The caller owns the binary checked value and enabled state.")) {
        BasicText(text = "States", style = BeezTheme.typography.label)
        BeezCheckbox(checked = false, onCheckedChange = {}, label = "Unchecked option")
        BeezCheckbox(checked = true, onCheckedChange = {}, label = "Checked option")
        BeezCheckbox(checked = false, onCheckedChange = {}, label = "Disabled unchecked option", enabled = false)
        BeezCheckbox(checked = true, onCheckedChange = {}, label = "Disabled checked option", enabled = false)
    }
    CatalogCard(title = copy.guidelinesTitle, body = localized(copy.locale, "서로 독립적인 option에 사용하고, 즉시 실행되는 action이나 상호 배타적 선택에는 사용하지 않습니다.", "Use for independent options, not immediate actions or mutually exclusive choices.")) {
        BasicText(text = "Long label · RTL", style = BeezTheme.typography.label)
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Box(modifier = Modifier.fillMaxWidth().widthIn(max = 320.dp)) {
                BeezCheckbox(
                    checked = true,
                    onCheckedChange = {},
                    label = "أوافق على تلقي تحديثات مفصلة حول هذا الخيار",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
    CatalogCard(title = copy.accessibilityGuide, body = localized(copy.locale, "Indicator와 label을 하나의 checkbox node로 병합하고 checked/disabled 상태와 전체 label click target을 제공합니다.", "Merges indicator and label into one checkbox node with checked/disabled state and a full-label activation target."))
}

@Composable
private fun TextFieldDetail(copy: CatalogCopy) {
    var email by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }
    var readOnly by remember { mutableStateOf(false) }
    var disabled by remember { mutableStateOf(false) }

    CatalogCard(title = "Playground", body = playgroundDescription(copy.locale)) {
        BeezTextField(
            value = email,
            onValueChange = { email = it },
            label = copy.email,
            placeholder = copy.placeholder,
            supportingText = if (error) "Please enter a valid email address." else copy.supporting,
            enabled = !disabled,
            readOnly = readOnly,
            isError = error,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap)) {
            CatalogChoice(copy.error, error, { error = !error }, BeezTheme.colors.backgroundCritical, BeezTheme.colors.foregroundPrimary)
            CatalogChoice(copy.readOnly, readOnly, { readOnly = !readOnly }, BeezTheme.colors.backgroundBrand, BeezTheme.colors.foregroundPrimary)
            CatalogChoice(copy.disabled, disabled, { disabled = !disabled }, BeezTheme.colors.backgroundBrand, BeezTheme.colors.foregroundPrimary)
            CatalogChoice(copy.reset, false, {
                email = ""
                error = false
                readOnly = false
                disabled = false
            }, BeezTheme.colors.backgroundBrand, BeezTheme.colors.foregroundPrimary)
        }
        BasicText(text = if (email.isEmpty()) copy.ready else "Value changed", style = BeezTheme.typography.caption)
    }
    CatalogCard(title = copy.anatomy, body = localized(copy.locale, "필수 label, 단일 행 input, 선택적인 placeholder/supporting text와 leading/trailing content로 구성됩니다.", "A required label and single-line input can be supported by placeholder, supporting text, and leading or trailing content."))
    CatalogCard(title = copy.properties, body = localized(copy.locale, "Empty, filled, read-only, disabled와 error 상태를 비교합니다.", "Compare empty, filled, read-only, disabled, and error states.")) {
        BeezTextField(value = "", onValueChange = {}, label = "Empty field", placeholder = "Placeholder", supportingText = "Supporting text", modifier = Modifier.fillMaxWidth())
        BeezTextField(value = "Filled value", onValueChange = {}, label = "Filled field", modifier = Modifier.fillMaxWidth())
        BeezTextField(value = "Read-only value", onValueChange = {}, label = "Read-only field", readOnly = true, modifier = Modifier.fillMaxWidth())
        BeezTextField(value = "Disabled value", onValueChange = {}, label = "Disabled field", enabled = false, modifier = Modifier.fillMaxWidth())
        BeezTextField(value = "Invalid value", onValueChange = {}, label = "Error field", supportingText = "Resolve this error before continuing.", isError = true, modifier = Modifier.fillMaxWidth())
        BasicText(text = "Leading · trailing slots", style = BeezTheme.typography.label)
        BeezTextField(
            value = "account",
            onValueChange = {},
            label = "Slotted field",
            leadingContent = { BasicText("@", style = BeezTheme.typography.body) },
            trailingContent = { BasicText("✓", style = BeezTheme.typography.body) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
    CatalogCard(title = copy.guidelinesTitle, body = localized(copy.locale, "Label은 입력 목적을 유지해서 설명하고 placeholder만으로 필드 의미를 전달하지 않습니다.", "Keep a persistent label that explains the input purpose; do not rely on placeholder text alone.")) {
        BasicText(text = "Long content · RTL", style = BeezTheme.typography.label)
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Box(modifier = Modifier.fillMaxWidth().widthIn(max = 320.dp)) {
                BeezTextField(
                    value = "قيمة طويلة للاختبار",
                    onValueChange = {},
                    label = "عنوان حقل طويل للتحقق من التخطيط",
                    supportingText = "نص مساعد يظل مرئيًا في العرض الضيق",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
    CatalogCard(title = copy.accessibilityGuide, body = localized(copy.locale, "Label을 접근성 이름으로 제공하고 disabled와 error 상태를 semantics에 전달합니다. 실제 IME와 clipboard 검증은 플랫폼별로 남아 있습니다.", "Uses the label as the accessible name and exposes disabled and error state. Platform IME and clipboard verification remains pending."))
}

@Composable
private fun SurfaceDetail(copy: CatalogCopy) {
    var elevation by remember { mutableStateOf(BeezSurfaceElevation.Raised) }

    CatalogCard(title = "Playground", body = playgroundDescription(copy.locale)) {
        Row(horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap)) {
            BeezSurfaceElevation.entries.forEach { option ->
                CatalogChoice(
                    label = "Elevation ${option.name}",
                    selected = elevation == option,
                    onClick = { elevation = option },
                    selectedColor = BeezTheme.colors.backgroundBrand,
                    contentColor = BeezTheme.colors.foregroundPrimary,
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BeezTheme.colors.backgroundBrand.copy(alpha = 0.14f))
                .padding(BeezTheme.spacing.screenGutter),
        ) {
            BeezSurface(
                elevation = elevation,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(BeezTheme.spacing.screenGutter),
                    verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap),
                ) {
                    BasicText(text = "${elevation.name} Surface", style = BeezTheme.typography.sectionTitle.copy(color = BeezTheme.colors.foregroundPrimary))
                    BasicText(text = localized(copy.locale, "관련 콘텐츠를 하나의 중립 영역으로 묶습니다.", "Groups related content in one neutral region."), style = BeezTheme.typography.body.copy(color = BeezTheme.colors.foregroundSecondary))
                }
            }
        }
    }
    CatalogCard(title = copy.anatomy, body = localized(copy.locale, "Root가 neutral background, container shape와 elevation을 제공하고 content semantics는 그대로 유지합니다.", "The root provides a neutral background, container shape, and elevation while preserving content semantics."))
    CatalogCard(title = copy.properties, body = localized(copy.locale, "Flat, Raised와 Floating는 깊이 관계만 표현하며 interaction state가 아닙니다.", "Flat, Raised, and Floating express depth only; they are not interaction states.")) {
        Row(horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentStackGap)) {
            BeezSurfaceElevation.entries.forEach { option ->
                BeezSurface(elevation = option, modifier = Modifier.weight(1f)) {
                    BasicText(
                        text = option.name,
                        style = BeezTheme.typography.label.copy(color = BeezTheme.colors.foregroundPrimary),
                        modifier = Modifier.padding(BeezTheme.spacing.contentStackGap),
                    )
                }
            }
        }
    }
    CatalogCard(title = copy.guidelinesTitle, body = localized(copy.locale, "관련 content를 묶는 비대화형 container로 사용합니다. 전체 영역이 action이면 목적에 맞는 interactive pattern을 사용합니다.", "Use it as a non-interactive container for related content. If the whole region is an action, use a purpose-built interactive pattern.")) {
        BasicText(text = "Long content · RTL", style = BeezTheme.typography.label)
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            BeezSurface(elevation = BeezSurfaceElevation.Raised, modifier = Modifier.fillMaxWidth()) {
                BasicText(
                    text = "محتوى طويل داخل سطح مشترك يظل مرئيًا",
                    style = BeezTheme.typography.body.copy(color = BeezTheme.colors.foregroundPrimary),
                    modifier = Modifier.padding(BeezTheme.spacing.contentStackGap),
                )
            }
        }
    }
    CatalogCard(title = copy.accessibilityGuide, body = localized(copy.locale, "Surface는 role, name, focus 또는 click action을 추가하거나 자식 semantics를 병합하지 않습니다.", "Surface adds no role, name, focus, or click action and does not merge child semantics."))
}

private fun componentTitle(component: CatalogComponent, copy: CatalogCopy): String = when (component) {
    CatalogComponent.ActionButton -> copy.actionButton
    CatalogComponent.Checkbox -> copy.checkbox
    CatalogComponent.TextField -> copy.textField
    CatalogComponent.Surface -> copy.surface
}

private fun componentSummary(component: CatalogComponent, locale: CatalogLocale): String = when (component) {
    CatalogComponent.ActionButton -> localized(locale, "명확한 action을 실행하는 기본 interaction component입니다.", "A primary interaction component for clear, immediate actions.")
    CatalogComponent.Checkbox -> localized(locale, "서로 독립적인 binary option을 선택하거나 해제합니다.", "Selects or clears an independent binary option.")
    CatalogComponent.TextField -> localized(locale, "Label과 상태 안내를 갖춘 단일 행 text input입니다.", "A single-line text input with a persistent label and state guidance.")
    CatalogComponent.Surface -> localized(locale, "관련 콘텐츠를 shape와 elevation으로 묶는 비대화형 container입니다.", "A non-interactive container that groups related content with shape and elevation.")
}

private fun playgroundDescription(locale: CatalogLocale): String = localized(
    locale,
    "Control을 조작해 실제 commonMain API의 상태 변화를 확인하세요.",
    "Use the controls to inspect state changes in the actual commonMain API.",
)

private fun localized(locale: CatalogLocale, korean: String, english: String): String =
    if (locale == CatalogLocale.Korean) korean else english
