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
import androidx.compose.ui.text.font.FontFamily
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
    var variant by remember { mutableStateOf(BeezActionButtonVariant.BrandSolid) }
    var size by remember { mutableStateOf(BeezActionButtonSize.Medium) }
    var fillWidth by remember { mutableStateOf(false) }

    CatalogGuideSection(
        title = "Playground",
        body = playgroundDescription(copy.locale),
    ) {
        CatalogChoiceGroup(
            title = "variant",
            labels = BeezActionButtonVariant.entries.map { it.name },
            selectedIndex = BeezActionButtonVariant.entries.indexOf(variant),
            onSelect = { variant = BeezActionButtonVariant.entries[it] },
        )
        CatalogChoiceGroup(
            title = "size",
            labels = BeezActionButtonSize.entries.map { it.name },
            selectedIndex = BeezActionButtonSize.entries.indexOf(size),
            onSelect = { size = BeezActionButtonSize.entries[it] },
        )
        CatalogChoiceGroup(
            title = localized(copy.locale, "너비", "width"),
            labels = listOf("Hug", "Fill"),
            selectedIndex = if (fillWidth) 1 else 0,
            onSelect = { fillWidth = it == 1 },
        )
        CatalogChoiceGroup(
            title = localized(copy.locale, "상태", "state"),
            labels = listOf(
                localized(copy.locale, "기본", "Enabled"),
                localized(copy.locale, "비활성", "Disabled"),
                localized(copy.locale, "로딩", "Loading"),
            ),
            selectedIndex = when {
                disabled -> 1
                loading -> 2
                else -> 0
            },
            onSelect = {
                disabled = it == 1
                loading = it == 2
            },
        )
        CatalogExampleCanvas {
            BeezActionButton(
                label = localized(copy.locale, "계속하기", "Continue"),
                onClick = { clicks += 1 },
                variant = variant,
                size = size,
                enabled = !disabled,
                loading = loading,
                modifier = if (fillWidth) Modifier.fillMaxWidth() else Modifier,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap),
        ) {
            BasicText(
                text = localized(copy.locale, "실행 횟수: $clicks", "Clicks: $clicks"),
                style = BeezTheme.typography.caption.copy(color = BeezTheme.colors.foregroundSecondary),
                modifier = Modifier.weight(1f),
            )
            CatalogChoice(
                label = copy.reset,
                selected = false,
                onClick = {
                    clicks = 0
                    disabled = false
                    loading = false
                    variant = BeezActionButtonVariant.BrandSolid
                    size = BeezActionButtonSize.Medium
                    fillWidth = false
                },
                selectedColor = BeezTheme.colors.backgroundBrand,
                contentColor = BeezTheme.colors.foregroundPrimary,
            )
        }
    }

    CatalogGuideSection(
        title = copy.anatomy,
        body = localized(
            copy.locale,
            "하나의 action root 안에서 필수 label과 선택적인 앞·뒤 content가 목적을 전달합니다.",
            "One action root combines a required label with optional leading and trailing content.",
        ),
    ) {
        CatalogDefinitionRow("root", "Required", localized(copy.locale, "전체 layout, focus, click과 button semantics를 소유합니다.", "Owns layout, focus, click, and button semantics."))
        CatalogDefinitionRow("leadingContent", "Optional", localized(copy.locale, "Label 앞에서 action 의미를 보조하는 짧은 visual입니다.", "A short visual before the label that supports the action meaning."))
        CatalogDefinitionRow("label", "Required", localized(copy.locale, "결과를 예측할 수 있는 action 이름이자 기본 접근성 이름입니다.", "The predictable action name and default accessible name."))
        CatalogDefinitionRow("trailingContent", "Optional", localized(copy.locale, "Label 뒤에서 다음 단계나 방향을 보조하는 visual입니다.", "A visual after the label that supports direction or the next step."))
    }

    CatalogGuideSection(
        title = copy.properties,
        body = localized(copy.locale, "Public API에서 선택할 수 있는 축과 안전한 기본값입니다.", "The public API axes and their safe defaults."),
    ) {
        CatalogDefinitionRow("label", "String · required", localized(copy.locale, "Action의 결과를 설명합니다.", "Explains the result of the action."))
        CatalogDefinitionRow("onClick", "() -> Unit · required", localized(copy.locale, "사용자가 실행했을 때 호출됩니다.", "Called when the user activates the action."))
        CatalogDefinitionRow("variant", "BrandSolid · Neutral · Outline", localized(copy.locale, "기본값 BrandSolid. 화면의 강조 위계를 선택합니다.", "Defaults to BrandSolid and selects visual hierarchy."))
        CatalogDefinitionRow("size", "Small · Medium · Large", localized(copy.locale, "기본값 Medium. 높이와 content inset을 선택합니다.", "Defaults to Medium and selects height and content insets."))
        CatalogDefinitionRow("enabled", "Boolean · true", localized(copy.locale, "false이면 action과 callback을 차단합니다.", "When false, blocks the action and callback."))
        CatalogDefinitionRow("loading", "Boolean · false", localized(copy.locale, "true이면 진행 상태를 알리고 중복 실행을 차단합니다.", "When true, exposes progress and blocks duplicate activation."))
        CatalogDefinitionRow("modifier", "Modifier · empty", localized(copy.locale, "Hug가 기본이며 Fill과 constraint는 부모 layout이 명시합니다.", "Hug is the default; the parent explicitly requests Fill or constraints."))
    }

    CatalogGuideSection(
        title = localized(copy.locale, "Variants", "Variants"),
        body = localized(copy.locale, "색상 취향이 아니라 action의 중요도에 따라 선택합니다.", "Choose by action importance, not color preference."),
    ) {
        CatalogExampleGrid(items = BeezActionButtonVariant.entries) { item ->
            val description = when (item) {
                BeezActionButtonVariant.BrandSolid -> localized(copy.locale, "가장 중요한 action 하나", "One highest-priority action")
                BeezActionButtonVariant.Neutral -> localized(copy.locale, "일반 또는 보조 action", "General or supporting actions")
                BeezActionButtonVariant.Outline -> localized(copy.locale, "낮은 강조도의 secondary action", "Low-emphasis secondary actions")
            }
            CatalogExampleTile(title = item.name, body = description) {
                BeezActionButton(
                    label = when (item) {
                        BeezActionButtonVariant.BrandSolid -> localized(copy.locale, "계속하기", "Continue")
                        BeezActionButtonVariant.Neutral -> localized(copy.locale, "나중에", "Not now")
                        BeezActionButtonVariant.Outline -> localized(copy.locale, "취소하기", "Cancel")
                    },
                    onClick = {},
                    variant = item,
                )
            }
        }
    }

    CatalogGuideSection(
        title = localized(copy.locale, "Sizes", "Sizes"),
        body = localized(copy.locale, "화면 크기가 아니라 배치 밀도와 action 역할에 맞춥니다.", "Match layout density and action role rather than window size."),
    ) {
        CatalogExampleGrid(items = BeezActionButtonSize.entries) { item ->
            val description = when (item) {
                BeezActionButtonSize.Small -> localized(copy.locale, "Toolbar와 compact action", "Toolbars and compact actions")
                BeezActionButtonSize.Medium -> localized(copy.locale, "대부분의 기본 action", "Most default actions")
                BeezActionButtonSize.Large -> localized(copy.locale, "주요 CTA와 여유 있는 layout", "Primary CTAs and comfortable layouts")
            }
            CatalogExampleTile(title = item.name, body = description) {
                BeezActionButton(label = localized(copy.locale, "실행하기", "Run action"), onClick = {}, size = item)
            }
        }
    }

    CatalogGuideSection(
        title = localized(copy.locale, "States", "States"),
        body = localized(copy.locale, "상태는 호출자가 소유하며 Disabled와 Loading은 실행을 차단합니다.", "The caller owns state; Disabled and Loading block activation."),
    ) {
        CatalogExampleGrid(items = listOf("Enabled", "Disabled", "Loading")) { item ->
            CatalogExampleTile(
                title = item,
                body = when (item) {
                    "Disabled" -> localized(copy.locale, "현재 실행할 수 없음", "Currently unavailable")
                    "Loading" -> localized(copy.locale, "진행 중이며 중복 실행 차단", "In progress with duplicate activation blocked")
                    else -> localized(copy.locale, "입력과 callback 허용", "Accepts input and invokes the callback")
                },
            ) {
                BeezActionButton(
                    label = localized(copy.locale, "저장하기", "Save"),
                    onClick = {},
                    enabled = item != "Disabled",
                    loading = item == "Loading",
                )
            }
        }
    }

    CatalogGuideSection(
        title = localized(copy.locale, "Width and layout", "Width and layout"),
        body = localized(copy.locale, "Hug가 기본입니다. Fill은 전체 행이 하나의 CTA일 때만 부모가 명시합니다.", "Hug is the default. The parent requests Fill only when one CTA owns the whole row."),
    ) {
        CatalogExampleGrid(items = listOf("Hug", "Fill"), wideColumns = 2) { item ->
            CatalogExampleTile(
                title = item,
                body = if (item == "Hug") {
                    localized(copy.locale, "Toolbar, inline action과 action group", "Toolbars, inline actions, and action groups")
                } else {
                    localized(copy.locale, "Form 제출이나 화면 하단의 주요 CTA", "Form submission or a primary bottom CTA")
                },
            ) {
                BeezActionButton(
                    label = if (item == "Hug") localized(copy.locale, "저장하기", "Save") else localized(copy.locale, "검토 계속하기", "Continue to review"),
                    onClick = {},
                    modifier = if (item == "Fill") Modifier.fillMaxWidth() else Modifier,
                    size = if (item == "Fill") BeezActionButtonSize.Large else BeezActionButtonSize.Medium,
                )
            }
        }
    }

    CatalogGuideSection(
        title = copy.guidelinesTitle,
        body = localized(copy.locale, "강조 위계와 예측 가능한 label을 함께 관리합니다.", "Manage visual hierarchy together with predictable labels."),
    ) {
        CatalogGuidancePair(
            doTitle = localized(copy.locale, "권장", "Do"),
            doBody = localized(copy.locale, "가장 중요한 action에 BrandSolid 하나를 사용하고 ‘저장하기’처럼 결과가 분명한 label을 씁니다.", "Use one BrandSolid action for the highest priority and a predictable label such as ‘Save changes’."),
            dontTitle = localized(copy.locale, "피하기", "Do not"),
            dontBody = localized(copy.locale, "여러 버튼을 모두 Fill이나 BrandSolid로 만들어 중요도를 같게 보이게 하지 않습니다.", "Do not make every button Fill or BrandSolid and flatten their hierarchy."),
        )
        CatalogDefinitionRow(
            name = localized(copy.locale, "긴 label과 RTL", "Long labels and RTL"),
            meta = localized(copy.locale, "줄바꿈 · logical order", "Wrapping · logical order"),
            description = localized(copy.locale, "번역과 확대 글꼴로 공간이 부족하면 action group을 세로로 전환합니다.", "When translation or font scaling exceeds the row, switch the action group to a vertical stack."),
        )
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            CatalogExampleCanvas {
                BeezActionButton(label = "متابعة إلى الخطوة التالية", onClick = {})
            }
        }
    }

    CatalogGuideSection(
        title = copy.accessibilityGuide,
        body = localized(copy.locale, "상태와 실행 가능 여부를 시각 표현뿐 아니라 semantics로 전달합니다.", "Expose state and availability through semantics as well as visuals."),
    ) {
        CatalogDefinitionRow("Role and name", "Button · label", localized(copy.locale, "Root는 button role을 갖고 label을 기본 접근성 이름으로 사용합니다.", "The root has button role and uses the label as its accessible name."))
        CatalogDefinitionRow("Interaction", "Touch · pointer · Enter · Space", localized(copy.locale, "최소 48dp interaction 영역과 keyboard 실행을 제공합니다.", "Provides a minimum 48dp interaction area and keyboard activation."))
        CatalogDefinitionRow("State", "Disabled · Loading", localized(copy.locale, "비활성과 진행 상태를 보조기술에 전달하고 callback을 차단합니다.", "Exposes disabled and progress states and blocks the callback."))
        CatalogDefinitionRow("Content", "Font scale · CJK · RTL", localized(copy.locale, "Label을 임의로 자르지 않고 logical slot 순서를 유지합니다.", "Does not arbitrarily truncate labels and preserves logical slot order."))
    }

    CatalogGuideSection(
        title = "API",
        body = localized(copy.locale, "현재 commonMain public signature와 최소 사용 예제입니다.", "The current commonMain public signature and minimal usage."),
    ) {
        CatalogCodeBlock(
            """fun BeezActionButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: BeezActionButtonVariant = BrandSolid,
    size: BeezActionButtonSize = Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
)""",
        )
        CatalogCodeBlock(
            """BeezActionButton(
    label = "Continue",
    onClick = onContinue,
)""",
        )
    }
}

