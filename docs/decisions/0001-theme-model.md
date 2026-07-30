# ADR-0001: BEEZ 기본 테마와 브랜드 확장 모델

- 상태: Accepted
- 결정일: 2026-07-30

## 배경

BEEZ는 여러 Compose Multiplatform 프로젝트에서 재사용할 디자인 시스템이다. 프로젝트마다 브랜드 색상과 일부 형태가 다를 수 있으므로 테마 확장이 필요하다.

모든 값을 자유롭게 바꾸게 하면 컴포넌트의 일관성과 접근성을 보장하기 어렵다. 반대로 하나의 고정된 시각 스타일만 제공하면 서로 다른 브랜드의 제품에서 재사용하기 어렵다.

## 결정

BEEZ는 다음 두 가지를 함께 제공한다.

1. 별도 설정 없이 사용할 수 있는 BEEZ 기본 테마
2. semantic token을 교체하여 제품 브랜드를 표현하는 확장 지점

Scale token은 시스템이 사용할 수 있는 값의 범위를 정의한다. Semantic token은 배경, 전경, 테두리, 상태, 간격과 같이 값의 사용 의도를 표현한다. 제품 테마는 주로 semantic token의 매핑을 교체한다.

컴포넌트는 원칙적으로 scale token이나 원시 값을 직접 참조하지 않고 semantic token을 사용한다.

```text
Raw value
    ↓
Scale token
    ↓
Semantic token
    ↓
Component
```

브랜드 확장은 컴포넌트 복제나 분기보다 테마 교체를 우선한다. semantic token으로 표현하기 어려운 구조적 차이가 반복해서 발견되면 별도 컴포넌트 API 또는 recipe 도입을 검토한다.

## 결과

### 장점

- BEEZ 자체의 완성된 기본 경험을 제공할 수 있다.
- 제품은 컴포넌트를 포크하지 않고 브랜드를 표현할 수 있다.
- Light 및 Dark theme와 브랜드 theme를 같은 토큰 구조에서 다룰 수 있다.
- 컴포넌트가 구체적인 색상값과 형태값에 결합되지 않는다.
- 브랜드 변경이 컴포넌트 API 변경으로 이어질 가능성이 줄어든다.

### 비용과 제약

- semantic token의 역할과 경계를 신중하게 설계해야 한다.
- 모든 내부 값을 공개적인 테마 옵션으로 제공하지 않는다.
- 브랜드가 컴포넌트의 구조나 동작까지 임의로 변경할 수는 없다.
- 테마 조합마다 접근성과 시각적 품질을 검증해야 한다.

## 초기 확장 범위

초기에는 다음 semantic token 범주의 교체를 지원한다.

- Color scheme
- Typography scheme
- Shape scheme
- Spacing scheme
- Motion scheme

Elevation과 플랫폼별 시스템 표현은 기술 검증 후 공개 확장 범위를 결정한다.

## 보류 사항

다음 항목은 이 결정에 포함하지 않는다.

- 런타임에서 여러 브랜드를 동시에 전환하는 기능
- 원격 설정으로 테마를 배포하는 기능
- 브랜드별 asset 및 icon pack 관리
- Figma variable collection과의 자동 동기화
- 화이트라벨 제품군을 위한 테마 상속과 병합 규칙

실제 제품 요구가 확인되면 별도의 ADR로 다룬다.
