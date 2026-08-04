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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import beez.design.components.BeezActionButton
import beez.design.components.BeezActionButtonSize
import beez.design.components.BeezActionButtonVariant
import beez.design.components.BeezCheckbox
import beez.design.components.BeezSurface
import beez.design.components.BeezSurfaceElevation
import beez.design.components.BeezText
import beez.design.components.BeezTextField
import beez.design.components.BeezTextRole
import beez.design.components.BeezTextTone
import beez.design.foundation.BeezTheme

internal enum class CatalogComponent {
    ActionButton,
    Checkbox,
    Text,
    TextField,
    Surface,
}

private enum class TextPlaygroundAlignment(val textAlign: TextAlign) {
    Start(TextAlign.Start),
    Center(TextAlign.Center),
    End(TextAlign.End),
}

private enum class TextPlaygroundLayout(
    val maxLines: Int,
    val overflow: TextOverflow,
) {
    Wrap(Int.MAX_VALUE, TextOverflow.Clip),
    TwoLines(2, TextOverflow.Clip),
    Ellipsis(1, TextOverflow.Ellipsis),
}

private enum class TextFieldPlaygroundState {
    Enabled,
    ReadOnly,
    Disabled,
    Error,
}

private enum class TextFieldExampleState {
    Empty,
    Filled,
    ReadOnly,
    Disabled,
    Error,
    Slotted,
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
                CatalogMaturityBadge(label = componentMaturityLabel(component, copy))
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