@Composable
private fun CatalogGuideSection(
    title: String,
    body: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentStackGap),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(BeezTheme.colors.strokeNeutral),
        )
        BasicText(
            text = title,
            style = BeezTheme.typography.sectionTitle.copy(color = BeezTheme.colors.foregroundPrimary),
        )
        BasicText(
            text = body,
            style = BeezTheme.typography.body.copy(color = BeezTheme.colors.foregroundSecondary),
        )
        content()
    }
}

@Composable
private fun CatalogChoiceGroup(
    title: String,
    labels: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.controlContentGap)) {
        BasicText(
            text = title,
            style = BeezTheme.typography.caption.copy(color = BeezTheme.colors.foregroundSecondary),
        )
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            if (maxWidth < 560.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.controlContentGap)) {
                    labels.forEachIndexed { index, label ->
                        CatalogChoice(label, index == selectedIndex, { onSelect(index) }, BeezTheme.colors.backgroundBrand, BeezTheme.colors.foregroundPrimary)
                    }
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.controlContentGap)) {
                    labels.forEachIndexed { index, label ->
                        CatalogChoice(label, index == selectedIndex, { onSelect(index) }, BeezTheme.colors.backgroundBrand, BeezTheme.colors.foregroundPrimary)
                    }
                }
            }
        }
    }
}

