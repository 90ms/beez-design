# BEEZ Theme Guide

- 상태: Draft
- 기준일: 2026-08-03

BEEZ theme은 semantic token scheme을 Compose Multiplatform tree에 제공한다. 제품 코드는 원시 색상이나 spacing 값을 직접 반복하지 않고 BEEZ theme을 읽는다.

## 기본 사용

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import beez.design.foundation.BeezTheme
import beez.design.tokens.BeezTokenSchemes

@Composable
fun ProductContent() {
    BeezTheme {
        // BEEZ components and product content
        val titleStyle = BeezTheme.typography.screenTitle
        val brandColor = BeezTheme.colors.backgroundBrand
    }
}
```

기본 wrapper 없이 theme access를 읽는 경우에도 BEEZ Light scheme이 fallback으로 제공된다. 제품 화면의 명확한 appearance 경계를 위해 실제 root에는 BeezTheme 또는 BeezTheme.Dark를 배치한다.

## Dark appearance

```kotlin
BeezTheme.Dark {
    ProductContent()
}
```

Light와 Dark는 같은 semantic role을 제공하며 값과 mapping만 다르다. 컴포넌트는 현재 appearance를 직접 분기하지 않는다.

## 브랜드 theme

브랜드는 전체 scheme을 다시 만들기보다 BEEZ 기본 scheme을 copy하고 semantic 영역을 교체한다.

```kotlin
val productScheme = BeezTokenSchemes.light.withColors(
    BeezTokenSchemes.light.colors.copy(
        backgroundBrand = Color(0xFF1769AB),
        foregroundOnBrand = Color.White,
        strokeFocus = Color(0xFF1769AB),
    ),
)

BeezTheme.Provide(productScheme) {
    ProductContent()
}
```

브랜드가 형태를 바꿀 때는 shape semantic radius를 교체한다. component 구조와 동작을 theme으로 변경하지 않는다.

## 운영 규칙

- 색상·간격·typography는 BeezTheme의 semantic scheme을 사용한다.
- MaterialTheme 또는 Material color/type을 BEEZ core API에 연결하지 않는다.
- 브랜드 scheme은 Light와 Dark 각각의 전경·배경 조합을 검토한다.
- theme 변경은 component screenshot, 접근성 대비, font scale과 상태 표현에 영향을 줄 수 있다.
- 새 semantic role이 필요하면 [토큰 분류](token-taxonomy.md)와 ADR을 먼저 갱신한다.

## 현재 상태

현재 default scheme 값은 `specification/tokens`의 DTCG 원본에서 생성하며 provider API는 provisional이다. Compose compile과 실제 target 검증이 완료되기 전에는 Stable 품질을 주장하지 않는다.