        CatalogComponent.Text -> Column(
            verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.controlContentGap),
        ) {
            BeezText(
                text = if (copy.locale == CatalogLocale.Korean) "의미가 스타일을 만듭니다." else "Meaning shapes style.",
                role = BeezTextRole.SectionTitle,
            )
            BeezText(
                text = if (copy.locale == CatalogLocale.Korean) "토큰이 위계와 색상 역할을 일관되게 연결합니다." else "Tokens keep hierarchy and foreground intent consistent.",
                tone = BeezTextTone.Secondary,
            )
        }

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
            CatalogComponent.Text -> TextDetail(copy)
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
            CatalogMaturityBadge(label = componentMaturityLabel(component, copy))
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
                label = localized(copy.locale, "예제 실행", "Run example"),
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
private fun TextDetail(copy: CatalogCopy) {
    var role by remember { mutableStateOf(BeezTextRole.Body) }
    var tone by remember { mutableStateOf(BeezTextTone.Primary) }
    var alignment by remember { mutableStateOf(TextPlaygroundAlignment.Start) }
    var layout by remember { mutableStateOf(TextPlaygroundLayout.Wrap) }

    CatalogGuideSection(
        title = "Playground",
        body = localized(
            copy.locale,
            "콘텐츠 위계와 인접 배경의 의미를 선택해 실제 commonMain Text를 확인하세요.",
            "Choose content hierarchy and foreground intent to inspect the actual commonMain Text.",
        ),
    ) {
        CatalogChoiceGroup(
            title = localized(copy.locale, "타이포그래피 역할", "typography role"),
            labels = BeezTextRole.entries.map { it.name },
            selectedIndex = BeezTextRole.entries.indexOf(role),
            onSelect = { role = BeezTextRole.entries[it] },
        )
        CatalogChoiceGroup(
            title = localized(copy.locale, "전경 역할", "foreground tone"),
            labels = BeezTextTone.entries.map { it.name },
            selectedIndex = BeezTextTone.entries.indexOf(tone),
            onSelect = { tone = BeezTextTone.entries[it] },
        )
        CatalogChoiceGroup(
            title = localized(copy.locale, "정렬", "alignment"),
            labels = TextPlaygroundAlignment.entries.map { it.name },
            selectedIndex = TextPlaygroundAlignment.entries.indexOf(alignment),
            onSelect = { alignment = TextPlaygroundAlignment.entries[it] },
        )
        CatalogChoiceGroup(
            title = localized(copy.locale, "줄바꿈과 넘침", "wrapping and overflow"),
            labels = TextPlaygroundLayout.entries.map { option ->
                when (option) {
                    TextPlaygroundLayout.Wrap -> localized(copy.locale, "제한 없음", "Wrap")
                    TextPlaygroundLayout.TwoLines -> localized(copy.locale, "최대 2줄", "Two lines")
                    TextPlaygroundLayout.Ellipsis -> localized(copy.locale, "한 줄 말줄임", "Ellipsis")
                }
            },
            selectedIndex = TextPlaygroundLayout.entries.indexOf(layout),
            onSelect = { layout = TextPlaygroundLayout.entries[it] },
        )
        CatalogExampleCanvas {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (tone == BeezTextTone.OnBrand) {
                            BeezTheme.colors.backgroundBrand
                        } else {
                            BeezTheme.colors.backgroundNeutral
                        },
                    )
                    .padding(BeezTheme.spacing.contentStackGap),
            ) {
                BeezText(
                    text = localized(
                        copy.locale,
                        "의미 있는 역할은 긴 문장과 다양한 화면에서도 일관된 위계를 유지합니다.",
                        "Semantic roles keep hierarchy consistent across long copy and different screens.",
                    ),
                    role = role,
                    tone = tone,
                    textAlign = alignment.textAlign,
                    maxLines = layout.maxLines,
                    overflow = layout.overflow,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    CatalogGuideSection(
        title = copy.anatomy,
        body = localized(
            copy.locale,
            "Text는 부모 constraint를 받는 root와 하나의 semantic style을 적용한 문자열로 구성됩니다.",
            "Text consists of a parent-constrained root and one string using a single semantic style.",
        ),
    ) {
        CatalogDefinitionRow("root", "Required", localized(copy.locale, "크기와 배치는 부모가 정하며 Text는 padding이나 interaction을 추가하지 않습니다.", "The parent controls size and placement; Text adds no padding or interaction."))
        CatalogDefinitionRow("text", "String · required", localized(copy.locale, "화면에 표시하고 접근성 bridge에 전달하는 실제 문구입니다.", "The actual copy rendered on screen and exposed through the accessibility bridge."))
    }

    CatalogGuideSection(
        title = copy.properties,
        body = localized(
            copy.locale,
            "공개 속성은 의미와 layout 동작만 표현하며 임의 Color와 TextStyle override를 열지 않습니다.",
            "Public properties express intent and layout behavior without arbitrary Color or TextStyle overrides.",
        ),
    ) {
        CatalogDefinitionRow("text", "String · required", localized(copy.locale, "표시할 완료된 문자열입니다.", "The complete string to display."))
        CatalogDefinitionRow("role", "BeezTextRole · Body", localized(copy.locale, "Display부터 Caption까지 콘텐츠 위계를 선택합니다.", "Selects content hierarchy from Display through Caption."))
        CatalogDefinitionRow("tone", "BeezTextTone · Primary", localized(copy.locale, "Primary, Secondary, Critical 또는 OnBrand 전경 의미를 선택합니다.", "Selects Primary, Secondary, Critical, or OnBrand foreground intent."))
        CatalogDefinitionRow("textAlign", "TextAlign · Start", localized(copy.locale, "논리적 시작점을 기준으로 줄 내부를 정렬합니다.", "Aligns each line from the logical start edge."))
        CatalogDefinitionRow("overflow", "TextOverflow · Clip", localized(copy.locale, "제한된 줄을 넘는 문구의 시각 처리입니다.", "Controls visual treatment when copy exceeds limited lines."))
        CatalogDefinitionRow("maxLines", "Int · unlimited", localized(copy.locale, "1 이상의 최대 줄 수입니다. 중요한 정보는 기본적으로 제한하지 않습니다.", "The positive maximum line count; important copy remains unlimited by default."))
        CatalogDefinitionRow("modifier", "Modifier · empty", localized(copy.locale, "부모가 폭과 배치를 명시하는 확장 지점입니다.", "The extension point through which the parent sets width and placement."))
    }

    CatalogGuideSection(
        title = localized(copy.locale, "Typography roles", "Typography roles"),
        body = localized(
            copy.locale,
            "글자 크기 취향이 아니라 콘텐츠가 화면에서 맡는 위계로 role을 선택합니다.",
            "Choose a role by the copy's hierarchy in the screen, not by a preferred font size.",
        ),
    ) {
        CatalogExampleGrid(items = BeezTextRole.entries, wideColumns = 2) { item ->
            val description = when (item) {
                BeezTextRole.Display -> localized(copy.locale, "짧은 hero 또는 대표 메시지", "Short hero or signature message")
                BeezTextRole.ScreenTitle -> localized(copy.locale, "화면의 목적을 나타내는 제목", "Title that names the screen purpose")
                BeezTextRole.SectionTitle -> localized(copy.locale, "관련 콘텐츠 그룹의 제목", "Title for a related content group")
                BeezTextRole.Body -> localized(copy.locale, "설명, 안내와 일반 콘텐츠", "Descriptions, guidance, and general content")
                BeezTextRole.Label -> localized(copy.locale, "짧은 항목명과 metadata key", "Short item names and metadata keys")
                BeezTextRole.Caption -> localized(copy.locale, "출처, 시간과 보조 정보", "Sources, time, and supporting information")
            }
            CatalogExampleTile(title = item.name, body = description) {
                BeezText(
                    text = when (item) {
                        BeezTextRole.Display -> localized(copy.locale, "대표 메시지", "Signature message")
                        BeezTextRole.ScreenTitle -> localized(copy.locale, "계정 개요", "Account overview")
                        BeezTextRole.SectionTitle -> localized(copy.locale, "결제 정보", "Payment details")
                        BeezTextRole.Body -> localized(copy.locale, "일반적인 설명 문구", "General explanatory copy")
                        BeezTextRole.Label -> localized(copy.locale, "배송 주소", "Delivery address")
                        BeezTextRole.Caption -> localized(copy.locale, "오늘 업데이트됨", "Updated today")
                    },
                    role = item,
                )
            }
        }
    }

    CatalogGuideSection(
        title = localized(copy.locale, "Foreground tones", "Foreground tones"),
        body = localized(
            copy.locale,
            "Tone은 장식 색상이 아니라 문구의 강조와 인접 배경 관계를 나타냅니다.",
            "Tone describes emphasis and the adjacent background relationship, not a decorative color.",
        ),
    ) {
        CatalogExampleGrid(items = BeezTextTone.entries, wideColumns = 2) { item ->
            val onBrand = item == BeezTextTone.OnBrand
            CatalogExampleTile(
                title = item.name,
                body = when (item) {
                    BeezTextTone.Primary -> localized(copy.locale, "일반 배경의 핵심 정보", "Core information on a neutral background")
                    BeezTextTone.Secondary -> localized(copy.locale, "보조 설명과 metadata", "Supporting descriptions and metadata")
                    BeezTextTone.Critical -> localized(copy.locale, "오류 또는 위험을 설명하는 문구", "Copy that explains an error or risk")
                    BeezTextTone.OnBrand -> localized(copy.locale, "검증된 brand 배경 위 전경", "Foreground on a verified brand background")
                },
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (onBrand) BeezTheme.colors.backgroundBrand else BeezTheme.colors.backgroundNeutral)
                        .padding(BeezTheme.spacing.contentStackGap),
                ) {
                    BeezText(
                        text = when (item) {
                            BeezTextTone.Primary -> localized(copy.locale, "주문이 접수되었습니다.", "Your order was received.")
                            BeezTextTone.Secondary -> localized(copy.locale, "오늘 오후 3시에 업데이트됨", "Updated today at 3 PM")
                            BeezTextTone.Critical -> localized(copy.locale, "카드 번호를 다시 확인해 주세요.", "Check the card number and try again.")
                            BeezTextTone.OnBrand -> localized(copy.locale, "브랜드 영역의 문구", "Text on the brand area")
                        },
                        tone = item,
                    )
                }
            }
        }
    }

    CatalogGuideSection(
        title = localized(copy.locale, "Wrapping and layout", "Wrapping and layout"),
        body = localized(
            copy.locale,
            "줄 수를 제한하지 않는 것이 기본이며 ellipsis는 정보 손실을 감수할 수 있는 보조 문구에만 사용합니다.",
            "Unlimited wrapping is the default; use ellipsis only for supporting copy where truncation is acceptable.",
        ),
    ) {
        CatalogExampleGrid(items = TextPlaygroundLayout.entries, wideColumns = 3) { item ->
            CatalogExampleTile(
                title = item.name,
                body = when (item) {
                    TextPlaygroundLayout.Wrap -> localized(copy.locale, "번역과 확대 글꼴을 위해 자연스럽게 줄바꿈", "Natural wrapping for translation and font scaling")
                    TextPlaygroundLayout.TwoLines -> localized(copy.locale, "카드의 보조 설명처럼 제한된 영역", "A bounded region such as supporting card copy")
                    TextPlaygroundLayout.Ellipsis -> localized(copy.locale, "대체 경로가 있는 낮은 중요도 정보", "Low-priority information with another access path")
                },
            ) {
                BeezText(
                    text = localized(copy.locale, "길어진 번역 문구는 좁은 화면에서 여러 줄로 자연스럽게 배치됩니다.", "Translated copy wraps naturally when the available width becomes narrow."),
                    maxLines = item.maxLines,
                    overflow = item.overflow,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    CatalogGuideSection(
        title = copy.guidelinesTitle,
        body = localized(copy.locale, "콘텐츠 위계와 읽을 수 있는 의미를 함께 유지합니다.", "Preserve content hierarchy together with readable meaning."),
    ) {
        CatalogGuidancePair(
            doTitle = localized(copy.locale, "권장", "Do"),
            doBody = localized(copy.locale, "화면 목적에는 ScreenTitle, 설명에는 Body처럼 문구의 역할로 선택합니다.", "Use ScreenTitle for the screen purpose and Body for explanations."),
            dontTitle = localized(copy.locale, "피하기", "Do not"),
            dontBody = localized(copy.locale, "글자가 커 보이게 하려고 모든 문구에 Display를 사용하거나 OnBrand를 강조색처럼 사용하지 않습니다.", "Do not use Display everywhere to make copy look larger or treat OnBrand as an accent color."),
        )
        CatalogDefinitionRow(
            localized(copy.locale, "Critical", "Critical"),
            localized(copy.locale, "색상 + 설명", "Color + explanation"),
            localized(copy.locale, "오류 원인과 해결 방법을 문구로 전달하고 색상만으로 상태를 표현하지 않습니다.", "Explain the error and resolution in words instead of communicating state through color alone."),
        )
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            CatalogExampleCanvas {
                BeezText(
                    text = "نص طويل يحافظ على ترتيب القراءة ويلتف داخل المساحة المتاحة",
                    role = BeezTextRole.Body,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    CatalogGuideSection(
        title = copy.accessibilityGuide,
        body = localized(
            copy.locale,
            "Text는 원본 문구 semantics를 제공하지만 role, focus 또는 action을 암묵적으로 만들지 않습니다.",
            "Text exposes the original copy semantics without implicitly adding a role, focus target, or action.",
        ),
    ) {
        CatalogDefinitionRow("Semantics", "Text · no action", localized(copy.locale, "시각적으로 말줄임돼도 원본 문자열을 accessibility bridge에 유지합니다.", "Keeps the original string in the accessibility bridge even when visually ellipsized."))
        CatalogDefinitionRow("Interaction", "None", localized(copy.locale, "링크, 선택과 복사가 필요하면 그 동작과 semantics를 소유하는 별도 pattern을 사용합니다.", "Use a dedicated pattern that owns behavior and semantics when links, selection, or copy are needed."))
        CatalogDefinitionRow("Contrast", "WCAG 2.2 AA", localized(copy.locale, "Primary, Secondary, Critical은 neutral 배경, OnBrand는 brand 배경에서 검증합니다.", "Primary, Secondary, and Critical are verified on neutral; OnBrand is verified on brand."))
        CatalogDefinitionRow("Content", "Font scale · CJK · RTL", localized(copy.locale, "고정 높이로 문구를 자르지 않고 논리적 Start/End와 자연스러운 줄바꿈을 사용합니다.", "Avoid fixed-height clipping and use logical Start/End alignment with natural wrapping."))
    }

    CatalogGuideSection(
        title = "API",
        body = localized(copy.locale, "현재 String 기반 commonMain signature와 semantic role 사용 예제입니다.", "The current String-based commonMain signature and semantic-role example."),
    ) {
        CatalogCodeBlock(
            """fun BeezText(
    text: String,
    modifier: Modifier = Modifier,
    role: BeezTextRole = Body,
    tone: BeezTextTone = Primary,
    textAlign: TextAlign = Start,
    overflow: TextOverflow = Clip,
    maxLines: Int = Int.MAX_VALUE,
)""",
        )
        CatalogCodeBlock(
            """BeezText(
    text = "Payment details",
    role = BeezTextRole.SectionTitle,
)""",
        )
    }
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
    var playgroundState by remember { mutableStateOf(TextFieldPlaygroundState.Enabled) }
    val isError = playgroundState == TextFieldPlaygroundState.Error

    CatalogGuideSection(title = "Playground", body = playgroundDescription(copy.locale)) {
        CatalogChoiceGroup(
            title = localized(copy.locale, "입력 상태", "state"),
            labels = TextFieldPlaygroundState.entries.map { state ->
                when (state) {
                    TextFieldPlaygroundState.Enabled -> localized(copy.locale, "사용 가능", "Enabled")
                    TextFieldPlaygroundState.ReadOnly -> localized(copy.locale, "읽기 전용", "Read only")
                    TextFieldPlaygroundState.Disabled -> localized(copy.locale, "비활성", "Disabled")
                    TextFieldPlaygroundState.Error -> localized(copy.locale, "오류", "Error")
                }
            },
            selectedIndex = playgroundState.ordinal,
            onSelect = { playgroundState = TextFieldPlaygroundState.entries[it] },
        )
        CatalogExampleCanvas {
            BeezTextField(
                value = email,
                onValueChange = { email = it },
                label = copy.email,
                placeholder = copy.placeholder,
                supportingText = if (isError) {
                    localized(copy.locale, "올바른 이메일 주소를 입력하세요.", "Please enter a valid email address.")
                } else {
                    copy.supporting
                },
                enabled = playgroundState != TextFieldPlaygroundState.Disabled,
                readOnly = playgroundState == TextFieldPlaygroundState.ReadOnly,
                isError = isError,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap),
        ) {
            BasicText(
                text = if (email.isEmpty()) localized(copy.locale, "값 없음", "Value empty") else localized(copy.locale, "현재 값: $email", "Current value: $email"),
                style = BeezTheme.typography.caption.copy(color = BeezTheme.colors.foregroundSecondary),
                modifier = Modifier.weight(1f),
            )
            CatalogChoice(copy.reset, false, {
                email = ""
                playgroundState = TextFieldPlaygroundState.Enabled
            }, BeezTheme.colors.backgroundBrand, BeezTheme.colors.foregroundPrimary)
        }
    }

    CatalogGuideSection(
        title = copy.anatomy,
        body = localized(copy.locale, "필수 label과 단일 행 input을 중심으로 필요한 보조 content만 추가합니다.", "A required label and single-line input are supported only by the content the field needs."),
    ) {
        CatalogDefinitionRow("root", "Required", localized(copy.locale, "Label, input row와 supporting text를 세로로 배치합니다.", "Vertically arranges the label, input row, and supporting text."))
        CatalogDefinitionRow("label", "Required", localized(copy.locale, "입력 목적을 계속 보여주고 접근성 이름을 제공합니다.", "Keeps the input purpose visible and provides the accessible name."))
        CatalogDefinitionRow("input", "Required", localized(copy.locale, "값, 커서, 편집과 selection을 담당하는 단일 행 영역입니다.", "A single-line region responsible for value, cursor, editing, and selection."))
        CatalogDefinitionRow("placeholder", "Optional", localized(copy.locale, "값이 비었을 때 형식 예시나 짧은 힌트를 제공합니다.", "Provides a format example or short hint while the value is empty."))
        CatalogDefinitionRow("supportingText", "Optional", localized(copy.locale, "도움말 또는 원인과 해결 방법을 설명하는 오류 메시지입니다.", "Provides help or an error message that explains cause and resolution."))
        CatalogDefinitionRow("leadingContent / trailingContent", "Optional", localized(copy.locale, "입력 의미를 보조하는 icon이나 짧은 visual을 담습니다.", "Holds an icon or short visual that supports the input meaning."))
    }

    CatalogGuideSection(
        title = copy.properties,
        body = localized(copy.locale, "값과 validation은 호출자가 소유하고 Text Field는 표시와 입력 규칙을 적용합니다.", "The caller owns value and validation; Text Field applies presentation and input rules."),
    ) {
        CatalogDefinitionRow("value", "String · required", localized(copy.locale, "호출자가 소유하는 현재 입력 값입니다.", "The current input value owned by the caller."))
        CatalogDefinitionRow("onValueChange", "(String) -> Unit · required", localized(copy.locale, "편집 결과를 호출자에게 전달합니다.", "Delivers editing results to the caller."))
        CatalogDefinitionRow("label", "String · required", localized(copy.locale, "입력 목적을 설명하는 영구적인 이름입니다.", "A persistent name that explains the input purpose."))
        CatalogDefinitionRow("placeholder / supportingText", "String? · null", localized(copy.locale, "값 예시와 도움말 또는 오류 설명을 선택적으로 추가합니다.", "Optionally adds a value example and help or error explanation."))
        CatalogDefinitionRow("enabled", "Boolean · true", localized(copy.locale, "false이면 focus와 편집을 차단합니다.", "When false, blocks focus and editing."))
        CatalogDefinitionRow("readOnly", "Boolean · false", localized(copy.locale, "Focus와 selection은 허용하고 값 변경만 차단합니다.", "Allows focus and selection while blocking value changes."))
        CatalogDefinitionRow("isError", "Boolean · false", localized(copy.locale, "Critical stroke와 오류 semantics를 활성화합니다.", "Activates critical stroke and error semantics."))
        CatalogDefinitionRow("leadingContent / trailingContent", "Composable? · null", localized(copy.locale, "Interactive content를 넣으면 semantics와 focus 순서는 호출자가 책임집니다.", "When interactive content is supplied, the caller owns semantics and focus order."))
    }

    CatalogGuideSection(
        title = localized(copy.locale, "States", "States"),
        body = localized(copy.locale, "Disabled > Error > Focused > ReadOnly > Enabled 우선순위로 표시하며 supporting text로 상태 의미를 보완합니다.", "Uses Disabled > Error > Focused > ReadOnly > Enabled precedence and supporting text to reinforce meaning."),
    ) {
        CatalogExampleGrid(items = TextFieldExampleState.entries, wideColumns = 2) { state ->
            val label = when (state) {
                TextFieldExampleState.Empty -> localized(copy.locale, "빈 필드", "Empty field")
                TextFieldExampleState.Filled -> localized(copy.locale, "값이 있는 필드", "Filled field")
                TextFieldExampleState.ReadOnly -> localized(copy.locale, "읽기 전용 필드", "Read-only field")
                TextFieldExampleState.Disabled -> localized(copy.locale, "비활성 필드", "Disabled field")
                TextFieldExampleState.Error -> localized(copy.locale, "오류 필드", "Error field")
                TextFieldExampleState.Slotted -> localized(copy.locale, "슬롯이 있는 필드", "Slotted field")
            }
            CatalogExampleTile(
                title = state.name,
                body = when (state) {
                    TextFieldExampleState.Empty -> localized(copy.locale, "Label은 유지하고 placeholder로 형식 예시", "Persistent label with a format example")
                    TextFieldExampleState.Filled -> localized(copy.locale, "호출자가 소유하는 현재 값", "Current caller-owned value")
                    TextFieldExampleState.ReadOnly -> localized(copy.locale, "Focus와 selection은 허용", "Focus and selection remain available")
                    TextFieldExampleState.Disabled -> localized(copy.locale, "Focus와 편집 차단", "Focus and editing blocked")
                    TextFieldExampleState.Error -> localized(copy.locale, "원인과 해결 방법을 함께 설명", "Cause and resolution explained together")
                    TextFieldExampleState.Slotted -> localized(copy.locale, "논리적 시작과 끝의 보조 content", "Supporting content at logical start and end")
                },
            ) {
                BeezTextField(
                    value = when (state) {
                        TextFieldExampleState.Empty -> ""
                        TextFieldExampleState.Filled -> "Filled value"
                        TextFieldExampleState.ReadOnly -> "Read-only value"
                        TextFieldExampleState.Disabled -> "Disabled value"
                        TextFieldExampleState.Error -> "Invalid value"
                        TextFieldExampleState.Slotted -> "account"
                    },
                    onValueChange = {},
                    label = label,
                    placeholder = if (state == TextFieldExampleState.Empty) localized(copy.locale, "name@example.com", "Placeholder") else null,
                    supportingText = when (state) {
                        TextFieldExampleState.Empty -> localized(copy.locale, "형식 예시를 확인하세요.", "Supporting text")
                        TextFieldExampleState.Error -> localized(copy.locale, "계속하기 전에 올바른 값을 입력하세요.", "Resolve this error before continuing.")
                        else -> null
                    },
                    enabled = state != TextFieldExampleState.Disabled,
                    readOnly = state == TextFieldExampleState.ReadOnly,
                    isError = state == TextFieldExampleState.Error,
                    leadingContent = if (state == TextFieldExampleState.Slotted) ({ BasicText("@", style = BeezTheme.typography.body) }) else null,
                    trailingContent = if (state == TextFieldExampleState.Slotted) ({ BasicText("✓", style = BeezTheme.typography.body) }) else null,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    CatalogGuideSection(
        title = localized(copy.locale, "Layout", "Layout"),
        body = localized(copy.locale, "Text Field는 폼 행의 사용 가능한 너비를 채우고 부모가 최대 너비와 필드 간격을 결정하는 배치가 일반적입니다.", "Text Field usually fills the available form-row width while its parent sets maximum width and spacing between fields."),
    ) {
        CatalogDefinitionRow("Width", localized(copy.locale, "부모 제약", "Parent constrained"), localized(copy.locale, "modifier.fillMaxWidth()로 행을 채우되 읽기 편한 폼 너비는 부모에서 제한합니다.", "Fill the row with modifier.fillMaxWidth(), but constrain readable form width in the parent."))
        CatalogDefinitionRow("Height", localized(copy.locale, "Content driven", "Content driven"), localized(copy.locale, "Font scale과 supporting text에 따라 늘어나도록 고정 높이를 지정하지 않습니다.", "Do not set a fixed height; allow font scale and supporting text to increase it."))
        CatalogDefinitionRow("Narrow / RTL", localized(copy.locale, "Slot 유지", "Keep slots"), localized(copy.locale, "좁은 너비에서도 label을 placeholder로 대체하거나 slot을 삭제하지 않습니다.", "Do not replace the label with placeholder or remove slots at narrow widths."))
    }

    CatalogGuideSection(
        title = copy.guidelinesTitle,
        body = localized(copy.locale, "Label은 입력 목적을 계속 설명하고 placeholder는 형식 예시로만 사용합니다.", "Keep a persistent label for purpose and use placeholder only as a format example."),
    ) {
        CatalogGuidancePair(
            doTitle = localized(copy.locale, "권장", "Do"),
            doBody = localized(copy.locale, "구체적인 label을 사용하고 오류 원인과 해결 방법을 supporting text로 알려줍니다.", "Use a specific label and explain error cause and resolution in supporting text."),
            dontTitle = localized(copy.locale, "피하기", "Do not"),
            dontBody = localized(copy.locale, "Placeholder만으로 목적을 설명하거나 오류를 색상으로만 표시하지 않습니다.", "Do not rely on placeholder for purpose or communicate an error with color alone."),
        )
        CatalogDefinitionRow("Text / Description", localized(copy.locale, "읽기 전용 값", "Read-only value"), localized(copy.locale, "Focus와 selection이 필요 없는 값은 일반 text로 표시합니다.", "Display a value as regular text when focus and selection are unnecessary."))
        CatalogDefinitionRow("Picker / Checkbox", localized(copy.locale, "제한된 선택지", "Constrained choice"), localized(copy.locale, "직접 입력보다 유효한 값을 선택하는 편이 명확할 때 사용합니다.", "Use when selecting a valid value is clearer than free-form input."))
        CatalogDefinitionRow("Long content / RTL", localized(copy.locale, "줄바꿈 · logical order", "Wrapping · logical order"), localized(copy.locale, "Label과 supporting text는 줄바꿈하고 input slot은 논리적 순서를 따릅니다.", "Labels and supporting text wrap while input slots follow logical order."))
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

    CatalogGuideSection(
        title = copy.accessibilityGuide,
        body = localized(copy.locale, "Label을 접근성 이름으로 사용하고 현재 값, disabled와 error 상태를 semantics에 전달합니다.", "Uses the label as accessible name and exposes current value, disabled, and error state."),
    ) {
        CatalogDefinitionRow("Name and value", "Label · current value", localized(copy.locale, "Label은 placeholder와 독립적인 이름이고 input은 현재 값을 제공합니다.", "Label remains independent from placeholder and input exposes its current value."))
        CatalogDefinitionRow("State", "Editable · read-only · disabled · error", localized(copy.locale, "Interaction 규칙과 오류 설명을 상태와 함께 전달합니다.", "Exposes interaction rules and error explanation with state."))
        CatalogDefinitionRow("Interaction", "Keyboard · selection · copy/paste", localized(copy.locale, "Compose text input primitive의 focus, 편집, selection과 clipboard 동작을 유지합니다.", "Preserves focus, editing, selection, and clipboard behavior from the Compose text input primitive."))
        CatalogDefinitionRow("Platform verification", "IME · clipboard", localized(copy.locale, "실제 플랫폼 IME와 clipboard 통합 검증은 아직 남아 있습니다.", "Real platform IME and clipboard integration verification remains pending."))
    }

    CatalogGuideSection(
        title = "API",
        body = localized(copy.locale, "현재 single-line Text Field의 commonMain signature와 최소 사용 예제입니다.", "The current single-line Text Field commonMain signature and minimal usage example."),
    ) {
        CatalogCodeBlock(
            """fun BeezTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    supportingText: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
)""",
        )
        CatalogCodeBlock(
            """BeezTextField(
    value = email,
    onValueChange = { email = it },
    label = "Email address",
    placeholder = "name@example.com",
    modifier = Modifier.fillMaxWidth(),
)""",
        )
    }
}

@Composable
private fun SurfaceDetail(copy: CatalogCopy) {
    var elevation by remember { mutableStateOf(BeezSurfaceElevation.Raised) }

    CatalogGuideSection(title = "Playground", body = playgroundDescription(copy.locale)) {
        CatalogChoiceGroup(
            title = "elevation",
            labels = BeezSurfaceElevation.entries.map { "Elevation ${it.name}" },
            selectedIndex = elevation.ordinal,
            onSelect = { elevation = BeezSurfaceElevation.entries[it] },
        )
        CatalogExampleCanvas {
            BeezSurface(
                elevation = elevation,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(BeezTheme.spacing.screenGutter),
                    verticalArrangement = Arrangement.spacedBy(BeezTheme.spacing.contentInlineGap),
                ) {
                    BasicText(text = "${elevation.name} Surface", style = BeezTheme.typography.sectionTitle.copy(color = BeezTheme.colors.foregroundPrimary))
                    BasicText(
                        text = when (elevation) {
                            BeezSurfaceElevation.Flat -> localized(copy.locale, "같은 배경 레벨의 콘텐츠를 묶습니다.", "Groups content at the same background level.")
                            BeezSurfaceElevation.Raised -> localized(copy.locale, "주변 영역과 구분되는 작업 단위를 표시합니다.", "Separates a working region from its surroundings.")
                            BeezSurfaceElevation.Floating -> localized(copy.locale, "다른 영역 위에 떠 있는 임시 영역을 표시합니다.", "Presents a temporary region above other surfaces.")
                        },
                        style = BeezTheme.typography.body.copy(color = BeezTheme.colors.foregroundSecondary),
                    )
                }
            }
        }
    }

    CatalogGuideSection(
        title = copy.anatomy,
        body = localized(copy.locale, "Surface는 시각적 container와 호출자가 배치하는 content 두 부분으로 구성됩니다.", "Surface consists of a visual container and caller-provided content."),
    ) {
        CatalogDefinitionRow("root", "Required", localized(copy.locale, "Neutral background, container shape와 semantic elevation을 적용합니다.", "Applies neutral background, container shape, and semantic elevation."))
        CatalogDefinitionRow("content", "Required", localized(copy.locale, "독립적인 layout과 semantics를 유지하는 호출자 content입니다.", "Caller content that retains independent layout and semantics."))
    }

    CatalogGuideSection(
        title = copy.properties,
        body = localized(copy.locale, "Surface는 neutral container로 유지하고 깊이, 배치와 content만 조합합니다.", "Surface remains a neutral container composed through depth, layout, and content."),
    ) {
        CatalogDefinitionRow("elevation", "BeezSurfaceElevation · Flat", localized(copy.locale, "Flat, Raised 또는 Floating으로 주변 surface와의 깊이 관계를 선택합니다.", "Selects Flat, Raised, or Floating depth relative to surrounding surfaces."))
        CatalogDefinitionRow("modifier", "Modifier · empty", localized(copy.locale, "부모 constraint, 크기와 외부 배치를 적용합니다.", "Applies parent constraints, size, and external placement."))
        CatalogDefinitionRow("content", "BoxScope.() -> Unit · required", localized(copy.locale, "내부 padding과 child 간격을 semantic spacing token으로 구성합니다.", "Builds internal padding and child spacing from semantic spacing tokens."))
    }

    CatalogGuideSection(
        title = localized(copy.locale, "Elevation", "Elevation"),
        body = localized(copy.locale, "Elevation은 상호작 상태가 아니며 동시에 하나의 깊이만 선택합니다.", "Elevation is not an interaction state; select exactly one depth at a time."),
    ) {
        CatalogExampleGrid(items = BeezSurfaceElevation.entries) { option ->
            CatalogExampleTile(
                title = option.name,
                body = when (option) {
                    BeezSurfaceElevation.Flat -> localized(copy.locale, "구분이 필요하지만 높이 차이가 없는 기본 영역", "Default grouping without a height difference")
                    BeezSurfaceElevation.Raised -> localized(copy.locale, "항상 보이는 독립적 작업 또는 정보 영역", "Persistent, distinct work or information region")
                    BeezSurfaceElevation.Floating -> localized(copy.locale, "다른 영역 위에 일시적으로 표시되는 영역", "Temporary region displayed above other surfaces")
                },
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BeezTheme.colors.backgroundBrand.copy(alpha = 0.14f))
                        .padding(BeezTheme.spacing.contentStackGap),
                ) {
                    BeezSurface(elevation = option, modifier = Modifier.fillMaxWidth()) {
                        BasicText(
                            text = "${option.name} Surface",
                            style = BeezTheme.typography.label.copy(color = BeezTheme.colors.foregroundPrimary),
                            modifier = Modifier.padding(BeezTheme.spacing.contentStackGap),
                        )
                    }
                }
            }
        }
    }

    CatalogGuideSection(
        title = localized(copy.locale, "Layout", "Layout"),
        body = localized(copy.locale, "Surface자체는 크기와 내부 여백을 결정하지 않고 부모 constraint와 content를 따릅니다.", "Surface does not choose its own size or padding; it follows parent constraints and content."),
    ) {
        CatalogDefinitionRow("Size", localized(copy.locale, "부모와 content", "Parent and content"), localized(copy.locale, "고정 size variant 없이 부모 modifier와 content 크기를 존중합니다.", "Respects parent modifier and content size without fixed-size variants."))
        CatalogDefinitionRow("Padding", localized(copy.locale, "호출자 책임", "Caller-owned"), localized(copy.locale, "Surface는 암묵적 padding을 추가하지 않으며 content가 semantic spacing token을 사용합니다.", "Surface adds no implicit padding; content uses semantic spacing tokens."))
        CatalogDefinitionRow("Overflow", localized(copy.locale, "Shape clip", "Shape clip"), localized(copy.locale, "Content는 container shape으로 clip되고 shadow는 outline 밖에 그려집니다.", "Content clips to container shape while shadow renders outside the outline."))
    }

    CatalogGuideSection(
        title = copy.guidelinesTitle,
        body = localized(copy.locale, "관련 content를 묶는 비대화형 container로 사용하고 상호작이 필요하면 목적에 맞는 component를 선택합니다.", "Use as a non-interactive container for related content and choose a purpose-built component when interaction is required."),
    ) {
        CatalogGuidancePair(
            doTitle = localized(copy.locale, "권장", "Do"),
            doBody = localized(copy.locale, "하나의 주제나 task에 관련된 content를 묶고 semantic foreground와 spacing token을 사용합니다.", "Group content for one topic or task and use semantic foreground and spacing tokens."),
            dontTitle = localized(copy.locale, "피하기", "Do not"),
            dontBody = localized(copy.locale, "Surface 전체에 clickable을 붙여 role, focus와 feedback 없는 숨은 button으로 만들지 않습니다.", "Do not add clickable to the whole Surface and create a hidden button without role, focus, or feedback."),
        )
        CatalogDefinitionRow("Action Button", localized(copy.locale, "작업 실행", "Run an action"), localized(copy.locale, "명확한 action을 실행할 때 button role과 input feedback을 제공합니다.", "Provides button role and input feedback for a clear action."))
        CatalogDefinitionRow("Interactive Card", localized(copy.locale, "전체 영역 action", "Whole-region action"), localized(copy.locale, "반복 사용 요구가 확인되면 role, focus, state와 target을 갖춘 별도 pattern을 정의합니다.", "Define a separate pattern with role, focus, state, and target when repeated demand is confirmed."))
        CatalogDefinitionRow("Box / Column", localized(copy.locale, "단순 layout", "Layout only"), localized(copy.locale, "시각적 container 계약이 필요 없다면 Compose layout primitive를 사용합니다.", "Use a Compose layout primitive when no visual container contract is needed."))
        CatalogDefinitionRow("Long content / RTL", localized(copy.locale, "Content 책임", "Content-owned"), localized(copy.locale, "Surface는 LayoutDirection을 전달하고 줄바꿈과 child 배치는 content가 관리합니다.", "Surface passes LayoutDirection through; content manages wrapping and child layout."))
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

    CatalogGuideSection(
        title = copy.accessibilityGuide,
        body = localized(copy.locale, "Surface는 role, name, state, focus 또는 action을 추가하지 않고 자식 semantics를 병합하지 않습니다.", "Surface adds no role, name, state, focus, or action and does not merge child semantics."),
    ) {
        CatalogDefinitionRow("Container", "No implicit semantics", localized(copy.locale, "의미 있는 grouping이 필요하면 호출자가 화면 문맥에 맞게 semantics를 추가합니다.", "The caller adds context-appropriate semantics when meaningful grouping is needed."))
        CatalogDefinitionRow("Children", "Independent nodes", localized(copy.locale, "내부 text와 control은 각자의 이름, 상태와 action을 유지합니다.", "Inner text and controls retain their own names, states, and actions."))
        CatalogDefinitionRow("Interaction", "None", localized(copy.locale, "Surface 자체는 focus target이 아니며 내부 control이 각자의 input 규칙을 따릅니다.", "Surface itself is not focusable; inner controls follow their own input rules."))
        CatalogDefinitionRow("Visual meaning", "Not elevation alone", localized(copy.locale, "Shadow가 보이지 않는 환경에서도 content 순서와 의미가 유지되어야 합니다.", "Content order and meaning must survive environments where shadow is not visible."))
        CatalogDefinitionRow("Platform verification", "Shadow · screen reader", localized(copy.locale, "플랫폼별 shadow rendering과 보조 기술 grouping 검증은 아직 남아 있습니다.", "Platform shadow rendering and assistive-technology grouping verification remains pending."))
    }

    CatalogGuideSection(
        title = "API",
        body = localized(copy.locale, "현재 neutral Surface의 commonMain signature와 semantic elevation 사용 예제입니다.", "The current neutral Surface commonMain signature and semantic elevation usage example."),
    ) {
        CatalogCodeBlock(
            """enum class BeezSurfaceElevation {
    Flat,
    Raised,
    Floating,
}

fun BeezSurface(
    modifier: Modifier = Modifier,
    elevation: BeezSurfaceElevation = BeezSurfaceElevation.Flat,
    content: @Composable BoxScope.() -> Unit,
)""",
        )
        CatalogCodeBlock(
            """BeezSurface(
    elevation = BeezSurfaceElevation.Raised,
) {
    Column(
        modifier = Modifier.padding(BeezTheme.spacing.screenGutter),
    ) {
        // Related content
    }
}""",
        )
    }
}

private fun componentTitle(component: CatalogComponent, copy: CatalogCopy): String = when (component) {
    CatalogComponent.ActionButton -> copy.actionButton
    CatalogComponent.Checkbox -> copy.checkbox
    CatalogComponent.Text -> copy.text
    CatalogComponent.TextField -> copy.textField
    CatalogComponent.Surface -> copy.surface
}

private fun componentSummary(component: CatalogComponent, locale: CatalogLocale): String = when (component) {
    CatalogComponent.ActionButton -> localized(locale, "명확한 action을 실행하는 기본 interaction component입니다.", "A primary interaction component for clear, immediate actions.")
    CatalogComponent.Checkbox -> localized(locale, "서로 독립적인 binary option을 선택하거나 해제합니다.", "Selects or clears an independent binary option.")
    CatalogComponent.Text -> localized(locale, "Semantic typography와 foreground 역할로 제품 문구의 위계를 표현합니다.", "Expresses product copy hierarchy through semantic typography and foreground roles.")
    CatalogComponent.TextField -> localized(locale, "Label과 상태 안내를 갖춘 단일 행 text input입니다.", "A single-line text input with a persistent label and state guidance.")
    CatalogComponent.Surface -> localized(locale, "관련 콘텐츠를 shape와 elevation으로 묶는 비대화형 container입니다.", "A non-interactive container that groups related content with shape and elevation.")
}

private fun componentMaturityLabel(component: CatalogComponent, copy: CatalogCopy): String =
    if (component == CatalogComponent.Text) copy.proposed else copy.experimental

private fun playgroundDescription(locale: CatalogLocale): String = localized(
    locale,
    "Control을 조작해 실제 commonMain API의 상태 변화를 확인하세요.",
    "Use the controls to inspect state changes in the actual commonMain API.",
)

private fun localized(locale: CatalogLocale, korean: String, english: String): String =
    if (locale == CatalogLocale.Korean) korean else english