@Composable
private fun CatalogExampleCanvas(content: @Composable () -> Unit) {
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(BeezTheme.shapes.containerRadius)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 144.dp)
            .clip(shape)
            .background(BeezTheme.colors.strokeNeutral.copy(alpha = 0.12f))
            .border(1.dp, BeezTheme.colors.strokeNeutral, shape)
            .padding(BeezTheme.spacing.screenGutter),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
private fun CatalogDefinitionRow(name: String, meta: String, description: String) {
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(BeezTheme.shapes.controlRadius)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .border(1.dp, BeezTheme.colors.strokeNeutral, shape)
            .padding(BeezTheme.spacing.contentStackGap),
        verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.controlContentGap),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap),
        ) {
            BasicText(
                text = name,
                style = BeezTheme.typography.label.copy(color = BeezTheme.colors.foregroundPrimary),
                modifier = Modifier.weight(1f),
            )
            BasicText(
                text = meta,
                style = BeezTheme.typography.caption.copy(color = BeezTheme.colors.foregroundSecondary),
            )
        }
        BasicText(
            text = description,
            style = BeezTheme.typography.body.copy(color = BeezTheme.colors.foregroundSecondary),
        )
    }
}

@Composable
private fun <T> CatalogExampleGrid(
    items: List<T>,
    wideColumns: Int = 3,
    content: @Composable (T) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val columns = if (maxWidth < 720.dp) 1 else wideColumns
        Column(verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentStackGap)) {
            items.chunked(columns).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentStackGap),
                ) {
                    rowItems.forEach { item ->
                        Box(modifier = Modifier.weight(1f)) { content(item) }
                    }
                    repeat(columns - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun CatalogExampleTile(
    title: String,
    body: String,
    content: @Composable () -> Unit,
) {
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(BeezTheme.shapes.containerRadius)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 224.dp)
            .clip(shape)
            .border(1.dp, BeezTheme.colors.strokeNeutral, shape)
            .padding(BeezTheme.spacing.contentStackGap),
        verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.controlContentGap),
    ) {
        BasicText(
            text = title,
            style = BeezTheme.typography.label.copy(color = BeezTheme.colors.foregroundPrimary),
        )
        BasicText(
            text = body,
            style = BeezTheme.typography.caption.copy(color = BeezTheme.colors.foregroundSecondary),
        )
        Box(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}

@Composable
private fun CatalogGuidancePair(
    doTitle: String,
    doBody: String,
    dontTitle: String,
    dontBody: String,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        if (maxWidth < 720.dp) {
            Column(verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentStackGap)) {
                CatalogGuidanceCard(doTitle, doBody, BeezTheme.colors.backgroundBrand)
                CatalogGuidanceCard(dontTitle, dontBody, BeezTheme.colors.strokeCritical)
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentStackGap)) {
                CatalogGuidanceCard(doTitle, doBody, BeezTheme.colors.backgroundBrand, Modifier.weight(1f))
                CatalogGuidanceCard(dontTitle, dontBody, BeezTheme.colors.strokeCritical, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun CatalogGuidanceCard(
    title: String,
    body: String,
    accent: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(BeezTheme.shapes.containerRadius)
    Column(
        modifier = modifier
            .clip(shape)
            .border(1.dp, accent, shape)
            .padding(BeezTheme.spacing.contentStackGap),
        verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.controlContentGap),
    ) {
        BasicText(text = title, style = BeezTheme.typography.label.copy(color = accent))
        BasicText(text = body, style = BeezTheme.typography.body.copy(color = BeezTheme.colors.foregroundPrimary))
    }
}

@Composable
private fun CatalogCodeBlock(code: String) {
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(BeezTheme.shapes.controlRadius)
    BasicText(
        text = code,
        style = BeezTheme.typography.caption.copy(
            color = BeezTheme.colors.foregroundPrimary,
            fontFamily = FontFamily.Monospace,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(BeezTheme.colors.strokeNeutral.copy(alpha = 0.12f))
            .border(1.dp, BeezTheme.colors.strokeNeutral, shape)
            .padding(BeezTheme.spacing.contentStackGap),
    )
}

@Composable
private fun CheckboxDetail(copy: CatalogCopy) {
    var checked by remember { mutableStateOf(false) }
    var disabled by remember { mutableStateOf(false) }

    CatalogGuideSection(title = "Playground", body = playgroundDescription(copy.locale)) {
        CatalogChoiceGroup(
            title = localized(copy.locale, "선택 상태", "selection"),
            labels = listOf(localized(copy.locale, "선택 안 됨", "Unchecked"), localized(copy.locale, "선택됨", "Checked")),
            selectedIndex = if (checked) 1 else 0,
            onSelect = { checked = it == 1 },
        )
        CatalogChoiceGroup(
            title = localized(copy.locale, "사용 가능 여부", "availability"),
            labels = listOf(localized(copy.locale, "사용 가능", "Enabled"), localized(copy.locale, "비활성", "Disabled")),
            selectedIndex = if (disabled) 1 else 0,
            onSelect = { disabled = it == 1 },
        )
        CatalogExampleCanvas {
            BeezCheckbox(
                checked = checked,
                onCheckedChange = { checked = it },
                label = localized(copy.locale, "제품 업데이트 받기", "Receive product updates"),
                enabled = !disabled,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap),
        ) {
            BasicText(
                text = localized(copy.locale, "선택됨: $checked", "Checked: $checked"),
                style = BeezTheme.typography.caption.copy(color = BeezTheme.colors.foregroundSecondary),
                modifier = Modifier.weight(1f),
            )
            CatalogChoice(copy.reset, false, {
                checked = false
                disabled = false
            }, BeezTheme.colors.backgroundBrand, BeezTheme.colors.foregroundPrimary)
        }
    }

    CatalogGuideSection(
        title = copy.anatomy,
        body = localized(copy.locale, "하나의 toggle root가 indicator와 필수 label을 하나의 선택 control로 묶습니다.", "One toggle root groups the indicator and required label into one selection control."),
    ) {
        CatalogDefinitionRow("root", "Required", localized(copy.locale, "전체 layout, focus, toggle action과 checkbox semantics를 소유합니다.", "Owns layout, focus, toggle action, and checkbox semantics."))
        CatalogDefinitionRow("indicator", "Required", localized(copy.locale, "선택 여부를 container와 check mark로 표시합니다.", "Shows selection with a container and check mark."))
        CatalogDefinitionRow("label", "Required", localized(copy.locale, "선택하는 조건을 설명하고 접근성 이름을 제공합니다.", "Explains the option and provides its accessible name."))
    }

    CatalogGuideSection(
        title = copy.properties,
        body = localized(copy.locale, "Checkbox는 크기나 variant를 선택하지 않고 값과 사용 가능 여부만 노출합니다.", "Checkbox exposes value and availability without size or variant choices."),
    ) {
        CatalogDefinitionRow("checked", "Boolean · required", localized(copy.locale, "현재 binary 선택 값을 호출자가 소유합니다.", "The caller owns the current binary selection value."))
        CatalogDefinitionRow("onCheckedChange", "(Boolean) -> Unit · required", localized(copy.locale, "사용자가 전환하면 다음 값을 전달합니다.", "Receives the next value when the user toggles."))
        CatalogDefinitionRow("label", "String · required", localized(copy.locale, "선택 대상과 조건을 짧고 구체적으로 설명합니다.", "Describes the selected option or condition concisely."))
        CatalogDefinitionRow("enabled", "Boolean · true", localized(copy.locale, "false이면 현재 값은 유지하고 입력을 차단합니다.", "When false, preserves the value and blocks input."))
        CatalogDefinitionRow("modifier", "Modifier · empty", localized(copy.locale, "Group의 너비와 간격은 부모 layout이 관리합니다.", "The parent layout manages group width and spacing."))
    }

    CatalogGuideSection(
        title = localized(copy.locale, "States", "States"),
        body = localized(copy.locale, "Disabled 상태에서도 check mark를 유지해 색상 없이 현재 값을 구분합니다.", "Disabled states retain the check mark so the value is not communicated by color alone."),
    ) {
        data class CheckboxExample(val name: String, val value: Boolean, val enabled: Boolean)
        CatalogExampleGrid(
            items = listOf(
                CheckboxExample("Unchecked", false, true),
                CheckboxExample("Checked", true, true),
                CheckboxExample("Disabled / unchecked", false, false),
                CheckboxExample("Disabled / checked", true, false),
            ),
            wideColumns = 2,
        ) { item ->
            CatalogExampleTile(
                title = item.name,
                body = if (item.enabled) localized(copy.locale, "입력 허용", "Accepts input") else localized(copy.locale, "현재 값을 유지하고 입력 차단", "Preserves value and blocks input"),
            ) {
                BeezCheckbox(
                    checked = item.value,
                    onCheckedChange = {},
                    label = when (item.name) {
                        "Unchecked" -> localized(copy.locale, "선택 안 된 옵션", "Unchecked option")
                        "Checked" -> localized(copy.locale, "선택된 옵션", "Checked option")
                        "Disabled / unchecked" -> localized(copy.locale, "비활성 선택 안 된 옵션", "Disabled unchecked option")
                        else -> localized(copy.locale, "비활성 선택된 옵션", "Disabled checked option")
                    },
                    enabled = item.enabled,
                )
            }
        }
    }

    CatalogGuideSection(
        title = copy.guidelinesTitle,
        body = localized(copy.locale, "서로 독립적인 옵션을 선택하거나 해제할 때 사용합니다.", "Use Checkbox to select or clear independent options."),
    ) {
        CatalogGuidancePair(
            doTitle = localized(copy.locale, "권장", "Do"),
            doBody = localized(copy.locale, "알림 수신이나 약관 동의처럼 각각 독립적으로 유지되는 선택에 사용합니다.", "Use for independently retained choices such as notifications or agreement."),
            dontTitle = localized(copy.locale, "피하기", "Do not"),
            dontBody = localized(copy.locale, "즉시 실행되는 action이나 여러 값 중 하나만 고르는 선택에 사용하지 않습니다.", "Do not use for immediate actions or a mutually exclusive choice."),
        )
        CatalogDefinitionRow("Action Button", localized(copy.locale, "즉시 실행", "Immediate action"), localized(copy.locale, "값을 유지하지 않고 작업을 실행할 때 사용합니다.", "Use when activating work without retaining a selection value."))
        CatalogDefinitionRow("Radio / Segmented control", localized(copy.locale, "상호 배타 선택", "Mutually exclusive"), localized(copy.locale, "여러 값 중 하나만 선택할 때 사용합니다.", "Use when exactly one value can be selected."))
        CatalogDefinitionRow("Switch", localized(copy.locale, "즉시 적용 설정", "Immediate setting"), localized(copy.locale, "기능의 on/off가 즉시 적용되는 설정에 사용합니다.", "Use when an on/off setting takes effect immediately."))
        CatalogDefinitionRow(
            name = localized(copy.locale, "긴 label과 RTL", "Long labels and RTL"),
            meta = localized(copy.locale, "줄바꿈 · logical start", "Wrapping · logical start"),
            description = localized(copy.locale, "Label은 잘리지 않고 줄바꿈하며 indicator는 논리적 시작 위치를 유지합니다.", "The label wraps without truncation and the indicator stays at logical start."),
        )
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            CatalogExampleCanvas {
                BeezCheckbox(
                    checked = true,
                    onCheckedChange = {},
                    label = "أوافق على تلقي تحديثات مفصلة حول هذا الخيار",
                )
            }
        }
    }

    CatalogGuideSection(
        title = copy.accessibilityGuide,
        body = localized(copy.locale, "Indicator와 label을 하나의 checkbox node로 병합하고 상태와 전체 label click target을 제공합니다.", "Merges indicator and label into one checkbox node with state and a full-label activation target."),
    ) {
        CatalogDefinitionRow("Role and value", "Checkbox · on/off", localized(copy.locale, "Root가 checkbox role과 checked 값을 전달합니다.", "The root exposes checkbox role and checked value."))
        CatalogDefinitionRow("Interaction", "Touch · pointer · Space", localized(copy.locale, "Indicator와 label을 포함한 최소 48dp 영역 전체가 toggle됩니다.", "The full minimum 48dp area, including indicator and label, toggles."))
        CatalogDefinitionRow("Disabled", "State preserved", localized(copy.locale, "비활성 상태와 현재 on/off 값을 함께 전달합니다.", "Exposes disabled state while preserving the current on/off value."))
        CatalogDefinitionRow("Content", "Font scale · CJK · RTL", localized(copy.locale, "긴 label을 생략하지 않고 확대 글꼴과 RTL layout을 지원합니다.", "Supports long labels, enlarged fonts, and RTL without omission."))
    }

    CatalogGuideSection(
        title = "API",
        body = localized(copy.locale, "현재 binary Checkbox의 commonMain signature와 상태 소유 예제입니다.", "The current binary Checkbox commonMain signature and state ownership example."),
    ) {
        CatalogCodeBlock(
            """fun BeezCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
)""",
        )
        CatalogCodeBlock(
            """BeezCheckbox(
    checked = accepted,
    onCheckedChange = { accepted = it },
    label = "I agree to the terms",
)""",
        )
    }
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
